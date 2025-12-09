package aoc2025;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day x:  ---
// https://adventofcode.com/2025/day/x

class Day09 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 4758598740
		timed(() -> new Day09().part1());
		System.out.println("=== part 2");
		timed(() -> new Day09().part2());
	}

	List<String> data = file2lines("input09.txt");
//	List<String> data = Util.splitLines(example);

	long area(Point p1, Point p2) {
		return (Math.abs(p2.x() - p1.x()) + 1L) * (Math.abs(p2.y() - p1.y()) + 1L);
	}

	void part1() {
		var tiles = data.map(Point::parse);
//		System.out.println(tiles);

		var max = tiles.combinations(2).map(l -> area(l.get(0), l.get(1))).max();
		System.out.println(max);

		var m = new PointMap<Character>();
		tiles.forEach(t -> m.put(t, '#'));
		var bb = m.boundingBox();
		System.out.println(bb.width());
		System.out.println(bb.height());
		System.out.println(bb.width() * bb.height());

	}

	record Line(Point p1, Point p2) {

		List<Point> points() {
			int dx = p2.x() - p1.x();
			int dy = p2.y() - p1.y();
			int sgx = (int) Math.signum(dx);
			int sgy = (int) Math.signum(dy);
			int len = Math.abs(Math.max(dx * sgx, dy * sgy));
			return List.rangeClosed(0, len).map(i -> p1.translate((i * sgx), (i * sgy)));
		}
	}

	void part2() {
		var red = data.map(Point::parse);
		var lines = red.append(red.head()).sliding(2).map(x -> new Line(x.get(0), x.get(1)));
		
		var green = lines.flatMap(l -> l.points()).toList().removeAll(red);
		
		
		
		System.out.println("candidates: " + red.combinations(2).size());
		System.out.println("red: " + red.size());
		System.out.println("green: " + green.size());
	}

	static String example = """
			7,1
			11,1
			11,7
			9,7
			9,5
			2,5
			2,3
			7,3""";
}
