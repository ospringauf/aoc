package aoc2015;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 24: It Hangs in the Balance ---
// https://adventofcode.com/2015/day/24

class Day24 extends AocPuzzle {

	void part1() {
		// var w = Util.string2ints(example);
		var w = Util.string2ints(myInput);
		System.out.println(w);

		var total = w.sum().intValue();
		System.out.println(total);
		var l = 6;
		var front = List.ofAll(Generator.combination(w.asJava()).simple(l)).map(List::ofAll)
				.filter(x -> x.sum().intValue() == total / 3);

		System.out.println(front.size());
		var best = front.minBy(x -> x.product().longValue()).get();
		System.out.println(best);
		System.out.println(best.product());
	}

	void part2() {
//		 var w = Util.string2ints(example);
		var w = Util.string2ints(myInput);
		System.out.println(w);

		var total = w.sum().intValue();
		System.out.println(total);
		var l = 4;
		var front = List.ofAll(Generator.combination(w.asJava()).simple(l)).map(List::ofAll)
				.filter(x -> x.sum().intValue() == total / 4);

		System.out.println(front.size());
		var best = front.minBy(x -> x.product().longValue()).get();
		System.out.println(best);
		System.out.println(best.product());

	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 11846773891
		new Day24().part1();

		System.out.println("=== part 2"); // 80393059
		new Day24().part2();
	}

	static String example = """
			1
			2
			3
			4
			5
			7
			8
			9
			10
			11
						""";

	static String myInput = """
			1
			2
			3
			7
			11
			13
			17
			19
			23
			31
			37
			41
			43
			47
			53
			59
			61
			67
			71
			73
			79
			83
			89
			97
			101
			103
			107
			109
			113
			""";
}
