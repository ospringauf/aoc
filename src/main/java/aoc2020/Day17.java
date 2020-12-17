package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;

// --- Day 17: Conway Cubes ---
// https://adventofcode.com/2020/day/17

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day17 extends AocPuzzle {

	private static final char ACTIVE = '#';
	private static final char INACTIVE = '.';

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day17().test();
		
		var t0 = System.currentTimeMillis();

		System.out.println("=== part 1"); // 291
		new Day17().part1();
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);

		System.out.println("=== part 2"); // 1524
		new Day17().part2();
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
	}
	
	record P4(int x, int y, int z, int w) {
		public List<P4> neighbors3d() {
			List<P4> n = List.rangeClosed(x-1, x+1)
					.flatMap(_x -> List.rangeClosed(y-1, y+1)
							.flatMap(_y -> List.rangeClosed(z-1, z+1)
									.map(_z -> new P4(_x,_y,_z,0))));
			return n.remove(this);
		}
		
		public List<P4> neighbors4d() {
			List<P4> n = List.rangeClosed(x-1, x+1)
					.flatMap(_x -> List.rangeClosed(y-1, y+1)
							.flatMap(_y -> List.rangeClosed(z-1, z+1)
									.flatMap(_z -> List.rangeClosed(w-1, w+1)
											.map(_w -> new P4(_x,_y,_z, _w)))));
			return n.remove(this);
			
//			var n = List.rangeClosed(w-1, w+1).flatMap(_w -> neighbors3d().map(p -> new P4(p.x, p.y, p.z, _w)));
//			return n.remove(this);
		}

		public P4(Point p, int z, int w) {
			this(p.x(), p.y(), z, w);
		}
	}

	void part1() {
		conway(input, 6, P4::neighbors3d);
	}

	void part2() {
		conway(input, 6, P4::neighbors4d);
	}

	private void conway(String data, int cycles, Function<P4, List<P4>> neighbors) {
		
		// read 2d map
		var start = new PointMap<Character>();
		start.read(data.split("\n"), c->c);
		
		var map = new HashMap<P4, Character>();
		for (var p : start.keySet())
			map.put(new P4(p,0,0), start.get(p));
		expand(map, neighbors);
		
		
		for (int i=0; i<cycles; ++i) {
			final var old = map;
			var next = new HashMap<P4, Character>();
			
			for (var p : old.keySet()) {

				var active = neighbors.apply(p).count(n -> old.getOrDefault(n, '?') == ACTIVE);
				
				if (old.get(p) == ACTIVE) { //active
					next.put(p, (active == 2 || active == 3)? ACTIVE : INACTIVE);					
				} else {
					next.put(p, (active == 3)? ACTIVE : INACTIVE);
				}
			}
			map = next;
			expand(map, neighbors);
		}
		
		var r = map.values().stream().filter(v -> v==ACTIVE).count();
		System.out.println(r);
	}

	private void expand(Map<P4, Character> map, Function<P4, List<P4>> neighbors) {
		var t0 = System.currentTimeMillis();
		
		var n = List.ofAll(map.keySet()).flatMap(p -> neighbors.apply(p));
		for (var p : n) {
			if (!map.containsKey(p)) 
				map.put(p, '.');
		}
		System.out.format("--- expanded to %d point (%d ms)\n", map.size(), (System.currentTimeMillis()-t0));
	}

	void test() {
		var p0 = new P4(0,0,0,0);
		
		assertEquals(26, p0.neighbors3d().size());
		assertEquals(80, p0.neighbors4d().size());
		
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
