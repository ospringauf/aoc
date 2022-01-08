package aoc2016;

import common.AocPuzzle;
import common.BoundingBox;
import common.PointMap;
import io.vavr.collection.HashSet;

// --- Day 8: Two-Factor Authentication ---
// https://adventofcode.com/2016/day/8

class Day08 extends AocPuzzle {

	void solve() {
		var instructions = file2lines("day08.txt");
//		var instructions = Util.splitLines(example);
		var g = new PointMap<Character>();

		for (var s : instructions) {
			var r = split(s, "[ =]");
			switch (r.s(0)) {
			case "rect" -> {
				r = split(r.s(1), "x");
				var bb = new BoundingBox(0, r.i(0)-1, 0, r.i(1)-1);
				bb.generatePoints().forEach(p -> g.put(p, '#'));
			}
			case "rotate" -> {
				switch (r.s(1)) {
				case "row" -> {
					var y = r.i(3);
					var dx = r.i(5);
					var row = HashSet.ofAll(g.keySet()).filter(p -> p.y() == y);
					row.forEach(p -> g.remove(p));
					row.forEach(p -> g.put(p.translate(dx, 0).modulo(50, 6), '#'));
				}
				case "column" -> {
					var x = r.i(3);
					var dy = r.i(5);
					var row = HashSet.ofAll(g.keySet()).filter(p -> p.x() == x);
					row.forEach(p -> g.remove(p));
					row.forEach(p -> g.put(p.translate(0, dy).modulo(50, 6), '#'));
				}
				default -> throw new IllegalArgumentException("Unexpected value: " + r.s(1));
				}
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + r.s(0));
			}
			
//			g.print();
//			System.out.println();
		}
		
		System.out.println(g.countValues('#'));
		g.print();
	}

	public static void main(String[] args) {
		timed(() -> new Day08().solve()); // 115 EFEYKFRFIJ
	}

	static String example = """
			rect 3x2
			rotate column x=1 by 1
			rotate row y=0 by 4
			rotate column x=1 by 1
						""";
}
