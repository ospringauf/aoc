package aoc2021;

import common.AocPuzzle;
import common.PointMap;
import io.vavr.collection.List;

// --- Day 25: Sea Cucumber ---
// https://adventofcode.com/2021/day/25

class Day25 extends AocPuzzle {

//	List<String> input = Util.splitLines(example2);
	List<String> input = file2lines("input25.txt");

	void part1() {
		var map = new PointMap<Character>();
		map.read(input);
		var bb = map.boundingBox();

		map.print();
		System.out.println();

		int step = 0;		
		boolean moved = false;
		
		var herd1 = map.findPoints('>');
		var herd2 = map.findPoints('v');
		
		var eastOf = List.ofAll(map.keySet()).toMap(p -> p, p -> bb.wrapX(p.east()));
		var southOf = List.ofAll(map.keySet()).toMap(p -> p, p -> bb.wrapY(p.south()));
		
		do {
			step++;
			moved = false;
			// east
			{
				var move = herd1.filter(p -> map.get(eastOf.get(p).get()) == '.');
				moved |= move.nonEmpty();
				for (var p : move) {
					var pn = eastOf.get(p).get();
					map.put(p, '.');
					map.put(pn, '>');
					herd1 = herd1.replace(p, pn);
				}
			}

			// south
			{
				var move = herd2.filter(p -> map.get(southOf.get(p).get()) == '.');
				moved |= move.nonEmpty();
				for (var p : move) {
					var pn = southOf.get(p).get();
					map.put(p, '.');
					map.put(pn, 'v');
					herd2 = herd2.replace(p, pn);
				}
			}
			System.out.println(step);
		} while (moved);

		map.print();
		System.out.println(step);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 601
		new Day25().part1();
	}

	static String example1 = """
			...>...
			.......
			......>
			v.....>
			......>
			.......
			..vvv..
						""";

	static String example2 = """
			v...>>.vv>
			.vv>>.vv..
			>>.>v>...v
			>>v>>.>.v.
			v>v.vv.v..
			>.>>..v...
			.vv..>.>v.
			v.v..>>v.v
			....v..v.>
						""";

}
