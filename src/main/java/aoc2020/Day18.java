package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 18: Operation Order ---
// https://adventofcode.com/2020/day/18
// TODO there must be a better way

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


	enum TType { LPAREN, RPAREN, ADD, MUL, NUMBER };

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
			return (s==null) ? Long.toString(value) : s;
		}
	}

	List<String> lines = lines("input18.txt");

	void part1() {
		var r = lines.map(l -> eval1(tokenize(l)).value).sum();
		System.out.println(r);
	}

	void part2() {
		var r = lines.map(l -> eval2(tokenize(l)).value).sum();
		System.out.println(r);
	}

	void test() {
//		System.out.println(tokenize("1 + 2 * 3 + 4 * 5 + 6"));
//		System.out.println(tokenize("1 + (2 * 3) + (4 * (5 + 6))"));
		
		// part 1
		assertEquals(71, eval1(tokenize("1 + 2 * 3 + 4 * 5 + 6")).value);
		assertEquals(51, eval1(tokenize("1 + (2 * 3) + (4 * (5 + 6))")).value);
		assertEquals(26, eval1(tokenize("2 * 3 + (4 * 5)")).value);
		assertEquals(437, eval1(tokenize("5 + (8 * 3 + 9 + 3 * 4 * 3)")).value);
		assertEquals(12240, eval1(tokenize("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).value);
		assertEquals(13632, eval1(tokenize("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).value);

		// part 2
		assertEquals(231, eval2(tokenize("1 + 2 * 3 + 4 * 5 + 6")).value);
		assertEquals(51, eval2(tokenize("1 + (2 * 3) + (4 * (5 + 6))")).value);
		assertEquals(46, eval2(tokenize("2 * 3 + (4 * 5)")).value);
		assertEquals(1445, eval2(tokenize("5 + (8 * 3 + 9 + 3 * 4 * 3)")).value);
		assertEquals(669060, eval2(tokenize("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).value);
		assertEquals(23340, eval2(tokenize("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).value);

		System.out.println("passed");
	}

	Token eval1(List<Token> tokens) {
		tokens = resolveParens(tokens, this::eval1);

	    // no (...) remaining
		long val = 0;
		Token op = Token.ADD;

		for (var current : tokens) {			
			if (current.typ != TType.NUMBER)
				op = current;
			else {
				val = switch (op.typ) {
					case ADD -> val + current.value;
					case MUL -> val * current.value;
					default -> current.value;
				};
			}
		}
		return new Token(val);
	}

	Token eval2(List<Token> tokens) {
		tokens = resolveParens(tokens, this::eval2);

	    // no (...) remaining; split at first "*" and evaluate left/right separately
		if (tokens.contains(Token.MUL)) {
			var a = tokens.splitAt(tokens.indexOf(Token.MUL));
			var t1 = eval2(a._1);
			var t2 = eval2(a._2.tail());
			return new Token(t1.value * t2.value);
		}

		// now: only expressions of type "a + b + ..." remain, that's easy
		long r = tokens.map(t -> t.value).sum().longValue();

        return new Token(r);
	}

	List<Token> resolveParens(List<Token> tokens, Function<List<Token>, Token> eval) {
    	int i = tokens.indexOf(Token.LPAREN);
    	if (i == -1)
    	    return tokens;
    	int j = i + 1 + closingRParen(tokens.subSequence(i+1));
    
    	var s1 = tokens.subSequence(0, i); // no "("
    	var s2 = tokens.subSequence(i + 1, j); // "( ... )" -> evaluate
    	var s3 = tokens.subSequence(j + 1); // remaining tokens -> resolve recursively
    
    	var resolved = s1.append(eval.apply(s2)).appendAll(resolveParens(s3, eval));
    	return resolved;
    }

    
    int closingRParen(List<Token> tokens) {
		int depth = 0;
		for (int n = 0; n < tokens.size(); ++n) {
			var t = tokens.get(n);

			if (t == Token.LPAREN)
				depth++;
			
			if (t == Token.RPAREN)
				if (depth == 0)
					return n;
				else
					depth--;
		}
		throw new RuntimeException("closing ')' not found  in " + tokens.mkString());
	}
  
	List<Token> tokenize(String s) {
		s = s.replaceAll("\\(", "( ").replaceAll("\\)", " )");
		return List.of(s.split("\\s")).map(Token::parse);
	}
}