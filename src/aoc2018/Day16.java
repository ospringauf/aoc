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


public class Day16 {
	
	static class Op {
		int opcode, a, b, c;
		
		static Op parse(String op) {
			Op s = new Op();
			String[] x = op.split("\\s+");
			s.opcode = Integer.parseInt(x[0]);
			s.a = Integer.parseInt(x[1]);
			s.b = Integer.parseInt(x[2]);
			s.c = Integer.parseInt(x[3]);
			return s;
		}
	}
	
	static class Sample {
		Op op;
		int[] before = new int[4];
		int[] after = new int[4];
		
		public static Sample parse(String bef, String op, String aft) {
			Sample s = new Sample();			
			s.op = Op.parse(op);
			s.before = Arrays.stream(bef.substring(9, bef.indexOf("]")).split("\\D+"))
					.mapToInt(i -> Integer.parseInt(i)).toArray();
			s.after = Arrays.stream(aft.substring(9, bef.indexOf("]")).split("\\D+"))
					.mapToInt(i -> Integer.parseInt(i)).toArray();
			return s;
		}
	}
	
	static class Cpu {
		int reg[] =  new int[4];
		
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
		Map<Integer, Consumer<Op>> ops = new HashMap<>();
		
		private List<Consumer<Op>> allOps; 
		
		{
			allOps = Seq.of(addr, addi, mulr, muli, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir, eqri, eqrr).toList();
		}
		
		boolean testSample(Sample s, Consumer<Op> op) {
			range(0,4).forEach(i -> reg[i] = s.before[i]);
			op.accept(s.op);
			
			return Arrays.equals(reg, s.after);
		}
		
		List<Consumer<Op>> matching(Sample s, List<Consumer<Op>> ops) {
			return seq(ops).filter(op -> testSample(s, op)).toList();
		}
		
		void assignOpCodes(List<Sample> l) {
			List<Consumer<Op>> unass = seq(allOps).toList();
			List<Sample> remain = new ArrayList<Sample>(l);
			
			while (unass.size() > 0) {
				Sample s = seq(remain).findFirst(x -> matching(x, unass).size() == 1).get();
				Consumer<Op> op = matching(s, unass).get(0);
				ops.put(s.op.opcode, op);
				unass.remove(op);
				remain = seq(remain).filter(x -> x.op.opcode != s.op.opcode).toList();
			}
		}

		public void exec(List<Op> prog) {
			range(0,4).forEach(i -> reg[i] = 0);
			seq(prog).forEach(x -> {
				Consumer<Op> op = ops.get(x.opcode);
				op.accept(x);				
			});			
		}
		
	}

	private static List<Sample> samples;
	
	public static void main(String[] args) throws Exception {
		List<String> input = Util.lines("aoc2018/day16a.txt");

		Cpu cpu = new Cpu();

//		// test (should find 3 ops)
//		Sample s = Sample.parse("Before: [3, 2, 1, 1]", "9 2 1 2", "After:  [3, 2, 2, 1]");
//		System.out.println(s);	
//		System.out.println(cpu.matching(s, cpu.allOps));
		
		// load samples
		samples = range(0,input.size()/4)
			.map(i -> Sample.parse(input.get(i*4), input.get(i*4+1), input.get(i*4+2)))
			.toList();
		
		long result1 = seq(samples).count(x -> cpu.matching(x, cpu.allOps).size() >= 3);
		System.out.println(result1);
		
		cpu.assignOpCodes(samples);
		
		// load program
		List<Op> prog = seq(Util.lines("aoc2018/day16b.txt")).map(x -> Op.parse(x)).toList();
		cpu.exec(prog);
		int result2 = cpu.reg[0];
		System.out.println(result2);
	}

}
