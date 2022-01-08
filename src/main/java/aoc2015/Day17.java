package aoc2015;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 17: No Such Thing as Too Much ---
// https://adventofcode.com/2015/day/17

class Day17 extends AocPuzzle {

	void solve() {
		var containers = Util.string2ints(myInput);

		long result = 0;
		for (int n = 1; n < containers.size(); ++n) {
			var combi = List.ofAll(Generator.combination(containers.asJava()).simple(n));
			int count = combi.count(l -> List.ofAll(l).sum().intValue() == 150);
			System.out.println(n + " --> " + count);
			result += count;
		}
		System.out.println(result);
	}

	public static void main(String[] args) {

		new Day17().solve();
	}

	static String example = """
			20
			15
			10
			5
			5
						""";

	static String myInput = """
			33
			14
			18
			20
			45
			35
			16
			35
			1
			13
			18
			13
			50
			44
			48
			6
			24
			41
			30
			42
									""";
}
