package aoc2015;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

// --- Day 16: Aunt Sue ---
// https://adventofcode.com/2015/day/16

class Day16 extends AocPuzzle {

	record Sue(String name, Map<String, Integer> properties) {

		static Sue parse(String s) {
			var s0 = s.split(": ", 2);
			var p = split(s0[1], ", ").map(x -> split(x, ": ").to(r -> Tuple.of(r.s(0), r.i(1))));

			return new Sue(s0[0], p.toMap(t -> t._1, t -> t._2));
		}

		boolean match1(Map<String, Integer> d) {
			Set<String> commonKeys = properties.keySet().intersect(d.keySet());
			return commonKeys.forAll(p -> properties.get(p).eq(d.get(p)));
		}

		boolean match2(Map<String, Integer> d) {
			Set<String> commonKeys = properties.keySet().intersect(d.keySet());
			return commonKeys.map(k -> Tuple.of(k, properties.get(k).get(), d.get(k).get())).forAll(this::match2);
		}

		private boolean match2(Tuple3<String, Integer, Integer> t) {
			return switch (t._1) {
			case "cats", "trees" -> t._2 > t._3;
			case "pomeranians", "goldfish" -> t._2 < t._3;
			default -> t._2.equals(t._3);
			};
		}

	}

	void part1() {
		var sues = file2lines("day16.txt").map(Sue::parse);
		var detect = Util.splitLines(ticker).toMap(s -> s.split(": ")[0], s -> Integer.parseInt(s.split(": ")[1]));

		var result = sues.filter(s -> s.match1(detect));
		System.out.println(result);
	}

	void part2() {
		var sues = file2lines("day16.txt").map(Sue::parse);
		var detect = Util.splitLines(ticker).toMap(s -> s.split(": ")[0], s -> Integer.parseInt(s.split(": ")[1]));

		var result = sues.filter(s -> s.match2(detect));
		System.out.println(result);
	}

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day16().part1();

		System.out.println("=== part 2");
		new Day16().part2();
	}

	static String ticker = """
			children: 3
			cats: 7
			samoyeds: 2
			pomeranians: 3
			akitas: 0
			vizslas: 0
			goldfish: 5
			trees: 3
			cars: 2
			perfumes: 1
									""";
}
