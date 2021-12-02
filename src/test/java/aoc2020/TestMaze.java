package aoc2020;


import java.util.function.Predicate;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

public class TestMaze extends AocPuzzle {
	
	private static final char WALL = '#';

	public static void main(String[] args) throws Exception {
		var m = new PointMap<Character>();
		m.read(new TestMaze().file2lines("maze_1.txt"));
		
		new TestMaze().solve(m);
	}

	private void solve(PointMap<Character> map) {
		map.print();
		Predicate<Character> allowed = c -> c!=WALL;
		
		Point start = map.findPoint('@');
		System.out.println(start);
		
		var keys = HashSet.ofAll(map.values()).removeAll(List.of('@','.',WALL));
		var keypos = keys.toMap(k -> k, k -> map.findPoint(k));
		System.out.println(keypos);
		System.out.println(keypos.getOrElse('A', null));

		Point target = keypos.getOrElse('C', null);
		
		System.out.println("=== Dijkstra");
		
		var bfs = map.dijkstraAll(start, allowed, c -> true);
		time(() -> List.range(0, 10).forEach(i -> map.dijkstraAll(start, allowed, c -> true)));
		var dist = bfs.distance;
		System.out.println(dist.get(target));
		System.out.println(bfs.path(start, target));
		
//		var nextk = keys.maxBy(k -> dist.get(keypos.getOrElse(k, null))).get();
//		System.out.println(nextk);
//		System.out.println(dist.get(keypos.getOrElse(nextk, null)));
		
		System.out.println("=== A*");
		var r = map.astar(start, target, allowed);
		time(() -> List.range(0, 10).forEach(i -> map.astar(start, target, allowed)));
		System.out.println(r.distance.get(target));
		System.out.println(r.path(start, target));
		

//		time(() -> List.range(0, 1000).forEach(i -> map.findPoint('@')));
//		time(() -> List.range(0, 1000).forEach(i -> map.findPoint('@')));
		
//		time(() -> List.range(0, 1000).forEach(i -> map.findPoints(c -> c=='@')));
//		time(() -> List.range(0, 1000).forEach(i -> map.findPoints(c -> c=='@')));

	}

	private void time(Runnable r) {
		var t0 = System.currentTimeMillis();
		r.run();
		var t1 = System.currentTimeMillis();
		System.err.println("time: " + (t1-t0));
		
	}

}
