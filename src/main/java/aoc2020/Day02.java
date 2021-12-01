package aoc2020;

import common.AocPuzzle;

// --- Day 2: Password Philosophy ---
// https://adventofcode.com/2020/day/2

@SuppressWarnings( { "deprecation", "preview" })
class Day02 extends AocPuzzle {

	static record Rule(int low, int high, char letter, String pw) {
		
		boolean check1() {
			long n = pw.chars().filter(c -> c == letter).count();
			return (n >= low) && (n <= high);
		}
		
		boolean check2() {
			return (pw.charAt(low-1) == letter) != (pw.charAt(high-1) == letter); 
		}
		
		static Rule parse(String s) {
			String[] a = s.split("[- :]+");
			return new Rule(Integer.valueOf(a[0]), Integer.valueOf(a[1]), a[2].charAt(0), a[3]);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		var rules = new Day02().file2lines("input02.txt").map(Rule::parse);
		
		System.out.println("=== part 1");
		System.out.println(rules.count(Rule::check1));
		
		System.out.println("=== part 2");
		System.out.println(rules.count(Rule::check2));			
	}
}
