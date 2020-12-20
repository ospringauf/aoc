package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import common.AocPuzzle;
import io.vavr.collection.List;

// https://adventofcode.com/2020/day/21

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day21 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day21().test();

		System.out.println("=== part 1");
		new Day21().part1();

		System.out.println("=== part 2");
		new Day21().part2();
	}

	void part1() {
		List<String> data = lines("input21.txt");
	}

	void part2() {
		
	}

	void test() {
		assertEquals(1, 1);
		System.out.println("passed");
	}

	static String example = """

			""";

}
