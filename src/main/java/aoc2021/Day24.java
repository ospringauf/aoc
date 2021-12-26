package aoc2021;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 24: Arithmetic Logic Unit ---
// https://adventofcode.com/2021/day/24

// This is not a general solver - run over your input to extract the dependencies
// between the input, and insert the 7 derived inputs into "findSerial" 

class Day24 extends AocPuzzle {

//	List<String> input = Util.splitLines(example);
	List<String> input = file2lines("input24.txt");

	enum Op {
		inp, add, mul, div, mod, eql
	};

	static Map<Character, Expr> current = HashMap.empty();

	static int inputNr = 0;

	static class Expr {
		Op op;
		Expr arg1;
		Expr arg2;

		Expr() {
		}

		boolean isOutside1to9() {
			if (op == Op.add)
				if (arg1 instanceof Input && arg2 instanceof Const c)
					return (c.val <= -9) || (c.val >= 9);

			if (op == Op.add)
				if (arg1.op == Op.mod && arg2 instanceof Const c)
					return (c.val >= 10);

			return false;
		}

		Expr(Op op, Expr arg1, Expr arg2) {
			this.op = op;
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		static String toJava(String s) {
			var r = split(s, " ");
			var op = Enum.valueOf(Op.class, r.s(0));
			var tgt = r.s(1).charAt(0);

			if (op == Op.inp) {
				return "" + tgt + " = data[" + (inputNr++) + "];";
			} else {
				return switch (op) {
				case add -> tgt + " += " + r.s(2) + ";";
				case div -> tgt + " /= " + r.s(2) + ";";
				case mul -> tgt + " *= " + r.s(2) + ";";
				case mod -> tgt + " %= " + r.s(2) + ";";
				case eql -> tgt + " = (" + tgt + " == " + r.s(2) + ")? 1:0;";
				default -> "???" + s;
				};
			}
		}

		long eval(Long[] d) {
			return switch (op) {
			case add -> arg1.eval(d) + arg2.eval(d);
			case div -> arg1.eval(d) / arg2.eval(d);
			case mul -> arg1.eval(d) * arg2.eval(d);
			case mod -> arg1.eval(d) % arg2.eval(d);
			case eql -> (arg1.eval(d) == arg2.eval(d)) ? 1 : 0;
			default -> throw new RuntimeException("unknown op: " + op);
			};
		}

		static Expr parse(String s) {
			var r = split(s, " ");
			var op = Enum.valueOf(Op.class, r.s(0));
			var tgt = r.s(1).charAt(0);

			Expr cmd;
			if (op == Op.inp) {
				// inp w
				cmd = new Input(inputNr++);
			} else {
				var a = r.s(2).charAt(0);
				var arg1 = current.getOrElse(tgt, Const.ZERO);
				if ("wxyz".contains(Character.toString(a))) {
					// add x z
					var arg2 = current.getOrElse(a, Const.ZERO);
					cmd = new Expr(op, arg1, arg2);
				} else {
					// mod x 26
					cmd = new Expr(op, arg1, Const.build(r.i(2)));
				}

			}
			cmd = cmd.reduce();
			current = current.put(tgt, cmd);
			return cmd;
		}

		Expr reduce() {
			var p1 = arg1.reduce();
			var p2 = arg2.reduce();

			if (op == Op.add) {
				if (p1 == Const.ZERO)
					// 0+a -> a
					return p2;
				if (p2 == Const.ZERO)
					// a+0 -> a
					return p1;
				if ((p1 instanceof Const c1) && (p2 instanceof Const c2))
					// c1+c2 -> (c1+c2)
					return Const.build(c1.val + c2.val);
				if ((p1.op == Op.add) && p1.arg2 instanceof Const c12 && p2 instanceof Const c2) {
					return new Expr(Op.add, p1.arg1, Const.build(c12.val + c2.val));
				}
			}
			if (op == Op.mul) {
				if (p1 == Const.ZERO || p2 == Const.ZERO)
					// a*0 -> 0
					return Const.ZERO;
				if (p1 == Const.ONE)
					return p2;
				if (p2 == Const.ONE)
					return p1;
			}
			if (op == Op.div) {
				if (p1 == Const.ZERO)
					return Const.ZERO;
				if (p2 == Const.ONE)
					return p1;
			}
			if (op == Op.mod) {
				if (p1 == Const.ZERO)
					// 0 mod n -> 0
					return Const.ZERO;
				if ((p1 instanceof Const c1) && (p2 instanceof Const c2)) {
					return Const.build(c1.val % c2.val);
				}
				if ((p1 instanceof Input i) && (p2 instanceof Const c)) {
					if (c.val > 9)
						return i;
				}
				if (p1.op == Op.mul && p2 instanceof Const c) {
					if (p1.arg2.equals(c))
						// (a * c) % c -> 0
						return Const.ZERO;
				}
				if (p1.op == Op.mod && p2 instanceof Const c) {
					// (a % c) % c -> (a % c)
					if (p1.arg2.equals(c))
						return p1;
				}
				if (p1.op == Op.add && p2 instanceof Const c) {
					// (a+b)%c -> ((a%c)+(b%c))%c
					var pp1 = new Expr(Op.mod, p1.arg1, c).reduce();
					var pp2 = new Expr(Op.mod, p1.arg2, c).reduce();
					var pp3 = new Expr(Op.add, pp1, pp2).reduce();
					return new Expr(Op.mod, pp3, c);
				}
			}
			if (op == Op.eql) {
				if (p2 instanceof Input i) {
					// (a==x) -> a in (0..9)
					if (p1.isOutside1to9())
						return Const.ZERO;
				}
				if (p1 instanceof Const c1 && p2 instanceof Const c2) {
					return (c1.val == c2.val) ? Const.ONE : Const.ZERO;
				}
			}

			return new Expr(op, p1, p2);
		}

		public String toText() {
			if (arg1 == this || arg2 == this)
				throw new RuntimeException("loop");

			var s1 = arg1.toText();
			var s2 = arg2.toText();

			return switch (op) {
			case add -> "(" + s1 + " + " + s2 + ")";
			case mul -> "(" + s1 + " * " + s2 + ")";
			case div -> "(" + s1 + " / " + s2 + ")";
			case mod -> "(" + s1 + " % " + s2 + ")";
//			case eql -> "((" + s1 + " == " + s2 + ")? 1:0)";
			case eql -> s1 + " == " + s2;

			default -> throw new RuntimeException("unknown op: " + op);
			};
		}
	}

