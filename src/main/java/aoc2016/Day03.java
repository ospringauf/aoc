package aoc2016;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 3: Squares With Three Sides ---
// https://adventofcode.com/2016/day/3

class Day03 extends AocPuzzle {

	void part1() {
		var l = file2lines("day03.txt").map(s -> Util.string2ints(s.trim()));
		var r = l.count(this::isTriangle);
		System.out.println(r);
	}

	boolean isTriangle(List<Integer> x) {
		Integer max = x.max().get();
		return max < x.sum().intValue() - max;
	}

	void part2() {
		var l0 = file2lines("day03.txt").map(s -> Util.string2ints(s.trim()));
		var l = l0.grouped(3).flatMap(x -> List.transpose(x));
		var r = l.count(this::isTriangle);
		System.out.println(r);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day03().part1());

		System.out.println("=== part 2");
		timed(() -> new Day03().part2());
	}
}
