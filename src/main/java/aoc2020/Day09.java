package aoc2020;

import io.vavr.collection.Array;
import io.vavr.collection.List;

// https://adventofcode.com/2020/day/9

@SuppressWarnings({ "deprecation", "preview" })
class Day09 extends AocPuzzle {

	public static void main(String[] args) {
		var t0 = System.currentTimeMillis();

		System.out.println("=== part 1");
		var result1 = new Day09().part1();
		System.out.println(result1); // 29221323
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);

		System.out.println("=== part 2");
		var result2 = new Day09().part2(result1);
		System.out.println(result2); // 4389369
		System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
	}

//	int preamble = 5;
//	Array<Long> data = Array.of(example.split("\n")).map(Long::valueOf);

	int preamble = 25;
	Array<Long> data = lines("input09.txt").map(Long::valueOf).toArray();

	long part1() { 

		var r = data.sliding(preamble + 1)
				.filterNot(w -> check1(w))
				.map(w -> w.last());
		
		return r.get();
	}
	
	private boolean check1(Array<Long> window) {
		Long sum = window.last();
		return window.exists(x -> window.contains(sum - x));
	}

	long part2(long sum) {

		var r = List.range(0, data.size())
				.map(from -> check2(sum, from, data))
				.filterNot(Array::isEmpty)
				.get();
		
		System.out.println(r);

		return r.min().get() + r.max().get();
	}

	Array<Long> check2(long sum, int from, Array<Long> l) {
		var r = List.range(from, l.size())
				.filter(to -> l.subSequence(from, to).sum().longValue() == sum)
				.headOption();
		
		Array<Long> r2 = r.map(to -> l.subSequence(from, to)).getOrElse(Array.empty());
		return r2;
	}


	void part2() {}

	static String example = """
			35
			20
			15
			25
			47
			40
			62
			55
			65
			95
			102
			117
			150
			182
			127
			219
			299
			277
			309
			576
						""";
}
