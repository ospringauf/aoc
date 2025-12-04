package aoc2025;

import common.AocPuzzle;
import io.vavr.Function2;
import io.vavr.collection.List;

//--- Day 3: Lobby ---
// https://adventofcode.com/2025/day/3

class Day03 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 17193
        timed(() -> new Day03().part1());
        System.out.println("=== part 2"); // 171297349921310
        timed(() -> new Day03().part2());
    }

    List<String> data = file2lines("input03.txt");
//	List<String> data = Util.splitLines(example);

	int joltage1(String s) {
		var v = List.ofAll(s.toCharArray()).map(c -> c-'0');
		var max = List.range(0, v.length()).map(i -> v.subSequence(i).max().get());
		var j = List.range(0, v.length()-1).map(i -> 10*v.get(i) + max.get(i+1));
		return j.max().get();
	}
	
	long joltage2(String s) {
		var v = List.ofAll(s.toCharArray()).map(c -> c-'0');
//		System.out.println(v);
		
		return pick(12, v);
	}
	
	
	Function2<Integer, List<Integer>, Long> mpick = Function2.of(this::pick).memoized();
	
    long pick(int n, List<Integer> v) {
    	if (n == 0)
    		return 0L;
    	if (n > v.length())
    		return 0L;
    	if (n == 1) 
    		return v.max().get().longValue();
    	
    	// take head, pick (n-1) from tail
		var m1 = mpick.apply(n-1, v.tail());
		var j1 = Long.valueOf(v.head().toString() + m1.toString());
		
		// skip head, pick all from tail
		var j0 = mpick.apply(n, v.tail());
		
		var j = Math.max(j0,  j1);
		return j;
	}

    
	void part1() {
    	var j = data.map(s -> joltage1(s));
    	System.out.println(j);
    	System.out.println(j.sum());
    }

    void part2() {
    	var j = data.map(s -> joltage2(s));
    	System.out.println(j);
    	System.out.println(j.sum());
    }

    static String example = """
987654321111111
811111111111119
234234234234278
818181911112111""";
}
