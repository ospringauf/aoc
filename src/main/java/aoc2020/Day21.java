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

// - determine allergen/ingredient relation from fuzzy input
// - based on set theory and expression logic
// - choosíng good variable names helps to understand own code :-) 


@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day21 extends AocPuzzle {

	public static void main(String[] args) {
		new Day21().solve();
	}

	record Food(Set<String> ingredients, Set<String> allergens) {
		static Food parse(String s) {
			var a = s.split(" \\(contains ");
			var ing = a[0].split("\\s+");
			var alg = a[1].replaceAll("\\)", "").split(", ");
			return new Food(HashSet.of(ing), HashSet.of(alg));
		}
	}

	List<Food> foods = file2lines("input21.txt").map(Food::parse);
//	List<Food> foods = Util.splitLines(example).map(Food::parse);

	
	void solve() {
		var ingredients = foods.flatMap(x -> x.ingredients).toSet();
		var allergens = foods.flatMap(x -> x.allergens).toSet();

		System.out.println("ingredients: " + ingredients.mkString(", "));
		System.out.println("allergens: " + allergens.mkString(", "));

		System.out.println("=== part 1"); // 2262

		// a->i: allergen -> ingredient candidates
		Map<String, Set<String>> a2i = new HashMap<>();
		for (var a : allergens) {
			Seq<Set<String>> containedIn = foods.filter(f -> f.allergens.contains(a)).map(f -> f.ingredients);
			a2i.put(a, intersection(containedIn));
		}

		Set<String> knownAllergens;
		Set<String> dangerousIngredients;

		// narrow the a->i relation until we know the single dangerous ingredient for each allergen
		do {
			knownAllergens = allergens.filter(a -> a2i.get(a).size() == 1);
			dangerousIngredients = knownAllergens.flatMap(a2i::get);

			Set<String> unknownAllergens = allergens.removeAll(knownAllergens);

			for (var a : unknownAllergens) {
				a2i.put(a, a2i.get(a).removeAll(dangerousIngredients));
			}
			System.out.println("  known: " + knownAllergens);
		} while (!knownAllergens.containsAll(allergens));

		System.out.println("A->I: " + a2i);

		var harmlessIngredients = ingredients.removeAll(dangerousIngredients).toList();
		System.out.println("no allergens in: " + harmlessIngredients);

		var sum = harmlessIngredients.map(i -> foods.count(f -> f.ingredients.contains(i))).sum();
		System.out.println("=> result: " + sum);

		System.out.println("=== part 2"); // cxsvdm,glf,rsbxb,xbnmzr,txdmlzd,vlblq,mtnh,mptbpz

		// now that a->i is bijective, invert the relation
		var i2a = allergens.toMap(a -> a2i.get(a).single(), a -> a);
		System.out.println("I->A: " + i2a);

		Comparator<String> byAllergen = Comparator.comparing(f -> i2a.get(f).get());
		var canonicalDangerousIngredList = List.ofAll(dangerousIngredients).sorted(byAllergen).mkString(",");
		System.out.println("=> result: " + canonicalDangerousIngredList);
	}

	Set<String> intersection(Seq<Set<String>> sets) {
		return sets.fold(sets.head(), (a, b) -> a.intersect(b));
	}

	static String example = """
			mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
			trh fvjkl sbzzf mxmxvkd (contains dairy)
			sqjhc fvjkl (contains soy)
			sqjhc mxmxvkd sbzzf (contains fish)
						""";

}
