package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aoc2020.Comp.Instr;
import aoc2020.Comp.Opcode;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 8: Handheld Halting ---
// https://adventofcode.com/2020/day/8

@SuppressWarnings({ "deprecation", "preview" })
class Day08 extends AocPuzzle {

	public static void main(String[] args) {

//		var prog = Comp.parseProg(example);
		var prog = Comp.parseProg(new Day08().lines("input08.txt"));

		System.out.println("=== part 1");
		var r1 = new Day08().part1(prog);
		assertEquals(1949, r1);

		System.out.println("=== part 2");
		var r2 = new Day08().part2(prog);
		assertEquals(2092, r2);
	}

	int part1(Array<Instr> prog) {
//		System.out.println(prog);
		var c = new Comp();

		c.execute(prog);
		System.out.println(c.acc);
		return c.acc;
	}

	int part2(Array<Instr> prog) {
		var c = new Comp();

		var pos = List.range(0, prog.size()).filter(t -> c.execute(modifyProg(prog, t))).get();

		System.out.println(pos);

		c.execute(modifyProg(prog, pos));
		System.out.println(c.acc);
		return c.acc;
	}

	Array<Instr> modifyProg(Array<Instr> prog, Integer pos) {
		var instr = prog.get(pos);
		instr = switch (instr.op()) {
			case JMP -> new Instr(Opcode.NOP, instr.arg());
			case NOP -> new Instr(Opcode.JMP, instr.arg());
			default -> instr;
		};
		return prog.update(pos, instr);
	}

	static String example = """
			nop +0
			acc +1
			jmp +4
			acc +3
			jmp -3
			acc -99
			acc +1
			jmp -4
			acc +6
						""";
}
