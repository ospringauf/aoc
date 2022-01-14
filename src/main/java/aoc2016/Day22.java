package aoc2016;

import java.util.function.Predicate;

import aoc2016.Day22.Node;
import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 22:  ---
// https://adventofcode.com/2016/day/22

class Day22 extends AocPuzzle {
    
    record Node(Point p, int size, int used) {
        static Node parse(String s) {
            // /dev/grid/node-x0-y1     89T   70T    19T   78%
            return split(s, "[\\-xyT\\s]+").to(r -> new Node(Point.of(r.i(1), r.i(2)), r.i(3), r.i(4)));
        }
        int avail() {
            return size-used;
        }
    }

    List<Node> nodes = file2lines("day22.txt").drop(2).map(Node::parse);
    Map<Point, Node> grid = nodes.toMap(n -> n.p, n -> n);
    int NONE =Integer.MAX_VALUE;
    Node start;
    Node target;
    {
        start = grid.getOrElse(Point.of(nodes.map(n->n.p.x()).max().get(), 0), null);
        target = grid.getOrElse(Point.of(0,0), null);
    }
    
	void part1() {
	    System.out.println(nodes.get(1));
	    
	    var r = nodes.filter(a -> a.used > 0).map(a -> nodes.remove(a).count(b -> b.avail() >= a.used)).sum();
	    System.out.println(r);
	}

	void part2() {
	    var cost = moveData(start, start.used, grid, List.empty());
	    System.out.println(cost);
	    
//	    Node BLOCK = new Node(Point.of(100,100), 0, 0, 0);
//	    //Predicate<Node> immobile = n -> n.p.neighbors().forAll(a -> grid.getOrDefault(a, BLOCK).size < n.used);
//	    Predicate<Node> immobile = n -> 100*n.used/n.size > 80;
//	    Predicate<Node> empty = n -> n.used == 0;
//	    Function1<Node, Character> fprint = n -> immobile.test(n)? '#' : empty.test(n)? '-' : '.';
//	    
//	    grid.print(fprint);
	    
	}

	int moveData(Node n, int data, Map<Point,Node> grid, List<Point> visited) {
	    if (n == target && n.avail() >= data)
	        return 0;
	    
	    if (n.size < data)
	        return NONE;
	    
	    if (n == target) {
//	        var c = makeRoom(n, data);
//	        return c;
	    }
	        
	        
	    
	    return NONE;
    }

    public static void main(String[] args) {
		System.out.println("=== part 1"); 
		timed(() -> new Day22().part1());

		System.out.println("=== part 2"); 
		timed(() -> new Day22().part2());
	}
	
	String example = """
root@ebhq-gridcenter# df -h        
Filesystem            Size  Used  Avail  Use%
/dev/grid/node-x0-y0   10T    8T     2T   80%
/dev/grid/node-x0-y1   11T    6T     5T   54%
/dev/grid/node-x0-y2   32T   28T     4T   87%
/dev/grid/node-x1-y0    9T    7T     2T   77%
/dev/grid/node-x1-y1    8T    0T     8T    0%
/dev/grid/node-x1-y2   11T    7T     4T   63%
/dev/grid/node-x2-y0   10T    6T     4T   60%
/dev/grid/node-x2-y1    9T    8T     1T   88%
/dev/grid/node-x2-y2    9T    6T     3T   66%	        
	        """;
}
