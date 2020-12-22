package aoc2020;

import java.util.HashSet;
import java.util.Set;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 22: Crab Combat ---
// https://adventofcode.com/2020/day/22

// lessons learned:
// - Java records and vavr collections are a great match, no need to write equals/hashCode
// - it helps to read all the text in the problem description (subgame decks!)

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day22 extends AocPuzzle {

	/**
	 * card deck of a single player
	 */
	record Deck(List<Integer> deck) {
		public Deck(String s) {
			this(List.of(s.split("\n")).drop(1).map(Integer::parseInt));
		}

		boolean noMoreCards() {
			return deck.isEmpty();
		}

		Deck takeCard(Integer loserCard) {
			return new Deck(deck.tail().append(deck.head()).append(loserCard));
		}

		Deck loseCard() {
			return new Deck(deck.tail());
		}

		long score() {
			var a = deck.reverse();
			return List.range(0, a.size()).map(n -> (n + 1) * a.get(n)).sum().longValue();
		}

		boolean canRecurse() {
			return deck.head() < deck.size();
		}

		Deck subgame() {
			return new Deck(deck.tail().take(deck.head()));
		}
	}

	/**
	 * card decks of both players
	 */
	record Cards(Deck p1, Deck p2) {
		boolean exhausted() {
			return p1.noMoreCards() || p2.noMoreCards();
		}

		Cards next(int winner) {
			if (winner == 1)
				return new Cards(p1.takeCard(p2.deck.head()), p2.loseCard());
			else
				return new Cards(p1.loseCard(), p2.takeCard(p1.deck.head()));
		}

		boolean canRecurse() {
			return p1.canRecurse() && p2.canRecurse();
		}

		Cards subgame() {
			return new Cards(p1.subgame(), p2.subgame());
		}

		long score() {
			return p1.score() + p2.score();
		}

		int roundWinner() {
			return (p1.deck.head() > p2.deck.head()) ? 1 : 2;
		}
	}

	/**
	 * Combat game
	 */
	class Combat {
		Cards cards;

		public Combat(Cards c) {
			cards = c;
		}

		int play() {
			int round = 1;
			int winner = 0;
			while (!cards.exhausted()) {

				winner = cards.roundWinner();
				cards = cards.next(winner);
//				System.out.println(round + " -> " + cards.p1 + " / " + cards.p2);
				round++;
			}
			return winner;
		}
	}

	/**
	 * Recursice Combat game
	 */
	class RecursiveCombat {
		Cards cards;
		Set<Cards> seen = new HashSet<>();

		public RecursiveCombat(Cards c) {
			cards = c;
		}

		int play() {
			int round = 1;
			int winner = 0;
			while (!cards.exhausted()) {

				if (seen.contains(cards)) {
					return 1;
				}
				seen.add(cards);

				if (cards.canRecurse()) {
					// play sub-game
					winner = new RecursiveCombat(cards.subgame()).play();
				} else {				
					winner = cards.roundWinner();
				}
				
				cards = cards.next(winner);
//				System.out.println(round + " -> " + cards.p1 + " / " + cards.p2);
				round++;
			}
			return winner;
		}
	}

	String input = readString("input22.txt");
//	String input = example;

	void part1() {
		var blocks = input.split("\n\n");
		Cards cards = new Cards(new Deck(blocks[0]), new Deck(blocks[1]));
		var game = new Combat(cards);
		game.play();

		System.out.println(game.cards.score());
	}

	void part2() {
		var blocks = input.split("\n\n");
		Cards cards = new Cards(new Deck(blocks[0]), new Deck(blocks[1]));
		var game = new RecursiveCombat(cards);
		game.play();

		System.out.println(game.cards.score());
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 33098
		new Day22().part1();

		System.out.println("=== part 2"); // 35055
		new Day22().part2();
	}

	static String example = """
			Player 1:
			9
			2
			6
			3
			1

			Player 2:
			5
			8
			4
			7
			10
						""";

	static String example2 = """
			Player 1:
			43
			19

			Player 2:
			2
			29
			14
									""";

}
