package aoc2022;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import common.AocPuzzle;

//--- Day 1: Calorie Counting ---
// https://adventofcode.com/2022/day/1

class Day01 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 69693 
		new Day01().part1();
		System.out.println("=== part 2"); // 200945
		new Day01().part2();
	}

	String input = file2string("input01.txt");
	
	void part1() {
		var l = split(input, "\n\n")
				.map(block -> lines(block).map(Integer::parseInt));
		
		var sums = l.map(elf -> elf.sum());
		System.out.println(sums.max());
		
		var top3 = sums.sorted().reverse().take(3);
		System.out.println(top3.sum());
	}
	
	
	// same as pure java
	void part2() {
		String[] blocks = input.split("\n\n");
		List<Integer> sums = Arrays.stream(blocks).map(b -> sum(b.split("\n"))).toList();
		
		Optional<Integer> max = sums.stream().max(Integer::compareTo);
		System.out.println(max);
		
		IntStream top3 = sums.stream().sorted(Comparator.reverseOrder()).mapToInt(x->x).limit(3);
		System.out.println(top3.sum());		
	}
	
	int sum(String[] lines) {
	    return Arrays.stream(lines).mapToInt(Integer::parseInt).sum();
	}

}
