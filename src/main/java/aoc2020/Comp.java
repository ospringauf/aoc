package aoc2020;

import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

@SuppressWarnings({ "deprecation", "preview" })
class Comp {

	public enum Opcode {
		NOP,
		JMP,
		ACC
	}
	
	public static record Instr(Opcode op, long arg) {
		static Instr parse(String s) {
			var a = s.split(" ");
			return new Instr(Opcode.valueOf(a[0].toUpperCase()), Long.valueOf(a[1]));
		}
		
		public String toString() {
			return String.format("%s %d", op, arg);
		}
	}
	
	static Array<Instr> parseProg(List<String> lines) {
		return lines.map(Instr::parse).toArray();
	}

	static Array<Instr> parseProg(String lines) {
		return Array.of(lines.split("\\n")).map(Instr::parse);
	}

	long acc = 0;

	boolean execute(final Seq<Instr> prog) {

		int iptr = 0;
		acc = 0;
		var visited = new boolean[prog.size()];

		while (iptr < prog.size() && ! visited[iptr]) {

			visited[iptr] = true;
			var instr = prog.get(iptr);

			switch (instr.op) {
			case NOP:
				iptr++;
				break;
			case ACC:
				acc += instr.arg;
				iptr++;
				break;
			case JMP:
				iptr += instr.arg;

			}
		}

		return iptr >= prog.size();
	}
}
