package aoc2021;

import common.AocPuzzle;
import io.vavr.Tuple;
import io.vavr.collection.List;

//--- Day 6: Lanternfish ---
// https://adventofcode.com/2021/day/6

class Day06 extends AocPuzzle {

    String input = myInput;
	List<Integer> data = split(input, ",").map(Integer::parseInt);

//	void part1() {
//		for (int i = 1; i <= 80; ++i) {
//			data = data.map(x -> (x == 0) ? 6 : (x - 1)).appendAll(data.filter(x -> x == 0).map(x -> 8));
//		}
//		System.out.println(data.length());
//	}

	void simDays(int days) {
		var fish = List.range(0, 8).toMap(i -> i, i -> (long) data.count(x -> x == i)); // number of fish per "age"
		for (int d = 1; d <= days; ++d) {
			var offspring = fish.getOrElse(0, 0L);
			fish = fish.remove(0).map((a, num) -> Tuple.of(a - 1, num));
			fish = fish.put(6, fish.getOrElse(6, 0L) + offspring);
			fish = fish.put(8, offspring);
		}
		System.out.println(fish.values().sum());
	}
	
	public static void main(String[] args) {
		System.out.println("=== part 1"); // 345387
		new Day06().simDays(80);

		System.out.println("=== part 2"); // 1574445493136
		new Day06().simDays(256);
	}

	static final String example = "3,4,3,1,2";
	static final String myInput = "2,5,3,4,4,5,3,2,3,3,2,2,4,2,5,4,1,1,4,4,5,1,2,1,5,2,1,5,1,1,1,2,4,3,3,1,4,2,3,4,5,1,2,5,1,2,2,5,2,4,4,1,4,5,4,2,1,5,5,3,2,1,3,2,1,4,2,5,5,5,2,3,3,5,1,1,5,3,4,2,1,4,4,5,4,5,3,1,4,5,1,5,3,5,4,4,4,1,4,2,2,2,5,4,3,1,4,4,3,4,2,1,1,5,3,3,2,5,3,1,2,2,4,1,4,1,5,1,1,2,5,2,2,5,2,4,4,3,4,1,3,3,5,4,5,4,5,5,5,5,5,4,4,5,3,4,3,3,1,1,5,2,4,5,5,1,5,2,4,5,4,2,4,4,4,2,2,2,2,2,3,5,3,1,1,2,1,1,5,1,4,3,4,2,5,3,4,4,3,5,5,5,4,1,3,4,4,2,2,1,4,1,2,1,2,1,5,5,3,4,1,3,2,1,4,5,1,5,5,1,2,3,4,2,1,4,1,4,2,3,3,2,4,1,4,1,4,4,1,5,3,1,5,2,1,1,2,3,3,2,4,1,2,1,5,1,1,2,1,2,1,2,4,5,3,5,5,1,3,4,1,1,3,3,2,2,4,3,1,1,2,4,1,1,1,5,4,2,4,3";

}
