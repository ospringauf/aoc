package aoc2016;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 23:  ---
// https://adventofcode.com/2016/day/23

class Day23 extends AocPuzzle {

    record Op(String code, String p1, String p2) {
        static Op parse(String s) {
            var sr = split(s, " ");
            if (sr.fields().length == 2)
                return new Op(sr.s(0), sr.s(1), "");
            else 
                return new Op(sr.s(0), sr.s(1), sr.s(2));
        }
        
        public String toString() {
            return code + " " + p1 + " " + p2;
        }
        
        Op toggle() {
            var c = switch(code) {
            case "inc" -> "dec";
            case "dec" -> "inc";
            case "tgl" -> "inc";
            case "jnz" -> "cpy";
            case "cpy" -> "jnz";
            default -> "";
            };
            return new Op(c, p1, p2);
        }
    }
    
    List<String> data = file2lines("day23.txt");
//    List<String> data = Util.splitLines(example);
    
	void part1() {
	    runProg(7);
	}

    void runProg(int eggs) {
        var prog = data.map(Op::parse);
	    var reg = HashMap.of("a", eggs, "b", 0, "c", 0, "d", 0);
	    var i = 0;
	    
	    while (i < prog.size()) {
	        var op = prog.get(i);
	        switch (op.code) {
            case "inc" -> {
                reg = reg.put(op.p1, reg.getOrElse(op.p1, 0) + 1);
                i++;
            }
            case "dec" -> {
                reg = reg.put(op.p1, reg.getOrElse(op.p1, 0) - 1);
                i++;
            }
            case "cpy" -> {
                var valid = reg.containsKey(op.p2);
                if (valid) {
                    var v = reg.get(op.p1).getOrElse(() -> Integer.parseInt(op.p1));
                    reg = reg.put(op.p2, v);
                }
                i++;
            }
            case "jnz" -> {
                var x = reg.get(op.p1).getOrElse(() -> Integer.parseInt(op.p1));
                var y = reg.get(op.p2).getOrElse(() -> Integer.parseInt(op.p2));
                if (x != 0)
                    i += y;
                else 
                    i++;
            }
            case "tgl" -> {
                var x = reg.get(op.p1).getOrElse(() -> Integer.parseInt(op.p1));
                if (i+x < prog.size()) {
                    System.out.println("toggle " + (i+x));
                    prog = prog.update(i+x, prog.get(i+x).toggle());
                }
                i++;
            }
            default ->
                throw new IllegalArgumentException("Unexpected value: " + op.code);
            }
	        
//	        System.out.println(prog);
	    }
	    
	    System.out.println(reg);
    }

	void part2() {
	    System.out.println("takes about 2 minutes, please wait ...");
	    runProg(12);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); 
		timed(() -> new Day23().part1());

		System.out.println("=== part 2"); 
		timed(() -> new Day23().part2());
	}
	
	static String example = """
cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a	        
	        """;
}
