package aoc2015;

import common.AocPuzzle;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 23: Opening the Turing Lock ---
// https://adventofcode.com/2015/day/23

class Day23 extends AocPuzzle {

	enum Op {
		hlf, tpl, inc, jmp, jie, jio
	}

	static Map<Character, Integer> regs;
	static int iptr = 0;
	
	record Instr(Op op, char reg, int arg) {
		static Instr parse(String s) {
			var r = split(s, "[, ]+");
			var op = Op.valueOf(r.s(0));
			return switch (op) {
			case jmp -> new Instr(op, ' ', r.i(1));
			case jio, jie -> new Instr(op, r.c(1), r.i(2));
			default -> new Instr(op, r.c(1), 0);
			};
		}
		
		void exec() {
			switch (op) {
			case hlf -> { regs = regs.put(reg, regs.getOrElse(reg, 0) / 2); iptr++; }
			case tpl -> { regs = regs.put(reg, regs.getOrElse(reg, 0) * 3); iptr++; }
			case inc -> { regs = regs.put(reg, regs.getOrElse(reg, 0) + 1); iptr++; }
			case jmp -> { iptr += arg; }
			case jie -> { iptr += (regs.getOrElse(reg, 0) % 2 == 0)? arg : 1; }
			case jio -> { iptr += (regs.getOrElse(reg, 0) == 1)? arg : 1; }
			default ->
				throw new IllegalArgumentException("Unexpected value: " + op);
			}
		}
	}

	List<Instr> prog = file2lines("day23.txt").map(Instr::parse);

	void part1() {
		regs = HashMap.of('a', 0, 'b', 0);
		iptr = 0;
		
		while (iptr < prog.size()) {
			prog.get(iptr).exec();
		}
		
		System.out.println(regs);
	}

	void part2() {
		regs = HashMap.of('a', 1, 'b', 0);
		iptr = 0;
		
		while (iptr < prog.size()) {
			prog.get(iptr).exec();
		}
		
		System.out.println(regs);
	}

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day23().part1();

		System.out.println("=== part 2");
		new Day23().part2();
	}
}
