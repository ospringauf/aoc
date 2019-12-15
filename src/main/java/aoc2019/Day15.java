package aoc2019;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Day 15: Oxygen System
 * https://adventofcode.com/2019/day/15
 *
 */
public class Day15 {

	static long[] my_input = { 3, 1033, 1008, 1033, 1, 1032, 1005, 1032, 31, 1008, 1033, 2, 1032, 1005, 1032, 58, 1008,
			1033, 3, 1032, 1005, 1032, 81, 1008, 1033, 4, 1032, 1005, 1032, 104, 99, 1001, 1034, 0, 1039, 102, 1, 1036,
			1041, 1001, 1035, -1, 1040, 1008, 1038, 0, 1043, 102, -1, 1043, 1032, 1, 1037, 1032, 1042, 1106, 0, 124,
			1001, 1034, 0, 1039, 1002, 1036, 1, 1041, 1001, 1035, 1, 1040, 1008, 1038, 0, 1043, 1, 1037, 1038, 1042,
			1105, 1, 124, 1001, 1034, -1, 1039, 1008, 1036, 0, 1041, 1001, 1035, 0, 1040, 101, 0, 1038, 1043, 1002,
			1037, 1, 1042, 1105, 1, 124, 1001, 1034, 1, 1039, 1008, 1036, 0, 1041, 1001, 1035, 0, 1040, 101, 0, 1038,
			1043, 102, 1, 1037, 1042, 1006, 1039, 217, 1006, 1040, 217, 1008, 1039, 40, 1032, 1005, 1032, 217, 1008,
			1040, 40, 1032, 1005, 1032, 217, 1008, 1039, 9, 1032, 1006, 1032, 165, 1008, 1040, 39, 1032, 1006, 1032,
			165, 1102, 2, 1, 1044, 1105, 1, 224, 2, 1041, 1043, 1032, 1006, 1032, 179, 1102, 1, 1, 1044, 1106, 0, 224,
			1, 1041, 1043, 1032, 1006, 1032, 217, 1, 1042, 1043, 1032, 1001, 1032, -1, 1032, 1002, 1032, 39, 1032, 1,
			1032, 1039, 1032, 101, -1, 1032, 1032, 101, 252, 1032, 211, 1007, 0, 72, 1044, 1105, 1, 224, 1102, 1, 0,
			1044, 1105, 1, 224, 1006, 1044, 247, 102, 1, 1039, 1034, 1002, 1040, 1, 1035, 1002, 1041, 1, 1036, 1002,
			1043, 1, 1038, 1001, 1042, 0, 1037, 4, 1044, 1106, 0, 0, 43, 44, 92, 18, 58, 24, 84, 34, 94, 19, 51, 95, 1,
			54, 20, 78, 88, 51, 71, 20, 92, 96, 11, 50, 22, 21, 3, 96, 74, 15, 26, 56, 99, 18, 80, 56, 99, 50, 12, 71,
			93, 48, 25, 99, 83, 45, 4, 68, 98, 82, 26, 95, 97, 98, 6, 3, 79, 32, 98, 34, 9, 80, 74, 24, 95, 75, 12, 26,
			80, 54, 10, 71, 94, 79, 40, 38, 99, 57, 58, 78, 31, 97, 40, 85, 38, 83, 87, 27, 85, 29, 42, 99, 69, 29, 80,
			94, 56, 88, 21, 17, 84, 87, 78, 54, 27, 85, 31, 77, 30, 82, 83, 52, 30, 90, 49, 93, 69, 58, 74, 42, 86, 40,
			85, 79, 23, 98, 14, 11, 79, 26, 86, 33, 82, 83, 17, 84, 53, 65, 97, 10, 68, 99, 48, 76, 83, 44, 98, 18, 82,
			11, 3, 81, 84, 1, 42, 82, 73, 99, 35, 83, 42, 24, 97, 31, 78, 41, 82, 75, 11, 86, 86, 3, 99, 11, 15, 84, 53,
			79, 93, 53, 62, 82, 64, 98, 56, 76, 69, 74, 5, 83, 97, 63, 4, 81, 32, 10, 33, 94, 93, 87, 70, 31, 76, 68,
			22, 7, 7, 96, 96, 57, 41, 95, 11, 96, 85, 83, 85, 50, 27, 82, 89, 56, 20, 95, 96, 93, 91, 92, 40, 68, 78,
			84, 7, 52, 42, 55, 37, 75, 58, 80, 28, 80, 10, 92, 54, 89, 52, 55, 78, 75, 71, 65, 82, 30, 50, 81, 99, 39,
			68, 74, 30, 87, 58, 31, 74, 10, 1, 85, 66, 93, 85, 9, 88, 74, 74, 24, 86, 1, 91, 12, 76, 65, 85, 82, 93, 95,
			32, 98, 67, 16, 80, 79, 42, 79, 33, 93, 45, 91, 99, 73, 48, 84, 96, 35, 95, 14, 99, 55, 61, 84, 53, 63, 54,
			54, 89, 88, 85, 25, 97, 96, 88, 51, 73, 29, 79, 31, 94, 32, 74, 92, 48, 63, 28, 92, 9, 52, 91, 26, 78, 75,
			22, 39, 1, 99, 20, 86, 91, 9, 73, 84, 23, 27, 59, 36, 83, 29, 52, 88, 39, 2, 90, 41, 46, 83, 2, 3, 96, 55,
			28, 89, 89, 33, 90, 21, 22, 82, 7, 87, 17, 75, 83, 98, 33, 73, 73, 2, 31, 88, 10, 56, 49, 78, 78, 42, 88,
			91, 21, 83, 21, 83, 27, 82, 21, 85, 35, 91, 98, 70, 45, 91, 87, 90, 95, 15, 11, 77, 53, 49, 55, 92, 21, 9,
			91, 95, 46, 61, 63, 82, 11, 77, 47, 98, 20, 90, 25, 64, 81, 20, 80, 93, 41, 5, 91, 91, 55, 95, 57, 76, 97,
			75, 9, 99, 52, 73, 55, 95, 89, 28, 98, 57, 99, 66, 34, 81, 87, 39, 85, 56, 8, 16, 74, 85, 18, 24, 99, 76,
			58, 89, 46, 53, 86, 98, 89, 65, 81, 51, 77, 18, 12, 64, 83, 18, 96, 36, 33, 73, 70, 85, 89, 52, 82, 82, 37,
			38, 85, 83, 28, 58, 98, 69, 10, 86, 86, 2, 32, 83, 87, 85, 29, 88, 32, 98, 11, 88, 29, 74, 64, 89, 91, 6,
			41, 89, 45, 91, 79, 87, 34, 76, 7, 21, 89, 40, 97, 74, 28, 62, 58, 3, 92, 66, 92, 78, 87, 67, 22, 41, 54,
			81, 69, 24, 97, 65, 30, 87, 88, 61, 55, 96, 85, 40, 98, 53, 80, 32, 66, 88, 3, 47, 98, 77, 56, 30, 15, 92,
			77, 20, 56, 80, 79, 52, 25, 77, 23, 87, 74, 76, 34, 77, 75, 1, 5, 82, 27, 93, 50, 82, 82, 2, 6, 52, 19, 78,
			93, 15, 83, 48, 92, 82, 60, 90, 98, 99, 57, 69, 16, 87, 52, 26, 79, 82, 49, 51, 85, 30, 62, 73, 92, 40, 86,
			88, 37, 14, 76, 71, 79, 43, 84, 82, 8, 98, 38, 1, 80, 85, 76, 54, 17, 74, 17, 7, 96, 10, 43, 26, 88, 97, 6,
			70, 94, 96, 23, 3, 74, 23, 80, 17, 26, 81, 39, 89, 91, 10, 94, 26, 13, 92, 5, 43, 95, 70, 87, 51, 36, 86,
			74, 57, 88, 42, 88, 84, 57, 10, 77, 10, 36, 99, 96, 62, 89, 40, 86, 98, 24, 93, 43, 79, 17, 26, 32, 84, 24,
			94, 56, 85, 94, 43, 75, 82, 65, 80, 63, 6, 75, 70, 81, 99, 73, 58, 34, 93, 23, 76, 70, 89, 42, 86, 48, 80,
			66, 88, 83, 81, 61, 80, 62, 86, 74, 85, 40, 84, 81, 93, 45, 74, 30, 73, 24, 84, 83, 88, 41, 77, 69, 89, 2,
			95, 47, 84, 80, 85, 0, 0, 21, 21, 1, 10, 1, 0, 0, 0, 0, 0, 0 };

