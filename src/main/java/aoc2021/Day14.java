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
	Map<Prod, Freq> cache = new HashMap<>();

	record Rule(char a, char b, char c) {
		static Rule parse(String s) {
			return split(s, " -> ").to(r -> new Rule(r.s(0).charAt(0), r.s(0).charAt(1), r.s(1).charAt(0)));
		}

		public boolean match(char a2, char b2) {
			return (a == a2) && (b == b2);
		}
	}

	// vector of element occurences
	record Freq(Array<Long> f) {
		Freq plus(Freq f2) {
			return new Freq(f.zip(f2.f).map(t -> t._1 + t._2).toArray());
		}

		Freq minus(Freq f2) {
			return new Freq(f.zip(f2.f).map(t -> t._1 - t._2).toArray());
		}
		
		Freq plus(char c) {
			return new Freq(f.update(c, f.get(c)+1));
		}

		Freq minus(char c) {
			return new Freq(f.update(c, f.get(c)-1));
		}

		static Freq of(List<Character> l) {
		    return l.foldLeft(empty(), (f, c) -> f.plus(c));
		}

		static Freq empty() {
			return new Freq(Array.fill('Z'+1, 0L));
		}
	}

	// cache key
	record Prod(int step, char a, char b) {	}

	List<Character> expand1(char a, char b) {
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
    		polymer = polymer
    				.sliding(2)
    				.flatMap(x -> expand1(x.get(0), x.get(1)).tail())
    				.toList()
    				.prepend(polymer.head());
    	}
    	var p = polymer;
    	var freq = p.map(c -> p.count(c::equals)).sorted().toList();
    	System.out.println(freq.last() - freq.head());
    }

    Freq expandN(int steps, char a, char b) {
		var f = cache.getOrDefault(new Prod(steps, a, b), null);
		if (f != null)
			return f;

		Freq f0 = Freq.of(List.of(a, b));
		
		if (steps == 0) {
			cache.put(new Prod(0, a, b), f0);
			return f0;
		}

		Freq result = null;
		var rule = rules.find(r -> r.match(a, b));
		if (rule.isDefined()) {		
			// AB -> C
			var c = rule.get().c;
			var f1 = expandN(steps-1, a, c);
			var f2 = expandN(steps-1, c, b);
			result = f1.plus(f2).minus(c);
		} else {		
			result = f0;
		}

		cache.put(new Prod(steps, a, b), result);
		return result;
	}

	void solve(List<String> input, int steps) {
		rules = input.drop(2).map(Rule::parse);

		List<Character> template = List.ofAll(input.head().toCharArray());

		var freq = template
		        .sliding(2)
		        .map(x -> expandN(steps, x.get(0), x.get(1))) // pairwise results
		        .foldLeft(Freq.empty(), (f1, f2) -> f1.plus(f2));

		// all inner elements are counted twice
		freq = freq
		        .minus(Freq.of(template))
		        .plus(template.head())
		        .plus(template.last());
		
		var r = freq.f.filter(x -> x > 0).sorted();
		System.out.println(r.last() - r.head());
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
