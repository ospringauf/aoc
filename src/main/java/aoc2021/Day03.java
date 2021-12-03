package aoc2021;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 3: Binary Diagnostic ---
// https://adventofcode.com/2021/day/3

class Day03 extends AocPuzzle {

	List<Integer> input = file2lines("input03.txt").map(s -> Integer.parseInt(s, 2));
	int bits = 12;
//	List<Integer> input = Util.splitLines(example).map(s -> Integer.parseInt(s, 2));
//	int bits = 5;

	int mostCommon(List<Integer> l, int pos) {
		int ones = l.count(n -> (n & (1 << pos)) > 0);
		return (ones >= (l.size() - ones)) ? 1 : 0;
	}

	void part1() {
		var gamma = List.range(0, bits)
					.map(i -> mostCommon(input, i))
					.reverse()
					.foldLeft(0, (R, x) -> (R << 1) + x);

		System.out.println(gamma); // 110010111011, 3259

		var epsilon = List.range(0, bits)
				.map(i -> 1 - mostCommon(input, i))
				.reverse()
				.foldLeft(0, (R, x) -> (R << 1) + x);

		System.out.println(epsilon); // 836

		System.out.println(gamma * epsilon);
	}

	void part2() {
		var numbers = List.ofAll(input);
		int i = bits-1;
		
		while (numbers.size() > 1) {
			int pos = i;
			int bit = mostCommon(numbers, pos);
			numbers = numbers.filter(n -> ((n >> pos) & 1) == bit);
			i--;
		}
		Integer oxygen = numbers.single();

		numbers = List.ofAll(input);
		i = bits-1;
		while (numbers.size() > 1) {
			int pos = i;
			int bit = 1 - mostCommon(numbers, pos);
			numbers = numbers.filter(n -> ((n >> pos) & 1) == bit);
			i--;
		}
		Integer co2 = numbers.single();

		System.out.println(oxygen); // 4023
		System.out.println(co2); // 690
		System.out.println(oxygen * co2);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 2724524
		new Day03().part1();

		System.out.println("=== part 2"); // 2775870
		new Day03().part2();
	}

	static String example = """
			00100
			11110
			10110
			10111
			10101
			01111
			00111
			11100
			10000
			11001
			00010
			01010
						""";

}
