package aoc2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Day 20: Donut Maze
 * https://adventofcode.com/2019/day/20
 * 
 * part2 brute force - create 3d stacked maze
 *
 */
public class Day20Force {

	static final String inputFile = "input20.txt";
	static final Character CLEAR = '.';
	static final Character WALL = '#';

	PointMap<Character> maze;
	PointMap<String> label = new PointMap<>();

	Map<String, Point> innerPortals;
	Map<String, Point> outerPortals;
	private Set<Point> portals;

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		System.out.println("=== part 1 ===");
		new Day20Force().part1();

		System.out.println("=== part 2 ===");
		new Day20Force().part2();

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}

	void part2() throws Exception {
		maze = new PointMap<Character>();
		maze.read(Util.linesArray(inputFile), c -> c);

//		maze.print();
		findPortals();

		System.out.println(innerPortals);

		var warp = new HashMap<ZPoint, ZPoint>();

		Function<Point, Stream<Point>> neigbors = p -> warp.containsKey(p)
				? Stream.of(p.north(), p.south(), p.east(), p.west(), warp.get(p))
				: p.neighbors();

		int maxLevel = 50;

		var zmaze = new PointMap<Integer>();
		zmaze.neigbors = neigbors;

		// build 3D maze 
		for (int level = 0; level < maxLevel; ++level) {
			final int z = level;
			maze.findPoints(CLEAR).forEach(p -> zmaze.put(new ZPoint(p, z), 1));
			if (level > 0) {
				innerPortals.forEach((l, p) -> {
					var upper = new ZPoint(p, z);
					var lower = new ZPoint(outerPortals.get(l), z - 1);
					warp.put(upper, lower);
					warp.put(lower, upper);
				});
			}
			
			if (level <= 20) continue;
			
			var aa = new ZPoint(label.findPoint("AA"), level);
			var zz = new ZPoint(label.findPoint("ZZ"), level);

			var dmap = zmaze.minDistances(aa, i -> true);
			var dist = dmap.get(zz);

			System.out.printf("AA-ZZ on level %d: %d%n", level, dist);
		}

		
	}

	void part1() throws Exception {
		maze = new PointMap<Character>();
		maze.read(Util.linesArray(inputFile), c -> c);

		maze.print();
		findPortals();

		// connect warp points
		var warp = new PointMap<Point>();
		for (var p : portals) {
			label.findPoints(label.get(p)).filter(x -> x != p).findFirst().ifPresent(x -> warp.put(p, x));
		}

//		System.out.println(warp);
		var aa = label.findPoint("AA");
		var zz = label.findPoint("ZZ");

		maze.neigbors = p -> warp.containsKey(p) ? Stream.of(p.north(), p.south(), p.east(), p.west(), warp.get(p))
				: p.neighbors();
		var r = maze.calcPaths(aa, c -> c == CLEAR, c -> true);

		System.out.println(r.distance.get(zz));
	}

	void findPortals() {
		portals = maze.findPoints(CLEAR).filter(p -> p.neighbors().anyMatch(n -> Character.isUpperCase(maze.get(n))))
				.collect(Collectors.toSet());

//		maze.boundingBox().print(p -> portals.contains(p) ? '*' : maze.get(p));
		for (var p : portals) {
			label.put(p, getLabel(p));
		}

		// find inner/outer portals
		var center = maze.boundingBox().center();
		var inner = maze.minDistances(center, c -> c != WALL, c -> c != CLEAR).keySet();
		inner.retainAll(portals);
		innerPortals = portals.stream().filter(inner::contains).collect(Collectors.toMap(p -> label.get(p), p -> p));
		outerPortals = portals.stream().filter(p -> !inner.contains(p))
				.collect(Collectors.toMap(p -> label.get(p), p -> p));
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
