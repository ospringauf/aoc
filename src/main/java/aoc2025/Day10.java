package aoc2025;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.List;

//--- Day 10: Factory  ---
// https://adventofcode.com/2025/day/10

class Day10 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day10().part1()); // 385
		System.out.println("=== part 2");
		timed(() -> new Day10().part2()); // 16757
	}

	List<String> data = file2lines("input10.txt");
//	List<String> data = Util.splitLines(example);

	record Machine(List<Integer> lights, Array<Boolean> lightsOn, Array<Array<Integer>> buttons, Array<Integer> joltage,
			List<List<Integer>> powerSet) {

		// [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
		static Machine parse(String s) {
			var s1 = s.substring(1, s.indexOf(']'));
			List<Integer> lights = List.empty();
			Array<Boolean> lightsOn = Array.fill(s1.length(), Boolean.FALSE);
			for (int i = 0; i < s1.length(); ++i)
				if (s1.charAt(i) == '#') {
					lights = lights.append(i);
					lightsOn = lightsOn.update(i, Boolean.TRUE);
				}

			var s2 = s.substring(s.indexOf(']') + 2, s.indexOf('{') - 1);
			Array<Array<Integer>> buttons = Array.empty();
			s2 = s2.replace("(", "").replace(")", "");
			for (var b : List.of(s2.split(" "))) {
				var bi = Util.string2ints(b.replace(',', ' ')).toArray();
				buttons = buttons.append(bi);
			}

			var s3 = s.substring(s.indexOf('{') + 1);
			var sj = s3.replace("}", "").replace(',', ' ');
			var joltage = Util.string2ints(sj).toArray();

			List<Integer> elements = List.range(0, buttons.size());
			List<Integer> s0 = List.empty();
			var powerSet = elements.foldLeft(List.of(s0),
					(acc, e) -> acc.appendAll(acc.map(subset -> subset.append(e))));

			return new Machine(lights, lightsOn, buttons, joltage, powerSet);
		}

		List<Integer> lights(List<Integer> but) {
			List<Integer> l = List.empty();
			for (var b : but) {
				for (var bl : buttons.get(b)) {
					if (l.contains(bl))
						l = l.remove(bl);
					else
						l = l.append(bl);
				}
			}
			return l;
		}

		Array<Boolean> pushLightButtons(List<Integer> p) {
			var a = Array.fill(lightsOn.size(), Boolean.FALSE);
			for (var b : p) {
				var bl = buttons.get(b);
				for (var l : bl) {
					a = a.update(l, !a.get(l));
				}
			}
			return a;
		}

		Array<Integer> pushJoltageButtons(List<Integer> p) {
			var a = Array.fill(lightsOn.size(), 0);
			for (var b : p) {
				var bl = buttons.get(b);
				for (var l : bl) {
					a = a.update(l, a.get(l) + 1);
				}
			}
			return a;
		}

		int solve1() {
			var onSet = lightsOn;
//			System.out.println("\tsolve1: " + onSet);

			var ps1 = findButtons(onSet);
//			System.out.println("\tmatch: " + ps1);
			var min = ps1.map(x -> x.length()).min().get();
			var best = ps1.filter(x -> x.length() == min).head();
//			System.out.println("\tbest: " + best);
			return best.size();
		}

		List<List<Integer>> findButtons(Array<Boolean> onSet) {
			return powerSet.filter(s -> pushLightButtons(s).eq(onSet));
		}

		int solve2(Array<Integer> j) {
//			System.out.println("solve2: " + j);

			if (j.exists(x -> x < 0))
				return 1000;

			if (j.sum().intValue() == 0)
				return 0;

			var even = j.forAll(x -> x % 2 == 0);
			if (even)
				return 2 * solve2(j.map(x -> x / 2));

			var odds = j.map(x -> x % 2 == 1);

			var ra = findButtons(odds);
			if (ra.isEmpty())
				return 1000;
//			var min = ra.map(x -> x.length()).min().get();
//			ra = ra.filter(x -> x.length()==min);
			return ra.map(r -> r.length() + solve2(minus(j, pushJoltageButtons(r)))).min().get();
		}

		Array<Integer> minus(Array<Integer> a, Array<Integer> b) {
			var r = a;
			for (int i = 0; i < a.size(); ++i)
				r = r.update(i, r.get(i) - b.get(i));
			return r;
		}

	}

	void part1() {
		var machines = data.map(Machine::parse);
		var r = machines.map(m -> m.solve1()).sum();
		System.err.println(r);
	}

	void part2() {
		var machines = data.map(Machine::parse);
		var r = 0;
//		machines = List.of(machines.get(0));
		for (var m : machines) {
			var rm = m.solve2(m.joltage);
			r += rm;
			System.out.println(rm + " <== " + m);
		}

		System.err.println(r);
	}

	static String example = """
			[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
			[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
			[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""";
}
