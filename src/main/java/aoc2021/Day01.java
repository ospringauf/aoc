package aoc2021;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 1: Sonar Sweep ---
// https://adventofcode.com/2021/day/1

class Day01 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 1195
		new Day01().part1();
		System.out.println("=== part 2"); // 1235
		new Day01().part2();
	}

	List<Integer> depths = file2ints("input01.txt");
	
	private void part1() {
		var increases = depths.sliding(2).count(w -> w.get(0) < w.get(1));
		System.out.println(increases);
	}
	
	private void part2() {
		var sum3 = depths.sliding(3).map(w -> w.sum().intValue());
		var increases = sum3.sliding(2).count(w -> w.get(0) < w.get(1));
		System.out.println(increases);
	}

}
