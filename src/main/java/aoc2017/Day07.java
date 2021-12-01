package aoc2017;

import java.util.HashMap;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// https://adventofcode.com/2017/day/7

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day07 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // hmvwl
		new Day07().part1();
		
		System.out.println("=== part 2"); // 1853
		new Day07().part2();
	}

	record Prog(String name, int weight, List<String> upper) {
		
		static Prog parse(String s) {
			var lr = s.split(" -> ");
			var a1 = lr[0].split("[\\(\\) ]+");
			List<String> sub = (lr.length>1) ? List.of(lr[1].split(", ")) : List.empty();
			return new Prog(a1[0], Integer.valueOf(a1[1]), sub);
		}		
	}

//	final List<Prog> data = List.of(example.split("\n")).map(Prog::parse);
	final List<Prog> data = file2lines("input07.txt").map(Prog::parse);
	final Map<String, Prog> progs = data.toMap(x -> x.name, x->x);
	
	void part1() {
		var r = progs.keySet().filter(n -> ! data.exists(p -> p.upper.contains(n)));
		System.out.println(r);
	}

	
	void part2() {	
		// calc all summed weights
		var todo = progs.keySet().toJavaSet();
		var weight = new HashMap<String, Integer>();
		
		while (! todo.isEmpty()) {
			var ready = data.filter(p -> p.upper.forAll(weight::containsKey));
			for (Prog p : ready) {
				var w = p.upper.map(u -> weight.get(u)).sum().intValue() + p.weight;
				weight.put(p.name, w);
				todo.remove(p.name);
			}
		}
		
		// find unbalanced discs
		for (Prog p : data) {
			var unbalanced = p.upper.map(u -> weight.get(u)).distinct().size() > 1;
			if (unbalanced) {
				System.out.println("unbalanced: " + p.name);
				for (String u : p.upper)
					System.out.format("\t%s(%d) %d\n", u, progs.get(u).get().weight, weight.get(u));
			}
		}
		
		System.out.println("TODO: find the lowest unbalanced program and calc its adjusted weight");
	}
	
	static String example = """
pbga (66)
xhth (57)
ebii (61)
havc (66)
ktlj (57)
fwft (72) -> ktlj, cntj, xhth
qoyq (66)
padx (45) -> pbga, havc, qoyq
tknk (41) -> ugml, padx, fwft
jptl (61)
ugml (68) -> gyxo, ebii, jptl
gyxo (61)
cntj (57)			
			""";

}
