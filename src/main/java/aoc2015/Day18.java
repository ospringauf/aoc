package aoc2015;

import java.util.List;

import common.AocPuzzle;
import common.Point;
import common.PointMap;

// --- Day 18: Like a GIF For Your Yard ---
// https://adventofcode.com/2015/day/18

class Day18 extends AocPuzzle {

	void part1() {
		var grid = new PointMap<Character>();
		grid.read(file2lines("day18.txt"));
//		grid.read(Util.splitLines(example));

		for (int i = 0; i < 100; ++i) {
			var next = new PointMap<Character>();
			for (var p : grid.keySet()) {
				var g0 = grid;
				var n = p.neighbors8().count(x -> g0.getOrDefault(x, '.') == '#');
				if (grid.get(p) == '#')
					next.put(p, (n == 2 || n == 3) ? '#' : '.');
				else
					next.put(p, (n == 3) ? '#' : '.');
			}
			grid = next;
		}

		System.out.println(grid.countValues('#'));
	}

	void part2() {
		var grid = new PointMap<Character>();
		grid.read(file2lines("day18.txt"));
//		grid.read(Util.splitLines(example));
		var bb = grid.boundingBox();
		var corners = List.of(Point.of(bb.xMin(), bb.yMin()), Point.of(bb.xMax(), bb.yMin()),
				Point.of(bb.xMax(), bb.yMin()), Point.of(bb.xMax(), bb.yMax()));

		for (int i = 0; i < 100; ++i) {
			var next = new PointMap<Character>();
			for (var p : grid.keySet()) {
				var g0 = grid;
				var n = p.neighbors8().count(x -> g0.getOrDefault(x, '.') == '#');
				if (grid.get(p) == '#')
					next.put(p, (n == 2 || n == 3) ? '#' : '.');
				else
					next.put(p, (n == 3) ? '#' : '.');
			}
			corners.forEach(p -> next.put(p, '#'));
			grid = next;
		}

		System.out.println(grid.countValues('#'));
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		new Day18().part1();

		System.out.println("=== part 2");
		new Day18().part2();
	}

	static String example = """
			.#.#.#
			...##.
			#....#
			..#...
			#.#..#
			####..
				""";

}
