package aoc2025;

import common.AocPuzzle;
import io.vavr.Function2;
import io.vavr.collection.List;

//--- Day 3: Lobby ---
// https://adventofcode.com/2025/day/3

class Day03a extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 17193
        timed(() -> new Day03a().part1());
        System.out.println("=== part 2"); // 171297349921310
        timed(() -> new Day03a().part2());
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
		
		// Greedy approach: at each position, pick the largest digit 
		// that still leaves enough digits to complete the selection
		long result = 0;
		int picked = 0;
		int pos = 0;
		
		while (picked < 12) {
			// Find the maximum digit in the window where we can still pick enough remaining digits
			int windowEnd = v.length() - (12 - picked - 1);
			int maxIdx = pos;
			int maxVal = v.get(pos);
			
			for (int i = pos + 1; i < windowEnd; i++) {
				if (v.get(i) > maxVal) {
					maxVal = v.get(i);
					maxIdx = i;
				}
			}
			
			result = result * 10 + maxVal;
			pos = maxIdx + 1;
			picked++;
		}
		
		return result;
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
