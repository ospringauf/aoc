package aoc2020;

import common.*;
import io.vavr.collection.List;

// --- Day 12: Rain Risk ---
// https://adventofcode.com/2020/day/12

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day12 extends AocPuzzle {

	public static void main(String[] args) {
		
		System.out.println("=== part 1"); // 445
		new Day12().part1();
		
		System.out.println("=== part 2"); // 42495
		new Day12().part2();
	}

	List<String> data = lines("input12.txt");
//	List<String> data = List.of(example.split("\n"));

	void part1() {
		Pose p = new Pose(Direction.EAST, 0, 0);
		
		for (String s : data) {
			int value = Integer.valueOf(s.substring(1));
			
			p = switch (s.charAt(0)) {
			
			case 'N' -> p.go(p.pos().north(value));
			case 'S' -> p.go(p.pos().south(value));
			case 'E' -> p.go(p.pos().east(value));
			case 'W' -> p.go(p.pos().west(value));
			
			case 'L' -> p.repeat(value/90, Pose::turnLeft);
			case 'R' -> p.repeat(value/90, Pose::turnRight);
			
			case 'F' -> p.ahead(value);
			
			default -> p;
			};
		}
		System.out.println(p.pos());
		System.out.println(p.pos().manhattan());
	}

	void part2() {	
		var pos = new Point(0,0);
		var waypoint = new Point(0,0).east(10).north(1);
//		System.out.println("start: " + pos + "  " + wp);
		
		for (String s : data) {
			int value = Integer.valueOf(s.substring(1));
			
			switch (s.charAt(0)) {
			
			case 'N' -> { waypoint = waypoint.north(value); }
			case 'S' -> { waypoint = waypoint.south(value); }
			case 'E' -> { waypoint = waypoint.east(value); }
			case 'W' -> { waypoint = waypoint.west(value); }
			
			case 'L' -> { waypoint = waypoint.repeat(value/90, Point::rotLeft); }
			case 'R' -> { waypoint = waypoint.repeat(value/90, Point::rotRight); }
			
			case 'F' -> { pos = pos.translate(value*waypoint.x(), value*waypoint.y()); }
			
			default -> {}
			};
			
//			System.out.println(s + ": " + pos + "  " + wp);
		}
		System.out.println(pos);
		System.out.println(pos.manhattan());
	}
	
	static String example = """
F10
N3
F7
R90
F11			
			""";

}
