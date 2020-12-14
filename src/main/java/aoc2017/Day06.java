package aoc2017;

import java.util.ArrayList;
import java.util.Arrays;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/6

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day06 extends AocPuzzle {

	public static void main(String[] args) {

		new Day06().solve();
	}

	void solve() {
		var banks = Util.string2intArray(input);
		String config = Arrays.toString(banks);

		final int N = banks.length;

		var seen = new ArrayList<String>();
		int cycles = 0;

		do {
			cycles++;
			seen.add(config);
			var r = List.range(0, N).maxBy(i -> banks[i]).get();
			int blocks = banks[r];
			banks[r] = 0;
			
			while (blocks-- > 0) 
				banks[++r % N]++;

			config = Arrays.toString(banks);
		} while (!seen.contains(config));

		System.out.println("=== part 1"); // 14029
		System.out.println(cycles);
		
		System.out.println("=== part 2"); // 2765
		System.out.println(seen.size() - seen.indexOf(config));
	}


	static String example = "0 2 7 0";

	static String input = "10	3	15	10	5	15	5	15	9	2	5	8	5	2	3	6";

}
