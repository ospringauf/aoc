package aoc2017;

import common.AocPuzzle;
import common.Direction;
import common.PointMap;
import common.Pose;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

// --- Day 19: A Series of Tubes ---
// https://adventofcode.com/2017/day/19

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day19 extends AocPuzzle {
	
	void solve() {
//		var lines = Util.splitLines(example);
		var lines = lines("input19.txt");

		var map = new PointMap<Character>();
		map.read(lines);
		
		var start = HashSet.ofAll(map.keySet()).filter(p -> p.y() == 0 && map.get(p) == '|').single();
		var p = new Pose(Direction.SOUTH, start);
		
		List<Character> collected = List.empty();
		
//		m.print();
//		System.out.println(p);
		
		int i=0;
		while (true) {
			var current = map.get(p.pos());
			var ahead = map.getOrDefault(p.ahead().pos(), ' ');
			
//			System.out.println(p + " " + c + " " + a + "  -> " + collected);
			
			if (Character.isAlphabetic(current)) {
				collected = collected.append(current);
			}
			
			if (ahead == ' ' && current == '+') {
				var l = map.getOrDefault(p.left().pos(), ' ');
				if (l != ' ')
					p = p.turnLeft();
				else	
					p = p.turnRight();
			} else if (ahead == ' ') {
				break;
			} else {
				p = p.ahead();
				i++;
			}
		}
		
		System.out.println("=== part 1"); // SXPZDFJNRL 
		System.out.println("collected: " + collected.mkString());
		
		System.out.println("=== part 2"); // 18126
		System.out.println("steps: " + (i+1));
	}


	public static void main(String[] args) {
		new Day19().solve();
	}

	static String example = """
			    |
			    |  +--+
			    A  |  C
			F---|----E|--+
			    |  |  |  D
			    +B-+  +--+
					""";

}
