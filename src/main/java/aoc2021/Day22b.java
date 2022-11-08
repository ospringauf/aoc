package aoc2021;

import java.util.ArrayList;
import java.util.Collections;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day x: ---
// https://adventofcode.com/2021/day/x

class Day22b extends AocPuzzle {

	record Pos(int x, int y, int z) {
	}

	record Range(int min, int max) {
		static Range parse(String s) {
			return split(s, "[=\\.]+").to(r -> new Range(r.i(1), r.i(2)));
		}

//		List<Integer> range1() {
//			var v1 = Math.max(min, -50);
//			var v2 = Math.min(50, max);
//			return List.rangeClosed(v1, v2);
//		}

		List<Integer> range() {
			return List.rangeClosed(min, max);
		}

		Range part1() {
			var v1 = Math.max(min, -50);
			var v2 = Math.min(50, max);
			return new Range(v1, v2);
		}

		long size() {
			return max - min + 1;
		}

		boolean empty() {
			return min > max;
		}

		Range intersect(Range r) {
			var v1 = Math.max(min, r.min);
			var v2 = Math.min(max, r.max);
			return new Range(v1, v2);
		}
	}

	record Cuboid(boolean on, Range xr, Range yr, Range zr) {
		static Cuboid parse(String s) {
			return split(s.trim(), "[ ,]+").to(r -> new Cuboid(r.s(0).equals("on"), Range.parse(r.s(1)),
					Range.parse(r.s(2)), Range.parse(r.s(3))));
		}

		Cuboid part1() {
			return new Cuboid(on, xr.part1(), yr.part1(), zr.part1());
		}

		long size() {
			return xr.size() * yr.size() * zr.size();
		}

		List<Pos> points() {
			return xr.range().flatMap(xv -> yr.range().flatMap(yv -> zr.range().map(zv -> new Pos(xv, yv, zv))));
		}

		boolean empty() {
			return xr.empty() || yr.empty() || zr.empty();
		}

		public Cuboid intersect(Cuboid c) {
			return new Cuboid(on, xr.intersect(c.xr), yr.intersect(c.yr), zr.intersect(c.zr));
		}

//		@Override
//		public boolean equals(Object o) {
//			return this==o;
//		}
//
//		@Override
//		public int hashCode() {
//			// TODO Auto-generated method stub
//			return nr;
//		}

	}


	void part1() {
		Set<Pos> c = HashSet.empty();

		var instr = data.map(Cuboid::parse).map(Cuboid::part1);
		for (var i : instr) {
			var d = i.points();
			if (i.on)
				c = c.addAll(d);
			else
				c = c.removeAll(d);
		}
		System.out.println(c.size());
	}

	void part2a() {

		var l = data.map(Cuboid::parse).map(Cuboid::part1); // .filter(c -> ! c.empty());
		long r = 0;

		System.out.println(l.map(c -> c.size()));

		while (!l.isEmpty()) {
			System.out.println(l.size());
			var a = l.head();

			if (a.on) {
			var as = a.size();
			var sub = intersections(a, l.tail());
			var op = -1;
			while (sub.size() > 1) {
				var subsizes = sub.toList().map(c -> c.size());
				System.out.println("\t" + op + " / " + subsizes);
				long delta = op * subsizes.sum().longValue();
				System.out.println("\tdelta=" + delta);
				as += delta;
				op = (-1) * op;
				sub = intersections(sub.toList());
			}
			if (!sub.isEmpty()) {
				long delta = op * sub.single().size();
				System.out.println("\tdelta=" + delta);
				as += delta;
			}

			System.out.println("\tas=" + as);
			if (a.on)
				r += as;
			else
				r -= 0;
			System.out.println("\n\t -> " + r);
			}

			l = l.tail();
		}

		System.out.println(r);

	}

	record Cut(Set<Cuboid> cubes, Cuboid result) {

		Cut(Set<Cuboid> cubes) {
			this(cubes, getResult(cubes));
		}

		public Cut(Cut c1, Cut c2) {
			this(c1.cubes.addAll(c2.cubes), c1.result.intersect(c2.result));
		}

		Cuboid getResult() {
			return cubes.toList().foldLeft(cubes.head(), (c1, c2) -> c1.intersect(c2));
		}

		static Cuboid getResult(Set<Cuboid> cubes) {
			return cubes.toList().foldLeft(cubes.head(), (c1, c2) -> c1.intersect(c2));
		}

//		public String toString() {
//			return cubes.map(c -> c.nr).mkString(".");
//		}
	}

//	List<String> data = file2lines("input22.txt");
	List<String> data = Util.splitLines(example2);

