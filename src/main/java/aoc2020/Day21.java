package aoc2020;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;

// --- Day 21: Allergen Assessment ---
// https://adventofcode.com/2020/day/21

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day21 extends AocPuzzle {

	public static void main(String[] args) {

		new Day21().solve();
	}

	record Ingredients(Set<String> foods, Set<String> allergens) {

		static Ingredients parse(String s) {
			var a = s.split(" \\(contains ");
			var f = a[0].split("\\s+");
			var r = a[1].replaceAll("\\)", "").split(", ");
			return new Ingredients(HashSet.of(f), HashSet.of(r));
		}
	}

	void solve() {
		List<Ingredients> data = lines("input21.txt").map(Ingredients::parse);
//		List<R> data = List.of(example.split("\n")).map(R::parse);

		var allF = data.flatMap(x -> x.foods).toSet();
		var allA = data.flatMap(x -> x.allergens).toSet();

		System.out.println("foods: " + allF.mkString(", "));
		System.out.println("allergens: " + allA.mkString(", "));

		System.out.println("=== part 1"); // 2262
		
		Map<String, Set<String>> a2f = new HashMap<>();
		for (var a : allA) {
			var fs = intersect(data.filter(d -> d.allergens.contains(a)).map(d -> d.foods));
			a2f.put(a, fs);
		}

		boolean repeat = true;
		while (repeat) {
			repeat = false;
			var knownA = allA.filter(a -> a2f.getOrDefault(a, HashSet.empty()).size() == 1);
			var knownAF = knownA.flatMap(f -> a2f.get(f));
			for (var a : allA) {
				var f = a2f.getOrDefault(a, HashSet.empty());
				if (f.size() > 1) {
					repeat = true;
					a2f.put(a, f.removeAll(knownAF));
				}
			}
		}
		System.out.println(a2f);

		var knownA = allA.filter(a -> a2f.getOrDefault(a, HashSet.empty()).size() == 1);
		var knownAF = knownA.flatMap(f -> a2f.get(f));
		var noAF = allF.removeAll(knownAF).toList();

		System.out.println("no allerg: " + noAF);
		var sum = noAF.map(x -> data.count(d -> d.foods.contains(x))).sum();
		System.out.println("part 1 result: " + sum);

		System.out.println("=== part 2"); // cxsvdm,glf,rsbxb,xbnmzr,txdmlzd,vlblq,mtnh,mptbpz
		Map<String, String> f2a = new HashMap<>();
		for (var e : a2f.entrySet()) {
			if (e.getValue().size() == 1)
				f2a.put(e.getValue().single(), e.getKey());
		}
		System.out.println(f2a);

		var r2 = List.ofAll(knownAF).sorted(Comparator.comparing(f -> f2a.get(f))).mkString(",");
		System.out.println("part 2 result: " + r2);

	}

	private Set<String> intersect(Seq<Set<String>> a) {
		return a.fold(a.get(0), (x, y) -> x.intersect(y));
	}

	static String example = """
			mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
			trh fvjkl sbzzf mxmxvkd (contains dairy)
			sqjhc fvjkl (contains soy)
			sqjhc mxmxvkd sbzzf (contains fish)
						""";

}
