package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 17: Spinlock ---
// https://adventofcode.com/2017/day/17

class Day17 extends AocPuzzle {

	private static final int STEP = 369;

	static class Elem {
		int value;
		Elem next;

		Elem(int value) {
			this.value = value;
			this.next = this;
		}

		Elem insert(int value) {
			var e = new Elem(value);
			e.next = this.next;
			this.next = e;
			return e;
		}

		Elem fwd(int n) {
			var e = this;
			for (int i = 0; i < n; ++i)
				e = e.next;
			return e;
		}
	}

	void part1() {
		var e0 = new Elem(0);
		var e = e0;
		for (int i = 1; i <= 2017; ++i) {
			e = e.fwd(STEP);
			e = e.insert(i);
		}
		System.out.println(e.next.value);
	}

	void part2() {
		var pos = 0;
		var value1 = 0;
		var len = 1;
		int rounds = 50_000_000;
		for (int i = 1; i <= rounds; ++i) {
			// 369 steps ahead
			pos = (pos + STEP) % len;
			// insert next value (i)
			if (pos == 0)
				value1 = i;
			len++;
			// current = new element
			pos++;
		}
		System.out.println(value1);
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 1547
		new Day17().part1();

		System.out.println("=== part 2"); // 31154878
		new Day17().part2();
	}
}
