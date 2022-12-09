package aoc2022;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 9: Rope Bridge ---
// https://adventofcode.com/2022/day/9

class Day09 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 6642
        timed(() -> new Day09().solve(List.of(START, START)));
        
        System.out.println("=== part 2"); // 2765
        timed(() -> new Day09().solve(List.fill(10, START)));
    }

    static final Point START = Point.of(0,0);
    
//    List<String> rules = Util.splitLines(example1);
//    List<String> rules = Util.splitLines(example2);
    List<String> rules = file2lines("input09.txt");
    
    
    // parse "L 3" into LEFT,LEFT,LEFT
    List<Direction> parse(String s) {
    	var f = split(s, " ");
    	var dir = Direction.parse(f.s(0));
		var dist = f.i(1);
		return List.fill(dist, dir);
    }
    
    // tail movement: move towards the head
    Point moveKnot(Point h, Point t) {
        var dx = h.x() - t.x();
        var dy = h.y() - t.y();
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1)
        	return t.translate(Integer.signum(dx), Integer.signum(dy));
        else
            return t;
    }
    
    void solve(List<Point> rope) {
    	List<Direction> motions = rules.flatMap(this::parse);        
        Set<Point> visited = HashSet.empty();
        Point head = rope.head();
        
        for (var dir : motions) {
        	head = head.translate(dir);
        	// move each following knot according to its old position and the new predecessor position
            rope = rope.tail()
            		.foldLeft(List.of(head), (r, knot) -> r.append(moveKnot(r.last(), knot)));
            visited = visited.add(rope.last());
        }

        System.out.println(visited.size());
    }

    static String example1 = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
                        """;

    static String example2 = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
                        """;

}
