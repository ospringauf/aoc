package aoc2019;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Day 17: Set and Forget
 * https://adventofcode.com/2019/day/17
 *
 */
public class Day17 {

	static long[] my_input = { 1, 330, 331, 332, 109, 2734, 1102, 1182, 1, 15, 1102, 1, 1429, 24, 1002, 0, 1, 570, 1006,
			570, 36, 1001, 571, 0, 0, 1001, 570, -1, 570, 1001, 24, 1, 24, 1106, 0, 18, 1008, 571, 0, 571, 1001, 15, 1,
			15, 1008, 15, 1429, 570, 1006, 570, 14, 21102, 58, 1, 0, 1105, 1, 786, 1006, 332, 62, 99, 21101, 0, 333, 1,
			21102, 73, 1, 0, 1105, 1, 579, 1101, 0, 0, 572, 1101, 0, 0, 573, 3, 574, 101, 1, 573, 573, 1007, 574, 65,
			570, 1005, 570, 151, 107, 67, 574, 570, 1005, 570, 151, 1001, 574, -64, 574, 1002, 574, -1, 574, 1001, 572,
			1, 572, 1007, 572, 11, 570, 1006, 570, 165, 101, 1182, 572, 127, 1001, 574, 0, 0, 3, 574, 101, 1, 573, 573,
			1008, 574, 10, 570, 1005, 570, 189, 1008, 574, 44, 570, 1006, 570, 158, 1105, 1, 81, 21102, 1, 340, 1, 1105,
			1, 177, 21101, 0, 477, 1, 1106, 0, 177, 21101, 0, 514, 1, 21101, 0, 176, 0, 1106, 0, 579, 99, 21102, 1, 184,
			0, 1106, 0, 579, 4, 574, 104, 10, 99, 1007, 573, 22, 570, 1006, 570, 165, 1001, 572, 0, 1182, 21102, 1, 375,
			1, 21101, 211, 0, 0, 1106, 0, 579, 21101, 1182, 11, 1, 21102, 1, 222, 0, 1105, 1, 979, 21102, 1, 388, 1,
			21102, 233, 1, 0, 1105, 1, 579, 21101, 1182, 22, 1, 21101, 0, 244, 0, 1106, 0, 979, 21101, 401, 0, 1, 21101,
			0, 255, 0, 1105, 1, 579, 21101, 1182, 33, 1, 21102, 266, 1, 0, 1106, 0, 979, 21102, 1, 414, 1, 21102, 1,
			277, 0, 1106, 0, 579, 3, 575, 1008, 575, 89, 570, 1008, 575, 121, 575, 1, 575, 570, 575, 3, 574, 1008, 574,
			10, 570, 1006, 570, 291, 104, 10, 21102, 1182, 1, 1, 21101, 313, 0, 0, 1106, 0, 622, 1005, 575, 327, 1102,
			1, 1, 575, 21102, 327, 1, 0, 1105, 1, 786, 4, 438, 99, 0, 1, 1, 6, 77, 97, 105, 110, 58, 10, 33, 10, 69,
			120, 112, 101, 99, 116, 101, 100, 32, 102, 117, 110, 99, 116, 105, 111, 110, 32, 110, 97, 109, 101, 32, 98,
			117, 116, 32, 103, 111, 116, 58, 32, 0, 12, 70, 117, 110, 99, 116, 105, 111, 110, 32, 65, 58, 10, 12, 70,
			117, 110, 99, 116, 105, 111, 110, 32, 66, 58, 10, 12, 70, 117, 110, 99, 116, 105, 111, 110, 32, 67, 58, 10,
			23, 67, 111, 110, 116, 105, 110, 117, 111, 117, 115, 32, 118, 105, 100, 101, 111, 32, 102, 101, 101, 100,
			63, 10, 0, 37, 10, 69, 120, 112, 101, 99, 116, 101, 100, 32, 82, 44, 32, 76, 44, 32, 111, 114, 32, 100, 105,
			115, 116, 97, 110, 99, 101, 32, 98, 117, 116, 32, 103, 111, 116, 58, 32, 36, 10, 69, 120, 112, 101, 99, 116,
			101, 100, 32, 99, 111, 109, 109, 97, 32, 111, 114, 32, 110, 101, 119, 108, 105, 110, 101, 32, 98, 117, 116,
			32, 103, 111, 116, 58, 32, 43, 10, 68, 101, 102, 105, 110, 105, 116, 105, 111, 110, 115, 32, 109, 97, 121,
			32, 98, 101, 32, 97, 116, 32, 109, 111, 115, 116, 32, 50, 48, 32, 99, 104, 97, 114, 97, 99, 116, 101, 114,
			115, 33, 10, 94, 62, 118, 60, 0, 1, 0, -1, -1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 6, 0, 0, 109, 4, 1202, -3, 1, 587,
			20101, 0, 0, -1, 22101, 1, -3, -3, 21101, 0, 0, -2, 2208, -2, -1, 570, 1005, 570, 617, 2201, -3, -2, 609, 4,
			0, 21201, -2, 1, -2, 1105, 1, 597, 109, -4, 2106, 0, 0, 109, 5, 2102, 1, -4, 630, 20102, 1, 0, -2, 22101, 1,
			-4, -4, 21102, 0, 1, -3, 2208, -3, -2, 570, 1005, 570, 781, 2201, -4, -3, 652, 21002, 0, 1, -1, 1208, -1,
			-4, 570, 1005, 570, 709, 1208, -1, -5, 570, 1005, 570, 734, 1207, -1, 0, 570, 1005, 570, 759, 1206, -1, 774,
			1001, 578, 562, 684, 1, 0, 576, 576, 1001, 578, 566, 692, 1, 0, 577, 577, 21101, 0, 702, 0, 1105, 1, 786,
			21201, -1, -1, -1, 1106, 0, 676, 1001, 578, 1, 578, 1008, 578, 4, 570, 1006, 570, 724, 1001, 578, -4, 578,
			21101, 0, 731, 0, 1106, 0, 786, 1105, 1, 774, 1001, 578, -1, 578, 1008, 578, -1, 570, 1006, 570, 749, 1001,
			578, 4, 578, 21101, 0, 756, 0, 1106, 0, 786, 1106, 0, 774, 21202, -1, -11, 1, 22101, 1182, 1, 1, 21102, 774,
			1, 0, 1105, 1, 622, 21201, -3, 1, -3, 1105, 1, 640, 109, -5, 2106, 0, 0, 109, 7, 1005, 575, 802, 21002, 576,
			1, -6, 20101, 0, 577, -5, 1105, 1, 814, 21102, 1, 0, -1, 21102, 1, 0, -5, 21102, 0, 1, -6, 20208, -6, 576,
			-2, 208, -5, 577, 570, 22002, 570, -2, -2, 21202, -5, 29, -3, 22201, -6, -3, -3, 22101, 1429, -3, -3, 1202,
			-3, 1, 843, 1005, 0, 863, 21202, -2, 42, -4, 22101, 46, -4, -4, 1206, -2, 924, 21102, 1, 1, -1, 1105, 1,
			924, 1205, -2, 873, 21102, 1, 35, -4, 1105, 1, 924, 1202, -3, 1, 878, 1008, 0, 1, 570, 1006, 570, 916, 1001,
			374, 1, 374, 2101, 0, -3, 895, 1101, 2, 0, 0, 2102, 1, -3, 902, 1001, 438, 0, 438, 2202, -6, -5, 570, 1,
			570, 374, 570, 1, 570, 438, 438, 1001, 578, 558, 922, 20101, 0, 0, -4, 1006, 575, 959, 204, -4, 22101, 1,
			-6, -6, 1208, -6, 29, 570, 1006, 570, 814, 104, 10, 22101, 1, -5, -5, 1208, -5, 45, 570, 1006, 570, 810,
			104, 10, 1206, -1, 974, 99, 1206, -1, 974, 1101, 1, 0, 575, 21101, 973, 0, 0, 1105, 1, 786, 99, 109, -7,
			2105, 1, 0, 109, 6, 21101, 0, 0, -4, 21102, 1, 0, -3, 203, -2, 22101, 1, -3, -3, 21208, -2, 82, -1, 1205,
			-1, 1030, 21208, -2, 76, -1, 1205, -1, 1037, 21207, -2, 48, -1, 1205, -1, 1124, 22107, 57, -2, -1, 1205, -1,
			1124, 21201, -2, -48, -2, 1105, 1, 1041, 21102, -4, 1, -2, 1106, 0, 1041, 21101, -5, 0, -2, 21201, -4, 1,
			-4, 21207, -4, 11, -1, 1206, -1, 1138, 2201, -5, -4, 1059, 1201, -2, 0, 0, 203, -2, 22101, 1, -3, -3, 21207,
			-2, 48, -1, 1205, -1, 1107, 22107, 57, -2, -1, 1205, -1, 1107, 21201, -2, -48, -2, 2201, -5, -4, 1090,
			20102, 10, 0, -1, 22201, -2, -1, -2, 2201, -5, -4, 1103, 2101, 0, -2, 0, 1106, 0, 1060, 21208, -2, 10, -1,
			1205, -1, 1162, 21208, -2, 44, -1, 1206, -1, 1131, 1105, 1, 989, 21101, 439, 0, 1, 1105, 1, 1150, 21102, 1,
			477, 1, 1105, 1, 1150, 21102, 1, 514, 1, 21102, 1, 1149, 0, 1105, 1, 579, 99, 21101, 1157, 0, 0, 1106, 0,
			579, 204, -2, 104, 10, 99, 21207, -3, 22, -1, 1206, -1, 1138, 2101, 0, -5, 1176, 1202, -4, 1, 0, 109, -6,
			2105, 1, 0, 6, 5, 28, 1, 28, 1, 28, 1, 28, 1, 28, 1, 20, 11, 1, 7, 10, 1, 7, 1, 1, 1, 1, 1, 5, 1, 10, 1, 5,
			11, 1, 1, 10, 1, 5, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 10, 9, 1, 1, 1, 1, 1, 5, 16, 1, 3, 1, 1, 1, 1, 1, 1, 1,
			18, 5, 1, 1, 1, 1, 1, 1, 24, 1, 1, 1, 1, 1, 24, 1, 1, 1, 1, 1, 24, 1, 1, 1, 1, 1, 24, 5, 7, 1, 18, 1, 9, 1,
			18, 1, 9, 1, 18, 1, 9, 1, 18, 1, 9, 1, 18, 1, 9, 1, 12, 7, 9, 1, 12, 1, 15, 1, 12, 1, 15, 1, 12, 1, 15, 1,
			12, 1, 15, 1, 12, 1, 15, 1, 2, 5, 5, 1, 9, 7, 2, 1, 3, 1, 5, 1, 9, 1, 8, 1, 3, 1, 5, 1, 9, 1, 8, 1, 3, 1, 5,
			1, 9, 1, 8, 1, 3, 13, 3, 1, 8, 1, 9, 1, 5, 1, 3, 1, 8, 11, 5, 1, 1, 5, 22, 1, 1, 1, 1, 1, 1, 1, 22, 1, 1, 1,
			1, 1, 1, 1, 22, 1, 1, 1, 1, 1, 1, 1, 22, 13, 18, 1, 1, 1, 1, 1, 5, 1, 16, 5, 1, 1, 5, 1, 16, 1, 1, 1, 3, 1,
			5, 1, 16, 1, 1, 11, 16, 1, 5, 1, 22, 7, 6

	};

//	static final int CMD_NORTH = 1;
//	static final int CMD_SOUTH = 2;
//	static final int CMD_WEST = 3;
//	static final int CMD_EAST = 4;
//
//	static final int WALL = 0;
//	static final int CLEAR = 1;
//	static final int OXYGEN = 2;
//	static final int START = 3;
//	static final int UNCHARTED = 4;
//	static final String PIXELS = "#.X0 ";
//
//	static Point nextPos(Point p, int moveCmd) {
//		switch (moveCmd) {
//		case CMD_NORTH:
//			return p.north();
//		case CMD_SOUTH:
//			return p.south();
//		case CMD_WEST:
//			return p.west();
//		case CMD_EAST:
//			return p.east();
//		}
//		return null;
//	}

