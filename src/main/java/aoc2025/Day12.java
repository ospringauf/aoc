package aoc2025;

import common.AocPuzzle;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 12: Christmas Tree Farm ---
// https://adventofcode.com/2025/day/12

class Day12 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day12().part1());
        System.out.println("=== part 2");
        timed(() -> new Day12().part2());
    }

//    List<String> data = file2lines("input00.txt");
	String data = example;

	record Problem(int w, int h, List<Integer> presents) {
		static Problem parse(String s) {
			var a = s.split(": ");
			int w = Integer.parseInt(a[0].split("x")[0]);
			int h = Integer.parseInt(a[0].split("x")[0]);
			var p = Util.string2ints(a[1]);
			return new Problem(w,h,p);
		}
	}
	
    void part1() {
    	var a = data.split("\n\n");
    	var problems = Util.splitLines(a[6]).map(Problem::parse);
    	System.out.println(problems);
    	
    	var shapes = List.range(0, 5).map(i -> {
    		var l = Util.splitLines(a[i]);
    		var m = new PointMap<Character>();
    		m.read(l.tail());
    		return m;
    	});
    	System.out.println(shapes);
    }

    void part2() {
    }

    static String example = """
0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2""";
}
