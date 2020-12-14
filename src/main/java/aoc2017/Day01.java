package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/1

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day01 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day01().part1();
		
		System.out.println("=== part 2");
		new Day01().part2();
	}

	final List<Integer> data = List.ofAll(readString("input01.txt").trim().toCharArray()).map(c -> c-'0');

	void part1() {
		var data1 = data.append(data.head());
		var r = data1.sliding(2).filter(l -> l.get(0)==l.get(1)).map(l -> l.get(0)).sum();
		System.out.println(r);
	}

	void part2() {	
		var len = data.size();
		var r = List.range(0, len).filter(i -> data.get(i) == data.get((i+len/2)%len)).map(i -> data.get(i)).sum();
		System.out.println(r);
	}
	
	static String example = """
			
			""";

}
