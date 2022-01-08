package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 7: Internet Protocol Version 7 ---
// https://adventofcode.com/2016/day/7

class Day07 extends AocPuzzle {

	boolean abba(String s) {
		return List.ofAll(s.toCharArray()).sliding(4).exists(l -> l.eq(l.reverse()) && l.distinct().size() == 2);
	}

	boolean tls(String s) {
		var f = List.of(s.split("[\\[\\]]"));
		var supernet = f.grouped(2).map(x -> x.get(0));
		var hypernet = f.tail().grouped(2).map(x -> x.get(0));

		return supernet.exists(x -> abba(x)) && !hypernet.exists(x -> abba(x));
	}

	List<String> aba(String s) {
		return List.ofAll(s.toCharArray()).sliding(3).filter(l -> l.eq(l.reverse()) && l.distinct().size() == 2)
				.map(x -> x.mkString()).toList();
	}

	String bab(String aba) {
		return aba.substring(1, 2) + aba.substring(0, 1) + aba.substring(1, 2);
	}

	boolean ssl(String s) {
		var f = List.of(s.split("[\\[\\]]"));
		var supernet = f.grouped(2).map(x -> x.get(0));
		var hypernet = f.tail().grouped(2).map(x -> x.get(0));

		var aba = supernet.flatMap(x -> aba(x)).toSet();
		var bab = hypernet.flatMap(x -> aba(x)).toSet();
		aba = aba.map(x -> bab(x));
		return aba.nonEmpty() && bab.nonEmpty() && bab.intersect(aba).nonEmpty();
	}

	void part1() {
		// var l = Util.splitLines(example);
		// l.forEach(s -> System.out.println(s + " -> " + check1(s)));

		var l = file2lines("day07.txt");
		System.out.println(l.count(s -> tls(s)));
	}

	void part2() {
//		var l = Util.splitLines(example2);
//		l.forEach(s -> System.out.println(s + " -> " + ssl(s)));

		var l = file2lines("day07.txt");
		System.out.println(l.count(s -> ssl(s)));
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day07().part1());

		System.out.println("=== part 2");
		timed(() -> new Day07().part2());
	}

	static String example1 = """
			abba[mnop]qrst
			abcd[bddb]xyyx
			aaaa[qwer]tyui
			ioxxoj[asdfgh]zxcvbn
						""";

	static String example2 = """
			aba[bab]xyz
			xyx[xyx]xyx
			aaa[kek]eke
			zazbz[bzb]cdb
						""";
}
