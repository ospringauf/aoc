package aoc2020;

import static common.Util.splitLines;
import static common.Util.string2ints;

import java.util.Map;
import java.util.regex.Pattern;

import common.AocPuzzle;
import io.vavr.Tuple2;
import io.vavr.collection.List;

// --- Day 19: Monster Messages ---
// https://adventofcode.com/2020/day/19

// lessons learned:
// - regex!
// - 42+31+ is not the same as 41{n}31{n} 

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day19 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 142
		new Day19().part1();

		System.out.println("=== part 2"); // 294
		new Day19().part2();
	}
	
	String data = readString("input19.txt"); 
//	String data = example2;
	static Map<Integer, Rule> RULES;
	
	

	interface Rule {
		// expand this rule into a regular expression
		String expand();
	}

	record LiteralRule(String c) implements Rule {
		public String expand() {
			return c;
		}
	}

	record ConcatRule(List<Integer> parts) implements Rule {
		public String expand() {
			return parts.map(n -> RULES.get(n).expand()).mkString();
		}
	}

	record OrRule(Rule r1, Rule r2) implements Rule {
		public String expand() {
			return String.format("(%s|%s)", r1.expand(), r2.expand());
		}
	}

	// 8: 42 | 42 8
	record Rule8() implements Rule {
		public String expand() {
			var r42 = RULES.get(42).expand();
			return String.format("%s+", r42);
		}
	}

	// 11: 42 31 | 42 11 31
	record Rule11(int n) implements Rule {
		public String expand() {
			var r42 = RULES.get(42).expand();
			var r31 = RULES.get(31).expand();
			// both must appear exactly the same number of times
			return String.format("%s{%d}%s{%d}", r42, n, r31, n);
		}
	}


	static Tuple2<Integer, Rule> parseRule(String s) {
		var a = s.split(": ");
		var right = a[1];
		var number = Integer.valueOf(a[0]);

		Rule r;

		if (right.contains("\"")) {
			final String c = a[1].substring(1, 2);
			r = new LiteralRule(c);
		} else if (right.contains("|")) {
			var b = right.split(" \\| ");
			r = new OrRule(new ConcatRule(string2ints(b[0])), new ConcatRule(string2ints(b[1])));
		} else {
			r = new ConcatRule(string2ints(right));
		}

		return new Tuple2(number, r);
	}

	void part1() {
		var blocks = data.split("\n\n");
		RULES = splitLines(blocks[0]).toJavaMap(s -> parseRule(s));
		var messages = splitLines(blocks[1]);

		String regex0 = RULES.get(0).expand();
		System.out.println(regex0);

		var result = messages.count(m -> Pattern.matches(regex0, m));
		System.out.println(result);

	}

	void part2() {
		var blocks = data.split("\n\n");
		RULES = splitLines(blocks[0]).toJavaMap(s -> parseRule(s));
		var messages = splitLines(blocks[1]);

		int result = 0;
		int delta = 0;
		int n = 1;
		do {
			RULES.put(8, new Rule8());
			RULES.put(11, new Rule11(n)); // try different n values
			
			String regex0 = RULES.get(0).expand();
//			System.err.println(regex0);
			
			delta = messages.count(m -> Pattern.matches(regex0, m));
			
			result += delta;
			n++;
		} while (delta > 0);
		
		System.out.println(result);
	}

	static String example = """
			0: 4 1 5
			1: 2 3 | 3 2
			2: 4 4 | 5 5
			3: 4 5 | 5 4
			4: "a"
			5: "b"

			ababbb
			bababa
			abbbab
			aaabbb
			aaaabbb
						""";

	static String example2 = """
			42: 9 14 | 10 1
			9: 14 27 | 1 26
			10: 23 14 | 28 1
			1: "a"
			11: 42 31
			5: 1 14 | 15 1
			19: 14 1 | 14 14
			12: 24 14 | 19 1
			16: 15 1 | 14 14
			31: 14 17 | 1 13
			6: 14 14 | 1 14
			2: 1 24 | 14 4
			0: 8 11
			13: 14 3 | 1 12
			15: 1 | 14
			17: 14 2 | 1 7
			23: 25 1 | 22 14
			28: 16 1
			4: 1 1
			20: 14 14 | 1 15
			3: 5 14 | 16 1
			27: 1 6 | 14 18
			14: "b"
			21: 14 1 | 1 14
			25: 1 1 | 1 14
			22: 14 14
			8: 42
			26: 14 22 | 1 20
			18: 15 15
			7: 14 5 | 1 21
			24: 14 1

			abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
			bbabbbbaabaabba
			babbbbaabbbbbabbbbbbaabaaabaaa
			aaabbbbbbaaaabaababaabababbabaaabbababababaaa
			bbbbbbbaaaabbbbaaabbabaaa
			bbbababbbbaaaaaaaabbababaaababaabab
			ababaaaaaabaaab
			ababaaaaabbbaba
			baabbaaaabbaaaababbaababb
			abbbbabbbbaaaababbbbbbaaaababb
			aaaaabbaabaaaaababaa
			aaaabbaaaabbaaa
			aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
			babaaabbbaaabaababbaabababaaab
			aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
						""";

}
