package aoc2017;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/2

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day02 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day02().part1();
		
		System.out.println("=== part 2");
		new Day02().part2();
	}

	List<String> data = lines("input02.txt");
//	List<String> data = List.of(example2.split("\n"));

	void part1() {
		var spreadsheet = data.map(l -> Util.string2ints(l));
		var r = spreadsheet.map(l -> l.max().get() - l.min().get()).sum();
		System.out.println(r);
	}

	
	void part2() {	
		var spreadsheet = data.map(l -> Util.string2ints(l));
		var nums = spreadsheet.map(l -> findNum(l));
		System.out.println(nums.sum());
	}
	
	int findNum(List<Integer> l) {
		return l.combinations(2)
			.map(c -> c.sorted())
			.map(c -> Tuple.of(c.get(0), c.get(1)))
			.filter(t -> (t._2 > t._1) && (t._2 % t._1 == 0))
			.map(t -> t._2/t._1)
			.single();
	}
	
	static String example = """
5 1 9 5
7 5 3
2 4 6 8			
			""";
	
	static String example2 = """
5 9 2 8
9 4 7 3
3 8 6 5			
			""";

}
