package aoc2020;

import java.util.HashMap;
import java.util.Map;

import io.vavr.collection.List;

// --- Day 10: Adapter Array ---
// https://adventofcode.com/2020/day/10

@SuppressWarnings({ "deprecation", "preview" })
class Day10 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 1984
		new Day10().part1();

		System.out.println("=== part 2"); // 3543369523456
		new Day10().part2();
	}

//		List<Integer> input = List.of(example2.split("\n")).map(Integer::valueOf);
	final List<Integer> input = ints("input10.txt");

	void part1() {
		var target = input.max().get() + 3;

		var curr = 0;
		var adapters = List.of(curr);
		while (curr < target - 3) {
			var last = curr;
			curr = input.filter(a -> a > last & a <= last + 3).min().get();
			adapters = adapters.append(curr);
		}
		adapters = adapters.append(target);
//		System.out.println(adapters);

		var diffs = adapters.sliding(2).map(w -> w.get(1) - w.get(0)).toList();
//		System.out.println(diff2);
		var d1 = diffs.count(x -> x == 1);
		var d3 = diffs.count(x -> x == 3);
		System.out.println(d1 * d3);

	}

	void part2() {
		var target = input.max().get() + 3;

		long n = options(0, target - 3);
		System.out.println(n);
	}

	Map<Integer, Long> cache = new HashMap<>();

	private long options(int curr, int target) {
		if (cache.containsKey(curr))
			return cache.get(curr);

		long n = 0;
		if (curr == target)
			n = 1;
		else
			n = input.filter(a -> a > curr && a <= curr + 3).map(a -> options(a, target)).sum().longValue();
		cache.put(curr, n);
		return n;
	}

	static String example2 = """
			28
			33
			18
			42
			31
			14
			46
			20
			48
			47
			24
			23
			49
			45
			19
			38
			39
			11
			1
			32
			25
			35
			8
			17
			7
			9
			4
			2
			34
			10
			3
						""";

	static String example1 = """
			16
			10
			15
			5
			1
			11
			7
			19
			6
			12
			4
					""";

}
