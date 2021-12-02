package aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 3: ---
// https://adventofcode.com/2021/day/3

class Day03 extends AocPuzzle {

	void part1() {
		List<String> data = file2lines("input03.txt");
	}

	void part2() {
		
	}

	void test() {
		assertEquals(1, 1);
		System.out.println("passed");
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== test");
		new Day03().test();
	
		System.out.println("=== part 1");
		new Day03().part1();
	
		System.out.println("=== part 2");
		new Day03().part2();
	}

	static String example = """

			""";

}
