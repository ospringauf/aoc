package aoc2020;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 16: Ticket Translation ---
// https://adventofcode.com/2020/day/16

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day16 extends AocPuzzle {

	public static void main(String[] args) {
		new Day16().solve();
	}

	static List<Rule> rules;

	record Range(int a, int b) {
		public boolean contains(int val) {
			return a <= val && val <= b;
		}

		public static Range parse(String s) {
			var a = s.split("-");
			return new Range(Integer.valueOf(a[0]), Integer.valueOf(a[1]));
		}
	}

	record Rule(String name, Range r1, Range r2) {
		boolean matches(int val) {
			return r1.contains(val) || r2.contains(val);
		}

		static Rule parse(String s) {
			var a = s.split(": ");
			var r = a[1].split(" or ");
			return new Rule(a[0], Range.parse(r[0]), Range.parse(r[1]));
		}
	}

	record Ticket(List<Integer> fields) {
		static Ticket parse(String s) {
			return new Ticket(List.of(s.split(",")).map(Integer::parseInt));
		}

		List<Integer> invalFields() {
			return fields.filter(f -> rules.forAll(r -> !r.matches(f))); // TODO forNone??
		}
	}

	void solve() {
//		var blocks = example.split("\n\n");
		var blocks = readString("input16.txt").split("\n\n");

		rules = List.of(blocks[0].split("\n")).map(Rule::parse);
		System.out.println("rules: " + rules);

		var myticket = Ticket.parse(blocks[1].split("\n")[1]);
		System.out.println("my ticket: " + myticket);

		var others = List.of(blocks[2].split("\n")).drop(1).map(Ticket::parse);
		System.out.println("other tickets: " + others);

		System.out.println("=== part 1"); // 29759
		var scanErrRate = others.flatMap(t -> t.invalFields()).sum();
		System.out.println(scanErrRate);

		System.out.println("=== part 2"); // 1307550234719
		var validTickets = others.filter(t -> t.invalFields().isEmpty());
//		System.out.println(validTickets);

		var N = rules.size();
		
		Map<Rule, Integer> ruleField = new HashMap<>();
		while (ruleField.size() < N) {
			for (int i = 0; i < N; ++i) {
				var fi = i; // TODO
				var possRules = rules
						.filter(r -> validTickets.forAll(t -> r.matches(t.fields.get(fi))))
						.removeAll(ruleField.keySet());
//				System.out.println(i + " --> " + possRules);
				
				// unambiguous?
				if (possRules.size() == 1) 
					ruleField.put(possRules.single(), i);
			}
		}
		System.out.println(ruleField);

		var departureFields = rules
				.filter(r -> r.name.startsWith("departure"))
				.map(r -> myticket.fields.get(ruleField.get(r)))
				;
		System.out.println(departureFields.product());

	}

	static String example = """
			class: 1-3 or 5-7
			row: 6-11 or 33-44
			seat: 13-40 or 45-50

			your ticket:
			7,1,14

			nearby tickets:
			7,3,47
			40,4,50
			55,2,20
			38,6,12
						""";

}
