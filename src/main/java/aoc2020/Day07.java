package aoc2020;

import java.util.function.Function;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;

// https://adventofcode.com/2020/day/x

@SuppressWarnings({ "deprecation", "preview" })
class Day07 extends AocPuzzle {

	public static void main(String[] args) throws Exception {

		System.out.println("=== part 1"); // 192
		new Day07().part1();

		System.out.println("=== part 2"); // 12128
		new Day07().part2();
	}

	record Bags(int amount, String color) {
		static Bags parse(String s) {
			var x = s.split(" ");
			if (x[0].equals("no"))
				return null;
			else
				return new Bags(Integer.parseInt(x[0]), x[1] + " " + x[2]);
		}

		boolean isColor(String c) {
			return c.equals(color);
		}
	}

	static record Rule(String outer, List<Bags> inner) {
		static Rule parse(String s) {
			var a = s.split(" bags contain ");
			var i = a[1].split(", ");

			return new Rule(a[0], List.of(i).map(x -> Bags.parse(x)).filter(b -> b!= null));
		}

		public boolean containsAny(HashSet<String> colors) {
			return inner.exists(i -> colors.contains(i.color));
		}

		public List<Bags> expand(List<Rule> rules, int N) {
			var expanded = inner.map(b -> new Bags(b.amount*N, b.color));
			
			Function<Bags, Rule> findRule = b -> rules.find(r -> r.outer.equals(b.color)).get();
			
			List<Bags> ib = expanded.flatMap(i -> findRule.apply(i).expand(rules,i.amount)); 
			
			expanded = expanded.appendAll(ib);			
			
			return expanded;
		}
	}

	private void part1() throws Exception {
//		var data = List.of(example.split("\\n"));
		var data = lines("input07.txt");
		// System.out.println(lines("input07.txt"));

		var bags = data.map(Rule::parse);

		boolean ok = true;
		var found = HashSet.of("shiny gold");
		while (ok) {
			ok = false;
			var f0 = found;
			var next = bags.filter(r -> r.containsAny(f0)).map(r -> r.outer);
			ok = !found.containsAll(next);
			found = found.addAll(next);
		}
		System.out.println(found.size() - 1);
	}

	private void part2() throws Exception {
//		var data = List.of(example.split("\\n"));
		var data = lines("input07.txt");

		var bags = data.map(Rule::parse);
		Rule shinyGold = bags.find(b -> b.outer.equals("shiny gold")).get();
		
		var expanded = shinyGold.expand(bags,1);
		System.out.println(expanded);
		System.out.println(expanded.map(b -> b.amount).sum());

	}

	static String example = """
			light red bags contain 1 bright white bag, 2 muted yellow bags.
			dark orange bags contain 3 bright white bags, 4 muted yellow bags.
			bright white bags contain 1 shiny gold bag.
			muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
			shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
			dark olive bags contain 3 faded blue bags, 4 dotted black bags.
			vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
			faded blue bags contain no other bags.
			dotted black bags contain no other bags.
						""";
}
