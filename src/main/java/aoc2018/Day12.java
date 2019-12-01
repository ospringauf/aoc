package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.List;

/**
 * "game of life"-like plant growth
 * https://adventofcode.com/2018/day/12
 *
 */
public class Day12 {
	
	static class Rule {
		String left;
		String right;
		
		public Rule(String l, String r) {
			left = l;
			right = r;
		}
		
		@Override
		public String toString() {
			return left + " => " + right;
		}

		static Rule parse(String s) {
			String[] f = s.split(" => ");
			return new Rule(f[0], f[1]);
		}

		public boolean match(String x, Integer i) {
			return x.substring(i-2).startsWith(left);
		}
	}

	static String initial = "####..##.##..##..#..###..#....#.######..###########.#...#.##..####.###.#.###.###..#.####..#.#..##..#";
	static String initialTest = "#..#.#..##......###...###";
	
	static long start = System.currentTimeMillis();

	static List<Rule> rules;
	static Rule dummy = new Rule("-----", ".");
	
	
	public static void main(String[] args) throws Exception {
		rules = seq(Util.lines("aoc2018/day12.txt")).map(Rule::parse).toList();
//		rules = seq(Util.lines("aoc2018/day12-test.txt")).map(Rule::parse).toList();
		
		System.out.println("=== part 1 ===");
		part1(initial);		
		System.out.println("=== part 2 ===");
		part2(initial);

		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}


	static Integer countPlants(String x, int offs) {
		return range(0, x.length())
				.filter(i -> x.charAt(i) == '#')
				.map(i -> i-offs)
				.sum().orElse(0);
	}

	static String nextGen(String x) {
		return ".." + 
		range(2, x.length())
			.map(i -> seq(rules).findFirst(r -> r.match(x, i)).orElse(dummy))
			.map(r -> r.right)
			.toString();
	}
	
	protected static void part1(String input) {
		String x = "...................." + input + "..."; 
		int offs = 20;
		
		for (int gen = 0; gen<=20; ++gen) {
			System.out.println("" + gen + ": " + x + " / " + countPlants(x, offs));
			x = nextGen(x) + ".";			
		}
	}
	
	protected static void part2(String input) {
		String x = "...................." + input + "..."; 
		
		int offs = 20;
		int plants = 0;
		int gen = 0;
		int lastDelta = 0;
		
		List<Integer> delta = new ArrayList<>();
		int stableGrowth = 0;
		
		// wait till growth is stable for 100 generations
		while (stableGrowth < 100) {
			int c = countPlants(x, offs);
			int newDelta = c - plants;
			stableGrowth = (newDelta == lastDelta) ? stableGrowth+1 : 0;
//			System.out.println("" + gen + ": " + c + " / " + newDelta);
			x = nextGen(x) + ".";			
			delta.add(newDelta);
			plants = c;
			lastDelta = newDelta;
			gen++;
		}
		
//		// constant increment 88 after gen 124 (11216 plants)
//		long r = (50000000000L - 124L)*88L + 11216L;
//		System.out.println(r);
		
		final int increment = lastDelta;
		delta = seq(delta).reverse().skipWhile(i -> i == increment).toList();
		int lastRealGen = delta.size()-1; // delta contains gen 0
		System.out.println((50000000000L - lastRealGen)*lastDelta + seq(delta).sum().get());
		
	}
}
