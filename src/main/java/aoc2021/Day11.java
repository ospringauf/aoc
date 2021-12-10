package aoc2021;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 11: ---
// https://adventofcode.com/2021/day/11

class Day11 extends AocPuzzle {

	List<String> input = Util.splitLines(example);
//	List<String> input = file2lines("input11.txt");
	
	void part1() {
	}

	void part2() {
		
	}

	void test() {
		assertThat(1, is(1));
		System.out.println("passed");
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== test");
		new Day11().test();
	
		System.out.println("=== part 1");
		new Day11().part1();
	
		System.out.println("=== part 2");
		new Day11().part2();
	}

	static String example = """

			""";

}
