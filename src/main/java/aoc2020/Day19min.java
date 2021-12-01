package aoc2020;

import static common.Util.splitLines;

import java.util.regex.Pattern;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 19: Monster Messages ---
// https://adventofcode.com/2020/day/19

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day19min extends AocPuzzle {

	public static void main(String[] args) {
		new Day19min().solve();
	}
	
	String data = file2string("input19.txt");
//	String data = example;
//	String data = example2;
	static Map<Integer, Rule> rules;
	
	record Rule(Integer number, List<String> elems) {
		
		static Rule parse(String s) {
			var a = s.split(": ");
			return new Rule(Integer.valueOf(a[0]), List.of(a[1].replaceAll("\"", "").split("\\s+")));
		}
		
		String expand() {
			var re = elems.map(x -> x.matches("\\d+") ? rules.getOrElse(Integer.valueOf(x), null).expand() : x).mkString();
			return (elems.contains("|")) ? "("+re+")" : re;
		}
	}

	void solve() {
		var blocks = data.split("\n\n");
		rules = splitLines(blocks[0]).map(Rule::parse).toMap(r -> r.number, r->r);
		var messages = splitLines(blocks[1]);

		System.out.println("=== part 1"); // 142

		String rule0re = rules.getOrElse(0, null).expand();
		System.out.println(messages.count(m -> Pattern.matches(rule0re, m)));

		System.out.println("=== part 2"); // 294

		var re42 = rules.getOrElse(42, null).expand();
		var re31 = rules.getOrElse(31, null).expand();
		
		int result2 = 0;
		int delta = 0;
		int n = 1;
		do {
			rules = rules.put( 8, new Rule(8, List.of(re42 + "+")));				
			String re11 = String.format("%s{%d}%s{%d}", re42, n, re31, n); // try different values of n
			rules = rules.put(11, new Rule(11, List.of(re11))); 
			
			String re0 = rules.getOrElse(0, null).expand();
			delta = messages.count(m -> Pattern.matches(re0, m));
			result2 += delta;
			n++;
		} while (delta > 0);
		
		System.out.println(result2);
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
