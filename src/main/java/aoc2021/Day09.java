package aoc2021;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 9: Smoke Basin ---
// https://adventofcode.com/2021/day/9

class Day09 extends AocPuzzle {

	private static final int HIGHEST = 9;

    PointMap<Integer> heightmap = new PointMap<>();

	List<String> input = file2lines("input09.txt");
//	List<String> input = Util.splitLines(example);

	void solve() {
		heightmap.read(input, c -> c - '0');

		Predicate<Point> localMin = p -> p.neighbors().forAll(n -> heightmap.getOrDefault(p, HIGHEST) < heightmap.getOrDefault(n, HIGHEST));
		var lowpoints = List.ofAll(heightmap.keySet()).filter(localMin);
		var result1 = lowpoints.map(p -> heightmap.get(p) + 1).sum();

		System.out.println("=== part 1"); // 498
		System.out.println(result1);

		var basins = lowpoints.map(p -> basin(p));
		var result2 = basins.map(b -> b.size()).sorted().reverse().take(3).product();
		
		System.out.println("=== part 2"); // 1071000
		System.out.println(result2);
	}

	// fixpoint: expand p until while not at level 9
	Set<Point> basin(Point p) {
		var b = HashSet.of(p);
		int size = 1;
		do {
			size = b.size();
			b = b.addAll(b.flatMap(Point::neighbors).filter(x -> heightmap.getOrDefault(x, HIGHEST) < HIGHEST));
		} while (b.size() > size);
		return b;
	}

	public static void main(String[] args) {
		timed(() -> new Day09().solve());
	}

	static String example = """
			2199943210
			3987894921
			9856789892
			8767896789
			9899965678
						""";
}
