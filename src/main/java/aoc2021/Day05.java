package aoc2021;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

//--- Day 5: Hydrothermal Venture ---
// https://adventofcode.com/2021/day/5

class Day05 extends AocPuzzle {

	List<String> data = file2lines("input05.txt");
//	List<String> data = Util.splitLines(example);

	record Line(Point p1, Point p2) {

		boolean isStraight() {
			return (p1.x() == p2.x()) || (p1.y() == p2.y());
		}

		List<Point> points() {
			int dx = p2.x() - p1.x();
			int dy = p2.y() - p1.y();
			int sgx = (int) Math.signum(dx);
			int sgy = (int) Math.signum(dy);
			int len = Math.abs(Math.max(dx * sgx, dy * sgy));
			return List.rangeClosed(0, len).map(i -> p1.translate((i * sgx), (i * sgy)));
		}

		static Line parse(String s) {
			var endpoints = split(s, " -> ").map(split(",").andThen(r -> Point.of(r.i(0), r.i(1))));
			return new Line(endpoints.get(0), endpoints.get(1));
		}
	}

	void part1() {
		var lines = data.map(Line::parse);
		var map = new PointMap<Integer>();

		lines
			.filter(Line::isStraight)
			.flatMap(Line::points)
			.forEach(p -> map.put(p, map.getOrDefault(p, 0) + 1));
		var overlap = map.findPoints(v -> v > 1);
		System.out.println(overlap.size());
	}

	void part2() {
		var lines = data.map(Line::parse);
		var map = new PointMap<Integer>();

		lines
			.flatMap(Line::points)
			.forEach(p -> map.put(p, map.getOrDefault(p, 0) + 1));
		var overlap = map.findPoints(v -> v > 1);
		System.out.println(overlap.size());
	}

	void test() {
		var lines = Util.splitLines(example).map(Line::parse);
		var map = new PointMap<Integer>();

		lines
//			.filter(Line::isStraight)
			.flatMap(Line::points)
			.forEach(p -> map.put(p, map.getOrDefault(p, 0) + 1));
		map.print();
		var overlap = map.findPoints(v -> v > 1);
		System.out.println(overlap.size());
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day05().test();

		System.out.println("=== part 1"); // 6225
		new Day05().part1();

		System.out.println("=== part 2"); // 22116
		new Day05().part2();
	}

	static String example = """
			0,9 -> 5,9
			8,0 -> 0,8
			9,4 -> 3,4
			2,2 -> 2,1
			7,0 -> 7,4
			6,4 -> 2,0
			0,9 -> 2,9
			3,4 -> 1,4
			0,0 -> 8,8
			5,5 -> 8,2
						""";

}
