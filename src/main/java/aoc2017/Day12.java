package aoc2017;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

// https://adventofcode.com/2017/day/12

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day12 extends AocPuzzle {

	record Pipe(Integer src, Set<Integer> tgts) {

		static Pipe parse(String s) {
			var a = s.split(" <-> ");
			return new Pipe(Integer.valueOf(a[0]), Util.strings2ints(a[1].split(", ")).toSet());
		}
	}

	List<String> data = file2lines("input12.txt");
//	data = List.of(example.split("\n"));

	void part1() {
		var pipes = data.map(Pipe::parse).toMap(p -> p.src, p -> p);
		System.out.println(pipes);

		Set<Pipe> group0 = findGroup(pipes, 0);
		System.out.println("=> " + group0.size());
	}

	Set<Pipe> findGroup(Map<Integer, Pipe> pipes, Integer start) {
		Set<Pipe> group = HashSet.of(pipes.getOrElse(start, null));
		
		while (true) {
			var delta = group.flatMap(p -> p.tgts.map(x -> pipes.get(x).get())).toSet();
			delta = delta.removeAll(group);
			group = group.addAll(delta);
			if (delta.isEmpty())
				break;
		}
		return group;
	}

	void part2() {
		var pipes = data.map(Pipe::parse).toMap(p -> p.src, p -> p);
		var remaining = pipes.values().toList(); 

		int groups = 0;
		while (! remaining.isEmpty()) {
			var gid = remaining.head().src;
			var grp = findGroup(pipes, gid);
			groups++;
			remaining = remaining.removeAll(grp);
		}
		System.out.println("=> " + groups);
		
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 152
		new Day12().part1();

		System.out.println("=== part 2"); // 186
		new Day12().part2();
	}

	static String example = """
			0 <-> 2
			1 <-> 1
			2 <-> 0, 3, 4
			3 <-> 2, 4
			4 <-> 2, 3, 6
			5 <-> 6
			6 <-> 4, 5
						""";

}
