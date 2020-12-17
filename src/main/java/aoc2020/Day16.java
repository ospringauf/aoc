package aoc2020;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.IndexedSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

// --- Day 16: Ticket Translation ---
// https://adventofcode.com/2020/day/16

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day16 extends AocPuzzle {

	public static void main(String[] args) {
		new Day16().solve();
	}

	
	record Range(int a, int b) {
	    
	    static Range parse(String s) {
	        var a = s.split("-");
	        return new Range(Integer.valueOf(a[0]), Integer.valueOf(a[1]));
	    }

	    boolean contains(int val) {
			return a <= val && val <= b;
		}
	}

	
	record Rule(String name, Range r1, Range r2) {
	    
	    static Rule parse(String s) {
	    	// seat: 13-40 or 45-50
	        var a = s.split(": ");
	        var r = a[1].split(" or ");
	        return new Rule(a[0], Range.parse(r[0]), Range.parse(r[1]));
	    }
	    
		boolean matches(int val) {
			return r1.contains(val) || r2.contains(val);
		}
		
		boolean matchesAll(Seq<Integer> values) {
		    return values.forAll(this::matches);
		}
	}
	

	record Ticket(IndexedSeq<Integer> fields) {
	    
		static Ticket parse(String s) {
			return new Ticket(Array.of(s.split(",")).map(Integer::valueOf));
		}

		Seq<Integer> invalidFields(Seq<Rule> rules) {
			return fields.filterNot(val -> rules.exists(r -> r.matches(val)));
		}
		
		int field(int idx) {
		    return fields.get(idx);
		}
	}

	void solve() {
//		var blocks = example.split("\n\n");
		var blocks = readString("input16.txt").split("\n\n");

		var rules = List.of(blocks[0].split("\n")).map(Rule::parse);
		System.out.println("rules: " + rules);

		var myticket = Ticket.parse(blocks[1].split("\n")[1]);
		System.out.println("my ticket: " + myticket);

		var nearby = List.of(blocks[2].split("\n")).drop(1).map(Ticket::parse);
		System.out.println("nearby tickets: " + nearby);

		
		System.out.println("=== part 1"); // 29759
		var scanErrorRate = nearby.flatMap(t -> t.invalidFields(rules)).sum();
		System.out.println(scanErrorRate);

		System.out.println("=== part 2"); // 1307550234719
		var validTickets = nearby.filter(t -> t.invalidFields(rules).isEmpty());
		
		var N = rules.size();
		
		// find rule -> field# mapping, repeat until unambiguous
		Map<Rule, Integer> ruleIndex = new HashMap<>();
				
		while (ruleIndex.size() < N) {
		    
			for (final int idx : List.range(0, N)) {
				var values = validTickets.map(t -> t.field(idx));
				var candidates = rules
					.filter(r -> r.matchesAll(values))
					.removeAll(ruleIndex.keySet());
				
//				System.out.println(i + " --> " + candidates);

				// did we find a single rule that matches all values?
				if (candidates.size() == 1) 
					ruleIndex.put(candidates.single(), idx);
			}
		}

		// print rule-field assignments
//		rule2Field.entrySet().forEach(e -> System.out.println(e.getValue() + " -> " + e.getKey()));

		var departureFields = rules
				.filter(r -> r.name.startsWith("departure"))
				.map(r -> ruleIndex.get(r))
				.map(idx -> myticket.field(idx));
		
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