	static final int CMD_NORTH = 1;
	static final int CMD_SOUTH = 2;
	static final int CMD_WEST = 3;
	static final int CMD_EAST = 4;

	static final int WALL = 0;
	static final int CLEAR = 1;
	static final int OXYGEN = 2;
	static final int START = 3;
	static final int UNCHARTED = 4;
	static final String PIXELS = "#.X0 ";

	static Point nextPos(Point p, int moveCmd) {
		switch (moveCmd) {
		case CMD_NORTH:
			return p.north();
		case CMD_SOUTH:
			return p.south();
		case CMD_WEST:
			return p.west();
		case CMD_EAST:
			return p.east();
		}
		return null;
	}

	static class Robo extends IntComputer {

		PointMap<Integer> map = new PointMap<>();
		Point pos = Point.ZERO;		
		int currentMove;
		Random random = new Random();
		int moveCommands = 0;


		Robo(long[] program) {
			super(program);
			map.put(pos, START);
		}

		int mapAt(Point p) {
			return map.getOrDefault(p, UNCHARTED);
		}
		
		@Override
		boolean complete() {
			Predicate<Point> unchartedNeighbor = p -> p.neighbors().anyMatch(n -> mapAt(n) == UNCHARTED);
			var waypoints = map.keySet().stream().filter(p -> mapAt(p) != WALL);
			return waypoints.noneMatch(unchartedNeighbor);
		}

