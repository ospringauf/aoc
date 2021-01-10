package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 20: Particle Swarm ---
// https://adventofcode.com/2017/day/20

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day20 extends AocPuzzle {

	record Coord(long x, long y, long z) {
		Coord add(Coord c) {
			return new Coord(x + c.x, y + c.y, z + c.z);
		}

		long manhattan() {
			return Math.abs(x) + Math.abs(y) + Math.abs(z);
		}
		
		static Coord parse(String s) {
			var a = s.split(",");
			return new Coord(Long.parseLong(a[0].trim()), Long.parseLong(a[1].trim()), Long.parseLong(a[2].trim()));
		}
	}

	record Particle(Coord p, Coord v, Coord a) {
		Particle next() {
			var v1 = v.add(a);					
			return new Particle(p.add(v1), v1, a);
		}
		
		static Particle parse(String s) {
			var a = s.split("(=<)|(>)");
			return new Particle(Coord.parse(a[1]), Coord.parse(a[3]), Coord.parse(a[5]));
		}

		public boolean samePos(Particle b) {
			return p.equals(b.p);
		}
	}

	List<String> lines = lines("input20.txt");
//	List<String> lines = Util.splitLines(example);

	void part1() {
		var p = lines.map(Particle::parse);
		
		for (int i=0; i<1000; ++i) {
			p = p.map(Particle::next);
		}
		var p0 = p;
		var min = List.range(0, p0.size()).minBy(n -> p0.get(n).p().manhattan());
		System.out.println(min);
			
	}

	void part2() {
		var p = lines.map(Particle::parse);
		
		for (int i=0; i<1000; ++i) {
			var p0 = p;
			p = p.filter(a -> !p0.exists(b -> a!=b && a.samePos(b)));			
			p = p.map(Particle::next);
		}
		System.out.println(p.size());
	}

	void test() {

	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day20().test();

		System.out.println("=== part 1");
		new Day20().part1();

		System.out.println("=== part 2");
		new Day20().part2();
	}

	static String example = """
p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>
p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0> 
			""";

}