	void part2() {

		var l = data.map(Cuboid::parse).map(Cuboid::part1);
		long r = 0;

		System.out.println(l.map(c -> c.size()));

		while (!l.isEmpty()) {
			System.out.println(l.size());
			var a = l.head();

			if (a.on) {
				var as = a.size();
				var sub = l.tail().map(t -> new Cut(HashSet.of(a, t))).filter(c -> ! c.result.empty()).toArray();
				var op = -1;
				while (sub.size() > 1) {

					var subsizes = sub.map(c -> c.result.size());
					System.out.println("\t" + op + " / " + subsizes);
					long delta = op * subsizes.sum().longValue();
					System.out.println("\tdelta=" + delta);
					as += delta;
					op = (-1) * op;
					sub = nextCuts(sub);
				}
				if (!sub.isEmpty()) {
					long delta = op * sub.single().result.size();
					System.out.println("\tdelta=" + delta);
					as += delta;
				}

				System.out.println("\tas=" + as);
				r += as;
				System.out.println("\n\t -> " + r);
			}

			l = l.tail();
		}

		System.out.println(r);

	}

	private Array<Cut> nextCuts(Array<Cut> list) {
		//Set<Cut> r = HashSet.empty();
		if (list.size() == 1)
			return Array.of(list.single());

		var a = list.toJavaArray(Cut[]::new);
		var r = new ArrayList<Cut>(a.length*a.length);
		for (int i = 0; i < a.length; ++i)
			for (int j = i + 1; j < a.length; ++j) {
				var c1 = a[i];
				var c2 = a[j];
				var n = new Cut(c1, c2);
				if (! n.result.empty())
					r.add(n);
			}

		System.out.println("..calc distinct start");
		var rr = new java.util.HashSet<>(r);
		//var rr = r.distinct().toArray();
		System.out.println("..calc distinct end");
		
		
		
		Array<Cut> rrr = Array.ofAll(rr);
		
		System.out.println(rrr.map(x -> x.result).size());
		System.out.println(rrr.map(x -> x.result).distinct().size());
		
        return rrr;
	}

	Set<Cuboid> intersections(Cuboid a, List<Cuboid> l) {
		return l.map(c -> a.intersect(c)).filter(c -> !c.empty()).toSet();
	}

	Set<Cuboid> intersections(List<Cuboid> l) {
		Set<Cuboid> r = HashSet.empty();
		if (l.size() == 1)
			return HashSet.of(l.single());

		for (int i = 0; i < l.size(); ++i)
			for (int j = i + 1; j < l.size(); ++j) {
				var c = l.get(i).intersect(l.get(j));
				if (!c.empty())
					r = r.add(c);
			}

		return r;
	}

	void test() {
//		System.out.println(Range.parse("x=10..12"));
		System.out.println(Cuboid.parse("on x=0..9,y=0..9,z=0..0"));
		System.out.println(Cuboid.parse("on x=0..9,y=0..9,z=0..0").size());

		var c1 = Cuboid.parse("on x=0..100,y=0..100,z=0..100");
		// var c2 = Cuboid.parse("on x=-10..9,y=0..100,z=0..20");
		var c2 = Cuboid.parse("on x=10..11,y=0..1,z=2..3");
		System.out.println(c1.intersect(c2));
		System.out.println(c2.intersect(c1));
		System.out.println(c2.size());
		System.out.println(c2.intersect(c1).size());

		System.out.println("passed");
	}

	public static void main(String[] args) {

//		System.out.println("=== test");
//		new Day22().test();

		System.out.println("=== part 1"); // 620241
		new Day22b().part1();

		System.out.println("=== part 2");
		new Day22b().part2();
	}

	static String example0 = """
						on x=0..9,y=0..9,z=0..0
						on x=2..5,y=-2..1,z=0..0
						off x=4..11,y=0..3,z=0..0
						on x=5..8,y=-1..0,z=0..0
			""";

	static String example1 = """
on x=10..12,y=10..12,z=10..12
on x=11..13,y=11..13,z=11..13
off x=9..11,y=9..11,z=9..11
on x=10..10,y=10..10,z=10..10
			""";
	static String example2 = """
			on x=-20..26,y=-36..17,z=-47..7
			on x=-20..33,y=-21..23,z=-26..28
			on x=-22..28,y=-29..23,z=-38..16
			on x=-46..7,y=-6..46,z=-50..-1
			on x=-49..1,y=-3..46,z=-24..28
			on x=2..47,y=-22..22,z=-23..27
			on x=-27..23,y=-28..26,z=-21..29
			on x=-39..5,y=-6..47,z=-3..44
			on x=-30..21,y=-8..43,z=-13..34
			on x=-22..26,y=-27..20,z=-29..19
			off x=-48..-32,y=26..41,z=-47..-37
			on x=-12..35,y=6..50,z=-50..-2
			off x=-48..-32,y=-32..-16,z=-15..-5
			on x=-18..26,y=-33..15,z=-7..46
			off x=-40..-22,y=-38..-28,z=23..41
			on x=-16..35,y=-41..10,z=-47..6
			off x=-32..-23,y=11..30,z=-14..3
			on x=-49..-5,y=-3..45,z=-29..18
			off x=18..30,y=-20..-8,z=-3..13
			on x=-41..9,y=-7..43,z=-33..15
			on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
			on x=967..23432,y=45373..81175,z=27513..53682
						""";

