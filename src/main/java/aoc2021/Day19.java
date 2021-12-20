package aoc2021;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 19: Beacon Scanner ---
// https://adventofcode.com/2021/day/19

class Day19 extends AocPuzzle {

//	scanner 1 at 68,-1246,-43
//	scanner 2 at 1105,-1205,1229 
//	scanner 3 at -92,-2380,-20 
//	scanner 4 at -20,-1133,1061
//	 String input = file2string("input19_example.txt");
	
	String input = file2string("input19.txt");

	record Pos(int x, int y, int z) {
		int manhattan(Pos b) {
			return Math.abs(b.x - x) + Math.abs(b.y - y) + Math.abs(b.z - z);
		}

		static Pos parse(String s) {
			return split(s, ",").to(r -> new Pos(r.i(0), r.i(1), r.i(2)));
		}

		public Pos plus(Pos p) {
			return new Pos(x + p.x, y + p.y, z + p.z);
		}

		public Pos minus(Pos p) {
			return new Pos(x - p.x, y - p.y, z - p.z);
		}
	}

	static class Scanner {
		int number;
		Set<Pos> beacons;

		Pos location;
		Function1<Pos, Pos> rot;

		static Scanner parse(List<String> l) {
			Scanner s = new Scanner();
			s.number = split(l.head(), " ").i(2);
			s.beacons = l.tail().map(Pos::parse).toSet();
			return s;
		}

		Array<Integer> coords(Function1<Pos, Pos> rot, Function1<Pos, Integer> axis) {
			return beacons.map(rot).toArray().map(axis);
		}

		Set<Pos> beaconLocations() {
			return beacons.map(rot).map(p -> location.plus(p));
		}

		boolean overlap(Scanner known) {
//			System.out.println("compare " + s1.number + " and " + s2.number);
			Set<Function1<Pos, Pos>> rots = HashSet.ofAll(allRot);
			List<Set<Integer>> coord = List.empty();
			
			// On each axis, check if there is a displacement (scanner distance) so that 12 beacons 
			// have equal coordinate. Must check for different rotations.  
			
			for (var ax : allAxes) {
				Set<Function1<Pos, Pos>> remRot = HashSet.empty();
				Set<Integer> vals = HashSet.empty();
				var c1 = known.coords(id, ax).toSortedSet();
				for (var rot : rots) {
					var c2 = this.coords(rot, ax);

					// displacement s1/s2 -> only look at differences between beacons
					var ds = c1.toList().crossProduct(c2).map(t -> t._2 - t._1).toSet();
					for (int d : ds) {
						var common = c2.map(c -> c - d).filter(c -> Math.abs(c) <= 1000).count(c -> c1.contains(c));
						if (common >= 12) {
							remRot = remRot.add(rot);
							vals = vals.add(-d);
							break;
						}
					}
				}
				coord = coord.append(vals);
				vals = HashSet.empty();
				rots = remRot;
			}

			if (rots.singleOption().isDefined()) {
				var rot = rots.single();
				var offset = new Pos(coord.get(0).single(), coord.get(1).single(), coord.get(2).single());
				offset = known.rot.apply(offset);
				this.rot = rot.andThen(known.rot);
				this.location = known.location.plus(offset);
				System.out.println("located scanner " + number + " at " + location + " (overlap with scanner "
						+ known.number + ")");
				return true;
			}
			return false;
		}
	}

	void solve() {
		var scanners = split(input, "\n\n").map(b -> Scanner.parse(Util.splitLines(b)));
//		System.out.println(scanners);

//		scanners.forEach(s -> s.prerot= allRot.toMap(r -> r, r -> s.beacons.map(r).toSet()));
//		System.out.println("pre-rot done");

		var s0 = scanners.get(0);
		s0.location = new Pos(0, 0, 0);
		s0.rot = id;

		var located = List.of(s0);
		var unknown = scanners.remove(s0);

//		overlap(s0, scanners.get(4));

		while (unknown.nonEmpty()) {
			for (var s1 : located)
				for (var s2 : unknown)
					if (s2.overlap(s1)) {
						located = located.append(s2);
						unknown = unknown.remove(s2);
					}
		}

		System.out.println("=== part 1"); // 308
		var beacons = located.flatMap(s -> s.beaconLocations()).toSet();
		System.out.println(beacons.size());

		System.out.println("=== part 2"); // 12124
		var manh = scanners.map(s -> s.location).crossProduct().map(t -> t._1.manhattan(t._2)).max();
		System.out.println(manh);
	}

