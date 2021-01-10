package aoc2017;

import java.util.Comparator;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;

// --- Day 24: Electromagnetic Moat ---
// https://adventofcode.com/2017/day/24

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day24 extends AocPuzzle {

	record Component(int a, int b) {
		int strength() {
			return a + b;
		}

		boolean connects(int x) {
			return a == x || b == x;
		}

		int otherEnd(int x) {
			return a == x ? b : a;
		}

		static Component parse(String s) {
			var a = s.split("/");
			return new Component(Integer.valueOf(a[0]), Integer.valueOf(a[1]));
		}
	}

	int strength(Seq<Component> bridge) {
		return bridge.map(c -> c.strength()).sum().intValue();
	}

	List<Component> strongest(int start, List<Component> bridge, Set<Component> comps) {
		var next = comps.filter(c -> c.connects(start));
		if (next.isEmpty())
			return bridge;
		var bridges = next.map(c -> strongest(c.otherEnd(start), bridge.prepend(c), comps.remove(c)));
		return bridges.maxBy(b -> strength(b)).get();
	}

	List<Component> longest(int start, List<Component> bridge, Set<Component> comps) {
		var next = comps.filter(c -> c.connects(start));
		if (next.isEmpty())
			return bridge;
		var bridges = next.map(c -> longest(c.otherEnd(start), bridge.prepend(c), comps.remove(c)));
		Comparator<List<Component>> cmp = Comparator.comparing(b -> b.size());
		cmp = cmp.thenComparing(b -> strength(b));
		return bridges.maxBy(cmp).get();
	}

	Set<Component> comps = lines("input24.txt").map(Component::parse).toSet();
//	Set<Component> comps = Util.splitLines(example).map(Component::parse).toSet(); 

	void part1() {
		var bridge = strongest(0, List.empty(), comps);
		System.out.println(bridge);
		System.out.println(strength(bridge));
	}

	void part2() {
		var bridge = longest(0, List.empty(), comps);
		System.out.println(bridge);
		System.out.println(strength(bridge));
	}

	public static void main(String[] args) {

		System.out.println("=== part 1");
		timed(() -> new Day24().part1());

		System.out.println("=== part 2");
		timed(() -> new Day24().part2());
	}

	static String example = """
			0/2
			2/2
			2/3
			3/4
			3/5
			0/1
			10/1
			9/10
						""";

}