	static class Const extends Expr {
		static final Const ZERO = new Const(0);
		static final Const ONE = new Const(1);

		int val;

		private Const(int i) {
			super();
			this.val = i;
		}

		boolean isOutside1to9() {
			return (val < 1 || val > 9);
		}

		static Const build(int i) {
			return (i == 0) ? ZERO : (i == 1) ? ONE : new Const(i);
		}

		@Override
		long eval(Long[] w) {
			return val;
		}

		@Override
		public String toText() {
			return Integer.toString(val);
		}

		Expr reduce() {
			return this;
		}

		@Override
		public String toString() {
			return toText();
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Const c && val == c.val);
		}
	}

	static class Input extends Expr {
		int number;

		public Input(int number) {
			super();
			this.number = number;
		}

		boolean isOutside1to9() {
			return false;
		}

		@Override
		long eval(Long[] w) {
			return w[number];
		}

		@Override
		public String toText() {
			return "w[" + number + "]";
		}

		Expr reduce() {
			return this;
		}

		@Override
		public String toString() {
			return toText();
		}

	}

	static class Var extends Expr {
		String name;

		public Var(String name) {
			super();
			this.name = name;
		}

		boolean isOutside1to9() {
			return false;
		}

		@Override
		public String toText() {
			return name;
		}

		Expr reduce() {
			return this;
		}

