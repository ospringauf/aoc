package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

// --- Day 25: Clock Signal ---
// https://adventofcode.com/2016/day/25

class Day25 extends AocPuzzle {

    List<String> data = file2lines("day25.txt");

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
    }
    
    List<Integer> runProg(int a, int loops) {      
        List<Integer> out = List.empty();
        var prog = data.map(Op::parse);
        var reg = HashMap.of("a", a, "b", 0, "c", 0, "d", 0);
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
            case "out" -> {
                var x = reg.get(op.p1).getOrElse(() -> Integer.parseInt(op.p1));
                out = out.append(x);
                loops--;
                if (loops <= 0) {
                    return out;
                }
                i++;
            }
            default ->
                throw new IllegalArgumentException("Unexpected value: " + op.code);
            }
        }
        
        return out;
    }
    
    boolean isClock(List<Integer> l) {
        return l.sliding(2).forAll(x -> x.distinct().size() == 2);
    }
    
	void part1() {
	    int a = 1;
	    while (true) {
	        var out = runProg(a, 50);
	        if (isClock(out)) {
	            System.out.println(a);
	            return;
	        }
	        a++;
	    }
	}

	void part2() {
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); 
		timed(() -> new Day25().part1());

		System.out.println("=== part 2"); 
		timed(() -> new Day25().part2());
	}
}
