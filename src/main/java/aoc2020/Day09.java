package aoc2020;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 9: Encoding Error ---
// https://adventofcode.com/2020/day/9

@SuppressWarnings({ "deprecation", "preview" })
class Day09 extends AocPuzzle {
    
    record Range(int from, int to) {};

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
	Array<Long> data = file2longs("input09.txt").toArray();

	long part1() {
		var r = data.sliding(preamble + 1)
				.filter(w -> ! lastIsSumOfTwo(w))
				.map(w -> w.last())
				.single();

		return r;
	}
	
	boolean lastIsSumOfTwo(Array<Long> window) {
	    Long sum = window.last();
	    return window.exists(x -> window.contains(sum - x));
	}

	long part2(long sum) {
	    var w = ranges(data.size())
        	    .filter(r -> windowSum(r.from, r.to) == sum)
        	    .map(r -> data.subSequence(r.from, r.to))
        	    .single();
	    
        System.out.println(w);
        return w.min().get() + w.max().get();
	}
	
	// all ranges (from,to) having at least 2 elements
	List<Range> ranges(int m) {
	    return List.rangeClosed(0, m)
	            .flatMap(a -> List.rangeClosed(a + 2, m).map(b -> new Range(a,b)));
	}
	
	long windowSum(int from, int to) {
	    long s = 0;
	    for (int i = from; i < to; ++i) {
	        s += data.get(i);
	    }
	    return s;
	    
//	    return data.subSequence(from, to).sum().longValue();
	}

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
