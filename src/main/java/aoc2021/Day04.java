package aoc2021;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 4: Giant Squid ---
// https://adventofcode.com/2021/day/4

class Day04 extends AocPuzzle {

	record Board(List<Integer> numbers) {
		boolean wins(List<Integer> drawn) {
			return List.range(0, 5).exists(i -> drawn.containsAll(row(i)) || drawn.containsAll(col(i)));
		}

		List<Integer> col(Integer c) {
			return List.range(0, 5).map(i -> numbers.get(i*5 + c));
		}

		List<Integer> row(Integer r) {
			return List.range(0, 5).map(i -> numbers.get(r*5 + i));		
		}

		int unmarkedSum(List<Integer> drawn) {
			return numbers.removeAll(drawn).sum().intValue();
		}

		static Board parse(String s) {
			return new Board(Util.string2ints(s.trim()));			
		}
	}
	
	
	List<Integer> play(String data) {
		// input parsing
		var blocks = List.of(data.split("\n\n"));
		var numbers = split(blocks.head(), ",").map(Integer::parseInt);
		var boards = blocks.tail().map(Board::parse);
		
		// play bingo
		List<Integer> scores = List.empty();
		
		for (var round : List.range(0, numbers.size())) {
			var called = numbers.subSequence(0, round);
			var winner = boards.filter(b -> b.wins(called));
			if (winner.nonEmpty()) {				
				var score = winner.head().unmarkedSum(called) * called.last();
				scores = scores.append(score);
			}
			boards = boards.removeAll(winner);
		}
		
		return scores;
	}

	
	public static void main(String[] args) {
		System.out.println("=== test"); 
		var scores = new Day04().play(example);
		assertThat(scores.head(), is(4512));
		assertThat(scores.last(), is(1924));
		
		System.out.println("=== game"); 
		scores = new Day04().play(new Day04().file2string("input04.txt"));
		System.out.println("first board score: " + scores.head()); // 2496
		System.out.println("last board score : " + scores.last()); // 25925
	}

	
	static String example = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
""";

}
