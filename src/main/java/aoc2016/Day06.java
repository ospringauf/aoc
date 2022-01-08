package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

// --- Day 6: Signals and Noise ---
// https://adventofcode.com/2016/day/6

class Day06 extends AocPuzzle {

	void part1() {
		// var l = Util.splitLines(example);
		var l = file2lines("day06.txt");
		var len = l.get(0).length();
		var r = List.range(0, len)
				.map(i -> List.rangeClosed('a', 'z').maxBy(c -> l.count(s -> s.charAt(i) == c)).get());
		System.out.println(r.mkString());
	}

	void part2() {
//		var l = Util.splitLines(example);
		var l = file2lines("day06.txt");
		var len = l.get(0).length();
		var chars = l.flatMap(s -> HashSet.ofAll(s.toCharArray()));
		var r = List.range(0, len).map(i -> chars.minBy(c -> l.count(s -> s.charAt(i) == c)).get());
		System.out.println(r.mkString());
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day06().part1());

		System.out.println("=== part 2");
		timed(() -> new Day06().part2());
	}

	static String example = """
			eedadn
			drvtee
			eandsr
			raavrd
			atevrs
			tsrnev
			sdttsa
			rasrtv
			nssdts
			ntnada
			svetve
			tesnvt
			vntsnd
			vrdear
			dvrsen
			enarar
						""";
}
