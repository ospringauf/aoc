package aoc2023;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day x:  ---
// https://adventofcode.com/2023/day/x

class Day18p1 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day18p1().part1()); // 35991
        System.out.println("=== part 2");
        timed(() -> new Day18p1().part2());
    }

    List<String> data = file2lines("input18.txt");
//	List<String> data = Util.splitLines(example);

    void part1() {
    	var m = new PointMap<Character>();
    	
    	var p = Point.of(0,0);
    	m.put(p, '#');
    	for (var l : data) {
    		var f = split(l, " ");
    		var d = Direction.parse(f.c(0));
    		var n = f.i(1);
    		for (int i=0; i<n; ++i) {
    			p = p.translate(d);
    			m.put(p, '#');
    		}
    	}
    	
    	m.print();
    	System.out.println(m.keySet().size());
    	System.err.println(inside(m));
    }

	public void floodFill2(PointMap<Character> m, Point start, Predicate<Character> unfilled, Character filled) {
		var area = io.vavr.collection.HashSet.of(start);
		var front = io.vavr.collection.HashSet.of(start);
		boolean cont = true;
		while (cont) {
			var next = front.flatMap(m.neigbors).filter(p -> unfilled.test(m.getOrDefault(p, null)));
			next = next.removeAll(area);
			cont = next.nonEmpty();
			front = next;
			area = area.addAll(next);
		}
		area.forEach(p -> m.put(p, filled));
	}
	
    int inside(PointMap<Character> m) {
    	var bb = m.boundingBox();
    	var vol = 0;
    	var points = List.ofAll(m.keySet());
    	var y0 =List.ofAll(bb.yRange()).find(y -> points.filter(p -> p.y()==y).size() == 2);
    	var p1 = points.filter(p -> p.y()==y0.get()).head();
    	
    	System.out.println(p1);
    	floodFill2(m, p1.east(), c->c==null, '#');
    	
    	return m.keySet().size();
    }
    

	void part2() {
    }

    static String example = """
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
""";
}
