package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class Day19 {
	
	static class Op {
		String opcode;
		Consumer<Op> op;
		int a, b, c;
		
		static Op parse(String op) {
			Op s = new Op();
			String[] x = op.split("\\s+");
			s.opcode = x[0];
			s.a = Integer.parseInt(x[1]);
			s.b = Integer.parseInt(x[2]);
			s.c = Integer.parseInt(x[3]);
			return s;
		}
		
		@Override
		public String toString() {
			return opcode + " " + a + " " + b + " " + c;
		}
	}
	
	
	static class Cpu {
		long reg[] =  new long[6];
		int ip = 0;
		int ipr = 0;
		
		Consumer<Op> addr = op -> reg[op.c] = reg[op.a]+reg[op.b];
		Consumer<Op> addi = op -> reg[op.c] = reg[op.a]+op.b;
		Consumer<Op> mulr = op -> reg[op.c] = reg[op.a]*reg[op.b];
		Consumer<Op> muli = op -> reg[op.c] = reg[op.a]*op.b;
		Consumer<Op> banr = op -> reg[op.c] = reg[op.a]&reg[op.b];
		Consumer<Op> bani = op -> reg[op.c] = reg[op.a]&op.b;
		Consumer<Op> borr = op -> reg[op.c] = reg[op.a]|reg[op.b];
		Consumer<Op> bori = op -> reg[op.c] = reg[op.a]|op.b;
		Consumer<Op> setr = op -> reg[op.c] = reg[op.a];
		Consumer<Op> seti = op -> reg[op.c] = op.a;
		Consumer<Op> gtir = op -> reg[op.c] = (op.a>reg[op.b])? 1 : 0;
		Consumer<Op> gtri = op -> reg[op.c] = (reg[op.a]>op.b)? 1 : 0;
		Consumer<Op> gtrr = op -> reg[op.c] = (reg[op.a]>reg[op.b])? 1 : 0;
		Consumer<Op> eqir = op -> reg[op.c] = (op.a==reg[op.b])? 1 : 0;
		Consumer<Op> eqri = op -> reg[op.c] = (reg[op.a]==op.b)? 1 : 0;
		Consumer<Op> eqrr = op -> reg[op.c] = (reg[op.a]==reg[op.b])? 1 : 0;

		// opcode -> operation assignment
		Map<String, Consumer<Op>> ops = new HashMap<>();	
	
		{
			ops.put("addr", addr);
			ops.put("addi", addi);
			ops.put("mulr", mulr);
			ops.put("muli", muli);
			ops.put("banr", banr);
			ops.put("bani", bani);
			ops.put("borr", borr);
			ops.put("bori", bori);
			ops.put("setr", setr);
			ops.put("seti", seti);
			ops.put("gtir", gtir);
			ops.put("gtri", gtri);
			ops.put("gtrr", gtrr);
			ops.put("eqir", eqir);
			ops.put("eqri", eqri);
			ops.put("eqrr", eqrr);
		}
		
		void exec(Op op) {
			reg[ipr] = ip;
//			System.out.print("ip="+ip + " [ " + seq(Arrays.stream(reg)).toString(", ") + " ] " + op);
			Consumer<Op> f = ops.get(op.opcode);
			f.accept(op);
//			System.out.println(" [ " + seq(Arrays.stream(reg)).toString(", ") + " ]");
			ip = (int) reg[ipr];
			ip++;
		}
	
		public void exec(List<Op> prog) {
//			range(0,4).forEach(i -> reg[i] = 0);
			
			long i=0;
			while (ip < prog.size()) {
				exec(prog.get(ip));			
				i++;
			}
		}
		
		public void exec2(Op[] prog) {
//			range(0,4).forEach(i -> reg[i] = 0);
			
			long i=0;
			while (ip < prog.length /*&& i < 1000000*/) {
				reg[ipr] = ip;
				Op instr = prog[ip];
				instr.op.accept(instr);
				ip = (int) reg[ipr];
				ip++;
				
				i++;
			}
		}
	}

//	static int prog2() {
//		int r0=1, r1=0, r2=0, r3=0, r4=0, r5=0;
//		
//		//	00	addi 5 16 5
//		goto 17
//		//	01	seti 1 1 2
//		r2 = 1;
//		//	02	seti 1 8 1
//		r1 = 1;
//		
//		//	03	mulr 2 1 3
//		r3 = r1*r2;
//		//	04	eqrr 3 4 3
//		r3 = (r3==r4)? 1 : 0;
//		//	05	addr 3 5 5
//		//if (r3==r4) goto 7 else goto 8
//		//	06	addi 5 1 5
//		//	07	addr 2 0 0
//		if (r3==r4) 
//			r0 += r2;
//		//	08	addi 1 1 1
//		r1++;
//		//	09	gtrr 1 4 3
//		r3 = (r1==r4)? 1 : 0;
//		//	10	addr 5 3 5
//		//	11	seti 2 6 5
//		if (r1==r4) goto 12 else goto 3 // do {} while r1!=r4
//		
//		//	12	addi 2 1 2
//		r2++;
//		//	13	gtrr 2 4 3
//		r3 = (r2>r4)? 1 : 0;
//		//	14	addr 3 5 5
//		//	15	seti 1 2 5
//		if (r2>r4) goto 16 else goto 2
//		
//		//	16	mulr 5 5 5
//		return r0; // exit
//		
//		//	17	addi 4 2 4
//		r4 += 2;
//		//	18	mulr 4 4 4
//		r4 *= r4;
//		//	19	mulr 5 4 4
//		r4 *= 19;
//		//	20	muli 4 11 4
//		r4 *= 11;
//		//	21	addi 3 2 3
//		r3 += 2;
//		//	22	mulr 3 5 3
//		r3 *= 22;
//		//	23	addi 3 13 3
//		r3 += 13;
//		//	24	addr 4 3 4
//		r4 += r3;
//		//	25	addr 5 0 5
//		//	26	seti 0 8 5
//		r5 = 1; if r0=0 return 
//		//	27	setr 5 5 3
//		r3 = 27;
//		//	28	mulr 3 5 3
//		r3 *= 28;
//		//	29	addr 5 3 3
//		r3 += 29;
//		//	30	mulr 5 3 3
//		r3 *= 30;
//		//	31	muli 3 14 3
//		r3 *= 14;
//		//	32	mulr 3 5 3
//		r3 *= 32;
//		//	33	addr 4 3 4
//		r4 += r3;
//		//	34	seti 0 9 0
//		r0 = 0;
//		//	35	seti 0 9 5
//		return 
//		
//	}
	
	static int prog2() {
		int r0=1, r1=0, r2=0, r3=0, r4=0, r5=0;
		
		r4 = 2*2*19*11;
		r3 = 2*22 + 13;
		r4 += r3;
		if (r0==1) { 
			r3 = (27*28 + 29)*30*14*32;
			r4 += r3;
			r0 = 0;
		}

		//	01	seti 1 1 2
		r2 = 1;
		do {
		//	02	seti 1 8 1
		r1 = 1;
		
		do {
			if (r1*r2 == r4) 
				r0 += r2;
			r1++;
		} while (r1 != r4);
		
		
		r2++;
		} while (r4 <= r2);
		
		//	16	mulr 5 5 5
		return r0; // exit
		
		
	}	
	
	static int prog3() {
		int r0 = 0, r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;

		r4 = 2 * 2 * 19 * 11;
		r3 = 2 * 22 + 13;
		r4 += r3;
		if (r0 == 1) {
			r3 = (27 * 28 + 29) * 30 * 14 * 32;
			r4 += r3;
			r0 = 0;
		}

		r2 = 1;
		do {
			r1 = 1;
			do {
				if (r1 * r2 == r4) {
					r0 += r2;
					System.out.println(r0);
				}
				r1++;
			} while (r1 != r4);

			r2++;
		} while (r2 <= r4);

		System.out.println("result: " + r0);
		return r0;
	}
	
	static int prog4() {
		int r0 = 1, r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;

		r4 = 2 * 2 * 19 * 11;
		r3 = 2 * 22 + 13;
		r4 += r3;
		if (r0 == 1) {
			r3 = (27 * 28 + 29) * 30 * 14 * 32;
			r4 += r3;
			r0 = 0;
		}
		System.out.println("r4=" + r4);

		for (r2 = 1; r2 <= r4; r2++) {
			if (r4 % r2 == 0) 
			{
				r0 += r2;
				System.out.println(r0);
			}
//			for (r1 = 1; r1 <= r4; r1++) {
//				if (r1 * r2 == r4)
//				{
//					r0 += r2;
//					System.out.println(r0);
//				}
//			}
		}

		System.out.println("result: " + r0);
		return r0;
	}
	

	public static void main(String[] args) throws Exception {
		List<String> input = Util.lines("aoc2018/day19.txt");

		Cpu cpu = new Cpu();
		cpu.ipr = 5;
		cpu.reg[0] = 0;
		Op[] prog = (Op[]) seq(input).skip(1).map(l -> Op.parse(l)).toList().toArray(new Op[0]);
		for(Op x : prog) {
			x.op = cpu.ops.get(x.opcode);
		}
		
		System.out.println("=== part 1 ===");
		cpu.exec2(prog);
		System.out.println(cpu.reg[0]);

		System.out.println("=== part 2 ===");
		prog4();

	}

}
