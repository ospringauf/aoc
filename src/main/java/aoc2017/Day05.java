package aoc2017;

import common.AocPuzzle;

//https://adventofcode.com/2017/day/5

@SuppressWarnings({ "deprecation", "preview", "serial" })
public class Day05 extends AocPuzzle {

	public static void main(String[] args) throws Exception {
		System.out.println("=== part 1"); // 343467
		new Day05().part1();

		System.out.println("=== part 2"); // 24774780
		new Day05().part2();
	}

	final Integer[] instructions = file2ints("input05.txt").toJavaArray(Integer.class);

	void part1() throws Exception {
		int steps = 0;
		int idx = 0;
		while (idx < instructions.length) {
			int curr = instructions[idx];
			instructions[idx] = curr + 1;
			idx += curr;
			steps++;
		}

		System.out.println(steps);
	}

	void part2() throws Exception {
		int steps = 0;
		int idx = 0;
		while (idx < instructions.length) {
			int curr = instructions[idx];
			instructions[idx] =  (curr >= 3) ? curr - 1 : curr + 1;
			idx += curr;
			steps++;
		}

		System.out.println(steps);
	}

}
