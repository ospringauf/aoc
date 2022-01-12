package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 20: Firewall Rules ---
// https://adventofcode.com/2016/day/20

class Day20 extends AocPuzzle {

    long MAX = 4294967295L;
    
    record Range(long from, long to) {
        static Range parse(String s) {
            return split(s, "-").to(r -> new Range(r.l(0), r.l(1)));
        }
        
        boolean contains(long l) {
            return from <= l && l <= to;
        }
        
        long size() {
            return to-from+1;
        }
    }
    
    List<Range> ranges = file2lines("day20.txt").map(Range::parse);

    void part1() {
	    var f = 0L; //ranges.minBy(Range::from).get().to + 1 ;
	    f = nextFree(f);
	    
	    System.out.println(f);
	}

    long nextFree(long f) {
        boolean repeat = true;
	    while (repeat) {
	        var f0 = f;
	        var f1 = ranges.filter(r -> r.contains(f0)).map(r -> r.to + 1).max().getOrElse(f0);
	        repeat = (f1 > f0);
	        f = f1;
	    }
        return f;
    }
    long nextBlocked(long f) {
        return ranges.filter(r -> r.from > f).map(r -> r.from).min().getOrElse(f);
    }

	void part2() {
	    var i = 0L;
	    var free = 0L;
	    while (i<MAX) {
	        i = nextFree(i);
	        var j = nextBlocked(i);
//	        System.out.println("free " + i + " - " + j);
	        if (i==j) break;
	        else {
	            free += (j-i);
	            i=j;
	        }	            
	    }
	    
	    System.out.println(free);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 23923783
		timed(() -> new Day20().part1());

		System.out.println("=== part 2"); // 125
		timed(() -> new Day20().part2());
	}
}
