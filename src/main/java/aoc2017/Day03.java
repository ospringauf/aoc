package aoc2017;

import common.AocPuzzle;
import common.Heading;
import common.Point;
import common.PointMap;
import io.vavr.Function2;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/3

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day03 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 552
		new Day03().part1();

		System.out.println("=== part 2"); // 330785
		new Day03().part2();
	}

	final long input = 325489;

	void part1() {
		PointMap<Long> m = new PointMap<>();
		fillMap(m, (v,p) -> v+1);

		var r = m.findPoint(input).manhattan();
		System.out.println(r);
	}
	
	void part2() {
		PointMap<Long> m = new PointMap<>();
		Function2<Long,Point,Long> nextVal = (v,p) -> p.neighbors8().map(x -> m.getOrDefault(x, 0L)).sum().longValue();
		fillMap(m, nextVal);

		var r = List.ofAll(m.values()).filter(v -> v > input).min();
		System.out.println(r);
	}


	private void fillMap(PointMap<Long> m, Function2<Long, Point, Long> nextVal) {
		var p = new Point(0, 0);
		int runlength = 1;
		long val = 1;
		var heading = Heading.EAST;
		m.put(p, val);

		// counter-clockwise spiral structure:
		// 1 east / 1 north / 
		// 2 west / 2 south / 
		// 3 east / 3 north / 
		// 4 west / 4 south /
		// ...
		// turn left after each segment
		// increase length after every 2nd segment
		
		while (val <= input) {
			for (int b = 0; b < 2; ++b) {
				for (int a = 0; a < runlength; a++) {
					p = p.translate(heading);
					val = nextVal.apply(val, p);
					m.put(p, val);
				}
				heading = heading.left();
			}
			runlength++;
		}
	}

}
