package aoc2025;

import org.jooq.lambda.Seq;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 01: Secret Entrance ---
// https://adventofcode.com/2025/day/1

class Day01 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day01().part1());
		System.out.println("=== part 2");
		timed(() -> new Day01().part2());
	}

	List<String> data0 = file2lines("input01.txt");
//	List<String> data0 = Util.splitLines(example);
	List<Integer> data = data0.map(s -> s.replace('R', '+').replace('L', '-')).map(Integer::parseInt);

	void part1() {
		var zero = 0;
		var dial = 50;
		
		// stream approach
		var values = List.of(dial);
		values = data.foldLeft(values, (v, d) -> v.append((v.last() + d) % 100));
		System.out.println(values.count(x -> x==0));
		
		// classic approach
		for (var d : data) {
			dial += d;
			dial %= 100;
			if (dial == 0)
				zero++;
		}

		System.out.println(zero);
	}

	void part2() {
		var zero = 0;
		var dial = 50;
		for (var d : data) {
			var r = d >= 0;
			var clicks = Math.abs(d);

			for (var c = 0; c < clicks; ++c) {
				dial += (r ? 1 : -1);
				dial %= 100;
				if (dial == 0)
					zero++;
			}
		}

		System.out.println(zero);
	}

	static String example = """
			L68
			L30
			R48
			L5
			R60
			L55
			L1
			L99
			R14
			L82""";
}
