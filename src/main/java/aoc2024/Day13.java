package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 13: Claw Contraption ---
// https://adventofcode.com/2024/day/13

class Day13 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 33481
		timed(() -> new Day13().part1());
		System.out.println("=== part 2"); // 92572057880885
		timed(() -> new Day13().part2());
	}

	String data = file2string("input13.txt");
//    String data = example;
//	String data = testcase1;

	record Loc(long x, long y) {

		static Loc of(long x, long y) {
			return new Loc(x, y);
		}

		public Loc times(long n) {
			return new Loc(n * x, n * y);
		}

		public Loc plus(Loc p) {
			return new Loc(x + p.x, y + p.y);
		}

		public Loc minus(Loc p) {
			return new Loc(x - p.x, y - p.y);
		}

		static Loc parse(String s) {
			var f = split(s, "[XY\\+\\,\\=]");
			return new Loc(f.i(2), f.i(5));
		}
	}

	record Machine(Loc a, Loc b, Loc p) {
		static Machine parse(String s) {
			var f = s.split("\n");
			var a = Loc.parse(f[0].split(": ")[1]);
			var b = Loc.parse(f[1].split(": ")[1]);
			var p = Loc.parse(f[2].split(": ")[1]);
			return new Machine(a, b, p);
		}

		Machine shift(long amount) {
			return new Machine(a, b, p.plus(Loc.of(amount, amount)));
		}

		Long minTokens1() {
			long best = Long.MAX_VALUE;
			long steps = p.x() / a.x();

			for (long na = 0; na <= steps; ++na) {
				var nb = (p.x() - (na * a.x())) / b.x();
				var x = a.times(na).plus(b.times(nb));
				if (p.equals(x)) {
					var t = 3 * na + nb;
					best = (t<best)? t : best;
				}
			}

			return best;
		}

		// aka the method of shame
		Long minTokens2() {
			System.out.println("---");
			System.out.println(this);
			var px = p.x;
			var py = p.y;
			var ax = a.x;
			var bx = b.x;
			var ay = a.y;
			var by = b.y;

			var dp = px - py;

			// find initial combinations of A/B button counts that result in the desired x/y distance
			var c = List.range(0, 1500).crossProduct().filter(s -> (dp == s._1 * (ax - ay) + s._2 * (bx - by))).take(5)
					.toList();
			if (c.isEmpty()) {
				System.err.println(this);
				throw new RuntimeException("no initial solution");
			}
			
			// turns out that after the first solution, all other solutions are at regular intervals of A/B counts
			System.out.println(c);
			var c0 = c.get(0);
			var c1 = c.get(1);

			int a0 = c0._1;
			int b0 = c0._2;
			System.out.println("offset: " + (a0 * ax + b0 * bx) + " / " + (a0 * ay + b0 * by));

			int da = c1._1 - c0._1;
			int db = c1._2 - c0._2;
			System.out.println("delta: " + da + " / " + db);

			var n = (px - a0 * ax - b0 * bx) / (da * ax + db * bx);
			System.out.println("n = " + n);

			var x = a0 * ax + n * da * ax + b0 * bx + n * db * bx;
			var y = a0 * ay + n * da * ay + b0 * by + n * db * by;
			var loc = Loc.of(x, y);

			if (loc.equals(p))
				return 3 * (a0 + da * n) + (b0 + db * n);
			else {
				System.out.println("off by " + loc.minus(p));
				return 0L;
			}
		}
	}

	void part1() {
		var b = data.split("\n\n");
		var machines = List.of(b).map(Machine::parse);

		var r = machines.map(Machine::minTokens1).filter(t -> t < Long.MAX_VALUE).sum();
		System.out.println(r);
	}

	void part2() {
		var b = data.split("\n\n");
		var machines = List.of(b).map(Machine::parse).map(m -> m.shift(10000000000000L));

		var t = machines.map(Machine::minTokens2);
		System.out.println("--- tokens: ");
		System.out.println(t);
		System.out.println(t.sum());
	}

	static String example = """
			Button A: X+94, Y+34
			Button B: X+22, Y+67
			Prize: X=8400, Y=5400

			Button A: X+26, Y+66
			Button B: X+67, Y+21
			Prize: X=12748, Y=12176

			Button A: X+17, Y+86
			Button B: X+84, Y+37
			Prize: X=7870, Y=6450

			Button A: X+69, Y+23
			Button B: X+27, Y+71
			Prize: X=18641, Y=10279
			""";

	static String testcase1 = """
			Button A: X+23, Y+11
			Button B: X+13, Y+27
			Prize: X=16099, Y=2429
			""";
}
