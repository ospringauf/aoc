package aoc2017;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/4

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day04 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 337
		new Day04().part1();

		System.out.println("=== part 2"); // 231
		new Day04().part2();
	}

	final List<List<String>> data = lines("input04.txt").map(l -> Util.splitFields(l));

	void part1() {
		var r = data.count(pw -> pw.size() == pw.distinct().size());

		System.out.println(r);
	}

	void part2() {
//		System.out.println(anagram("abc","bac"));
//		System.out.println(anagram("oiii","ooii"));
		var r = data.count(pw -> ! pw.combinations(2).exists(l -> anagram(l.get(0), l.get(1))));
		System.out.println(r);
	}

	private boolean anagram(String s1, String s2) {
		var a1 = Array.ofAll(s1.toCharArray()).sorted();
		var a2 = Array.ofAll(s2.toCharArray()).sorted();
		return a1.eq(a2);
	}
}
