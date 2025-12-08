package aoc2025;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 5: Cafeteria  ---
// https://adventofcode.com/2025/day/5

class Day05 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 652
		timed(() -> new Day05().part1());
		System.out.println("=== part 2"); // 341753674214273
		timed(() -> new Day05().part2());
	}

	String data = file2string("input05.txt");
//	String data = example;

	List<Range> fresh = Util.splitLines(data.split("\n\n")[0]).map(Range::parse);
	List<Long> ids = Util.splitLines(data.split("\n\n")[1]).map(Long::valueOf);

	record Range(long from, long to) {
		static Range parse(String s) {
			var a = s.split("-");
			return new Range(Long.parseLong(a[0]), Long.parseLong(a[1]));
		}

		boolean contains(long n) {
			return n >= from && n <= to;
		}
	}

	void part1() {
		System.out.println(ids.count(n -> fresh.exists(f -> f.contains(n))));
	}

	void part2() {
		var from = fresh.map(r -> r.from);
		var to = fresh.map(r -> r.to + 1); // normalize to "range exclusive"
		var p = from.appendAll(to).sorted();

		long n = 0;
		for (var r : p.sliding(2)) {
			var start = r.head();
			var covered = fresh.exists(f -> f.contains(start));
			if (covered)
				n += r.tail().head() - r.head();
		}
		System.out.println(n);
	}

	static String example = """
			3-5
			7-7
			10-14
			16-20
			12-18

			1
			5
			8
			11
			17
			32""";
}
