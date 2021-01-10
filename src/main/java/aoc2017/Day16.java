package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 16: Permutation Promenade ---
// https://adventofcode.com/2017/day/16


@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day16 extends AocPuzzle {

	private static final long BILLION = 1_000_000_000L;
	List<String> moves = List.of(readString("input16.txt").split(","));

	Array<Character> spin(Array<Character> x, int n) {
		return x.rotateRight(n);
	}

	Array<Character> exchange(Array<Character> x, int a, int b) {
		return x.update(a, x.get(b)).update(b, x.get(a));
	}

	Array<Character> partner(Array<Character> x, char a, char b) {
		return x.update(x.indexOf(a), b).update(x.indexOf(b), a);
	}

	void part1() {
		
		System.out.println("#steps=" + moves.length());

		var order = Array.rangeClosed('a', 'p');
		System.out.println(order.mkString());

		order = dance(order);

		System.out.println("=> " + order.mkString());
	}

	Array<Character> dance(Array<Character> order) {
		for (var i : moves) {
			var v = i.substring(1).trim();

			order = switch (i.charAt(0)) {
			case 's' -> spin(order, Integer.valueOf(v));
			case 'x' -> exchange(order, Integer.parseInt(v.split("/")[0]), Integer.parseInt(v.split("/")[1]));
			case 'p' -> partner(order, v.charAt(0), v.charAt(2));
			default -> order;
			};
		}
		return order;
	}

	void part2() {
		var initial = Array.rangeClosed('a', 'p');
		var order = initial;
		System.out.println(order.mkString());

		// when are we back to initial order? 
		int cycle = 0;
		do {
			order = dance(order);
			cycle++;
		} while (! order.equals(initial));
				
		System.out.println("cycle length: " + cycle);
		
		// remaining rounds to 1B?
		var rem = BILLION % cycle;
		System.out.println("dancing " + rem + " dances from " + (BILLION-rem+1) + ".." + BILLION);

		for (int i=0; i<rem; ++i)
			order = dance(order);
		
		System.out.println("=> " + order.mkString());
	}
	
	void test() {
		var x = Array.rangeClosed('a', 'e');
		System.out.println(x);
		System.out.println(x = spin(x, 1));
		System.out.println(x = exchange(x, 3, 4));
		System.out.println(x = partner(x, 'e', 'b'));
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day16().test();

		System.out.println("=== part 1"); // ebjpfdgmihonackl
		timed(() -> new Day16().part1());

		System.out.println("=== part 2"); // abocefghijklmndp
		timed(() -> new Day16().part2());
	}

}
