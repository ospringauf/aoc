package aoc2019;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
 * Day 17: Set and Forget
 * https://adventofcode.com/2019/day/17
 *
 */
public class Day17 {

	static final long[] PROGRAM = Util.readIntProg("input17.txt");

	static class AsciiCam extends IntComputer {
		AsciiCam(long[] program) {
			super(program);
		}

		int x = 0, y = 0;
		PointMap<Character> map = new PointMap<>();

		@Override
		void output(long value) {
			if (value > 255)
				System.out.println(value);
			else {
				char c = (char) value;
				System.out.print(c);
				if (c == '\n') {
					x = 0;
					y++;
				} else {
					map.put(Point.of(x++, y), c);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("=== part 1 ===");
		var map = new Day17().part1();

		System.out.println("=== part 2 ===");
		new Day17().part2(map);
	}

	PointMap<Character> part1() {
		var robo = new AsciiCam(PROGRAM);
		robo.run();
		var map = robo.map;

		var scaffolds = map.findPoints('#').collect(Collectors.toSet());
		var intersections = map.findPoints('#').filter(p -> p.neighbors().allMatch(scaffolds::contains));
		var sap = intersections.mapToInt(p -> p.x * p.y).sum();
		System.out.println("scaffold alignment parameter sum: " + sap);

		return map;
	}
	
	

	void part2(PointMap<Character> map) {
		var cmd = tracePath(map);

		System.out.println(cmd);
		
		
		var A = "R,4,L,12,R,6,L,12";
		var B = "R,10,R,6,R,4";
		var C = "R,4,R,10,R,8,R,4";
		var M = "C,B,C,B,A,B,A,C,B,A";
		var video = "n";

		var commands = M + "\n" + A + "\n" + B + "\n" + C + "\n" + video + "\n";
		var input = commands.chars().boxed().collect(Collectors.toList());

		var robo = new AsciiCam(PROGRAM);
		robo.input = () -> input.remove(0);
		robo.mem[0] = 2;
		robo.run();
	}
//
//	void part1b() throws Exception {
//		String PIXELS = "#.^v><";
//		PointMap<Integer> map = new PointMap<>();
//		map.read(Util.linesArray("output17.txt"), PIXELS::indexOf);
//
////        map.boundingBox().print(p -> PIXELS.charAt(map.get(p)));
//
//		
//	}
//
//	void part2() {
//		var A = "R,4,L,12,R,6,L,12";
//		var B = "R,10,R,6,R,4";
//		var C = "R,4,R,10,R,8,R,4";
//		var M = "C,B,C,B,A,B,A,C,B,A";
//		var video = "n";
//
//		var commands = M + "\n" + A + "\n" + B + "\n" + C + "\n" + video + "\n";
//		var input = commands.chars().boxed().collect(Collectors.toList());
//
//		var robo = new AsciiCam(PROGRAM);
//		robo.input = () -> input.remove(0);
//		robo.mem[0] = 2;
//		robo.run();
//	}

	private ArrayList<String> tracePath(PointMap<Character> map) {
		System.out.println("extract path");
		var start = map.findPoints(c -> "^v><".indexOf(c) >= 0).findFirst().get();
		var d = map.get(start);
		var p = new Pose(start.x, start.y, (d == '^') ? Direction.NORTH
				: (d == '>') ? Direction.EAST : (d == 'v') ? Direction.SOUTH : Direction.WEST);

		int steps = 0;
		Predicate<Point> scaff = pt -> map.getOrDefault(pt, ' ') == '#';

		var cmd = new ArrayList<String>();
		while (scaff.test(p.next()) || scaff.test(p.left()) || scaff.test(p.right())) {
			if (scaff.test(p.next())) {
				steps++;
				p = p.next();
			} else {
				if (steps > 0)
					cmd.add(Integer.toString(steps));
				steps = 0;
				if (scaff.test(p.left())) {
					cmd.add("L");
					p.turnLeft();
				}
				if (scaff.test(p.right())) {
					cmd.add("R");
					p.turnRight();
				}
			}
		}
		if (steps > 0)
			cmd.add(Integer.toString(steps));
		return cmd;
	}
}

// solved by hand:
// R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12
//
//     01234567890123456789
// a = R,4,L,12,R,6,L,12
// m = R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,4,a,R,10,R,6,R,4,a,R,4,R,10,R,8,R,4,R,10,R,6,R,4,a
//
// b = R,10,R,6,R,4
// m = R,4,R,10,R,8,R,4,b,R,4,R,10,R,8,R,4,b,a,b,a,R,4,R,10,R,8,R,4,b,a
//
// c = R,4,R,10,R,8,R,4
// m = C,B,C,B,A,B,A,C,B,A
