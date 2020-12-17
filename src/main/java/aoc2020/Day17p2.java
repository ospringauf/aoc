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
class Day17p2 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day17p2().part1();

		System.out.println("=== part 2");
		new Day17p2().part2();
	}
	
	record P4(int x, int y, int z, int w) implements SpacePoint {

		@Override
		public List<P4> neighbors() {
//			BiFunction<Integer, Integer, List<P3>> nz = (_x,_y) -> List.rangeClosed(z-1, z+1).map(_z -> new P3(_x,_y,_z));
			
			List<P4> n = List.rangeClosed(x-1, x+1)
					.flatMap(_x -> List.rangeClosed(y-1, y+1)
							.flatMap(_y -> List.rangeClosed(z-1, z+1)
									.flatMap(_z -> List.rangeClosed(w-1, w+1)
										.map(_w -> new P4(_x,_y,_z, _w)))));
			return n.remove(this);
		}
		
		public P4(common.Point p, int z, int w) {
			this(p.x(), p.y(), z, w);
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
		
		System.out.println(new P4(0,0,0,0).neighbors().size());
		
		//List<String> data = lines("input17.txt");
		List<String> data = List.of(example.split("\n"));
		
		var m2 = new PointMap<Character>();
		m2.read(input.split("\n"), c->c);
		
		var m0 = new SpaceMap<P4, Character>();
		m2.keySet().forEach(p -> m0.put(new P4(p,0,0), m2.get(p)));
		
		var m4 = m0;
		expand(m4);
		
		for (int i=0; i<6; ++i) {
			final var old = m4;
			var next = new SpaceMap<P4, Character>();
			for (var p : m4.keySet()) {

				var act = p.neighbors().count(n -> old.getOrDefault(n, '?') == '#');
				
				if (m4.get(p) == '#') { //active
					next.put(p, (act == 2 || act == 3)? '#' : '.');					
				} else {
					next.put(p, (act == 3)? '#' : '.');
				}
			}
			m4 = next;
			expand(m4);
		}
		
		var r = m4.countValues('#');
		System.out.println(r);
	}

	private void expand(SpaceMap<P4, Character> map) {
		var n = List.ofAll(map.keySet()).flatMap(p -> p.neighbors());
		for (var p : n) {
			if (!map.containsKey(p)) 
				map.put(p, '.');
		}
		
	}

	void part2() {}

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
