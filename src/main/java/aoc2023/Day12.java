package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 12:  ---
// https://adventofcode.com/2023/day/12

class Day12 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day12().part1());
        System.out.println("=== part 2");
//        timed(() -> new Day12().part2());
    }

//    List<String> data = file2lines("input12.txt");
    List<String> data = Util.splitLines(example);

	void part1() {
		//var x = solve2("?###????????", List.of(3,2,1));
		//var x = solve2("????????", List.of(2,1));
//		var x = solve2(".", List.of());
		var x = solve2("?????", List.of(2,1));
		System.out.println(x);
	}  
	
	int mind(String s) {
		int r = 0;
		for (int i=0; i<s.length(); ++i) {
			if (s.charAt(i)=='#') r++;
		}
		return r;
	}

    int solve2(String s, List<Integer> l) {
    	System.out.println("s=" + s + "  l=" + l);
    	if (s.length() == 0) {
    		if (l.size() == 0) return 1;
    		if (l.size() ==1 && l.head().intValue() == 0) return 1;
    		return 0;
    	}
    	if (l.isEmpty()) {
    		return mind(s) == 0? 1: 0;
    	}
    	var c = s.charAt(0);
    	var s2 = s.substring(1);
    	
    	var h = l.head().intValue();
    	if (h == 0) {
    		if (c == '#') return 0;
    		return solve2(s2, l.tail());
    	} else {
    		// #
    		if (c == '#') 
    			return solve2(s2, List.of(h-1).appendAll(l.tail()));
    		// .
    		if (c == '.')
    			return 0;
    		// ?
    		var r1 = solve2(s2, l); //.
    		var r2 = solve2(s2, List.of(h-1).appendAll(l.tail())); //#
    		return r1+r2;
    	}
	}

	void part2() {
   	
//    	var l = data.last();
//    	var springs = l.split(" ")[0] + ".";
//    	var dmg = Util.string2ints(l.split(" ")[1].replace(',', ' '));
//    	
//    	var r = solve(springs, dmg, "");

//    	List<Integer> dmg = List.of(1); String springs = "????.";
//    	List<Integer> dmg = List.of(2,1); String springs = "???????.";
    	List<Integer> dmg = List.of(3,2,1); String springs = "?###????????.";
		var r = solve(springs, dmg, "");
    	
    	System.out.println("=== " + r);
    	System.out.println(springs + " (" + dmg.mkString(",") + ") -> " + r);
    	
    }

    long solve(String s, List<Integer> dmg, String pref) {
    	if (dmg.isEmpty())
    		return 1;
		var dmg1 = dmg.head();
		var rem = dmg.tail().map(x -> x+1).sum().intValue();
		
		if (dmg.tail().isEmpty())
			return ways(s, dmg1);
		
		var min = dmg1+1;
		var max = s.length()-rem;
		
		
		var r = 0L;
		for (int i=min; i<=max; ++i) {
			if (s.charAt(i-1) == '#') continue;
			var s1 = s.substring(0, i-1) + ".";
			var s2 = s.substring(i);
			var p1 = ways(s1, dmg1);
			var p2 = (p1 > 0) ? solve(s.substring(i), dmg.tail(), pref + "   ") : 0;
			System.out.println(pref+"s1="+s1+ "  s2="+s2 + "  p1="+p1 + "  dmg1="+dmg1 + "  p2="+p2);
			r += p2;
		}
		System.out.println(pref + r);
		return r;
	}

    // each block ends with .
    
    
	long ways(String s, Integer dmg) {
		int r = 0;
		for (int i=0; i<=s.length()-dmg; ++i) {
			// put ####. into s at position i
			var d = 0;
			for (int j=0; j<s.length(); ++j) {
				var c = s.charAt(j);
				if (j >= i && j < i+dmg) {
					if (c == '.') {
						d = -1;
						break;
					}
						d++;
				} else {
					if (c == '#') d++;
				}
			}
			if (d == dmg) r++;
		}
		return r;
	}

	static String example0 = """
            #.#.### 1,1,3
            .#...#....###. 1,1,3
            .#.###.#.###### 1,3,1,6
            ####.#...#... 4,1,1
            #....######..#####. 1,6,5
            .###.##....# 3,2,1
                        """;

    static String example = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;
}
