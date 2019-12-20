package aoc2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Day 20: Donut Maze
 * https://adventofcode.com/2019/day/20
 *
 */
public class Day20 {

	static final String inputFile = "input20.txt";

	final static Character CLEAR ='.';
	final static Character WALL ='#';

	PointMap<Character> maze;
	PointMap<String> label = new PointMap<>();
	
	Map<String, Point> innerPortals;
	Map<String, Point> outerPortals;
	private Set<Point> portals;

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		// 484
		System.out.println("=== part 1 ===");
		new Day20().part1();

		// level 25 : AA-ZZ = 5754
		System.out.println("=== part 2 ===");
		new Day20().part2();

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis()-t0);
	}



	void part1() throws Exception {
		maze = new PointMap<Character>();
		maze.read(Util.linesArray(inputFile), c->c);
		
		maze.print();
		findPortals();

		// connect warp points
		var warp = new PointMap<Point>();
		for (var p : portals) {			
			label.findPoints(label.get(p)).filter(x -> x!=p).findFirst().ifPresent(x -> warp.put(p, x));
		}
		
		// modified "neighbor" relation considers warping 
		maze.neigbors = p -> warp.containsKey(p) ? Stream.concat(p.neighbors(), Stream.of(warp.get(p))) : p.neighbors();

		var aa = label.findPoint("AA");
		var zz = label.findPoint("ZZ");
		var r = maze.calcPaths(aa, c->c==CLEAR, c->true);
		
		System.out.println(r.distance.get(zz));
	}

	
	void part2() throws Exception {
		maze = new PointMap<Character>();
		maze.read(Util.linesArray(inputFile), c->c);
		
		findPortals();
		
		// warp from inner to outer (on deeper level)
		var warp = new HashMap<String, String>();
		for (var p : innerPortals.values()) {
			warp.put(label.get(p)+"i", label.get(p)+"o");
		}
		System.out.println(warp);
		
		var dist0 = baseAdjacencyMatrix();
		var distance = dist0;
		var aazz = AdjMatrix.INF;
		int level = 0;
		
		while (aazz >= AdjMatrix.INF) {
			aazz = distance.get("AAo", "ZZo");
			System.out.printf("level %d : AA-ZZ = %d%n", level, aazz);

			// combine adj matrices
			distance = dist0.combine(distance, warp);
			level++;
		} 
	}

	AdjMatrix<String> baseAdjacencyMatrix() {
		var adj = new AdjMatrix<String>();
		
		for (var p1 : portals) {
			var r = maze.calcPaths(p1, c->c==CLEAR, c->true);
			var postf1 = outerPortals.values().contains(p1) ? "o" : "i"; 
			for (var p2 : portals) {
				if (p2 == p1) continue;
				var postf2 = outerPortals.values().contains(p2) ? "o" : "i";
				var d = r.distance.get(p2);
				adj.put(label.get(p1)+postf1, label.get(p2)+postf2, d);
			}
		}

		return adj;
	}

	void findPortals() {
		// points with adjacent text are portals
		portals = maze.findPoints(CLEAR).filter(p -> p.neighbors().anyMatch(n -> Character.isUpperCase(maze.get(n)))).collect(Collectors.toSet());
		
//		maze.boundingBox().print(p -> portals.contains(p) ? '*' : maze.get(p));
		for (var p : portals) {
			label.put(p, getLabel(p));
		}
		
		// find inner portals - reachable from center?
		var center = maze.boundingBox().center();
		var inner = maze.minDistances(center, c->c!=WALL, c->c!=CLEAR).keySet();
		inner.retainAll(portals);
		innerPortals = portals.stream().filter(inner::contains).collect(Collectors.toMap(p -> label.get(p), p -> p));
		outerPortals = portals.stream().filter(p -> ! inner.contains(p)).collect(Collectors.toMap(p -> label.get(p), p -> p));
	}

	
	String getLabel(Point p) {
		if (Character.isUpperCase(maze.get(p.north()))) {
			return "" + maze.get(p.north().north()) + maze.get(p.north());  
		}
		if (Character.isUpperCase(maze.get(p.east()))) {
			return "" + maze.get(p.east()) + maze.get(p.east().east());  
		}
		if (Character.isUpperCase(maze.get(p.south()))) {
			return "" + maze.get(p.south()) + maze.get(p.south().south());  
		}
		if (Character.isUpperCase(maze.get(p.west()))) {
			return "" + maze.get(p.west().west()) + maze.get(p.west());  
		}
		return "?";
	}

}
