package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * https://adventofcode.com/2018/day/12
 *
 */
public class Day12 {
	
	static class Rule {
		String left;
		boolean plant;
		
		public Rule(String string, boolean b) {
			left = string;
			plant = b;
		}
		
		@Override
		public String toString() {
			return left + " => " + plant;
		}

		static Rule parse(String s) {
			String[] f = s.split(" => ");
			return new Rule(f[0], "#".equals(f[1]));
		}

		public boolean match(String x, Integer i) {
			return x.substring(i-2).startsWith(left);
		}
	}

	static String initial = "####..##.##..##..#..###..#....#.######..###########.#...#.##..####.###.#.###.###..#.####..#.#..##..#";
	static String initialTest = "#..#.#..##......###...###";
	
	static long start = System.currentTimeMillis();

	static List<Rule> rules;
	static Rule dummy = new Rule("-----", false);
	
	
	public static void main(String[] args) throws Exception {
		rules = seq(Util.lines("aoc2018/day12.txt")).map(Rule::parse).toList();
//		rules = seq(Util.lines("aoc2018/day12-test.txt")).map(Rule::parse).toList();
		
		part1(initial);		
		part2(initial);
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}

	protected static void part1(String input) {
		String x = "...................." + input + "........."; 
		int offs = 20;
		
		System.out.println("=== part 1 ===");
		for (int gen = 0; gen<=20; ++gen) {
			System.out.println("" + gen + ": " + x + " / " + countPlants(x, offs));
			x = nextGen(x) + ".";			
		}
	}
	
	protected static void part2(String input) {
		String x = "...................." + input + "..."; 
		
		int offs = 20;
		long plants = 0;
		
		System.out.println("=== part 2 ===");
		
		List<Integer> delta = new ArrayList<>();
		
		for (int gen = 0; gen<200; ++gen) {			
			Integer c = countPlants(x, offs);
			System.out.println("" + gen + ": " + c + " / " + (c-plants));
			x = nextGen(x) + ".";			
			delta.add((int)(c-plants));
			plants = c;
		}
		
//		// constant increment 88 after gen 124 (11216 plants)
//		long r = (50000000000L - 124L)*88L + 11216L;
//		System.out.println(r);

		Integer increment = delta.get(delta.size()-1);
		delta = seq(delta).reverse().skipWhile(i -> i == increment).toList();
//		System.out.println(seq(delta).sum());
		int lastRealGen = delta.size()-1; // delta contains gen 0
		System.out.println((50000000000L - lastRealGen)*increment + seq(delta).sum().get());
		
	}

	protected static Integer countPlants(String x, int offs) {
		return range(0, x.length())
				.filter(i -> x.charAt(i) == '#')
				.map(i -> i-offs)
				.sum().orElse(0);
	}

	private static String nextGen(String x) {
		return ".." + 
		range(2, x.length())
			.map(i -> seq(rules).findFirst(r -> r.match(x, i)).orElse(dummy))
			.map(r -> r.plant ? "#" : ".")
			.toString();
	}



}
