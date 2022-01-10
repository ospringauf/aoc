package aoc2016;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;

// --- Day 14: A Maze of Twisty Little Cubicles ---
// https://adventofcode.com/2016/day/13

class Day13 extends AocPuzzle {

    int input = 1362;
    
    boolean isWall(Point p) {
        var v = (p.x() * p.x()) + (3 * p.x()) + (2 * p.x() * p.y()) + p.y() + (p.y() * p.y());
        v += input;
        int bits = 0;
        while (v > 0) {
            bits += v&1;
            v = v >> 1;
        }
        return bits%2 == 1;
    }
    
	void solve() {
	    var map = new PointMap<Character>();
	    var bb = new BoundingBox(0, 100, 0, 100);
	    bb.generatePoints().forEach(p -> map.put(p, isWall(p) ? '#' : '.'));
	    
//	    map.print();
	    
	    var r = map.dijkstraAll(Point.of(1, 1), c -> c=='.', c -> true);
	    var p = r.path(Point.of(1, 1), Point.of(31,39));
//	    var p = r.path(Point.of(1, 1), Point.of(7,4));

	    System.out.println("=== part 1"); 
	    System.out.println(p.length()-1);
	    
	    System.out.println("=== part 2"); 
	    System.out.println(r.distance.findPoints(d -> d<=50).size());
	}

	public static void main(String[] args) {
		timed(() -> new Day13().solve());
	}
}
