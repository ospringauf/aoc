package aoc2019;

import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.function.ToIntFunction;

/*
 * Day 12: The N-Body Problem
 * https://adventofcode.com/2019/day/12
 *
 */
public class Day12 {

	static class Coord {
		int x;
		int y;
		int z;

		static final Coord ZERO = new Coord(0, 0, 0);

		Coord() {
		}

		Coord(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		static Coord of(int x, int y, int z) {
			return new Coord(x, y, z);
		}

		static int relative(int a, int b) {
			return (b <= a) ? (b == a) ? 0 : -1 : 1;
		}

		Coord relative(Coord c) {
			return Coord.of(relative(x, c.x), relative(y, c.y), relative(z, c.z));
		}

		Coord add(Coord c) {
			return Coord.of(x + c.x, y + c.y, z + c.z);
		}

		int sumabs() {
			return Math.abs(x) + Math.abs(y) + Math.abs(z);
		}

		@Override
		public String toString() {
			return String.format("(%2d,%2d,%2d)", x, y, z);
		}

		@Override
		public boolean equals(Object obj) {
			Coord c = (Coord) obj;
			return x == c.x && y == c.y && z == c.z;
		}

		@Override
		public int hashCode() {
			return x + (100 * y) + (1000 * z);
		}
	}

	static class Moon {
		Coord pos;
		Coord v;

		Moon(Coord pos, Coord v) {
			this.pos = pos;
			this.v = v;
		}

		Moon(Coord pos) {
			this(pos, Coord.of(0, 0, 0));
		}
		
		Coord relative(Moon m) {
			return pos.relative(m.pos);
		}

		static Moon at(int x, int y, int z) {
			return new Moon(Coord.of(x, y, z));
		}

		int energy() {
			return pos.sumabs() * v.sumabs();
		}

		@Override
		public String toString() {
			return String.format("M[p=%s, v=%s]", pos, v);
		}

		@Override
		public boolean equals(Object obj) {
			Moon m = (Moon) obj;
			return pos.equals(m.pos) && v.equals(m.v);
		}

		@Override
		public int hashCode() {
			return pos.hashCode() + v.hashCode();
		}
	}

	static final int MOONS = 4;

	static final Moon[] example1 = { Moon.at(-1, 0, 2), Moon.at(2, -10, -7), Moon.at(4, -8, 8), Moon.at(3, 5, -1) };
	static final Moon[] example2 = { Moon.at(-8, -10, 0), Moon.at(5, 5, 10), Moon.at(2, -7, 3), Moon.at(9, -8, -3) };

	static final Moon[] input = { Moon.at(-7, -1, 6), Moon.at(6, -9, -9), Moon.at(-12, 2, -7), Moon.at(4, -17, -12) };

	public static void main(String[] args) {
		System.out.println("=== test ===");
		new Day12().part1(example1, 10);
		new Day12().part1(example2, 100);

		System.out.println("=== part1 ===");
		new Day12().part1(input, 1000);

		System.out.println("=== part2 test1 ===");
		new Day12().part2(example1);
		System.out.println("=== part2 test2 ===");
		new Day12().part2(example2);
		System.out.println("=== part2 ===");
		new Day12().part2(input);

	}

	void part1(Moon[] moons, int steps) {
		stream(moons).forEach(System.out::println);

		for (int n = 1; n <= steps; ++n) {
			Moon[] next = new Moon[MOONS];
			for (int i = 0; i < MOONS; ++i) {
				Moon m = moons[i];
				var grav = stream(moons).map(m::relative).reduce(Coord::add).get();
				Coord v = m.v.add(grav);
				Coord p = m.pos.add(v);
				next[i] = new Moon(p, v);
			}
			moons = next;
		}

		int e = stream(moons).mapToInt(Moon::energy).sum();
		System.out.println(e);
	}

	int axisPeriod(Moon[] moons, ToIntFunction<Moon> axis) {
		int n = 1;
		int[] initial = stream(moons).mapToInt(axis).toArray();
		int[] now;

		do {
			Moon[] next = new Moon[MOONS];
			for (int i = 0; i < MOONS; ++i) {
				Moon m = moons[i];
				var grav = stream(moons).map(m::relative).reduce(Coord::add).get();
				Coord v = m.v.add(grav);
				Coord p = m.pos.add(v);
				next[i] = new Moon(p, v);
			}
			moons = next;
			n++;
			now = stream(moons).mapToInt(axis).toArray();
		} while (!Arrays.equals(now, initial));

		System.out.println("  axis period: " + n);
		return n;
	}

	private void part2(Moon[] moons) {
		int px = axisPeriod(moons, m -> m.pos.x);
		int py = axisPeriod(moons, m -> m.pos.y);
		int pz = axisPeriod(moons, m -> m.pos.z);

		var result = Util.lcm(px, py, pz);
		System.out.println(result);
	}
}