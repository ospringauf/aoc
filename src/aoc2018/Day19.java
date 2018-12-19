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

	public static void main(String[] args) throws Exception {
		List<String> input = Util.lines("aoc2018/day19.txt");

		Cpu cpu = new Cpu();
		cpu.ipr = 5;
		cpu.reg[0] = 1;
		Op[] prog = (Op[]) seq(input).skip(1).map(l -> Op.parse(l)).toList().toArray(new Op[0]);
		for(Op x : prog) {
			x.op = cpu.ops.get(x.opcode);
		}
		
		cpu.exec2(prog);
		System.out.println(cpu.reg[0]);
		
	}

}
