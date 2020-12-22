package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 18: Operation Order ---
// https://adventofcode.com/2020/day/18

// lessons learned:
// - follow the rules of compiler construction - first scan, then parse, then evaluate 
// - a recursive stack parser is not that complicated if done right
// - better don't write parsers by hand, there are better solutions ready-to-use (-> parsington approach) 

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day18 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day18().test();

		System.out.println("=== part 1"); // 30753705453324
		new Day18().part1();

		System.out.println("=== part 2"); // 244817530095503
		new Day18().part2();
	}

	List<String> lines = lines("input18.txt");

	enum TType {
		LPAREN, RPAREN, ADD, MUL, NUMBER
	};

	record Token(TType typ, String s, long value) {

		static final Token LPAREN = new Token(TType.LPAREN, "(", 0);
		static final Token RPAREN = new Token(TType.RPAREN, ")", 0);
		static final Token ADD = new Token(TType.ADD, "+", 0);
		static final Token MUL = new Token(TType.MUL, "*", 0);

		public Token(long val) {
			this(TType.NUMBER, null, val);
		}

		static Token parse(String s) {
			return switch (s) {
			case "(" -> LPAREN;
			case ")" -> RPAREN;
			case "*" -> MUL;
			case "+" -> ADD;
			default -> new Token(Long.parseLong(s));
			};
		}

		public String toString() {
			return (s == null) ? Long.toString(value) : s;
		}
	}

	static List<Token> tokenize(String s) {
		s = s.replaceAll("\\(", "( ").replaceAll("\\)", " )");
		return List.of(s.split("\\s")).map(Token::parse);
	}
	
	void part1() {
		var r = lines.map(l -> eval1(tokenize(l)).head().value).sum();
		System.out.println(r);
	}

	void part2() {
		var r = lines.map(l -> eval2(tokenize(l)).head().value).sum();
		System.out.println(r);
		}

	List<Token> eval1(List<Token> tokens) {
		var r = 0L;
		var op = Token.ADD;

		while ( ! tokens.isEmpty()) {
			var next = tokens.peek();
			tokens = tokens.pop();

			switch (next.typ) {
			case LPAREN -> {
				tokens = eval1(tokens);
			}
			case RPAREN -> {
				return tokens.push(new Token(r));
			}
			case NUMBER -> {
				r = (op.typ == TType.ADD) ? r + next.value : r * next.value;
			}
			default -> {
				op = next;
			}
			}
		}
		return List.of(new Token(r));
	}


	List<Token> eval2(List<Token> tokens) {
		List<Token> flat = List.empty();

		while ( ! tokens.isEmpty()) {
//			System.out.println(tokens.mkString());
			var next = tokens.peek();
			tokens = tokens.pop();

			switch (next.typ) {
			case LPAREN -> {
				tokens = eval2(tokens);
			}
			case RPAREN -> {
				var r = sumprod(flat);
				return tokens.push(new Token(r));
			}
			default -> {
				flat = flat.append(next);
			}
			}
		}
		
		// remaining expression has +/* but no (...) --> group sums, then multiply
		var r = sumprod(flat);
		return List.of(new Token(r));
	}

	private long sumprod(List<Token> flat) {
		var sums = Util.split(flat, t -> t.typ==TType.MUL); 
		var r = sums.map(l -> l.map(t -> t.value).sum()).product().longValue();
		return r;
	}	

	void test() {

		var x = Util.split(tokenize("1 + 2 * 3 + 4 * 5 + 6"), t -> t.typ==TType.MUL);
		System.out.println(x);
		
		// part 1
		assertEquals(71, eval1(tokenize("1 + 2 * 3 + 4 * 5 + 6")).head().value);
		assertEquals(51, eval1(tokenize("1 + (2 * 3) + (4 * (5 + 6))")).head().value);
		assertEquals(26, eval1(tokenize("2 * 3 + (4 * 5)")).head().value);
		assertEquals(437, eval1(tokenize("5 + (8 * 3 + 9 + 3 * 4 * 3)")).head().value);
		assertEquals(12240, eval1(tokenize("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).head().value);
		assertEquals(13632, eval1(tokenize("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).head().value);

		// part 2
		assertEquals(19 , eval2(tokenize( "(2 * (1 * 3) + 4) + 5" )).head().value);
		assertEquals(5*14+1 , eval2(tokenize( "(5 * (4 * 3) + 2) + 1" )).head().value);
		assertEquals(22 , eval2(tokenize( "(2 * (3 * 1) + 5) + 6" )).head().value);
		assertEquals(30 , eval2(tokenize( "(2 * (1 * 3 + 4) + 5) + 6" )).head().value);
		assertEquals(30*7 , eval2(tokenize( "(2 * (1 * 3 + 4) + 5) + 6 * 7" )).head().value);
		
		assertEquals(231, eval2(tokenize("1 + 2 * 3 + 4 * 5 + 6")).head().value);
		assertEquals(51, eval2(tokenize("1 + (2 * 3) + (4 * (5 + 6))")).head().value);
		assertEquals(46, eval2(tokenize("2 * 3 + (4 * 5)")).head().value);
		assertEquals(1445, eval2(tokenize("5 + (8 * 3 + 9 + 3 * 4 * 3)")).head().value);
		assertEquals(669060, eval2(tokenize("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).head().value);
		assertEquals(23340, eval2(tokenize("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).head().value);

		System.out.println("passed");
	}
}