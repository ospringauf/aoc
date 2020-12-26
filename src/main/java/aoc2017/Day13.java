package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 13: Packet Scanners ---
// https://adventofcode.com/2017/day/13

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day13 extends AocPuzzle {

	

//	data = lines("input0x.txt");
//	data = List.of(example.split("\n"));

	void part1() {
		System.out.println(lines("input13.txt"));
	}

	void part2() {
	}
	
	public static void main(String[] args) {
	
		System.out.println("=== part 1");
		new Day13().part1();
		
		System.out.println("=== part 2");
		new Day13().part2();
	}

	static String example = """
0: 3
1: 2
4: 4
6: 4			
			""";

}
