package aoc2020;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// --- Day 23: Crab Cups ---
// https://adventofcode.com/2020/day/23

// lessons learned
// - cyclic linked list - again, after 2017.10 (knot hash) and 2018.9 (marble mania)

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day23 extends AocPuzzle {

	static class Cup {

		final long label;
		Cup next;

		Cup(long label) {
			this.label = label;
		}

//		Cup find(long label) {
//			var p = this;
//			while (p.label != label)
//				p = p.next;
//			return p;
//		}

		List<Long> collectLabels(int n) {
			return (n == 0) ? List.empty() : List.of(label).appendAll(next.collectLabels(n - 1));
		}

		static Cup buildCyclicList(List<Long> labels) {
			// generate cups
			var cups = labels.map(Cup::new);
			var first = cups.head();
			// link each cup to next cup
			Stream
				.iterate(cups, c -> c.tail())
				.takeUntil(List::isEmpty)
				.forEach(c -> c.head().next = c.tail().headOption().getOrElse(first));
			return first;
		}
	}
	
	
	/*
	 * returns the cup labeled "1"
	 */
	Cup playCrabCups(List<Long> labels, int rounds, boolean verbose) {
		var min = labels.min().get();
		var max = labels.max().get();

		Cup start = Cup.buildCyclicList(labels);

		// fast cup lookup via label->cup map
		var cupMap = Stream.iterate(start, c -> c.next).take(labels.size()).toMap(c -> c.label, c -> c);

		Cup current = start;

		for (int i = 1; i <= rounds; ++i) {
			
			if (verbose) {
				System.out.print(cups2String(labels, start, current));
			}		

			// cut 3 cups
			var pick3 = current.next;
			current.next = current.next.next.next.next;

			// select destination cup
			var destLabel = current.label;
			do {
				destLabel--;
				if (destLabel < min)
					destLabel = max;
			} while (destLabel == pick3.label || destLabel == pick3.next.label || destLabel == pick3.next.next.label);
			
			if (verbose) {
				System.out.println("  dest: " + destLabel);
			}

			var destination = cupMap.get(destLabel).get();
			
			// insert cut cups after destination
			pick3.next.next.next = destination.next;
			destination.next = pick3;

			// next current cup
			current = current.next;
			
			if (i % 1_000_000 == 0) System.out.println("..." + i + " rounds played");
		}		
		
		Cup cup1 = cupMap.getOrElse(1L, null);
		
		if (verbose) {
			System.out.println(cups2String(labels, cup1, current));
		}

		return cup1;
	}

	/*
	 *  for debugging
	 */
	String cups2String(List<Long> labels, Cup cup0, Cup current) {
		return cup0
				.collectLabels(labels.size())
				.map(l -> (l==current.label) ? "("+l+")" : Long.toString(l))
				.mkString(" ");
	}

	String testInput = "389125467"; 
	String input = "643719258";

	void part1test() {
		var labels = List.of(testInput.split("")).map(Long::valueOf);
	
		Cup cup1 = playCrabCups(labels, 10, true);
		
		var result = cup1.next.collectLabels(labels.size() - 1).mkString();
		System.out.println("result => " + result);
	}

	void part1() {
		var labels = List.of(input.split("")).map(Long::valueOf);

		Cup cup1 = playCrabCups(labels, 100, false);
		
		var result = cup1.next.collectLabels(labels.size() - 1).mkString();
		System.out.println("result => " + result);
	}
	
	void part2() {
		var labels = List.of(input.split("")).map(Long::valueOf);
		
		// generate labels up to 1M
		labels = labels.appendAll(List.rangeClosed(labels.max().get() + 1, 1_000_000));

		var cup1 = playCrabCups(labels, 10_000_000, false);

		System.out.println(cup1.next.label);
		System.out.println(cup1.next.next.label);

		var result = cup1.next.label * cup1.next.next.label;
		System.out.println("result => " + result);

	}

	
	public static void main(String[] args) {
		System.out.println("=== part 1 test"); // 10:92658374 / 100:67384529
		new Day23().part1test();

		System.out.println("=== part 1"); // 54896723
		new Day23().part1();
		printLapTime();

		System.out.println("=== part 2"); // 146304752384
		new Day23().part2();
		printLapTime();
	}
}