	public static void main(String[] args) {
		timed(() -> new Day19().solve());
	}

	static Function1<Pos, Pos> id = b -> b;
	static Function1<Pos, Pos> rotx = b -> new Pos(b.x, -b.z, b.y);
	static Function1<Pos, Pos> roty = b -> new Pos(b.z, b.y, -b.x);
	static Function1<Pos, Pos> rotz = b -> new Pos(b.y, -b.x, b.z);

	static List<Function1<Pos, Pos>> allRot = List.of(
//			1.  I
			id,
//			2.  X = YXZ
			rotx,
//			3.  Y = ZYX
			roty,
//			4.  Z = XZY
			rotz,
//			5.  XX = XYXZ = YXXY = YXYZ = YXZX = YYZZ = YZXZ = ZXXZ = ZZYY
			rotx.andThen(rotx),
//			6.  XY = YZ = ZX = XZYX = YXZY = ZYXZ
			rotx.andThen(roty),
//			7.  XZ = XXZY = YXZZ = YYYX = ZYYY
			rotx.andThen(rotz),
//			8.  YX = XZZZ = YYXZ = ZYXX = ZZZY
			roty.andThen(rotx),
//			9.  YY = XXZZ = XYYX = YZYX = ZXYX = ZYXY = ZYYZ = ZYZX = ZZXX
			roty.andThen(roty),
//			10. ZY = XXXZ = XZYY = YXXX = ZZYX
			rotz.andThen(roty),
//			11. ZZ = XXYY = XYZY = XZXY = XZYZ = XZZX = YYXX = YZZY = ZXZY
			rotz.andThen(rotz),
//			12. XXX
			rotx.andThen(rotx).andThen(rotx),
//			13. XXY = XYZ = XZX = YZZ = ZXZ
			rotx.andThen(rotx).andThen(roty),
//			14. XXZ = ZYY
			rotx.andThen(rotx).andThen(rotz),
//			15. XYX = YXY = YYZ = YZX = ZXX
			rotx.andThen(roty).andThen(rotx),
//			16. XYY = YZY = ZXY = ZYZ = ZZX
			rotx.andThen(roty).andThen(roty),
//			17. XZZ = YYX
			rotx.andThen(rotz).andThen(rotz),
//			18. YXX = ZZY
			roty.andThen(rotx).andThen(rotx),
//			19. YYY
			roty.andThen(roty).andThen(roty),
//			20. ZZZ
			rotz.andThen(rotz).andThen(rotz),
//			21. XXXY = XXYZ = XXZX = XYZZ = XZXZ = YZZZ = ZXZZ = ZYYX
			rotx.andThen(rotx).andThen(rotx).andThen(roty),
//			22. XXYX = XYXY = XYYZ = XYZX = XZXX = YXYY = YYZY = YZXY = YZYZ = YZZX = ZXXY = ZXYZ = ZXZX = ZYZZ = ZZXZ
			rotx.andThen(rotx).andThen(roty).andThen(rotx),
//			23. XYXX = XZZY = YXYX = YYXY = YYYZ = YYZX = YZXX = ZXXX
			rotx.andThen(roty).andThen(rotx).andThen(rotx),
//			24. XYYY = YXXZ = YZYY = ZXYY = ZYZY = ZZXY = ZZYZ = ZZZX
			rotx.andThen(roty).andThen(roty).andThen(roty));

	static List<Function1<Pos, Integer>> allAxes = List.of(b -> b.x, b -> b.y, b -> b.z);

}
