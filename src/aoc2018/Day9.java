package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.Arrays;

/**
 * marbles: cyclic linked list + pointer movement
 * https://adventofcode.com/2018/day/9
 *
 */
public class Day9 {

	static class Marble {
		Marble prev; // clockwise
		Marble next;
		int value;
		
		static Marble initial() {
			Marble m = new Marble();
			m.value = 0;
			m.prev = m;
			m.next = m;
			return m;
		}
		
		Marble remove() {
			prev.next = next;
			next.prev = prev;
			return next;
		}
		
		Marble insertAfter(int val) {
			Marble m = new Marble();
			m.value = val;
			m.next = next;
			m.prev = this;
			next.prev = m;
			next = m;
			return m;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("=== part 1 ===");
		System.out.println(marbles(427, 70723));

		System.out.println("=== part 2 ===");
		System.out.println(marbles(427, 7072300));
}

	protected static Long marbles(int players, int last) {
		Marble current = Marble.initial();
		int value = 1;
		long[] score = new long[players];
		
		while (value <= last) {
			if (value % 23 == 0) {
				int player = (value-1) % players; 
				score[player] += value;
				current = current.prev.prev.prev.prev.prev.prev.prev;
				score[player] += current.value;
				current = current.remove();
			}
			else {
				current = current.next.insertAfter(value);
			}

			value++;
		}
		
		return seq(Arrays.stream(score)).max().get();
	}
	
}
