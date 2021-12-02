package aoc2020;

import java.util.function.Function;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;

// --- Day 11: Seating System ---
// https://adventofcode.com/2020/day/11

class Day11 extends AocPuzzle {

	private static final char UNDEF = '?';
	private static final char FLOOR = '.';
	private static final char OCCUPIED = '#';
	private static final char EMPTY = 'L';

	static class SeatMap extends PointMap<Character> {
	}

	public static void main(String[] args) {
	    var t0 = System.currentTimeMillis();

	    System.out.println("=== part 1"); // 2481
	    new Day11().part1();
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);

		System.out.println("=== part 2"); // 2227
		new Day11().part2();
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
	}

//	final List<String> = List.of(example.split("\n"));
	final List<String> data = file2lines("input11.txt");

	void part1() {
		var map = new SeatMap();
		map.read(data);
		nextUntilStable(map, this::next1);
	}

	void part2() {
		var map = new SeatMap();
		map.read(data);
		nextUntilStable(map, this::next2);
	}

	void nextUntilStable(SeatMap map, Function<SeatMap, SeatMap> nextMap) {
		long occ = 0;
		long nextOcc = map.countValues(OCCUPIED);
		do {
			occ = nextOcc;
			map = nextMap.apply(map);
			nextOcc = map.countValues(OCCUPIED);
		} while (nextOcc != occ);
		System.out.println(occ);
	}

	SeatMap next1(SeatMap map) {
		var next = new SeatMap();
		for (Point p : map.keySet()) {
			var current = map.get(p);
			var empty = current == EMPTY;
			var occupied = current == OCCUPIED;
			
			var nocc = p.neighbors8().count(n -> map.getOrDefault(n, UNDEF) == OCCUPIED);

			if (empty && nocc == 0)
				next.put(p, OCCUPIED);
			else if (occupied && nocc >= 4)
				next.put(p, EMPTY);
			else
				next.put(p, current);
		}
		return next;
	}

	SeatMap next2(SeatMap map) {
		var next = new SeatMap();
		for (Point p : map.keySet()) {
			var current = map.get(p);
			var empty = current == EMPTY;
			var occupied = current == OCCUPIED;

			var nocc = visibleOccupied(map, p, x -> x.north()) 
					+ visibleOccupied(map, p, x -> x.south())
					+ visibleOccupied(map, p, x -> x.east()) 
					+ visibleOccupied(map, p, x -> x.west())
					+ visibleOccupied(map, p, x -> x.north().east()) 
					+ visibleOccupied(map, p, x -> x.north().west())
					+ visibleOccupied(map, p, x -> x.south().east()) 
					+ visibleOccupied(map, p, x -> x.south().west());

			if (empty && nocc == 0)
				next.put(p, OCCUPIED);
			else if (occupied && nocc >= 5)
				next.put(p, EMPTY);
			else
				next.put(p, current);
		}
		return next;
	}

	int visibleOccupied(SeatMap m, Point p, Function<Point, Point> nextPoint) {
		p = nextPoint.apply(p);
		return switch (m.getOrDefault(p, UNDEF)) {
		case FLOOR -> visibleOccupied(m, p, nextPoint);
		case OCCUPIED -> 1;
		case EMPTY -> 0;
		case UNDEF -> 0;
		default -> 0;
		};
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
