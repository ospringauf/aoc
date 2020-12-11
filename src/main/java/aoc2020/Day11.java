package aoc2020;

import java.util.function.Function;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;

// https://adventofcode.com/2020/day/11

@SuppressWarnings({ "deprecation", "preview" })
class Day11 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 2481
		new Day11().part1();

		System.out.println("=== part 2"); // 2227
//		new Day11().part2();
	}

	void part1() {
		// var data = List.of(example.split("\n"));
		var data = lines("input11.txt");

		var map = new PointMap<Character>();
		map.read(data);

		var occ = 0;
		var nextOcc = 0;
		PointMap<Character> n = map;
		do {
			occ = n.findPoints('#').size();
			n = next1(n);
			nextOcc = n.findPoints('#').size();
			System.out.println(occ);

		} while (nextOcc != occ);
	}

	void part2() {
//		 var data = List.of(example.split("\n"));
		var data = lines("input11.txt");

		var map = new PointMap<Character>();
		map.read(data);
		map.print();
		System.out.println();

		var occ = 0;
		var nextOcc = 0;
		PointMap<Character> n = map;
		do {
			occ = n.findPoints('#').size();
			n = next2(n);
			nextOcc = n.findPoints('#').size();
			System.out.println(occ);

		} while (nextOcc != occ);
	}

	PointMap<Character> next1(PointMap<Character> m) {
		var nxt = new PointMap<Character>();
		for (Point p : m.keySet()) {
			var empty = m.get(p) == 'L';
			var occupied = m.get(p) == '#';
			var nocc = allneighbors(p).count(n -> m.getOrDefault(n, '?') == '#');

			if (empty && nocc == 0)
				nxt.put(p, '#');
			else if (occupied && nocc >= 4)
				nxt.put(p, 'L');
			else
				nxt.put(p, m.get(p));

		}
		return nxt;
	}
	
	PointMap<Character> next2(PointMap<Character> m) {
		var nxt = new PointMap<Character>();
		for (Point p : m.keySet()) {
			var empty = m.get(p) == 'L';
			var occupied = m.get(p) == '#';
			var nocc = 
					visibleOccupied(m, p, x->x.north())
					+ visibleOccupied(m, p, x->x.south())
					+ visibleOccupied(m, p, x->x.east())
					+ visibleOccupied(m, p, x->x.west())
					+ visibleOccupied(m, p, x->x.north().east())
					+ visibleOccupied(m, p, x->x.south().east())
					+ visibleOccupied(m, p, x->x.north().west())
					+ visibleOccupied(m, p, x->x.south().west());

			if (empty && nocc == 0)
				nxt.put(p, '#');
			else if (occupied && nocc >= 5)
				nxt.put(p, 'L');
			else
				nxt.put(p, m.get(p));

		}
		return nxt;
	}

	private int visibleOccupied(PointMap<Character> m, Point p, Function<Point, Point> nextp) {
		p = nextp.apply(p);
		return switch (m.getOrDefault(p, '?')) {
		case 'L' -> 0;
		case '#' -> 1;
		case '.' -> visibleOccupied(m, p, nextp);
		case '?' -> 0;
		default -> 0;
		};
	}

	private List<Point> allneighbors(Point p) {
		return p.neighbors().appendAll(List.of(p.north().west(), p.north().east(), p.south().west(), p.south().east()));
	}

	static String example = """
			L.LL.LL.LL
			LLLLLLL.LL
			L.L.L..L..
			LLLL.LL.LL
			L.LL.LL.LL
			L.LLLLL.LL
			..L.L.....
			LLLLLLLLLL
			L.LLLLLL.L
			L.LLLLL.LL
						""";

}
