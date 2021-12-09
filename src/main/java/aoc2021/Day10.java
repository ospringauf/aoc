package aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day x: ---
// https://adventofcode.com/2021/day/x

class Day10 extends AocPuzzle {

	List<String> input = Util.splitLines(example);
//	List<String> input = file2lines("input0x.txt");
	
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
		new Day10().test();
	
		System.out.println("=== part 1");
		new Day10().part1();
	
		System.out.println("=== part 2");
		new Day10().part2();
	}

	static String example = """

			""";

}
