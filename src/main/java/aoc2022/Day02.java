package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day x:  ---
// https://adventofcode.com/2022/day/x

class Day02 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); 
		new Day02().part1();
		System.out.println("=== part 2"); 
		new Day02().part2();
	}

	List<Integer> data = file2ints("input02.txt");
	
	void part1() {
	    System.out.println(data.size());
	}
	
	void part2() {
	}

}
