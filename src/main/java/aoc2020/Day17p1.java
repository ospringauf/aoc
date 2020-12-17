package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.function.BiFunction;

import common.AocPuzzle;
import common.PointMap;
import common.experimental.SpaceMap;
import common.experimental.SpacePoint;
import io.vavr.collection.List;

// https://adventofcode.com/2020/day/17

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day17p1 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day17p1().test();

		System.out.println("=== part 1");
		new Day17p1().part1();

		System.out.println("=== part 2");
		new Day17p1().part2();
	}
	
	record P3(int x, int y, int z) implements SpacePoint {

		@Override
		public List<P3> neighbors() {
//			BiFunction<Integer, Integer, List<P3>> nz = (_x,_y) -> List.rangeClosed(z-1, z+1).map(_z -> new P3(_x,_y,_z));
			
			List<P3> n = List.rangeClosed(x-1, x+1).flatMap(_x -> List.rangeClosed(y-1, y+1).flatMap(_y -> List.rangeClosed(z-1, z+1).map(_z -> new P3(_x,_y,_z))));
			return n.remove(this);
		}
		
		public P3(common.Point p, int z) {
			this(p.x(), p.y(), z);
		}

		public Point point() {
			return new Point(x,y);
		}

		@Override
		public int manhattan(SpacePoint p) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

	void part1() {
		List<String> data = lines("input17.txt");
		
		//System.out.println(new P3(0,0,0).neighbors().size());
		var m2 = new PointMap<Character>();
		m2.read(input.split("\n"), c->c);
		
		var m0 = new SpaceMap<P3, Character>();
		m2.keySet().forEach(p -> m0.put(new P3(p,0), m2.get(p)));
		
		var m3 = m0;
		expand(m3);
		
		for (int i=0; i<6; ++i) {
			final var old = m3;
			var next = new SpaceMap<P3, Character>();
			for (var p : m3.keySet()) {

				var act = p.neighbors().count(n -> old.getOrDefault(n, '?') == '#');
				
				if (m3.get(p) == '#') { //active
					next.put(p, (act == 2 || act == 3)? '#' : '.');					
				} else {
					next.put(p, (act == 3)? '#' : '.');
				}
			}
			m3 = next;
			expand(m3);
		}
		
		var r = m3.countValues('#');
		System.out.println(r);
	}

	private void expand(SpaceMap<P3, Character> m3) {
		var n = List.ofAll(m3.keySet()).flatMap(p -> p.neighbors());
		for (var p : n) {
			if (!m3.containsKey(p)) 
				m3.put(p, '.');
		}
		
	}

	void part2() {}

	void test() {
		assertEquals(1, 1);
		System.out.println("passed");
	}

	static String example = """
.#.
..#
###
			""";
	
	static String input = """
##.#....
...#...#
.#.#.##.
..#.#...
.###....
.##.#...
#.##..##
#.####..			
			""";

}
