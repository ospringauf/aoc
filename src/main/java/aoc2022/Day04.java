package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 4:  ---
// https://adventofcode.com/2022/day/4

class Day04 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1");
		new Day04().part1();
		System.out.println("=== part 2");
		new Day04().part2();
	}

	List<Integer> data = file2ints("input04.txt");

	void part1() {
		System.out.println(data.size());
	}

	void part2() {
	}

	static String example = """

			""";

}
