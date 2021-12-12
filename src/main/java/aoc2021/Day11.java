package aoc2021;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.PointMap;
import common.Point;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

// --- Day 11: Dumbo Octopus ---
// https://adventofcode.com/2021/day/11

class Day11 extends AocPuzzle {

	@SuppressWarnings("serial")
	static class OctoMap extends PointMap<Integer> {

		OctoMap(String data) {
			read(Util.splitLines(data), c -> c - '0');
		}

		void inc(Point p) {
			put(p, get(p) + 1);
		}

		int step() {
			var points = List.ofAll(keySet());

			// increase energy level of each octopus
			points.forEach(p -> inc(p));

			// flash, increase neighbor energy, continue while new flashes
			Set<Point> flashed = HashSet.empty();
			boolean newFlashes = false;
			do {
				var flashing = findPoints(v -> v > 9).removeAll(flashed);
				flashing.flatMap(p -> p.neighbors8()).filter(points::contains).forEach(p -> inc(p));
				flashed = flashed.addAll(flashing);
				newFlashes = flashing.size() > 0;
			} while (newFlashes);

			// reset energy of flashed octopuses
			flashed.forEach(p -> put(p, 0));

			return flashed.size();
		}
	}

	OctoMap grid = new OctoMap(myInput);

	void solve() {
		grid.print();
		System.out.println();

		System.out.println("=== part 1"); // 1546
		var result1 = List.range(0, 100).map(i -> grid.step()).sum();
		System.out.println(result1);

		System.out.println("=== part 2"); // 471
		var result2 = Stream.iterate(101, n -> n + 1).find(i -> grid.step() == grid.size());
		System.out.println(result2.get());
	}

	public static void main(String[] args) {
		new Day11().solve();
	}

	static String example1 = """
			11111
			19991
			19191
			19991
			11111
						""";

	static String example2 = """
			5483143223
			2745854711
			5264556173
			6141336146
			6357385478
			4167524645
			2176841721
			6882881134
			4846848554
			5283751526
			""";

	static String myInput = """
			8448854321
			4447645251
			6542573645
			4725275268
			6442514153
			4515734868
			5513676158
			3257376185
			2172424467
			6775163586
			""";
}