	static class AsciiCam extends IntComputer {

		PointMap<Integer> map = new PointMap<>();
		Point pos = Point.ZERO;
		int currentMove;
		Random random = new Random();
		int moveCommands = 0;

		AsciiCam(long[] program) {
			super(program);
		}



		// update map and position
		@Override
		void output(long value) {

			System.out.print((char)value);
//			switch ((int) value) {
//			case WALL:
//				// robo has not moved!
//				map.put(nextPos(pos, currentMove), WALL);
//				break;
//			case CLEAR:
//				pos = nextPos(pos, currentMove);
//				map.put(pos, CLEAR);
//				break;
//			case OXYGEN:
//				pos = nextPos(pos, currentMove);
//				map.put(pos, OXYGEN);
//				System.out.println("found oxygen!");
//				display();
//				break;
//
//			default:
//				break;
//			}

		}

		void display() {
			System.out.println("--- map start ---");
//			Function<Point, Character> paintFunc = p -> PIXELS.charAt(map.getOrDefault(p, UNCHARTED));
//			map.boundingBox().print(paintFunc);
			System.out.println("--- map end  ---");
		}

	
	}

	public static void main(String[] args) throws Exception {
		new Day17().solve();
	}
	
	String PIXELS = ".#^v><";
	int OPEN=0;
	int SCAFF=1;

