package aoc2022;

import common.AocPuzzle;
import io.vavr.collection.List;

//--- Day 2: Rock Paper Scissors ---
// https://adventofcode.com/2022/day/2

class Day02 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 10941
		new Day02().part1();
		System.out.println("=== part 2"); // 13071
		new Day02().part2();
	}
	
	List<String> data = file2lines("input02.txt");
//	List<String> data = Util.splitLines(example);


	enum Shape {
		ROCK, PAPER, SCISSORS;

		Shape beats() {
			return switch (this) {
			case ROCK -> SCISSORS;
			case PAPER -> ROCK;
			case SCISSORS -> PAPER;
			};
		}

		Shape loses() {
			return switch (this) {
			case ROCK -> PAPER;
			case PAPER -> SCISSORS;
			case SCISSORS -> ROCK;
			};
		}

		int score() {
			return switch (this) {
			case ROCK -> 1;
			case PAPER -> 2;
			case SCISSORS -> 3;
			};
		}

		static Shape parse(String s) {
			return switch (s) {
			case "A", "X" -> ROCK;
			case "B", "Y" -> PAPER;
			case "C", "Z" -> SCISSORS;
			default -> null;
			};
		}
	}

	int score(Shape elf, Shape mine) {
		int score = mine.score();
		if (mine.beats() == elf)
			return 6 + score;
		if (mine == elf)
			return 3 + score;
		return score;
	}

	void part1() {
		int totalScore = 0;

		for (var line : data.map(split(" "))) {
			var elf = Shape.parse(line.s(0));
			var mine = Shape.parse(line.s(1));

			totalScore += score(elf, mine);
		}
		System.out.println(totalScore);
	}

	void part2() {
		int totalScore = 0;
		
		for (var line : data.map(split(" "))) {
			var elf = Shape.parse(line.s(0));
			var strategy = line.s(1);

			var mine = switch (strategy) {
			case "X" -> elf.beats(); // I lose
			case "Y" -> elf; // draw
			case "Z" -> elf.loses(); // I win
			default -> null;
			};

			totalScore += score(elf, mine);
		}
		System.out.println(totalScore);
	}

	static String example = """
			A Y
			B X
			C Z
						""";
}
