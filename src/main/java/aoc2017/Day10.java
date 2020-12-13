package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// https://adventofcode.com/2017/day/x

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day10 extends AocPuzzle {

	static class Element {
		public Element(int i) {
			this.number = i;
		}

		public int number;
		public Element next;

		public List<Integer> reverse(int i, List<Integer> behind) {
			if (i == 0)
				return behind;
			else {
				var l = next.reverse(i - 1, behind.append(number));
				number = l.head();
				return l.tail();
			}
		}

		public Element fwd(int i) {
			return i == 0 ? this : next.fwd(i - 1);
		}

		public void reverse(int i) {
			reverse(i, List.empty());
		}
		
		static Element buildCyclicList(int size) {
			var e0 = new Element(0);
			var e = e0;
			for (int i = 1; i < size; ++i) {
				var n = new Element(i);
				e.next = n;
				e = n;
			}
			e.next = e0;
			return e0;
		}
		
		void printAll() {
			var e = this;
			do {
				System.out.print(e.number + " ");
				e = e.next;
			} while (e != this);
			System.out.println();
		}
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day10().test();

		System.out.println("=== part 1"); // 52070
		new Day10().part1();

		System.out.println("=== part 2"); // 7f94112db4e32e19cf6502073c66f9bb
		new Day10().part2();
	}

	final String input = "46,41,212,83,1,255,157,65,139,52,39,254,2,86,0,204";
	
	private void test() {
		int[] lengths = { 3, 4, 1, 5 };
		var e0 = Element.buildCyclicList(5);

		e0.printAll();

		int skip = 0;
		var e = e0;

		for (int l : lengths) {
			e.reverse(l);
			e = e.fwd(l + skip);
			e0.printAll();
			skip++;
		}

		var x = List.of(65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22).reduce((a,b)->a^b);
		System.out.println(x);
	}


	void part1() {
		List<Integer> lengths = List.of(input.split(",")).map(s -> Integer.valueOf(s));
		var e0 = Element.buildCyclicList(256);

		var e = e0;
		int skip = 0;

		for (int l : lengths) {
			e.reverse(l);
			e = e.fwd(l + skip);
			skip++;
		}

		System.out.println(e0.number * e0.next.number);
	}

	

	void part2() {
//		var input = "1,2,3";
//		var input = "AoC 2017";
		
		List<Integer> lengths = List.ofAll(input.toCharArray()).map(c -> (int) c);
		lengths = lengths.appendAll(List.of(17, 31, 73, 47, 23));

//		System.out.println(lengths);

		var e0 = Element.buildCyclicList(256);

		var e = e0;
		int skip = 0;

		for (int i = 0; i < 64; ++i) {
			for (int l : lengths) {
				e.reverse(l);
				e = e.fwd(l + skip);
				skip++;
			}
		}
		var r = Stream.iterate(e0, x->x.next).map(x->x.number).take(256).toList();
//		System.out.println(r);
		var r2 = r.sliding(16, 16).map(l -> l.reduce((a,b) -> a ^ b)).toList();
//		System.out.println(r2);
		
		var hex = r2.map(i -> String.format("%02x", i));
//		System.out.println(hex);
		System.out.println(hex.mkString());
	}

}
