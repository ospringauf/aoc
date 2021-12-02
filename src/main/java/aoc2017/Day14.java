package aoc2017;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 14: Disk Defragmentation ---
// https://adventofcode.com/2017/day/14

class Day14 extends AocPuzzle {

	void solve() {
//		var input = "flqrgnkx"; // test
		var input = "uugsqrei";

		List<String> lines = List.empty();

		long ones = 0;
		for (int i = 0; i < 128; ++i) {
			var s = Day10.bitString(Day10.knothash(input + "-" + i));
			lines = lines.append(s);
			ones += s.chars().filter(c -> c == '1').count();

		}
		System.out.println("=== part 1"); // 8194
		System.out.println("used squares => " + ones);

		System.out.println("=== part 2"); // 1141
		var map = new PointMap<Character>();
		map.read(lines);
		map.findPoints('0').forEach(map::remove);
		map.print();

		long regions = 0;
		while (!map.keySet().isEmpty()) {
			regions++;
			var p = map.keySet().iterator().next();
			var r = region(p, HashSet.ofAll(map.keySet()));
			r.forEach(map::remove);
		}
		System.out.println("regions => " + regions);
	}

	Set<Point> region(Point p, Set<Point> m) {
		Set<Point> r = HashSet.of(p);
		while (true) {
			var delta = r.flatMap(Point::neighbors).intersect(m).removeAll(r);
			if (delta.isEmpty())
				return r;
			r = r.addAll(delta);
		}
	}

	void test() {
		var input = "flqrgnkx";
		var k = Day10.knothash(input);
		
		timed(() -> Day10.knothash(input)); 

		System.out.println(Day10.bitString(k));
		System.out.println(Day10.bitString(k).length());
		System.out.println(Day10.hexString(k));
		
		System.out.println(Day10.bitString(Day10.knothash(input + "-0")));
		System.out.println(Day10.bitString(Day10.knothash(input + "-1")));
		System.out.println(Day10.bitString(Day10.knothash(input + "-2")));
	}

	public static void main(String[] args) {
		System.out.println("=== test");
		new Day14().test();

		timed(() -> new Day14().solve());
	}

}
