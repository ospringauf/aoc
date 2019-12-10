package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;

/*
 * Day 5: Sunny with a Chance of Asteroids
 * https://adventofcode.com/2019/day/5
 */
public class Day05 {

	private static final int HCF = 99;
	private static final int ADD = 1;
	private static final int MUL = 2;
	private static final int INP = 3;
	private static final int OUT = 4;
	private static final int JIT = 5;
	private static final int JIF = 6;
	private static final int LES = 7;
	private static final int EQU = 8;

	public static void main(String[] args) throws Exception {
		System.out.println("=== part1 ===");
		new Day05().part1();
		System.out.println("=== part2 ===");
		new Day05().part2();
	}

	int[] input = { 3, 225, 1, 225, 6, 6, 1100, 1, 238, 225, 104, 0, 1102, 68, 5, 225, 1101, 71, 12, 225, 1, 117, 166,
			224, 1001, 224, -100, 224, 4, 224, 102, 8, 223, 223, 101, 2, 224, 224, 1, 223, 224, 223, 1001, 66, 36, 224,
			101, -87, 224, 224, 4, 224, 102, 8, 223, 223, 101, 2, 224, 224, 1, 223, 224, 223, 1101, 26, 51, 225, 1102,
			11, 61, 224, 1001, 224, -671, 224, 4, 224, 1002, 223, 8, 223, 1001, 224, 5, 224, 1, 223, 224, 223, 1101, 59,
			77, 224, 101, -136, 224, 224, 4, 224, 1002, 223, 8, 223, 1001, 224, 1, 224, 1, 223, 224, 223, 1101, 11, 36,
			225, 1102, 31, 16, 225, 102, 24, 217, 224, 1001, 224, -1656, 224, 4, 224, 102, 8, 223, 223, 1001, 224, 1,
			224, 1, 224, 223, 223, 101, 60, 169, 224, 1001, 224, -147, 224, 4, 224, 102, 8, 223, 223, 101, 2, 224, 224,
			1, 223, 224, 223, 1102, 38, 69, 225, 1101, 87, 42, 225, 2, 17, 14, 224, 101, -355, 224, 224, 4, 224, 102, 8,
			223, 223, 1001, 224, 2, 224, 1, 224, 223, 223, 1002, 113, 89, 224, 101, -979, 224, 224, 4, 224, 1002, 223,
			8, 223, 1001, 224, 7, 224, 1, 224, 223, 223, 1102, 69, 59, 225, 4, 223, 99, 0, 0, 0, 677, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 1105, 0, 99999, 1105, 227, 247, 1105, 1, 99999, 1005, 227, 99999, 1005, 0, 256, 1105, 1,
			99999, 1106, 227, 99999, 1106, 0, 265, 1105, 1, 99999, 1006, 0, 99999, 1006, 227, 274, 1105, 1, 99999, 1105,
			1, 280, 1105, 1, 99999, 1, 225, 225, 225, 1101, 294, 0, 0, 105, 1, 0, 1105, 1, 99999, 1106, 0, 300, 1105, 1,
			99999, 1, 225, 225, 225, 1101, 314, 0, 0, 106, 0, 0, 1105, 1, 99999, 7, 677, 677, 224, 1002, 223, 2, 223,
			1006, 224, 329, 1001, 223, 1, 223, 1007, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 344, 1001, 223, 1,
			223, 1108, 226, 677, 224, 102, 2, 223, 223, 1005, 224, 359, 1001, 223, 1, 223, 1107, 226, 677, 224, 1002,
			223, 2, 223, 1006, 224, 374, 101, 1, 223, 223, 1107, 677, 226, 224, 1002, 223, 2, 223, 1006, 224, 389, 101,
			1, 223, 223, 7, 226, 677, 224, 1002, 223, 2, 223, 1005, 224, 404, 101, 1, 223, 223, 1008, 677, 226, 224,
			102, 2, 223, 223, 1005, 224, 419, 101, 1, 223, 223, 1008, 226, 226, 224, 102, 2, 223, 223, 1006, 224, 434,
			101, 1, 223, 223, 107, 226, 226, 224, 1002, 223, 2, 223, 1005, 224, 449, 1001, 223, 1, 223, 108, 226, 677,
			224, 102, 2, 223, 223, 1005, 224, 464, 101, 1, 223, 223, 1108, 677, 226, 224, 102, 2, 223, 223, 1005, 224,
			479, 101, 1, 223, 223, 1007, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 494, 101, 1, 223, 223, 107, 677,
			677, 224, 102, 2, 223, 223, 1005, 224, 509, 101, 1, 223, 223, 108, 677, 677, 224, 102, 2, 223, 223, 1006,
			224, 524, 1001, 223, 1, 223, 8, 226, 677, 224, 102, 2, 223, 223, 1005, 224, 539, 101, 1, 223, 223, 107, 677,
			226, 224, 102, 2, 223, 223, 1005, 224, 554, 1001, 223, 1, 223, 8, 226, 226, 224, 102, 2, 223, 223, 1006,
			224, 569, 1001, 223, 1, 223, 7, 677, 226, 224, 1002, 223, 2, 223, 1005, 224, 584, 1001, 223, 1, 223, 1108,
			226, 226, 224, 102, 2, 223, 223, 1005, 224, 599, 1001, 223, 1, 223, 1107, 677, 677, 224, 1002, 223, 2, 223,
			1006, 224, 614, 1001, 223, 1, 223, 1007, 677, 677, 224, 1002, 223, 2, 223, 1006, 224, 629, 1001, 223, 1,
			223, 108, 226, 226, 224, 102, 2, 223, 223, 1005, 224, 644, 1001, 223, 1, 223, 8, 677, 226, 224, 1002, 223,
			2, 223, 1005, 224, 659, 1001, 223, 1, 223, 1008, 677, 677, 224, 1002, 223, 2, 223, 1006, 224, 674, 1001,
			223, 1, 223, 4, 223, 99, 226 };

