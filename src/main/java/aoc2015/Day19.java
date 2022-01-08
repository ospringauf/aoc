package aoc2015;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.regex.Pattern;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 19: Medicine for Rudolph ---
// https://adventofcode.com/2015/day/19

class Day19 extends AocPuzzle {

	void part1() {
		var f = file2string("day19.txt").split("\n\n");
		var input = f[1];
		var map = Util.splitLines(f[0]).map(s -> s.split(" => "));

		System.out.println(input);

		var result = expand(input, map);

		System.out.println(result.size());
	}

	private Set<String> expand(String input, List<String[]> map) {
		Set<String> result = HashSet.empty();
		for (var k : map) {
			for (var i = 0; i < input.length(); ++i) {
				String sub = input.substring(i);
				if (sub.startsWith(k[0])) {
					result = result.add(input.substring(0, i) + k[1] + sub.substring(k[0].length()));
					i += k[0].length() - 1;
				}
			}
		}

		return result;
	}

	int steps = 0;

	void part2() {
		var f = file2string("day19a.txt").split("\n\n");
		var input = f[1];
		var map = Util.splitLines(f[0]).map(s -> s.split(" => "));

		System.out.println(input);
		Set<String> result = HashSet.of(input);
		var s = input;

		for (int i = 0; i < 20; ++i) {
			s = reduce(s, map.filter(t -> t[1].contains(("("))));
			s = reduce(s, map.filter(t -> t[1].equals(t[0] + t[0])));
			s = reduce(s, map.filter(t -> t[1].startsWith(t[0])));
			s = reduce(s, map.filter(t -> t[1].equals("CaF")));
			s = reduce(s, map.filter(t -> t[1].contains(("("))));
			s = reduce(s, map.filter(t -> t[1].equals("PB")));
			s = reduce(s, map.filter(t -> t[1].equals("PMg")));
			s = reduce(s, map.filter(t -> t[1].equals("TiMg")));
			s = reduce(s, map.filter(t -> t[1].endsWith(t[0])));
			s = reduce(s, map.filter(t -> t[1].equals("SiAl")));
			s = reduce(s, map.filter(t -> t[1].equals("BF")));
			s = reduce(s, map.filter(t -> t[1].equals("ThF")));
		}

	}

	private String reduce(String s, List<String[]> rules) {
		boolean repeat = true;
		while (repeat) {
			repeat = false;
			for (var r : rules) {
				var i = s.indexOf(r[1]);
				if (i >= 0) {
					s = s.replaceFirst(Pattern.quote(r[1]), r[0]);
					repeat = true;
					steps++;
//					System.out.println(s.length());
				}
			}
		}
		System.out.println(s);
		System.out.println(steps);

		return s;
	}

	void test() {
		assertThat(1, is(1));
		System.out.println("passed");
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day19().test();

		System.out.println("=== part 1");
		new Day19().part1();

		System.out.println("=== part 2");
		new Day19().part2();
	}

	static String example = """
			""";

	static String myInput = """
			""";
}
