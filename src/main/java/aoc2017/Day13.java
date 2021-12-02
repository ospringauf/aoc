package aoc2017;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Predicate;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// --- Day 13: Packet Scanners ---
// https://adventofcode.com/2017/day/13

class Day13 extends AocPuzzle {

	record Scanner(int depth, int range) {
		static Scanner parse(String s) {
			var a = s.split(": ");
			return new Scanner(Integer.valueOf(a[0]), Integer.valueOf(a[1]));
		}

		int pos(int t) {
			int x = range - 1;
			if ((t / x) % 2 == 0)
				return t % x;
			else
				return x - (t % x);

		}
	}

//	List<Scanner> data = List.of(example.split("\n")).map(Scanner::parse);
	List<Scanner> data = file2lines("input13.txt").map(Scanner::parse);

	void part1() {
//		System.out.println(data);

		var severity = data.filter(s -> s.pos(s.depth) == 0).map(s -> s.depth * s.range).sum();
		System.out.println("severity => " + severity);
	}

	void part2() {
		Predicate<Integer> caught = d -> data.exists(s -> s.pos(s.depth + d) == 0);

		var starttime = Stream.from(0).dropWhile(caught).head();
		System.out.println("starttime => " + starttime);
	}

	void test() {
		var s = new Scanner(0, 5);
		System.out.println(List.range(0, 15).map(t -> s.pos(t)));
		
		assertEquals("0 1 2 3 4 3 2 1 0 1 2 3 4", List.range(0, 13).map(t -> s.pos(t)).mkString(" "));
	}

	public static void main(String[] args) {
		System.out.println("=== test");
		new Day13().test();

		System.out.println("=== part 1"); // 648
		new Day13().part1();

		System.out.println("=== part 2"); // 3_933_124
		timed(() -> new Day13().part2());
	}

	static String example = """
			0: 3
			1: 2
			4: 4
			6: 4
						""";

}
