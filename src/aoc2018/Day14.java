package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.List;

import org.jooq.lambda.Seq;

public class Day14 {

	static int e1 = 0;
	static int e2 = 1;
	static List<Integer> score = new ArrayList<>();
	
	
	
	static void next() {
		int res = score.get(e1) + score.get(e2);
		
		List<Integer> digits = Seq.seq(Integer.toString(res).chars()).map(i -> i - '0').toList();
		score.addAll(digits);
		
		e1 = (e1 + 1 + score.get(e1)) % score.size();
		e2 = (e2 + 1 + score.get(e2)) % score.size();
	}
	
	public static void main(String[] args) throws Exception {
		score.add(3);
		score.add(7);

		System.out.println("=== part 1 ===");
		part1();
		System.out.println("=== part 2 ===");
		part2();
		
	}

	protected static void part1() {
		int input = 293801;
		while (score.size() < input + 10) {
//			System.out.println(seq(score).toString(" "));
			next();
		}
		
		System.out.println(seq(score).skip(input).take(10).toString());
	}

	private static void part2() throws Exception {
		String input = "293801";
//		String input = "59414";
		int[] sub = seq(input.chars()).mapToInt(i -> i-'0').toArray();
		int p = 0;
		int matched = 0;
		
		while (matched < sub.length) {
			next();
			
			// find match between p and end of list
			while (p < score.size()-sub.length && matched < sub.length) {
				if (score.get(p) == sub[matched]) {
					p++;
					matched++;
				} else {
					p = p-matched+1;
					matched=0;
				}
			}
		}
		
		//20280190
		System.out.println(p-sub.length);
	}

}
