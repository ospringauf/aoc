package aoc2021;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.scijava.parsington.ExpressionParser;
import org.scijava.parsington.Group;
import org.scijava.parsington.SyntaxTree;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 18: Snailfish ---
// https://adventofcode.com/2021/day/18

class Day18 extends AocPuzzle {

	List<String> input = file2lines("input18.txt");

	// Snailfish number
	static class Snum {
		Snum outer;
		Snum left;
		Snum right;
		Integer val;

		Snum(Snum outer) {
			this.outer = outer;
		}

		Snum(Snum outer, int v) {
			this(outer);
			this.val = v;
		}

		Snum(Snum outer, Snum l, Snum r) {
			this(outer);
			this.left = l;
			this.right = r;
			left.outer = right.outer = this;
		}

		Snum(Snum outer, SyntaxTree tree) {
			this(outer);
			var token = tree.token();

			if (token instanceof Integer n) {
				this.val = n;
			} else if (token instanceof Group) {
				this.left = new Snum(this, tree.child(0));
				this.right = new Snum(this, tree.child(1));
			}
		}

		boolean literal() {
			return val != null;
		}

		int depth() {
			return (outer == null) ? 0 : outer.depth() + 1;
		}

		void addRight(Snum of, Integer v) {
			if (literal())
				val += v;
			else if (of == left)
				right.addLeft(this, v);
			else if (of == outer)
				right.addRight(this, v);
			else if (outer != null)
				outer.addRight(this, v);
		}

		void addLeft(Snum of, Integer v) {
			if (literal())
				val += v;
			else if (of == right)
				left.addRight(this, v);
			else if (of == outer)
				left.addLeft(this, v);
			else if (outer != null)
				outer.addLeft(this, v);
		}

		boolean explode() {
			if (literal())
				return false;
			else if (depth() == 4) {
				// System.out.println("exploding " + this);
				outer.addLeft(this, left.val);
				outer.addRight(this, right.val);
				this.left = this.right = null;
				this.val = 0;
				return true;
			} else {
				return left.explode() || right.explode();
			}
		}

		boolean split() {
			if (literal()) {
				if ((val >= 10)) {
//					System.out.println("split " + this);
					left = new Snum(this, val / 2);
					right = new Snum(this, val - (val / 2));
					val = null;
					return true;
				} else
					return false;
			} else {
				return left.split() || right.split();
			}
		}

		Snum reduce() {
//			System.out.println("reduce " + this);
			while (explode() || split())
//				System.out.println("... " + this);
				;
			return this;
		}

		long magnitude() {
			if (literal())
				return val;
			else
				return 3 * left.magnitude() + 2 * right.magnitude();
		}

		static Snum add(Snum a, Snum b) {
			return (a == null) ? b : new Snum(null, a, b).reduce();
		}

		public String toString() {
			if (val != null)
				return val.toString();
			else
				return "[" + left.toString() + "," + right.toString() + "]";
		}

		static Snum parse(String s) {
			var parser = new ExpressionParser(java.util.List.of(new Group("[", "]", 10)));
			return new Snum(null, parser.parseTree(s));
		}

	}

	void part1() {
		Snum n = input.map(Snum::parse).foldLeft(null, (a, b) -> Snum.add(a, b));
		System.out.println(n.magnitude());
	}

	void part2() {
		var m1 = input.crossProduct().map(t -> Snum.add(Snum.parse(t._1), Snum.parse(t._2)).magnitude()).max();
		System.out.println(m1);
	}

	void test() {
		System.out.println("--- parse");
		List<String> examples = Util.splitLines(testParse);

		System.out.println(Snum.parse(examples.get(1)));

		for (var x : examples) {
			assertThat(Snum.parse(x).toString(), is(x));
		}

		System.out.println("--- explode");
		examples = Util.splitLines(testExplode);
		for (var x : examples) {
			var f = x.split(" becomes ");
			var n = Snum.parse(f[0]);
			n.explode();
//			System.out.println(n.explode());
			assertThat(n.toString(), is(f[1]));
		}

		System.out.println("--- reduce");
		var n = Snum.add(Snum.parse("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"),
				Snum.parse("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"));
		assertThat(n.toString(), is("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"));

		System.out.println("--- sum");
		examples = Util.splitLines(testSum);
		n = examples.map(Snum::parse).foldLeft(null, (a, b) -> Snum.add(a, b));
		assertThat(n.toString(), is("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"));

		System.out.println("--- magnitude");
		examples = Util.splitLines(testMagnitude);
		for (var x : examples) {
			var f = x.split(" becomes ");
			var m = Snum.parse(f[0]);
			assertThat(m.magnitude(), is(Long.parseLong(f[1])));
		}

		System.out.println("--- homework");

		examples = Util.splitLines(homework);
		n = examples.map(Snum::parse).foldLeft(null, (a, b) -> Snum.add(a, b));
		assertThat(n.toString(), is("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"));
		assertThat(n.magnitude(), is(4140L));

		System.out.println("passed");
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day18().test();

		System.out.println("=== part 1"); // 4433
		new Day18().part1();

		System.out.println("=== part 2"); // 4559
		new Day18().part2();
	}

	static String testParse = """
			[1,2]
			[[1,2],3]
			[9,[8,7]]
			[[1,9],[8,5]]
			[[[[1,2],[3,4]],[[5,6],[7,8]]],9]
			[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
			[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
						""";

	static String testExplode = """
			 [[[[[9,8],1],2],3],4] becomes [[[[0,9],2],3],4]
			 [7,[6,[5,[4,[3,2]]]]] becomes [7,[6,[5,[7,0]]]]
			 [[6,[5,[4,[3,2]]]],1] becomes [[6,[5,[7,0]]],3]
			 [[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]] becomes [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]
			 [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]] becomes [[3,[2,[8,0]]],[9,[5,[7,0]]]]
			""";

	static String testSum = """
			[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
			[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
			[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
			[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
			[7,[5,[[3,8],[1,4]]]]
			[[2,[2,2]],[8,[8,1]]]
			[2,9]
			[1,[[[9,3],9],[[9,0],[0,7]]]]
			[[[5,[7,4]],7],1]
			[[[[4,2],2],6],[8,7]]
			""";

	static String testMagnitude = """
			 [[1,2],[[3,4],5]] becomes 143
			 [[[[0,7],4],[[7,8],[6,0]]],[8,1]] becomes 1384
			 [[[[1,1],[2,2]],[3,3]],[4,4]] becomes 445
			 [[[[3,0],[5,3]],[4,4]],[5,5]] becomes 791
			 [[[[5,0],[7,4]],[5,5]],[6,6]] becomes 1137
			 [[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]] becomes 3488
			""";

	static String homework = """
			[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
			[[[5,[2,8]],4],[5,[[9,9],0]]]
			[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
			[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
			[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
			[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
			[[[[5,4],[7,7]],8],[[8,3],8]]
			[[9,3],[[9,9],[6,[4,9]]]]
			[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
			[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
						""";
}
