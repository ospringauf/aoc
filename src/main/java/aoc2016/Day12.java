package aoc2016;

import common.AocPuzzle;

// --- Day 12: Leonardo's Monorail ---
// https://adventofcode.com/2016/day/12

class Day12 extends AocPuzzle {

	void part1() {
	    int a,b,c,d;
	    a=1;
	    b=1;
	    d=26;
	    // ...
	    while (d > 0) {
	        c=a;
	        a=a+b;
	        b=c;
	        d--;
	    }
	    a=a+12*16;
	    System.out.println(a);
	}

	void part2() {
        int a,b,c,d;
        a=1;
        b=1;
        d=26+7;
        
        while (d > 0) {
            c=a;
            a=a+b;
            b=c;
            d--;
        }
        a=a+12*16;
        System.out.println(a);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 318003
		timed(() -> new Day12().part1());

		System.out.println("=== part 2"); // 9227657
		timed(() -> new Day12().part2());
	}
	
	String example = """
cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
	        """;
}
