package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntSupplier;

import org.junit.jupiter.api.Assertions;
import org.paukov.combinatorics3.Generator;

/*
 * Day 7: Amplification Circuit
 * https://adventofcode.com/2019/day/7
 */
public class Day07 {

	public static void main(String[] args) throws Exception {
		System.out.println("=== diag  ===");
		Assertions.assertEquals(43210, part1(example1a));
		Assertions.assertEquals(65210, part1(example1c));
		Assertions.assertEquals(139629729, part2(example2a));
		Assertions.assertEquals(18216, part2(example2b));

		System.out.println("=== part1 ==="); // 440880
		part1(my_input);
		System.out.println("=== part2 ==="); // 3745599
		part2(my_input);
	}

	static int[] example1a = { 3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0 };
	static int[] example1b = { 3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23,
			99, 0, 0 };
	static int[] example1c = { 3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1,
			33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0 };
	static int[] example2a = { 3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26, 27, 4, 27, 1001, 28, -1, 28,
			1005, 28, 6, 99, 0, 0, 5 };
	static int[] example2b = { 3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
			-5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4, 53, 1001, 56, -1,
			56, 1005, 56, 6, 99, 0, 0, 0, 0, 10 };

	static int[] my_input = { 3, 8, 1001, 8, 10, 8, 105, 1, 0, 0, 21, 46, 55, 68, 89, 110, 191, 272, 353, 434, 99999, 3,
			9, 1002, 9, 3, 9, 1001, 9, 3, 9, 102, 4, 9, 9, 101, 4, 9, 9, 1002, 9, 5, 9, 4, 9, 99, 3, 9, 102, 3, 9, 9, 4,
			9, 99, 3, 9, 1001, 9, 5, 9, 102, 4, 9, 9, 4, 9, 99, 3, 9, 1001, 9, 5, 9, 1002, 9, 2, 9, 1001, 9, 5, 9, 1002,
			9, 3, 9, 4, 9, 99, 3, 9, 101, 3, 9, 9, 102, 3, 9, 9, 101, 3, 9, 9, 1002, 9, 4, 9, 4, 9, 99, 3, 9, 1001, 9,
			1, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9,
			2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9,
			1, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2,
			9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2,
			9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 101, 2, 9,
			9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9,
			4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4,
			9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4,
			9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4,
			9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 99, 3, 9, 1002, 9, 2, 9,
			4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4,
			9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9,
			3, 9, 102, 2, 9, 9, 4, 9, 99 };

	static final int AMPLIFIERS = 5;
	static final int LAST_AMP = AMPLIFIERS - 1;

	static class Amplifier {

		private static final int HCF = 99;
		private static final int ADD = 1;
		private static final int MUL = 2;
		private static final int INP = 3;
		private static final int OUT = 4;
		private static final int JIT = 5;
		private static final int JIF = 6;
		private static final int LES = 7;
		private static final int EQU = 8;

		int adr = 0;
		int[] mem;
		List<Integer> input = new ArrayList<>();
		int output = -1;

		Amplifier(int[] program, List<Integer> data) {
			mem = Arrays.copyOf(program, program.length + 10);
			input.addAll(data);
		}

		boolean halted() {
			return mem[adr] == HCF;
		}

		int run(List<Integer> data) {
			if (halted())
				throw new RuntimeException("already halted - cannot run");

			input.addAll(data);

			execution: while (!halted()) {

				int instr = mem[adr];
				int opcode = instr % 100;

				int p1 = mem[adr + 1];
				int p2 = mem[adr + 2];
				int p3 = mem[adr + 3];

				boolean imm1 = (instr / 100 % 10) == 1;
				boolean imm2 = (instr / 1000 % 10) == 1;
				boolean imm3 = (instr / 10000 % 10) == 1;
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
					output = arg1.getAsInt();
					adr += 2;
					break execution;

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
					System.out.println("halt");
					break;
				default:
					throw new RuntimeException("invalid opcode " + opcode + " at adr " + adr);
				}

			}
			return output;
		}
	}

	static int part1(int[] code) {
		var perms = Generator.permutation(0, 1, 2, 3, 4).simple();
		var result = new HashMap<String, Integer>();

		for (var phaseSetting : perms) {
			int output = 0;
			for (int i = 0; i < AMPLIFIERS; ++i) {
				output = new Amplifier(code, List.of(phaseSetting.get(i))).run(List.of(output));
			}
//			System.out.println(phaseSetting + " --> " + output);
			result.put(phaseSetting.toString(), output);
		}

		var bestSetting = result.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
		System.out.println("best setting: " + bestSetting);
		return bestSetting.getValue();
	}

	
	static int part2(int[] code) {
		var perms = Generator.permutation(5, 6, 7, 8, 9).simple();
		Amplifier[] amp = new Amplifier[AMPLIFIERS];
		var result = new HashMap<String, Integer>();

		for (var phaseSetting : perms) {

			Arrays.setAll(amp, i -> new Amplifier(code, List.of(phaseSetting.get(i))));
			int[] output = { 0, 0, 0, 0, 0 };
			int i = 0;

			while (!amp[LAST_AMP].halted()) {
				var input = output[(i + AMPLIFIERS - 1) % AMPLIFIERS];
				output[i] = amp[i].run(List.of(input));
//					System.out.println("amp=" + a + " inp= " + in + " outp=" + output[a]);
				i = (i + 1) % AMPLIFIERS;
			}

//			System.out.println(phaseSetting + " --> " + output[LAST_AMP]);
			result.put(phaseSetting.toString(), output[LAST_AMP]);
		}

		var bestSetting = result.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
		System.out.println("best setting: " + bestSetting);
		return bestSetting.getValue();
	}

}
