package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 9: Explosives in Cyberspace ---
// https://adventofcode.com/2016/day/9

class Day09 extends AocPuzzle {

	record Segment(int c, int t, String data) {
		int len1() {
			return c * t + (data.length() - c);
		}

		long len2() {
			if (c == 0)
				return data.length();
			var seg = parse(data.substring(0, c));
			var l = seg.map(s -> s.len2()).sum().longValue();
			return l * t + (data.length() - c);
		}
	}

	static List<Segment> parse(String s) {
		List<Segment> l = List.empty();
		while (s.length() > 0) {
			Segment seg;
			int b;
			if (!s.startsWith("(")) {
				b = s.indexOf("(");
				if (b == -1)
					b = s.length();

				seg = new Segment(0, 1, s.substring(0, b));
			} else {
				var a = s.indexOf(")");
				var f = split(s.substring(0, a), "[\\(x\\)]");
				b = s.indexOf("(", a + f.i(1));
				if (b == -1)
					b = s.length();
				seg = new Segment(f.i(1), f.i(2), s.substring(a + 1, b));
			}
			l = l.append(seg);
			s = s.substring(b);
		}
		return l;
	}

	void part1() {
		var s = file2lines("day09.txt").get(0);
		// var s = "(8x2)(3x3)ABCY";
		List<Segment> l = parse(s);

		l.forEach(x -> System.out.println(x));

		System.out.println(l.map(x -> x.len1()).sum());
	}

	void part2() {
		var s = file2lines("day09.txt").get(0);
//		var s = "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN";
		List<Segment> l = parse(s);

		l.forEach(x -> System.out.println(x));

		System.out.println(l.map(x -> x.len2()).sum());

	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 120765
		timed(() -> new Day09().part1());

		System.out.println("=== part 2"); // 11658395076
		timed(() -> new Day09().part2());
	}
}
