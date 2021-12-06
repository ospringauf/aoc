package aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day x: ---
// https://adventofcode.com/2021/day/x

class Day07 extends AocPuzzle {

	void part1() {
		List<String> data = file2lines("input0x.txt");
	}

	void part2() {
		
	}

	void test() {
		assertEquals(1, 1);
		System.out.println("passed");
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== test");
		new Day07().test();
	
		System.out.println("=== part 1");
		new Day07().part1();
	
		System.out.println("=== part 2");
		new Day07().part2();
	}

	static String example = """

			""";

}
