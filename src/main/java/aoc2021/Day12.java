package aoc2021;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 12: Passage Pathing ---
// https://adventofcode.com/2021/day/12

class Day12 extends AocPuzzle {

	List<Conn> conn = Util.splitLines(input).map(Conn::parse);

	record Conn(String from, String to) {
		static Conn parse(String s) {
			return new Conn(s.split("-")[0], s.split("-")[1]);
		}

		boolean has(String cave) {
			return cave.equals(from) || cave.equals(to);
		}

		String other(String cave) {
			return cave.equals(from) ? to : from;
		}
	}

	interface PathPredicate extends Function2<String, List<String>, Boolean> {}
	
	boolean allowed1(String cave, List<String> path) {
		return bigCave(cave) || !path.contains(cave);
	}
	
	boolean allowed2(String cave, List<String> path) {
		return !"start".equals(cave) && (bigCave(cave) || !path.contains(cave) || !smallTwice(path));
	}

	boolean smallTwice(List<String> path) {
		var small = path.filter(c -> !bigCave(c));
		return small.distinct().size() != small.size();
	}

	boolean bigCave(String cave) {
		return Character.isUpperCase(cave.charAt(0));
	}

	Set<String> next(String cave) {
		return conn.filter(c -> c.has(cave)).map(c -> c.other(cave)).toSet();
	}
	

	void findPaths(PathPredicate allowed) {
		var work = HashSet.of(List.of("start"));
		Set<List<String>> done = HashSet.empty();

		while (work.nonEmpty()) {
			work = work.flatMap(p -> next(p.last()).filter(n -> allowed.apply(n, p)).map(n -> p.append(n)));
			done = done.addAll(work.filter(p -> p.last().equals("end")));
			work = work.removeAll(done);
		}
		System.out.println(done.size());
	}

	void part1() {
		findPaths(this::allowed1);
	}

	void part2() {
		findPaths(this::allowed2);
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 4773
		new Day12().part1();

		System.out.println("=== part 2"); // 116985
		timed(() -> new Day12().part2());
	}

	static String example1 = """
			start-A
			start-b
			A-c
			A-b
			b-d
			A-end
			b-end
						""";

	static String example2 = """
			dc-end
			HN-start
			start-kj
			dc-start
			dc-HN
			LN-dc
			HN-end
			kj-sa
			kj-HN
			kj-dc
						""";
	
	static String input = """
			ln-nr
			ln-wy
			fl-XI
			qc-start
			qq-wy
			qc-ln
			ZD-nr
			qc-YN
			XI-wy
			ln-qq
			ln-XI
			YN-start
			qq-XI
			nr-XI
			start-qq
			qq-qc
			end-XI
			qq-YN
			ln-YN
			end-wy
			qc-nr
			end-nr
						""";
}
