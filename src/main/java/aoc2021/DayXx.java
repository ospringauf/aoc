package aoc2021;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day x: ---
// https://adventofcode.com/2021/day/x

class DayXx extends AocPuzzle {

	List<String> input = Util.splitLines(example);
//	List<String> input = file2lines("input0x.txt");
	
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
		new DayXx().test();
	
		System.out.println("=== part 1");
		new DayXx().part1();
	
		System.out.println("=== part 2");
		new DayXx().part2();
	}

	static String example = """

			""";

}
