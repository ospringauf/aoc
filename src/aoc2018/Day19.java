package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.jooq.lambda.Seq;


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
				if (i % 1000000 == 0) {
					System.out.println("" + i + "  ip=" + ip + "  [ " + seq(Arrays.stream(reg)).toString(", ") + " ]");
				}
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
//				if (i % 1000000 == 0) 
//				{
//					System.out.println("" + i + "  ip=" + ip + "  [ " + seq(Arrays.stream(reg)).toString(", ") + " ]");
//				}
			}
		}
	}
	
	static void prog() {
		int r0=1, r1=0, r2=0, r3=0, r4=0, r5=0;
		
		while (r5 <= 35) {
			switch (r5) {
			case 0:
				//addi 5 16 5     
				r5 = 16; 
				break;
			case 1:
				// seti 1 1 2      
				r2 = 1; 
			case 2: 
				// seti 1 8 1      
				r1 = 1;
			case 3:
				// 3 - mulr 2 1 3      
				r3 = r1*r2;  
				// 4 - eqrr 3 4 3      
				r3 = (r3==r4) ? 1 : 0;
				// 5 - addr 3 5 5
				r5 = 5 + r3; // 6/7 
				break;
			case 6:
				// 6 - addi 5 1 5		
				r5 = 6 + 1;
				break;
			case 7:
				// 7- addr 2 0 0		
				r0 = r0+r2;
				System.out.println("r0=" + r0);
			case 8:
				// 8 - addi 1 1 1		
				r1 = r1+1;
				// 9 - gtrr 1 4 3		
				r3 = (r1>r4) ? 1 : 0;
				// 10 - addr 5 3 5		# 10 	r5 = r5+r3	if (r1 > r4) goto 12
				r5 = 10 + r3; // 11/12
				break;
			case 11:
				// 11 - seti 2 6 5		
				r5 = 2;	//	goto 1/2
				break;
			case 12:
				// 12 - addi 2 1 2		
				r2 = r2+1;
//			case 13:
				// 13 - gtrr 2 4 3		
				r3 = (r2>r4) ? 1 : 0;
//			case 14:
				// 14 - addr 3 5 5		# 14	r5 = r5+r3	if (r2>r4) goto 16/exit
				r5 = 14 + r3;
				break;
			case 15:
				// 15 - seti 1 2 5		# 15	r5 = 1 goto 1
				r5 = 1;
				break;
			case 16:
				// 16 - mulr 5 5 5		# 16	r5 = r5*r5	exit
				r5 = 16*16;
				break;
				
			// subroutine
			case 17:
				// 17 - addi 4 2 4
				r4 = r4+2;
//			case 18:
				// 18 - mulr 4 4 4
				r4 = r4*r4;
//			case 19:
				// mulr 5 4 4
				r4 = r4*19;
//			case 20:
				// 20 - muli 4 11 4
				r4 = 11*r4;
//			case 21:
				// 21 - addi 3 2 3
				r3 = r3+2;
//			case 22:
				// 22 - mulr 3 5 3
				r3 = r3*22;
//			case 23:
				// 23 - addi 3 13 3
				r3 = r3+13;
//			case 24:
				// addr 4 3 4
				r4 = r4+r3;
//			case 25:
				// addr 5 0 5
				r5 = 25+r0;
				break;
			case 26:
				// seti 0 8 5
				r5 = 0;
				break;
			case 27:
				// setr 5 5 3
				r3 = 27;
//			case 28:
				// mulr 3 5 3
				r3 = r3*28;
//			case 29:
				// addr 5 3 3
				r3 = r3+29;
//			case 30:
				// mulr 5 3 3
				r3 = r3*30;
//			case 31:
				// muli 3 14 3
				r3 = r3*14;
//			case 32:
				// mulr 3 5 3
				r3 = r3*32;
//			case 33:
				// addr 4 3 4
				r4 = r4+r3;
//			case 34:
				// seti 0 9 0
				r0 = 0;
//			case 35: 
				// seti 0 9 5
				r5 = 0;
				break;
			default:
				System.out.println("invalid adr: " + r5);
			}
			r5++;
//			System.out.println(r5);
		}
		
		System.out.println("prog - reg0: " + r0);
	}

	public static void main(String[] args) throws Exception {
		List<String> input = Util.lines("aoc2018/day19.txt");

		prog();
//		Cpu cpu = new Cpu();
//		cpu.ipr = 5;
//		cpu.reg[0] = 1;
//		Op[] prog = (Op[]) seq(input).skip(1).map(l -> Op.parse(l)).toList().toArray(new Op[0]);
//		for(Op x : prog) {
//			x.op = cpu.ops.get(x.opcode);
//		}
//		
//		cpu.exec2(prog);
//		System.out.println(cpu.reg[0]);
		
	}

}
