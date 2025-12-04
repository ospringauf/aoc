package aoc2025;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 2: Gift Shop ---
// https://adventofcode.com/2025/day/2

class Day02 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 23701357374
		timed(() -> new Day02().part1());
		System.out.println("=== part 2"); // 34284458938
		timed(() -> new Day02().part2());
		timed(() -> new Day02().part2());
		timed(() -> new Day02().part2());
		timed(() -> new Day02().part2());
	}

//	String input = example;
    String input = file2string("input02.txt");
	List<Range> data = List.of(input.split(",")).map(Range::parse);

	record Range(long from, long to) {
		static Range parse(String s) {
			var a = s.split("-");
			return new Range(Long.parseLong(a[0]), Long.parseLong(a[1]));
		}

		List<Long> ids() {
			return List.rangeClosed(from, to);
		}
	}

	boolean invalid1(Long id) {
		var s = id.toString();
		var l = s.length();
		return (l%2 == 0) && s.substring(0, l / 2).equals(s.substring(l / 2));
	}

	// this is what I wrote
	boolean invalid2Human(Long id) {
		var s = id.toString();
		var l = s.length();
		var divisors = List.rangeClosed(1, l / 2).filter(x -> l % x == 0);

		for (var k : divisors) {
			var r = s.substring(0, k).repeat(l / k);
			if (s.equals(r))
				return true;
		}
		return false;
	}

	// this is what Claude suggests
	boolean invalid2(Long id) {
		var s = id.toString();
		var l = s.length();
		// Efficient check: if s is made of repeating pattern, 
		// it will appear in (s+s) at position < length
		// We exclude position 0 and position l (trivial matches)
		return (s + s).indexOf(s, 1) < l;
	}
	
	void part1() {
		List<Long> invalid = List.empty();
		for (var r : data) {
			invalid = invalid.appendAll(r.ids().filter(this::invalid1));
		}
		System.out.println(invalid);
		System.out.println(invalid.sum());
	}

	void part2() {
		List<Long> invalid = List.empty();
		for (var r : data) {
			invalid = invalid.appendAll(r.ids().filter(this::invalid2));
		}
		System.out.println(invalid);
		System.out.println(invalid.sum());
	}

	static String example = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";
}