		@Override
		public String toString() {
			return toText();
		}

	}


	void doSegments() {
		var remaining = input;
		current = current.put('w', Const.ZERO);
		current = current.put('x', Const.ZERO);
		current = current.put('y', Const.ZERO);
		current = current.put('z', Const.ZERO);

		List<Expr> stack = List.empty();

		while (remaining.nonEmpty()) {
			var n = remaining.indexOfOption("inp w", 1).getOrElse(remaining.size());
			var s1 = remaining.take(n);
			remaining = remaining.drop(n);
			s1.forEach(Expr::parse);

			Expr w = current.get('w').get();
			Expr x = current.get('x').get();
			Expr y = current.get('y').get();
			Expr z = current.get('z').get();

			if (Const.ONE.equals(x)) {
				System.out.println("push: " + y.toText());
				stack = stack.push(y);
			} else {
				var c = x.arg1;
//				System.out.println("pop if " + c.toText());
				c.arg1.arg1 = stack.head();
				System.out.println("pop: " + c.reduce().toText());
				stack = stack.pop();
			}

//			System.out.println("w = " + w.toText());
//			System.out.println("x = " + x.toText());
//			System.out.println("y = " + y.toText());
//			System.out.println("z = " + z.toText());

			current = current.put('w', new Var("w"));
			current = current.put('x', new Var("x"));
			current = current.put('y', new Var("y"));
			current = current.put('z', new Var("z"));
		}
	}

	void solve() {
		System.out.println("=== segments");
		doSegments();

		current = current.put('w', Const.ZERO);
		current = current.put('x', Const.ZERO);
		current = current.put('y', Const.ZERO);
		current = current.put('z', Const.ZERO);

//		input.forEach(s -> System.out.println(Cmd.toJava(s)));
//		System.out.println("---");

		inputNr = 0;
		input.forEach(Expr::parse);
		var monad = current.get('z').get();

		System.out.println("z => " + monad.toText());

		System.out.println("=== part 1");
		findSerial(monad, true);

		System.out.println("=== part 2");
		findSerial(monad, false);
	}

	private void findSerial(Expr monad, boolean max) {
		var nums = List.rangeClosed(1L, 9L);
		if (max)
			nums = nums.reverse();
		var inputs = nums.crossProduct(7);

		for (var in : inputs) {
			Long[] w = new Long[14];

			int i = 0;

			// 99691891979938 27141191213911
			w[0] = in.get(i++);
			w[1] = in.get(i++);
			w[2] = in.get(i++);
			w[3] = w[2] + 3;
			w[4] = in.get(i++);
			w[5] = in.get(i++);
			w[6] = in.get(i++);
			w[7] = w[6] - 8;
			w[8] = w[5] + 1;
			w[9] = in.get(i++);
			w[10] = w[9] + 2;
			w[11] = w[4] + 8;
			w[12] = w[1] - 6;
			w[13] = w[0] - 1;

//			// 51939397989999 11717131211195
//			w[0] = in.get(i++);
//			w[1] = in.get(i++);
//			w[2] = in.get(i++);
//			w[3] = in.get(i++);
//			w[4] = w[3] + 6;
//			w[5] = w[2] - 6;
//			w[6] = in.get(i++);
//			w[7] = w[6] - 2;
//			w[8] = in.get(i++);
//			w[9] = w[8] - 1;
//			w[10] = in.get(i++);
//			w[11] = w[10];
//			w[12] = w[1] + 8;
//			w[13] = w[0] + 4;
			
			if (Array.of(w).forAll(x -> x >= 1 && x <= 9)) {
				var valid = monad.eval(w);
				if (valid == 0L) {
					System.out.println(Array.of(w).mkString());
					return;
				}
			}
		}
	}

	public static void main(String[] args) {
		new Day24().solve(); // 99691891979938 27141191213911
	}

	static String example = """
			inp z
			inp x
			mul z 3
			eql z x						""";

}