		// update map and position
		@Override		
		void output(long value) {
			
			switch ((int) value) {
			case WALL:
				// robo has not moved!
				map.put(nextPos(pos, currentMove), WALL);
				break;
			case CLEAR:
				pos = nextPos(pos, currentMove);
				map.put(pos, CLEAR);
				break;
			case OXYGEN:
				pos = nextPos(pos, currentMove);
				map.put(pos, OXYGEN);
				System.out.println("found oxygen!");
				display();
				break;

			default:
				break;
			}

		}

		void display() {
			System.out.println("--- map start ---");
			Function<Point, Character> paintFunc = p -> PIXELS.charAt(map.getOrDefault(p, UNCHARTED));
			map.boundingBox().print(paintFunc);
			System.out.println("--- map end  ---");
		}

		// at each point, try to visit uncharted fields. otherwise, move to a random clear neighbor.
		int wanderRandomly() {
			var m = IntStream.rangeClosed(1, 4).filter(c -> mapAt(nextPos(pos, c)) == UNCHARTED).toArray();
			if (m.length == 0)
				m = IntStream.rangeClosed(1, 4).filter(c -> mapAt(nextPos(pos, c)) != WALL).toArray();
			int i = random.nextInt(m.length);
			currentMove = m[i];
			return currentMove;
		}
		
		// decision memory: remember previous decisions (1..4) as bitmap (2¹..2⁴)
		PointMap<Integer> decision = new PointMap<>();
		
		// at each point, try to visit uncharted fields first. otherwise, move randomly, but try to avoid making the same decision as before
		int wanderWithMemory() {
			final int d = decision.getOrDefault(pos, 0);
			
			// any uncharted neighbors?
			var m = IntStream.rangeClosed(1, 4).filter(c -> mapAt(nextPos(pos, c)) == UNCHARTED).toArray();
			// else: known clear neighbors
			if (m.length == 0)
				m = IntStream.rangeClosed(1, 4).filter(c -> mapAt(nextPos(pos, c)) != WALL).toArray();
			// any options we haven't tried yet?
			var n = Arrays.stream(m).filter(x -> (d & (2<<x)) == 0).toArray();
			if (n.length > 0) m = n;
			
			// make a random choice
			int i = random.nextInt(m.length);
			currentMove = m[i];
		
			// remember decision
			decision.put(pos, d | (2<<currentMove));
			moveCommands++;
			return currentMove;
		}
	}

	public static void main(String[] args) throws Exception {
		new Day15().solve();
	}	
	
	void solve() throws Exception {
		
		System.out.println("=== part1 ===");
		
		var robo = new Robo(my_input);
		//robo.input = robo::wanderRandomly;
		robo.input = robo::wanderWithMemory;
		robo.run();
		
		// now we have a complete map
		System.out.printf("move commands: %d%n", robo.moveCommands);

		robo.display();
		var map = robo.map;
		
		// test with prepared map
//		map = new PointMap<Integer>();
//		map.read(Util.linesArray("day15.txt"), c -> PIXELS.indexOf(c));

		var start = map.findPoint(START);		
		var oxygen = map.findPoint(OXYGEN);
		
		var dist1 = map.minDistances(start, c -> c!=WALL);	
		var doxy = dist1.get(oxygen);
		
		System.out.printf("=> shortest path to oxygen: %d%n", doxy);

		System.out.println("=== part2 ===");				
		
		var dist2 = map.minDistances(oxygen, c -> c!=WALL);
		var dmax = dist2.values().stream().mapToInt(x -> x).max().getAsInt();
		System.out.printf("=> longest path to oxygen: %d%n", dmax);
		
	}

}