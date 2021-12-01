package aoc2020;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 1: Report Repair ---
// https://adventofcode.com/2020/day/1

@SuppressWarnings("deprecation")
class Day01 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 800139
		new Day01().part1();
		System.out.println("=== part 2"); // 59885340
		new Day01().part2();
	}

	int findProd(int sum, List<Integer> n) {
		return n.filter(x -> n.contains(sum - x)).map(x -> x*(sum-x)).getOrElse(0);
	}
	
	String data = file2string("input01.txt");
	List<Integer> numbers = Util.string2ints(data);

	private void part1() {
		System.out.println(findProd(2020, numbers));
	}
	
	private void part2() {
		var prod = numbers.map(x -> x * findProd(2020-x, numbers)).filter(x -> x>0).head();
		System.out.println(prod);
	}

static String inp0 = """
1721
979
366
299
675
1456		
		""";	

}
