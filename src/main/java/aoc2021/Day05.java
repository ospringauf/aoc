package aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 5: ---
// https://adventofcode.com/2021/day/5

class Day05 extends AocPuzzle {

	List<String> data = file2lines("input05.txt");
	
	void part1() {
	}

	void part2() {
		
	}

	void test() {
		assertEquals(1, 1);
		System.out.println("passed");
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== test");
		new Day05().test();
	
		System.out.println("=== part 1");
		new Day05().part1();
	
		System.out.println("=== part 2");
		new Day05().part2();
	}

	static String example = """

			""";

}
