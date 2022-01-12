package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 18: Like a Rogue ---
// https://adventofcode.com/2016/day/18

class Day18 extends AocPuzzle {

    String input = "^..^^.^^^..^^.^...^^^^^....^.^..^^^.^.^.^^...^.^.^.^.^^.....^.^^.^.^.^.^.^.^^..^^^^^...^.....^....^.";
    
    String next(String s) {
        var trap = List.of("^^.", ".^^", "^..", "..^");
        var l = List.ofAll(("." + s + ".").toCharArray());
        return l.sliding(3).map(w -> trap.contains(w.mkString())? "^" : ".").mkString();
    }
    
	void part1() {
	    var l = List.of(input);
	    for (int i=1; i<40; ++i)
	        l = l.prepend(next(l.head()));

	    System.out.println(List.ofAll(l.mkString().toCharArray()).count(c -> c =='.'));
	}

	void part2() {
	    var s = input;
	    var r = 0;
        for (int i=1; i<=400000; ++i) {
            r += List.ofAll(s.toCharArray()).count(c -> c =='.');
            s = next(s);
        }
        System.out.println(r);
	    
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 2016
		timed(() -> new Day18().part1());

		System.out.println("=== part 2"); // 19998750
		timed(() -> new Day18().part2());
	}
}
