package aoc2020;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 15: Rambunctious Recitation ---
// https://adventofcode.com/2020/day/15

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day15 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day15().part1();
		
		System.out.println("=== part 2");
		new Day15().part2();
	}
	
	

	void part1() {
//		var data = List.of(0,3,6);
//		var data = List.of(3,1,2);
		var data = List.of(0,14,1,3,7,9);
		
		Array<Integer> spoken = data.reverse().toArray();
		System.out.println(spoken);
		
		int end = 2020;
		int i = spoken.size();
		int next;
		var last = spoken.head();
		
		while (i< end) {
			++i;
			if (! spoken.tail().contains(last)) {
//				System.out.println("not seen " + last);
				next = 0;
			} else {
				var S = spoken;
				var L = last;
				var a = spoken.indexOf(last);
				
				int b = a+1;
				while (! spoken.get(b).equals(last)) b++;
				
//				var b = List.range(a+1, S.size()).filter(x -> S.get(x) == L).get();
//				System.out.println(last + " seen on " + a + " and " + b);
				
				next = b-a;
			}
			
			spoken = spoken.prepend(next);
			last = next;
		}
		System.out.println(i + " --> " + spoken);
	}

	void part2() {	
//		var data = List.of(0,3,6);
//		var data = List.of(3,1,2);
		var data = List.of(0,14,1,3,7,9);
		
		int end = 30000000;
//		int end = 2020;
//		int end=10;
		
		Map<Integer, Integer> spokenLast = new HashMap<>();
		Map<Integer, Integer> spokenBefore = new HashMap<>();
		
		int i = 0;
		int next = 0;
		int last = 0;
		
		while (i < end) {
			int sb = spokenBefore.getOrDefault(last, -1);
			
			if (i < data.size())
				next = data.get(i);
			else {
				if (sb == -1) {
					next = 0;
				} else {
					int sl = spokenLast.get(last);
					next = sl-sb;
				}
			}
			
			
			spokenBefore.put(next, spokenLast.getOrDefault(next, -1));
			spokenLast.put(next, i);
//			System.out.println(last + " -> " + next + " / " + spokenLast + " / " + spokenBefore);
			last = next;
			i++;
		}
		System.out.println(last);
	
	}
}
