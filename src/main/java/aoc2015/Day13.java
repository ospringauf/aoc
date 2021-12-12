package aoc2015;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.collection.List;

// --- Day 13: ---
// https://adventofcode.com/2015/day/13

class Day13 extends AocPuzzle {

//	List<String> input = Util.splitLines(example);
	List<String> input = file2lines("day13.txt");
	
	record Rule(String name, int happy, String neighbor) {
		
		static Rule parse(String s) {
			var f = s.split(" ");
			return new Rule(f[0], Integer.parseInt(f[3]) * ("gain".equals(f[2]) ? 1 : -1), f[10].replaceAll("\\.", ""));
		}
		
		int gain(String left, String right) {
			return  (left.equals(neighbor) || right.equals(neighbor)) ? happy : 0;
		}
	}
	
	void part1() {
		var rules = input.map(Rule::parse).groupBy(r -> r.name);
		
		var persons = rules.keySet();
		//var persons = rules.keySet().add("You");
		System.out.println(persons);
		int n = persons.size();
		
		Function3<String, String, String, Integer> happiness = (l, name, r) -> rules.getOrElse(name, List.empty()).map(x -> x.gain(l, r)).sum().intValue();
		
		Function1<List<String>, Integer> scoreSeating = l -> List.range(0, l.size()).map(i -> happiness.apply(l.get((i-1+n)%n), l.get(i), l.get((i+1)%n))).sum().intValue();
		
		var perms = List.ofAll(Generator.permutation(persons.toJavaList()).simple()).map(List::ofAll);

		var r = perms.map(scoreSeating).max();
		System.out.println(r);		
	}

	
	public static void main(String[] args) {
		new Day13().part1();
	}

	static String example = """
Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.
			""";

}
