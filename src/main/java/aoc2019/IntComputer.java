package aoc2019;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.LongSupplier;

public class IntComputer {

	private static final int HCF = 99;
	private static final int ADD = 1;
	private static final int MUL = 2;
	private static final int INP = 3;
	private static final int OUT = 4;
	private static final int JIT = 5;
	private static final int JIF = 6;
	private static final int LES = 7;
	private static final int EQU = 8;
	private static final int RBO = 9;

	int adr = 0;
	int relBase = 0;
	protected long[] mem;

	public LongSupplier input;

	public IntComputer(long[] program) {
	    mem = Arrays.copyOf(program, 1000000);
	}
	
	public IntComputer() {}
	
	void output(long value) {
		System.out.println("output: " + value);
	}

	boolean halted() {
		return mem[adr] == HCF;
	}
	
	boolean complete() {
		return false;
	}

	void run() {
		if (halted())
			throw new RuntimeException("already halted - cannot run");
	
		while (!halted() && !complete()) {
	
			run1();
		}
	}

	void run1() {
		if (halted())
			throw new RuntimeException("already halted - cannot run");
		
		long instr = mem[adr];
		int opcode = (int) (instr % 100);

		boolean imm[] = { false, (instr / 100 % 10) == 1, (instr / 1000 % 10) == 1, (instr / 10000 % 10) == 1 };
		boolean rel[] = { false, (instr / 100 % 10) == 2, (instr / 1000 % 10) == 2, (instr / 10000 % 10) == 2 };
		long[] p = { 0, mem[adr + 1], mem[adr + 2], mem[adr + 3] };

		IntFunction<Long> arg = n -> imm[n] ? p[n] : rel[n] ? mem[(int) (p[n] + relBase)] : mem[(int) p[n]];
		IntFunction<Integer> tgt = n -> (int) (rel[n] ? p[n] + relBase : p[n]);

		switch (opcode) {
		case ADD:
			mem[tgt.apply(3)] = arg.apply(1) + arg.apply(2);
			adr += 4;
			break;

		case MUL:
			mem[tgt.apply(3)] = arg.apply(1) * arg.apply(2);
			adr += 4;
			break;

		case INP:
			mem[tgt.apply(1)] = input.getAsLong();
			adr += 2;
			break;

		case OUT:
			output(arg.apply(1));
			adr += 2;
			break;

		case JIT:
			if (arg.apply(1) != 0)
				adr = arg.apply(2).intValue();
			else
				adr += 3;
			break;

		case JIF:
			if (arg.apply(1) == 0)
				adr = arg.apply(2).intValue();
			else
				adr += 3;
			break;

		case LES:
			mem[tgt.apply(3)] = (arg.apply(1) < arg.apply(2)) ? 1 : 0;
			adr += 4;
			break;

		case EQU:
			mem[tgt.apply(3)] = (arg.apply(1).equals(arg.apply(2))) ? 1 : 0;
			adr += 4;
			break;

		case RBO:
			relBase += arg.apply(1);
			adr += 2;
			break;

		case HCF:
			System.out.println("halt");
			break;
			
		default:
			throw new RuntimeException("invalid opcode " + opcode + " at adr " + adr);
		}
	}

	
}