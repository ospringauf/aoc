package aoc2015;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 20: Infinite Elves and Infinite Houses ---
// https://adventofcode.com/2015/day/20

class Day20 extends AocPuzzle {

	int input = 36000000;

	void part1() {
		int houses = input / 10;
		int[] p = new int[2 * houses];
		for (int elf = 1; elf < houses; ++elf) {
			for (int h = elf; h < houses; h += elf) {
				p[h] += 10 * elf;
			}
		}
		var r = List.rangeClosed(1, houses).find(i -> p[i] >= input);
		System.out.println(r);

	}

	int dividerSum(int n) {
		return 1 + n + List.range(2, n / 2 + 1).filter(i -> n % i == 0).sum().intValue();
	}

	void part2() {
		int houses = input / 11;
		int[] p = new int[houses];
		for (int elf = 1; elf < houses; ++elf) {
			for (int h = 1; h < 50; ++h) {
				if (elf * h < houses)
					p[elf * h] += 11 * elf;
			}
		}
		var r = List.rangeClosed(1, houses).find(i -> p[i] >= input);
		System.out.println(r);
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 831600
		new Day20().part1();

		System.out.println("=== part 2"); // 884520
		new Day20().part2();
	}
}
