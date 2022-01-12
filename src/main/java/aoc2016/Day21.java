package aoc2016;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 21: Scrambled Letters and Hash ---
// https://adventofcode.com/2016/day/21

class Day21 extends AocPuzzle {

	void part1() {
//	    var s = "abcde";
//        var instr = Util.splitLines(example);
	    var s = "abcdefgh";
	    var instr = file2lines("day21.txt");
	    var l = List.ofAll(s.toCharArray());
	    
	    l = scramble(instr, l);
	    System.out.println(l.mkString());
	    
	}

    private List<Character> scramble(List<String> instr, List<Character> l) {
        for (var i : instr) {
	        var r = split(i, " ");
	        switch (r.s(0) + r.s(1)) {
	        case "swapposition" -> {
	            var p1 = r.i(2);
	            var p2 = r.i(5);
	            var c1 = l.get(p1);
	            var c2 = l.get(p2);
	            l = l.update(p1, c2).update(p2, c1);
	        }
            case "swapletter" -> {
                var c1 = r.c(2);
                var c2 = r.c(5);
                l = l.map(c -> (c==c1)? c2 : (c==c2)? c1 : c);
            }
            case "rotateleft" -> {
                var n = r.i(2);
                l = l.rotateLeft(n);
            }
            case "rotateright" -> {
                var n = r.i(2);
                l = l.rotateRight(n);
            }
            case "rotatebased" -> {
                var c = r.c(6);
                var p = l.indexOf(c);
                var n = 1 + p + ((p>=4)? 1:0);
                l = l.rotateRight(n);
            }
            case "reversepositions" -> {
                var p1 = r.i(2);
                var p2 = r.i(4);
                l = l.take(p1).appendAll(l.drop(p1).take(p2-p1+1).reverse()).appendAll(l.drop(p2+1));
            }
            case "moveposition" -> {
                var p1 = r.i(2);
                var p2 = r.i(5);
                var c = l.get(p1);
                l = l.removeAt(p1).insert(p2, c);
            }
            default ->
                throw new IllegalArgumentException("Unexpected value: " + r.s(0) + r.s(1));
            }
	    }
        return l;
    }

    void part2() {
        
        var s = "fbgdceah";
        var instr = file2lines("day21.txt");
        var x = List.ofAll("abcdefgh".toCharArray()).asJava();
        var ap = Generator.permutation(x).simple();
        for (var p : ap) {
            var l = List.ofAll(p);
            var r = scramble(instr, l);
            if (r.mkString().equals(s)) {
                System.out.println(p);
            }            
        }
    }
    

	public static void main(String[] args) {
		System.out.println("=== part 1"); // cbeghdaf 
		timed(() -> new Day21().part1());

		System.out.println("=== part 2"); // bacdefgh
		timed(() -> new Day21().part2());
	}
	
	String example = """
swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
swap letter d with letter b swaps the positions of d and b: edcba.
reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.	        
	        """;
}