	int[] test1 = { 3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002, 21,
			125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99 };

	int run(int[] program, List<Integer> data) throws Exception {
		var input = new ArrayList<Integer>(data);
		int[] mem = Arrays.copyOf(program, program.length);
		int adr = 0;

		while (adr < mem.length && mem[adr] != HCF) {

			int instr = mem[adr];
			int opcode = instr % 100;
			boolean imm1 = (instr / 100 % 10) == 1;
			boolean imm2 = (instr / 1000 % 10) == 1;
			boolean imm3 = (instr / 10000 % 10) == 1;

			int p1 = mem[adr + 1];
			int p2 = mem[adr + 2];
			int p3 = mem[adr + 3];

//			System.out.println("\t" + adr);

			IntSupplier arg1 = () -> imm1 ? p1 : mem[p1];
			IntSupplier arg2 = () -> imm2 ? p2 : mem[p2];

			switch (opcode) {
			case ADD:
				mem[p3] = arg1.getAsInt() + arg2.getAsInt();
				adr += 4;
				break;

			case MUL:
				mem[p3] = arg1.getAsInt() * arg2.getAsInt();
				adr += 4;
				break;

			case INP:
				mem[p1] = input.remove(0);
				adr += 2;
				break;

			case OUT:
				System.out.println(arg1.getAsInt());
				adr += 2;
				break;

			case JIT:
				if (arg1.getAsInt() != 0)
					adr = arg2.getAsInt();
				else
					adr += 3;
				break;

			case JIF:
				if (arg1.getAsInt() == 0)
					adr = arg2.getAsInt();
				else
					adr += 3;
				break;

			case LES:
				mem[p3] = (arg1.getAsInt() < arg2.getAsInt()) ? 1 : 0;
				adr += 4;
				break;

			case EQU:
				mem[p3] = (arg1.getAsInt() == arg2.getAsInt()) ? 1 : 0;
				adr += 4;
				break;

			case HCF:
			default:
				break;
			}

		}
		return mem[0];
	}

	void part1() throws Exception {
		run(input, List.of(1));
	}

	void part2() throws Exception {
//		run(test1, List.of(9));
		run(input, List.of(5));
	}

}