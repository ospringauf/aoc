package aoc2023;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

//--- Day 10: Pipe Maze ---
// https://adventofcode.com/2023/day/10

// this is a more strictly typed solution

class Day10 extends AocPuzzle {

	public static void main(String[] args) {
		timed(() -> new Day10().solve()); // 6738, 579
	}

	List<String> data = file2lines("input10.txt"); 	boolean debug = false;
//    List<String> data = Util.splitLines(example3); boolean debug = true;
//    List<String> data = Util.splitLines(example4); boolean debug = true;
//    List<String> data = Util.splitLines(example5); boolean debug = true;
//    List<String> data = Util.splitLines(example6); boolean debug = true;

	static record Pipe(Set<Direction> dirs, Character symbol) {

		static Pipe S = new Pipe(HashSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST), 's');
		static Pipe NONE = new Pipe(HashSet.empty(), '.');
		static Pipe INSIDE = new Pipe(HashSet.empty(), 'x');
		static Pipe EW = new Pipe(Direction.EAST, Direction.WEST, '─');
		static Pipe NS = new Pipe(Direction.NORTH, Direction.SOUTH, '│');

		static Map<Character, Pipe> pipes = HashMap.of(
				'-', Pipe.EW, 
				'|', Pipe.NS, 
				'L', new Pipe(Direction.NORTH, Direction.EAST, '└'), 
				'J', new Pipe(Direction.NORTH, Direction.WEST, '┘'),
				'7', new Pipe(Direction.SOUTH, Direction.WEST, '┐'), 
				'F', new Pipe(Direction.SOUTH, Direction.EAST, '┌'), 
				'S', Pipe.S, 
				'.', Pipe.NONE);

		Pipe(Direction d1, Direction d2, Character sym) {
			this(HashSet.of(d1, d2), sym);
		}

		boolean connectsTo(Direction d, Pipe to) {
			return dirs.contains(d) && to.dirs.contains(d.opposite());
		}
	}

	static class PipeMap extends PointMap<Pipe> {

		void prettyPrint() {
			super.print(p -> p.symbol);
		}

		List<Point> connectedNeighbors(Point p) {
			List<Direction> directions = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
			var pipe = get(p);

			return directions.filter(d -> pipe.connectsTo(d, getOrDefault(p.translate(d), Pipe.NONE)))
					.map(d -> p.translate(d));
		}

		HashSet<Point> findLoop() {
			Point start = findPoint(Pipe.S);
			var loop = HashSet.of(start);
			var front = HashSet.of(start);

			boolean closed = false;
			while (!closed) {
				var next = front.flatMap(p -> connectedNeighbors(p)).removeAll(loop);
				closed = next.isEmpty();
				loop = loop.addAll(next);
				front = next;
			}
			return loop;
		}
	}

	static Point expand(Point p) {
		return Point.of(2 * p.x(), 2 * p.y());
	}

	void solve() {
		var m = new PipeMap();
		m.read(data, c -> Pipe.pipes.get(c).get());
		Point start = m.findPoint(Pipe.S);

		System.out.println("=== part 1");
		System.out.println("-- input");
		if (debug)
			m.prettyPrint();

		System.out.println("-- find loop");

		var loop = m.findLoop();
		System.out.println("loop length: " + loop.size());
		System.out.println("max dist: " + loop.size() / 2);

		System.out.println("=== part 2");

		System.out.println("-- expanding map");
		var xm = new PipeMap();
		// draw the loop on the larger map
		loop.forEach(p -> xm.put(expand(p), m.get(p)));
		
		// fill gaps between connected segments of the loop
		for (var p : xm.boundingBox().generatePoints()) {
			var w = xm.getOrDefault(p.west(), Pipe.NONE);
			var e = xm.getOrDefault(p.east(), Pipe.NONE);
			if (w.connectsTo(Direction.EAST, e))
				xm.put(p, Pipe.EW);

			var n = xm.getOrDefault(p.north(), Pipe.NONE);
			var s = xm.getOrDefault(p.south(), Pipe.NONE);
			if (n.connectsTo(Direction.SOUTH, s))
				xm.put(p, Pipe.NS);
		}

		// add empty border to find paths from the outside
		// initially, treat all non-loop positions as "inside"
		if (debug)
			xm.prettyPrint();

		var bb = xm.boundingBox().expand(1, 1);
		bb.generatePoints().forEach(p -> xm.put(p, xm.getOrDefault(p, Pipe.INSIDE)));
		if (debug)
			xm.prettyPrint();

		System.out.println("-- flood filling");
		xm.floodFill(Point.of(bb.xMin(), bb.yMin()), p -> p == Pipe.INSIDE, Pipe.NONE);
		if (debug)
			xm.prettyPrint();

		System.out.println("-- shrinking map");
		m.keySet().forEach(p -> m.put(p, xm.getOrDefault(expand(p), Pipe.NONE)));
		if (debug)
			m.prettyPrint();

		System.out.println("enclosed points: " + m.findPoints(Pipe.INSIDE).size());
	}

	static String example1 = """
			.....
			.S-7.
			.|.|.
			.L-J.
			.....
			""";

	static String example2 = """
			..F7.
			.FJ|.
			SJ.L7
			|F--J
			LJ...
			""";

	static String example3 = """
			...........
			.S-------7.
			.|F-----7|.
			.||.....||.
			.||.....||.
			.|L-7.F-J|.
			.|..|.|..|.
			.L--J.L--J.
			...........
			            """;

	static String example4 = """
			..........
			.S------7.
			.|F----7|.
			.||....||.
			.||....||.
			.|L-7F-J|.
			.|..||..|.
			.L--JL--J.
			..........
			            """;

	static String example5 = """
			.F----7F7F7F7F-7....
			.|F--7||||||||FJ....
			.||.FJ||||||||L7....
			FJL7L7LJLJ||LJ.L-7..
			L--J.L7...LJS7F-7L7.
			....F-J..F7FJ|L7L7L7
			....L7.F7||L7|.L7L7|
			.....|FJLJ|FJ|F7|.LJ
			....FJL-7.||.||||...
			....L---J.LJ.LJLJ...
			            """;

	static String example6 = """
			FF7FSF7F7F7F7F7F---7
			L|LJ||||||||||||F--J
			FL-7LJLJ||||||LJL-77
			F--JF--7||LJLJ7F7FJ-
			L---JF-JLJ.||-FJLJJ7
			|F|F-JF---7F7-L7L|7|
			|FFJF7L7F-JF7|JL---7
			7-L-JL7||F7|L7F-7F7|
			L.L7LFJ|||||FJL7||LJ
			L7JLJL-JLJLJL--JLJ.L
			            """;

}
