package aoc2015;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 14: Reindeer Olympics ---
// https://adventofcode.com/2015/day/x

class Day14 extends AocPuzzle {

	record Reindeer(String name, int speed, int duration, int rest) {

		static Reindeer parse(String s) {
			var f = s.split(" ");
			return new Reindeer(f[0], Integer.parseInt(f[3]), Integer.parseInt(f[6]), Integer.parseInt(f[13]));
		}

		int speed(int second) {
			return (second % (duration + rest)) < duration ? speed : 0;
		}

		int dist(int second) {
			return List.range(0, second).map(this::speed).sum().intValue();
		}
	}

	void part1() {
		List<String> input = Util.splitLines(myInput);
		var deer = input.map(Reindeer::parse);

		var r = deer.map(d -> d.dist(2503));
		System.out.println(r);
		System.out.println(r.max());
	}

	void part2() {
		List<String> input = Util.splitLines(myInput);
		var deer = input.map(Reindeer::parse);

		var score = deer.toMap(d -> d.name, d -> 0);

		for (int s : List.rangeClosed(1, 2503)) {
			var md = deer.map(d -> d.dist(s)).max().get();
			for (var d : deer.filter(d -> d.dist(s) == md)) {
				score = score.put(d.name, score.get(d.name).get() +1);
			}
		}

		System.out.println(score);
		System.out.println(score.maxBy(t -> t._2));

	}

	void test() {
		assertThat(1, is(1));
		System.out.println("passed");
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day14().test();

		System.out.println("=== part 1");
		new Day14().part1();

		System.out.println("=== part 2");
		new Day14().part2();
	}

	static String example = """
			Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
			Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
						""";

	static String myInput = """
			Rudolph can fly 22 km/s for 8 seconds, but then must rest for 165 seconds.
			Cupid can fly 8 km/s for 17 seconds, but then must rest for 114 seconds.
			Prancer can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
			Donner can fly 25 km/s for 6 seconds, but then must rest for 145 seconds.
			Dasher can fly 11 km/s for 12 seconds, but then must rest for 125 seconds.
			Comet can fly 21 km/s for 6 seconds, but then must rest for 121 seconds.
			Blitzen can fly 18 km/s for 3 seconds, but then must rest for 50 seconds.
			Vixen can fly 20 km/s for 4 seconds, but then must rest for 75 seconds.
			Dancer can fly 7 km/s for 20 seconds, but then must rest for 119 seconds.
						""";
}
