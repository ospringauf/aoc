package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 5: Binary Boarding ---
// https://adventofcode.com/2020/day/5

@SuppressWarnings({ "deprecation", "preview" })
class Day05 extends AocPuzzle {

	public static void main(String[] args) throws Exception {

		// tests
		assertEquals(357, new Day05().decode("FBFBBFFRLR"));
		assertEquals(567, new Day05().decode("BFFFBBFRRR"));
		assertEquals(119, new Day05().decode("FFFBBBFRRR"));
		assertEquals(820, new Day05().decode("BBFFBBFRLL"));
		
		System.out.println("=== part 1");
		new Day05().part1();
		
		System.out.println("=== part 2");
		new Day05().part2();
	}

//	private int decode(String s) {
//		int n = 1 << 9;
//		int sid = 0;
//						
//		for (char c : s.toCharArray()) {
//			boolean hi = (c=='B') || (c=='R');
//			sid += hi ? n : 0;
//			n /= 2;
//		}
//		return sid;
//	}
//	
//	private int decode(String s) {
//		int len = s.length();
//		Predicate<Integer> backOrRight = i -> s.charAt(i)=='B'||s.charAt(i)=='R';
//		
//		var sid = List.range(0, len).filter(backOrRight).map(p -> 1 << (len-1-p)).sum().intValue();
//		return sid;
//	}
	
	private int decode(String s) {
		var s01 = s.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1');
		return Integer.parseInt(s01, 2);
	}
	

	private void part1() throws Exception {
		var passes = file2lines("input05.txt").map(this::decode);		
		System.out.println(passes.max());
	}

	private void part2() throws Exception {	
		var passes = file2lines("input05.txt").map(this::decode);
		var allSeats = List.range(0, passes.max().get());
		
		var myseat = allSeats.filter(n -> passes.contains(n-1) && ! passes.contains(n) && passes.contains(n+1));
		System.out.println(myseat);
	}

}
