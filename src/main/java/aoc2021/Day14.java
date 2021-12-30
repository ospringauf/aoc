package aoc2021;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 14: Extended Polymerization ---
// https://adventofcode.com/2021/day/14

class Day14 extends AocPuzzle {

	static List<String> inputExample = new Day14().file2lines("input14_example.txt");
	static List<String> inputReal = new Day14().file2lines("input14.txt");

	List<Rule> rules;
	Map<Param, Histogram> cache = new HashMap<>();

	record Rule(char a, char b, char c) {
		static Rule parse(String s) {
			return split(s, " -> ").to(r -> new Rule(r.s(0).charAt(0), r.s(0).charAt(1), r.s(1).charAt(0)));
		}

		public boolean match(char a2, char b2) {
			return (a == a2) && (b == b2);
		}
	}

	// cache key
	record Param(int step, char a, char b) {
	}

	// histogram: vector of element occurences
	record Histogram(Array<Long> a) {
		Histogram plus(Histogram h2) {
			return new Histogram(a.zip(h2.a).map(t -> t._1 + t._2).toArray());
		}

		Histogram minus(Histogram h2) {
			return new Histogram(a.zip(h2.a).map(t -> t._1 - t._2).toArray());
		}

		Histogram plus(char c) {
			return new Histogram(a.update(c, a.get(c) + 1));
		}

		Histogram minus(char c) {
			return new Histogram(a.update(c, a.get(c) - 1));
		}

		static Histogram of(List<Character> l) {
			return l.foldLeft(empty(), (h, c) -> h.plus(c));
		}

		static Histogram empty() {
			return new Histogram(Array.fill('Z' + 1, 0L));
		}

		public long min() {
			return a.filter(x -> x > 0).min().get();
		}

		public long max() {
			return a.max().get();
		}
	}

	List<Character> processPolymer(char a, char b) {
		var t = rules.find(r -> r.match(a, b));
		if (t.isDefined())
			return List.of(a, t.get().c, b);
		else
			return List.of(a, b);
	}

	void solveDumb(List<String> input, int steps) {
		var template = input.head();
		rules = input.drop(2).map(Rule::parse);

		var polymer = List.ofAll(template.toCharArray());

		for (var i : List.range(0, steps)) {
			polymer = polymer.sliding(2).flatMap(x -> processPolymer(x.get(0), x.get(1)).tail()).toList()
					.prepend(polymer.head());
		}
		var p = polymer;
		var freq = p.map(c -> p.count(c::equals)).sorted().toList();
		System.out.println(freq.last() - freq.head());
	}

	// now again, but clever --------------------------------------------------

	Histogram processPolymer(int steps, char a, char b) {
		Param key = new Param(steps, a, b);
		var h = cache.getOrDefault(key, null);
		if (h != null)
			return h;

		Histogram h0 = Histogram.of(List.of(a, b));
		Histogram result = null;

		if (steps == 0) {
			result = h0;
		} else {
			var rule = rules.find(r -> r.match(a, b));
			if (rule.isDefined()) {
				// AB -> C (ie. AB becomes ACB)
				var c = rule.get().c;
				var h1 = processPolymer(steps - 1, a, c);
				var h2 = processPolymer(steps - 1, c, b);
				result = h1.plus(h2).minus(c); // c was counted twice
			} else {
				result = h0;
			}
		}

		cache.put(key, result);
		return result;
	}

	void solve(List<String> input, int steps) {
		List<Character> template = List.ofAll(input.head().toCharArray());
		rules = input.drop(2).map(Rule::parse);

		Histogram result = template.sliding(2) // pairwise ...
				.map(pair -> processPolymer(steps, pair.get(0), pair.get(1))) // histogram for this pair
				.foldLeft(Histogram.empty(), (h1, h2) -> h1.plus(h2)); // add up

		// all inner elements were counted twice -> fix result
		result = result.minus(Histogram.of(template)).plus(template.head()).plus(template.last());

		System.out.println(result.max() - result.min());
	}

	public static void main(String[] args) {
		System.out.println("=== example (dumb, 10) -> 1588");
		new Day14().solveDumb(inputExample, 10);

		System.out.println("=== example (clever, 10) -> 1588");
		new Day14().solve(inputExample, 10);

		System.out.println("=== example (clever, 40) -> 2188189693529");
		new Day14().solve(inputExample, 40);

		System.out.println();

		System.out.println("=== my input (dumb, 10) -> 5656");
		new Day14().solveDumb(inputReal, 10);

		System.out.println("=== my input (clever, 10) -> 5656");
		new Day14().solve(inputReal, 10);

		System.out.println("=== my input (clever, 40) -> 12271437788530");
		timed(() -> new Day14().solve(inputReal, 40));
	}

}