	static String example3 = """
			on x=-5..47,y=-31..22,z=-19..33
			on x=-44..5,y=-27..21,z=-14..35
			on x=-49..-1,y=-11..42,z=-10..38
			on x=-20..34,y=-40..6,z=-44..1
			off x=26..39,y=40..50,z=-2..11
			on x=-41..5,y=-41..6,z=-36..8
			off x=-43..-33,y=-45..-28,z=7..25
			on x=-33..15,y=-32..19,z=-34..11
			off x=35..47,y=-46..-34,z=-11..5
			on x=-14..36,y=-6..44,z=-16..29
			on x=-57795..-6158,y=29564..72030,z=20435..90618
			on x=36731..105352,y=-21140..28532,z=16094..90401
			on x=30999..107136,y=-53464..15513,z=8553..71215
			on x=13528..83982,y=-99403..-27377,z=-24141..23996
			on x=-72682..-12347,y=18159..111354,z=7391..80950
			on x=-1060..80757,y=-65301..-20884,z=-103788..-16709
			on x=-83015..-9461,y=-72160..-8347,z=-81239..-26856
			on x=-52752..22273,y=-49450..9096,z=54442..119054
			on x=-29982..40483,y=-108474..-28371,z=-24328..38471
			on x=-4958..62750,y=40422..118853,z=-7672..65583
			on x=55694..108686,y=-43367..46958,z=-26781..48729
			on x=-98497..-18186,y=-63569..3412,z=1232..88485
			on x=-726..56291,y=-62629..13224,z=18033..85226
			on x=-110886..-34664,y=-81338..-8658,z=8914..63723
			on x=-55829..24974,y=-16897..54165,z=-121762..-28058
			on x=-65152..-11147,y=22489..91432,z=-58782..1780
			on x=-120100..-32970,y=-46592..27473,z=-11695..61039
			on x=-18631..37533,y=-124565..-50804,z=-35667..28308
			on x=-57817..18248,y=49321..117703,z=5745..55881
			on x=14781..98692,y=-1341..70827,z=15753..70151
			on x=-34419..55919,y=-19626..40991,z=39015..114138
			on x=-60785..11593,y=-56135..2999,z=-95368..-26915
			on x=-32178..58085,y=17647..101866,z=-91405..-8878
			on x=-53655..12091,y=50097..105568,z=-75335..-4862
			on x=-111166..-40997,y=-71714..2688,z=5609..50954
			on x=-16602..70118,y=-98693..-44401,z=5197..76897
			on x=16383..101554,y=4615..83635,z=-44907..18747
			off x=-95822..-15171,y=-19987..48940,z=10804..104439
			on x=-89813..-14614,y=16069..88491,z=-3297..45228
			on x=41075..99376,y=-20427..49978,z=-52012..13762
			on x=-21330..50085,y=-17944..62733,z=-112280..-30197
			on x=-16478..35915,y=36008..118594,z=-7885..47086
			off x=-98156..-27851,y=-49952..43171,z=-99005..-8456
			off x=2032..69770,y=-71013..4824,z=7471..94418
			on x=43670..120875,y=-42068..12382,z=-24787..38892
			off x=37514..111226,y=-45862..25743,z=-16714..54663
			off x=25699..97951,y=-30668..59918,z=-15349..69697
			off x=-44271..17935,y=-9516..60759,z=49131..112598
			on x=-61695..-5813,y=40978..94975,z=8655..80240
			off x=-101086..-9439,y=-7088..67543,z=33935..83858
			off x=18020..114017,y=-48931..32606,z=21474..89843
			off x=-77139..10506,y=-89994..-18797,z=-80..59318
			off x=8476..79288,y=-75520..11602,z=-96624..-24783
			on x=-47488..-1262,y=24338..100707,z=16292..72967
			off x=-84341..13987,y=2429..92914,z=-90671..-1318
			off x=-37810..49457,y=-71013..-7894,z=-105357..-13188
			off x=-27365..46395,y=31009..98017,z=15428..76570
			off x=-70369..-16548,y=22648..78696,z=-1892..86821
			on x=-53470..21291,y=-120233..-33476,z=-44150..38147
			off x=-93533..-4276,y=-16170..68771,z=-104985..-24507
			""";
}
