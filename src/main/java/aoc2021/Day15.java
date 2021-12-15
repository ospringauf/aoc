package aoc2021;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.PointMap.PathResult;
import common.Util;
import io.vavr.collection.List;

// --- Day 15: Chiton ---
// https://adventofcode.com/2021/day/15

class Day15 extends AocPuzzle {

//	List<String> input = Util.splitLines(example);
	List<String> input = file2lines("input15.txt");
	
	void part1() {
		var m = new PointMap<Integer>();
		m.read(input, c -> c-'0');
//		m.print();
		
		var max = m.boundingBox().xMax();
		Point start = Point.of(0, 0);
		Point target = Point.of(max, max);
		
		m.cost = (a,b) -> m.get(b);
		PathResult result = m.astar(start, target, v -> v!=null);

		System.out.println(result.path(start, target));
		System.out.println(result.distance.get(target));
		
	}

	void part2() {
		var m0 = new PointMap<Integer>();
		m0.read(input, c -> c-'1');
//		m0.print();
		
		var size = m0.boundingBox().xMax()+1;
		
		var m = new PointMap<Integer>();
		for (int x=0; x<5; ++x)
			for (int y=0; y<5; ++y)
				for (var p : m0.keySet()) {
					m.put(Point.of(p.x() + (x*size), p.y() + (y*size)), (m0.get(p) + x + y) % 9 + 1);
				}
		
//		m.print();
//		System.out.println(m.boundingBox());
		
		size = m.boundingBox().xMax()+1;
		Point start = Point.of(0, 0);
		Point target = Point.of(size-1, size-1);
		
		m.cost = (Point a, Point b) -> m.get(b);
		PathResult result = m.astar(start, target, v -> v!=null);
		System.out.println(result.path(start, target));
		System.out.println(result.distance.get(target));
	}

	
	public static void main(String[] args) {
	
		System.out.println("=== part 1"); // 613
		timed(() -> new Day15().part1());

		System.out.println("=== part 2"); // 2899
		timed(() -> new Day15().part2());
	}

	static String example = """
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
			""";

}
