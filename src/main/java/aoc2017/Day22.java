package aoc2017;

import common.AocPuzzle;
import common.Direction;
import common.PointMap;
import common.Pose;
import io.vavr.collection.List;

// --- Day 22: Sporifica Virus ---
// https://adventofcode.com/2017/day/22

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day22 extends AocPuzzle {

	static final char INFECTED = '#';
	static final char CLEAN = '.';
	static final char WEAKENED = 'W';
	static final char FLAGGED = 'F';

	 List<String> lines = lines("input22.txt");
//	List<String> lines = Util.splitLines(example);

	void part1() {
		var grid = new PointMap<Character>();
		grid.read(lines);

		var virus = new Pose(Direction.UP, grid.boundingBox().center());

		long spread = 0;

		for (int i = 0; i < 10000; ++i) {
			boolean infected = grid.getOrDefault(virus.pos(), '.') == '#';
			if (infected) {
				virus = virus.turnRight();
				grid.put(virus.pos(), '.');
			} else {
				virus = virus.turnLeft();
				grid.put(virus.pos(), '#');
				spread++;
			}
			virus = virus.ahead();
		}
//		grid.print();
		System.out.println("infections: " + spread);

	}

	void part2() {
		var grid = new PointMap<Character>();
		grid.read(lines);

		var virus = new Pose(Direction.UP, grid.boundingBox().center());

		long spread = 0;

		for (int i = 0; i < 10_000_000; ++i) {
			Character state = grid.getOrDefault(virus.pos(), CLEAN);
			
			virus = switch (state) {
			case CLEAN -> virus.turnLeft();
			case WEAKENED -> virus;
			case INFECTED -> virus.turnRight();
			case FLAGGED -> virus.turnLeft().turnLeft();
			default -> virus;
			};
			
			var next = switch (state) {
			case CLEAN -> WEAKENED;
			case WEAKENED -> INFECTED;
			case INFECTED -> FLAGGED;
			case FLAGGED -> CLEAN;
			default -> CLEAN;
			};
			
			if (next == INFECTED)
				spread++;
			grid.put(virus.pos(), next);
			
			virus = virus.ahead();
//			System.out.println("burst " + i);
//			grid.print();
		}
		System.out.println("infections: " + spread);
	}

	void test() {

	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day22().test();

		System.out.println("=== part 1");
		new Day22().part1();

		System.out.println("=== part 2");
		timed(() -> new Day22().part2());
	}

	static String example = """
			..#
			#..
			...
						""";

}
