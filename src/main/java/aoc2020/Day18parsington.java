package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.scijava.parsington.ExpressionParser;
import org.scijava.parsington.Group;
import org.scijava.parsington.Operator;
import org.scijava.parsington.Operator.Associativity;
import org.scijava.parsington.SyntaxTree;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 18: Operation Order ---
// https://adventofcode.com/2020/day/18

// alternative solution using "parsington" (https://github.com/scijava/parsington)

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day18parsington extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day18parsington().test();

		System.out.println("=== part 1"); // 30753705453324
		new Day18parsington().part1();

		System.out.println("=== part 2"); // 244817530095503
		new Day18parsington().part2();
	}

	List<String> lines = file2lines("input18.txt");

	
	long eval(SyntaxTree tree) {
		var token = tree.token();
		
		if (token instanceof Integer n) {
			return n.longValue();
		}
		
		if (token instanceof Group) {
			return eval(tree.child(0));
		}		
		
		if (token instanceof Operator op) {
			return switch (op.getToken()) {
			case "+" -> eval(tree.child(0)) + eval(tree.child(1));
			case "*" -> eval(tree.child(0)) * eval(tree.child(1));
			default -> 0;
			};
		}
		
		throw new RuntimeException("unknown: " + tree);	
	}

	void part1() {
		var parser = buildParser1();
		var r = lines.map(l -> eval(parser.parseTree(l))).sum();
		System.out.println(r);
	}

	void part2() {
		var parser = buildParser2();
		var r = lines.map(l -> eval(parser.parseTree(l))).sum();
		System.out.println(r);
	}

	ExpressionParser buildParser1() {
		// + and * have equal precedence, eval left to right
		var ops = List.of(
				new Operator("+", 2, Associativity.LEFT, 1),
				new Operator("*", 2, Associativity.LEFT, 1),
				new Group("(", ")", 10));
		return new ExpressionParser(ops.toJavaList());
	}

	ExpressionParser buildParser2() {
		// + is stronger than *
		var ops = List.of(
				new Operator("+", 2, Associativity.LEFT, 2), 
				new Operator("*", 2, Associativity.LEFT, 1),
				new Group("(", ")", 10));
		return new ExpressionParser(ops.toJavaList());
	}
	

	void test() {

//		String s = "1 + 2 * 3";
//		String s = "1 + 2 * 3 + 4 * 5 + 6";
//		String s = "1 + (2 * 3) + (4 * (5 + 6))";
//		System.out.println(parser.parseTree(s));
//		System.out.println(eval(parser.parseTree(s)));

		// part 1
		var parser = buildParser1();

		assertEquals(71, eval(parser.parseTree("1 + 2 * 3 + 4 * 5 + 6")));
		assertEquals(51, eval(parser.parseTree("1 + (2 * 3) + (4 * (5 + 6))")));
		assertEquals(26, eval(parser.parseTree("2 * 3 + (4 * 5)")));
		assertEquals(437, eval(parser.parseTree("5 + (8 * 3 + 9 + 3 * 4 * 3)")));
		assertEquals(12240, eval(parser.parseTree("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")));
		assertEquals(13632, eval(parser.parseTree("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")));

		// part 2
		parser = buildParser2();

		assertEquals(231, eval(parser.parseTree("1 + 2 * 3 + 4 * 5 + 6")));
		assertEquals(51, eval(parser.parseTree("1 + (2 * 3) + (4 * (5 + 6))")));
		assertEquals(46, eval(parser.parseTree("2 * 3 + (4 * 5)")));
		assertEquals(1445, eval(parser.parseTree("5 + (8 * 3 + 9 + 3 * 4 * 3)")));
		assertEquals(669060, eval(parser.parseTree("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")));
		assertEquals(23340, eval(parser.parseTree("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")));

		System.out.println("passed");
	}

}