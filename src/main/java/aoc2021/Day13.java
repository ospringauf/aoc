package aoc2021;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function1;

// --- Day 13: Transparent Origami ---
// https://adventofcode.com/2021/day/13

class Day13 extends AocPuzzle {

//	String data = example;
	String data = file2string("input13.txt");

	record FoldRule(boolean alongX, int pos) implements Function1<Point, Point> {
		static FoldRule parse(String s) {
			// fold along y=7
			return split(s, "[ =]").to(r -> new FoldRule("x".equals(r.s(2)), r.i(3)));
		}

		public Point apply(Point p) {
			if (alongX)
				return (p.x() < pos) ? p : Point.of(2 * pos - p.x(), p.y());
			else
				return (p.y() < pos) ? p : Point.of(p.x(), 2 * pos - p.y());
		}
	}

	void solve() {
		var blocks = data.split("\n\n");
		var points = Util.splitLines(blocks[0]).map(Point::parse).toSet();
		var rules = Util.splitLines(blocks[1]).map(FoldRule::parse);
		
		points = points.map(rules.head());
		
		System.out.println("=== part 1"); // 785
		System.out.println(points.size());

		points = rules.tail().foldLeft(points, (p, rule) -> p.map(rule)); 

		var paper = new PointMap<Character>();
		points.forEach(p -> paper.put(p, '#'));
		
		System.out.println("=== part 2"); // FJAHJGAH
		paper.print();
	}


	public static void main(String[] args) {
		new Day13().solve();
	}

	static String example = """
			6,10
			0,14
			9,10
			0,3
			10,4
			4,11
			6,0
			6,12
			4,1
			0,13
			10,12
			3,4
			3,0
			8,4
			1,10
			2,14
			8,10
			9,0

			fold along y=7
			fold along x=5
						""";

}
