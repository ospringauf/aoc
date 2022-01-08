package aoc2015;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 25: Let It Snow ---
// https://adventofcode.com/2015/day/25

class Day25 extends AocPuzzle {

	void part1() {

		var col = 3019;
		var row = 3010;

		var box = List.range(1, col + row - 1).sum().longValue() + col;
		System.out.println("box: " + box);

		var code = 20151125L;
		for (long i = 1; i < box; ++i)
			code = (code * 252533) % 33554393;
		System.out.println("code: " + code);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day25().part1());
	}

	static String myInput = "To continue, please consult the code grid in the manual.  Enter the code at row 3010, column 3019.";
}
