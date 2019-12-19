package aoc2019;

import java.util.Arrays;

/*
 * Day 19: Tractor Beam
 * https://adventofcode.com/2019/day/19
 *
 */
public class Day19 {

	static final long[] PROGRAM = Util.readIntProg("input19.txt");
	private Drone drone;

	static class Drone extends IntComputer {

		long result;
		long[] backup;
		int[] inputData = new int[2];
		int inputPos = 0;

		Drone(long[] program) {
			int free = 50;
			backup = Arrays.copyOf(program, program.length + free);
			mem = Arrays.copyOf(program, program.length + free);
			input = () -> inputData[inputPos++];
		}

		@Override
		void output(long value) {
			result = value;
		}

		void reset(int x, int y) {
			adr = 0;
			relBase = 0;
			inputPos = 0;
			inputData[0] = x;
			inputData[1] = y;

			System.arraycopy(backup, 0, mem, 0, backup.length);
		}
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		System.out.println("=== part 1 ===");
		new Day19().part1();

		System.out.println("=== part 2 ===");
		new Day19().part2();

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis()-t0);
	}


//	int beamWidth(int y) {
//		int w = 0;
//		for (int x = 0; x < y; ++x) {
//			w += beam(x, y) ? 1 : 0;
//		}
//		return w;
//	}
//
//	int beamX(int y) {
//		int x = 0;
//		while (!beam(x++, y))
//			;
//		return x;
//	}

	int SHIP = 100;

	boolean shipFits(int x, int y) {
		return beam(x, y) && beam(x + SHIP-1, y) && beam(x, y + SHIP-1);
	}

	boolean beam(int x, int y) {
		drone.reset(x, y);
		drone.run();
		return drone.result > 0;
	}

	void part1() {
		var map = new PointMap<Integer>();
		drone = new Drone(PROGRAM);

		for (int x = 0; x < 50; ++x)
			for (int y = 0; y < 50; ++y) {
				map.put(Point.of(x, y), beam(x, y) ? 1 : 0);
			}

		String PIXELS = ".#";
		map.print(v -> PIXELS.charAt(v));
		System.out.println(map.findPoints(1).count());
	}
	

	void part2() {
		drone = new Drone(PROGRAM);

		for (int y = 1250; y < 1350; ++y) {
			for (int x = 0; x < y; ++x) {
				if (shipFits(x, y)) {
					System.out.printf("ship fits at %4d,%4d  - %8d -- %d%n", x, y, 10000 * x + y, x + y);
					break;
				}
			}
		}
	}

}
