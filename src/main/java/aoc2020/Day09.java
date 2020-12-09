package aoc2020;

import io.vavr.collection.List;

// https://adventofcode.com/2020/day/9

@SuppressWarnings({ "deprecation", "preview" })
class Day09 extends AocPuzzle {

	public static void main(String[] args) {
		new Day09().solve();
	}
	
//		int preamble = 5;
//		List<Long> data = List.of(example.split("\n")).map(Long::valueOf);
	
	int preamble = 25;
	List<Long> data = lines("input09.txt").map(Long::valueOf);


	void solve() {
		
		System.out.println("=== part 1");

		var r = data
				.sliding(preamble+1)
				.map(w -> w.reverse())
				.filterNot(w -> check1(w.head(), w.tail()))
				.map(w -> w.head());
		
		long part1 = r.get();
		System.out.println(part1);
		
		System.out.println("=== part 2");
		
		var part2 = List.range(0, data.size()).map(from -> check2(part1, from, data)).filter(l -> ! l.isEmpty());
		System.out.println(part2);
		var r2 = part2.get();
		System.out.println(r2.min().get() + r2.max().get());
	}
	
	List<Long> check2(long n, int from, List<Long> l) {
		List<List<Long>> r = List.range(0, l.length()-from).map(x -> l.subSequence(from, from+x)).filter(s -> s.sum().longValue()==n);
		return r.getOrElse(List.empty());
	}
	

	private boolean check1(Long sum, List<Long> numbers) {
		return numbers.exists(x -> numbers.contains(sum-x));
	}


	void part2() {	
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
