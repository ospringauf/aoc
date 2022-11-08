package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 1:  ---
// https://adventofcode.com/2022/day/1

class Day01 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); 
		new Day01().part1();
		System.out.println("=== part 2"); 
		new Day01().part2();
	}

	List<Integer> data = file2ints("input01.txt");
	
	private void part1() {
	    System.out.println(data.size());
	}
	
	private void part2() {
	}

}
