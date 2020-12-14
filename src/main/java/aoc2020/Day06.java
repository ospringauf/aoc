package aoc2020;

import java.util.function.Function;
import java.util.function.Predicate;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 6: Custom Customs ---
// https://adventofcode.com/2020/day/6

@SuppressWarnings({ "deprecation", "preview" })
class Day06 extends AocPuzzle {

	public static void main(String[] args) throws Exception {

		String input = new Day06().readString("input06.txt");
//		String input = example;

		List<String> groups = List.of(input.split("\\n\\n"));
		List<Character> letters = List.rangeClosed('a', 'z');

		System.out.println("=== part 1"); // 6885

		Function<String, Integer> distinctAnswers = group -> letters.count(containedIn(group));
		Number n1 = groups.map(distinctAnswers).sum();

		System.out.println(n1);

		System.out.println("=== part 2"); // 3550

		Function<List<String>, Integer> commonAnswers = lines -> letters.count(c -> lines.forAll(containsChar(c)));
		Number n2 = groups.map(g -> List.of(g.split("\\n"))).map(commonAnswers).sum();

		System.out.println(n2);
	}

	private static Predicate<Character> containedIn(String s) {
		return c -> s.indexOf(c) >= 0;
	}

	private static Predicate<String> containsChar(Character c) {
		return s -> s.indexOf(c) >= 0;
	}

	static String example = """
			abc

			a
			b
			c

			ab
			ac

			a
			a
			a
			a

			b
						""";

}
