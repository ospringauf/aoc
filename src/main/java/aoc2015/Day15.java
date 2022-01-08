package aoc2015;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 15: Science for Hungry People ---
// https://adventofcode.com/2015/day/15

class Day15 extends AocPuzzle {

	record Ingredient(String name, Map<String, Integer> properties) {

		static Ingredient parse(String s) {
			var s0 = s.split(": ");
			var l = split(s0[1], ", ").map(x -> split(x, " ").to(r -> Tuple.of(r.s(0), r.i(1))));

			return new Ingredient(s0[0], l.toMap(t -> t._1, t -> t._2));
		}

		int score(int amount, String prop) {
			return amount * properties.getOrElse(prop, 0);
		}

	}

	List<String> cookieProperties = List.of("capacity", "durability", "texture", "flavor");

	void solve(boolean withCalories) {
		var ing = Util.splitLines(myInput).map(Ingredient::parse);

		long bestScore = 0;
		var amounts0 = List.rangeClosed(0, 100).crossProduct(3);
		for (var a0 : amounts0) {
			var last = 100 - a0.sum().intValue();
			if (last < 0)
				continue;

			var a = a0.append(last);
			if (withCalories) {
				var calories = ing.zip(a).map(t -> t._1.score(t._2, "calories")).sum().intValue();
				if (calories != 500)
					continue;
			}

			var score = cookieProperties.map(p -> ing.zip(a).map(t -> t._1.score(t._2, p)).sum().intValue())
					.map(s -> (s < 0) ? 0 : s).product().longValue();

			if (score > bestScore) {
				bestScore = score;
			}
		}
		System.out.println(bestScore);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		new Day15().solve(false);

		System.out.println("=== part 2");
		new Day15().solve(true);
	}

	static String example = """
			Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
			Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
									""";

	static String myInput = """
			Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2
			Sprinkles: capacity -3, durability 3, flavor 0, texture 0, calories 9
			Candy: capacity -1, durability 0, flavor 4, texture 0, calories 1
			Chocolate: capacity 0, durability 0, flavor -2, texture 2, calories 8
									""";
}
