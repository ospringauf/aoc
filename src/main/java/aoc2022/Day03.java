package aoc2022;

import common.AocPuzzle;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 3: Rucksack Reorganization ---
// https://adventofcode.com/2022/day/3

class Day03 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 7831
		new Day03().part1();
		System.out.println("=== part 2"); // 2683
		new Day03().part2();
	}

	List<String> data = file2lines("input03.txt");
//	List<String> data = Util.splitLines(example);
	
	int prio(char c) {
		return (c >= 'a')? (c-'a'+1) : (c-'A'+27);
	}
	
	Character commonInGroup(List<String> group) {
		return group
				.map(elf -> HashSet.ofAll(elf.toCharArray()))
				.reduce((e1,e2) -> e1.intersect(e2))
				.single();
	}

	Set<Character> commonInCompartments(String ruck) {
		int h = ruck.length()/2;
		var compartment1 = HashSet.ofAll(ruck.substring(0,h).toCharArray());
		var compartment2 = HashSet.ofAll(ruck.substring(h).toCharArray());
		var common = compartment1.intersect(compartment2);
		return common;
	}
	
	void part1() {
		List<Character> items = List.empty();
	    for (var r : data) {	    	
	    	var common = commonInCompartments(r);
			items = items.appendAll(common);
	    }
	    var sum = items.map(this::prio).sum();
		System.out.println(sum);
	}

	
	void part2() {		
		var result = data.grouped(3)
			.map(g -> commonInGroup(g))
			.map(c -> prio(c))
			.sum();
		System.out.println(result);
	}



	static String example = """
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw			
			""";
}
