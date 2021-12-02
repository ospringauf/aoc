package aoc2021;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 2: Dive! ---
// https://adventofcode.com/2021/day/2

class Day02 extends AocPuzzle {

	record Cmd(String dir, int x) {
		static Cmd parse(String s) {			
			String[] field = s.split(" ");
			return new Cmd(field[0], Integer.valueOf(field[1])); 
		}
	}
	
	List<Cmd> commands = file2lines("input02.txt").map(Cmd::parse);
	
	void part1() {
		var hpos = 0;
		var depth = 0;
		for (var cmd : commands)  {		
			switch (cmd.dir) {
			case "forward" -> hpos += cmd.x;
			case "down" -> depth += cmd.x;
			case "up" -> depth -= cmd.x;
		}}
		
		System.out.println(hpos*depth);
	}

	void part2() {
		var hpos = 0;
		var depth = 0;
		var aim = 0;
		for (var cmd : commands)  {		
			switch (cmd.dir) {
			case "forward" ->  { hpos += cmd.x; depth += aim*cmd.x; }
			case "down" -> aim += cmd.x;
			case "up" -> aim -= cmd.x;
		}}
		
		System.out.println(hpos*depth);
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== part 1"); // 2150351
		new Day02().part1();
	
		System.out.println("=== part 2"); // 1842742223
		new Day02().part2();
	}
}
