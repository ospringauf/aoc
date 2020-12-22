package aoc2020;

import java.util.HashSet;
import java.util.Set;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 22: Crab Combat ---
// https://adventofcode.com/2020/day/22

// lessons learned:
// - Java records and Vavr collections are a great match, no need to write equals/hashCode
// - it helps to read all the text in the problem description (subgame decks!)

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day22 extends AocPuzzle {

	/**
	 * card deck of a single player
	 */
	record Deck(List<Integer> deck) {
		boolean noMoreCards() {
			return deck.isEmpty();
		}

		Integer topCard() {
			return deck.head();
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

		public String toString() {
			return deck.mkString("[", " ", "]");
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
				return new Cards(p1.takeCard(p2.topCard()), p2.loseCard());
			else
				return new Cards(p1.loseCard(), p2.takeCard(p1.topCard()));
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
			return (p1.topCard() > p2.topCard()) ? 1 : 2;
		}

		public static Cards parse(String input) {
			String[] blocks = input.split("\n\n");
			List<Integer> d1 = List.of(blocks[0].split("\n")).drop(1).map(Integer::parseInt);
			List<Integer> d2 = List.of(blocks[1].split("\n")).drop(1).map(Integer::parseInt);
			return new Cards(new Deck(d1), new Deck(d2));
		}

		public String toString() {
			return "P1:" + p1 + " -  P2:" + p2;
		}
	}

	/**
	 * Combat game
	 */
	class Combat {
		Cards cards;

		Combat(Cards c) {
			cards = c;
		}

		int play() {
			int round = 1;
			int winner = 0;
			while (!cards.exhausted()) {
//				System.out.println(round + ": " + cards);

				winner = cards.roundWinner();
				cards = cards.next(winner);
				round++;
			}
			return winner;
		}
	}

	/**
	 * Recursive Combat game
	 */
	class RecursiveCombat {
		Cards cards;
		int depth;
		Set<Cards> seen = new HashSet<>();

		RecursiveCombat(Cards cards, int depth) {
			this.cards = cards;
			this.depth = depth;
		}

		int play() {
			int round = 1;
			int winner = 0;
			while (!cards.exhausted()) {
//				System.out.println("\t".repeat(depth) + round + ": " + cards);

				if (seen.contains(cards)) {
					return 1;
				}
				seen.add(cards);

				if (cards.canRecurse()) {
					// play sub-game
					winner = new RecursiveCombat(cards.subgame(), depth + 1).play();
				} else {
					winner = cards.roundWinner();
				}

				cards = cards.next(winner);
				round++;
			}
			return winner;
		}
	}

	String input = readString("input22.txt");
//	String input = example;

	void part1() {
		var game = new Combat(Cards.parse(input));
		game.play();

		System.out.println(game.cards.score());
	}

	void part2() {
		var game = new RecursiveCombat(Cards.parse(input), 0);
		game.play();

		System.out.println(game.cards.score());
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 33098
		new Day22().part1();

		// ca 1M rounds, 800ms
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
