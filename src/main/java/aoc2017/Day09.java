package aoc2017;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import common.AocPuzzle;
import io.vavr.collection.List;

// https://adventofcode.com/2017/day/9

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day09 extends AocPuzzle {

	record Group(List<Group> inner, boolean garbage, String raw) {
		int score(int x) {
			return garbage ? 0 : x + inner.map(i -> i.score(x + 1)).sum().intValue();
		}
		
		int garbageChars() {
			return garbage ? raw.length() : inner.map(i -> i.garbageChars()).sum().intValue();
		}
	}
	
	public static void main(String[] args) {

		System.out.println("=== test garbage");
		new Day09().testGarbage();

		System.out.println("=== test groups");
		new Day09().testGroups();

		System.out.println("=== test scores");
		new Day09().testScore();
 
		System.out.println("=== part 1"); // 12897
		new Day09().part1();

		System.out.println("=== part 2"); // 7031
		new Day09().part2();
	}

	Group parseGarbage() {
		var raw = List.empty();
		var c = data.pop();

		boolean masked = false;
		do {
			masked = !masked && c == '!';
			c = data.pop();
			if (!masked && c != '!' && c != '>') 
				raw = raw.append(c);
		} while (c != '>' || masked);

		return new Group(List.empty(), true, raw.mkString());
	}

	Group parseGroup() {
		var c = data.pop();
		assertEquals('{', c);
		List<Group> sub = List.empty();

		do {
			switch (c = data.peek()) {
			case '<' -> { sub = sub.append(parseGarbage()); }
			case '{' -> { sub = sub.append(parseGroup()); }
			default -> { data.pop(); }
			}

		} while (c != '}');

		return new Group(sub, false, "");
	}

	void input(String s) {
		data = new LinkedList<Character>();
		for (char c : s.toCharArray())
			data.add(c);
	}
	
	LinkedList<Character> data;

	void part1() {
		
		input(file2string("input09.txt"));
		System.out.println(parseGroup().score(1));
	}

	void part2() {
		input(file2string("input09.txt"));
		System.out.println(parseGroup().garbageChars());
	}

	

	private void testScore() {
		for (var test : exampleScores.split("\n")) {
			input(test);
			var g = parseGroup();
			System.out.println("\t" + test + " --> " + g.score(1));
		}
	}

	void testGarbage() {
		for (var test : exampleGarbage.split("\n")) {
			input(test);
			var g = parseGarbage();
			System.out.println("\t" + test + " --> " + g);
			assertEquals(0, data.size());
		}
	}

	void testGroups() {
		for (var test : exampleGroups.split("\n")) {
			input(test);
			var g = parseGroup();
			System.out.println("\t" + test + " --> " + g);
			assertEquals(0, data.size());
		}
	}
	
	static String exampleGarbage = """
			<>
			<random characters>
			<<<<>
			<{!>}>
			<!!>
			<!!!>>
			<{o"i!a,<{i<a>
						""";

	static String exampleGroups = """
			{}
			{{{}}}
			{{},{}}
			{{{},{},{{}}}}
			{<{},{},{{}}>}
			{<a>,<a>,<a>,<a>}
			{{<a>},{<a>},{<a>},{<a>}}
			{{<!>},{<!>},{<!>},{<a>}}
						""";

	static String exampleScores = """
			{}
			{{{}}}
			{{},{}}
			{{{},{},{{}}}}
			{<a>,<a>,<a>,<a>}
			{{<ab>},{<ab>},{<ab>},{<ab>}}
			{{<!!>},{<!!>},{<!!>},{<!!>}}
			{{<a!>},{<a!>},{<a!>},{<ab>}}
						""";

}
