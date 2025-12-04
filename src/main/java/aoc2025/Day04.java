package aoc2025;

import common.AocPuzzle;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 4: Printing Department ---
// https://adventofcode.com/2025/day/4

class Day04 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1370
        timed(() -> new Day04().part1());
        System.out.println("=== part 2"); // 8437
        timed(() -> new Day04().part2());
    }

    List<String> data = file2lines("input04.txt");
//	List<String> data = Util.splitLines(example);
	PointMap<Character> m = new PointMap<>();

    void part1() {
    	m.read(data);
    	
    	var paper = m.findPoints('@').toSet();
    	var acc = paper.filter(p -> p.neighbors8().count(x -> paper.contains(x)) < 4);
    	System.out.println(acc.size());
    }

    void part2() {
    	m.read(data);
    	boolean cont = true;
    	int removed = 0;
    	
    	while (cont) {
    		var paper = m.findPoints('@').toSet();
    		var acc = paper.filter(p -> p.neighbors8().count(x -> paper.contains(x)) < 4);
    		System.out.println("removing " + acc.size());
    		removed += acc.size();
    		cont = acc.size() > 0;
    		acc.forEach(p -> m.put(p, '.'));
    	}
    		
//    	m.print();
    	System.out.println(removed);
    }

    static String example = """
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.""";
}