	void solve() throws Exception {

		System.out.println("=== part1 ===");

		var robo = new AsciiCam(my_input);
//		robo.run();
		
		PointMap<Integer> map = new PointMap<Integer>();
		map.read(Util.linesArray("output17.txt"), c -> PIXELS.indexOf(c));
		
		Function<Point, Character> pointColor = p -> PIXELS.charAt(map.get(p));
		map.boundingBox().print(p -> PIXELS.charAt(map.get(p)));

		var scaff = map.findPoints(SCAFF).collect(Collectors.toSet());
		var inters = map.findPoints(SCAFF).filter(p -> p.neighbors().allMatch(scaff::contains));
		var sap = inters.mapToInt(p -> p.x*p.y).sum();
		System.out.println(sap);
		
		String c = "R,4,R,10,R,8,R,4,10,R,6,R,4,R,4,R,10,R,8,R,4,10,R,6,R,4,R,4,  L,12,R,6,L,12,R,10,R,6,R,4,R,4,L,12, R,6,L,12,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,  L,12,R,6,L,12";
		
		
	}

}

/*

R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12

    01234567890123456789
a = 4,R,4,L,12,R,6,L,12
R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,a,R,10,R,6,R,a,R,4,R,10,R,8,R,4,R,10,R,6,R,a
b = R,10,R,6,R
R,4,R,10,R,8,R,4,b,4,R,4,R,10,R,8,R,4,b,a,b,a,R,4,R,10,R,8,R,4,b,a
c = R,4,R,10,R,8,R,4
c,b,4,c,b,a,b,a,c,b,a

*/


