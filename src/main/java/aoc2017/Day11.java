package aoc2017;

import common.AocPuzzle;
import common.Point;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/11

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day11 extends AocPuzzle {

	public static void main(String[] args) {
		new Day11().solve();
	}

	Point hexNext(Point p, String d) {
		return switch (d) {
		case "n" -> p.north().north();
		case "nw" -> p.north().west();
		case "ne" -> p.north().east();
		case "sw" -> p.south().west();
		case "s" -> p.south().south();
		case "se" -> p.south().east();
		default -> null;
		};
	}

	void solve() {
		var data = readString("input11.txt");

		var dirs = List.of(data.split(","));
		
		var p0 = new Point(0, 0);
		var way = List.of(p0);
		
		for (var s : dirs)
			way = way.prepend(hexNext(way.head(), s));
		//List<Point> way = dirs.foldLeft(l0, (l,s) -> l.prepend(hexNext(l.head(), s)));
		
		System.out.println("=== part 1");
		System.out.println(way.head().manhattan() / 2);
		
		System.out.println("=== part 2");
		System.out.println(way.map(p -> p.manhattan() / 2).max());

	}
}
