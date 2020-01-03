package aoc2019;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Day 22: Slam Shuffle
 * https://adventofcode.com/2019/day/22
 *
 */
public class Day22 {

	static final String[] ex0 = { "deal with increment 7" };
	static final String[] ex1 = { "cut 6", "deal with increment 7", "deal into new stack" };
	static int LEN1 = 10007;
	static long L = 119315717514047L;
	static long ITER = 101741582076661L;
//	static long L = LEN1;
//	static long ITER = 1;
	static BigInteger BL = BigInteger.valueOf(L);
	static int MAX1 = LEN1 - 1;


	static class Mat {

		BigInteger a;
		BigInteger b;

		public Mat(long a, long b) {
			this.a = BigInteger.valueOf(a);
			this.b = BigInteger.valueOf(b);
		}

		public Mat(BigInteger a, BigInteger b) {
			this.a = a;
			this.b = b;
		}

		Mat mult(Mat m) {
			return new Mat(a.multiply(m.a).mod(BL), a.multiply(m.b).add(b).mod(BL));
		}

		long transform(long i) {
			var t = a.multiply(BigInteger.valueOf(i)).add(b).mod(BL);
			return t.longValue();
		}

		Mat pot(long n) {
			if (n == 1)
				return this;
			var mh = pot(n / 2L);
			if (n % 2 == 0) {
				return mh.mult(mh);
			} else {
				return mh.mult(mh).mult(this);
			}
		}
	}
	
	long pow(long a, long b, long mod) {
		if (b == 1) return a % mod;
		var p = BigInteger.valueOf(pow(a, b/2, mod));
		if (b % 2 == 0)
			return p.multiply(p).mod(BL).longValue();
		else
			return p.multiply(p).multiply(BigInteger.valueOf(a)).mod(BL).longValue();
	}


	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("=== part 1 test ===");
//		new Day22().part1(ex0);
		new Day22().part1(Util.linesArray("input22_1a.txt"));

		// 4485
		System.out.println("=== part 1 ===");
		new Day22().part1(Util.linesArray("input22.txt"));

		// 91967327971097
		System.out.println("=== part 2 ===");
		new Day22().part2(Util.linesArray("input22.txt"), true);

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}

	void part1(String[] deal) {
		var l = IntStream.rangeClosed(0, MAX1).boxed().collect(Collectors.toList());
		for (String s : deal) {
			if (s.startsWith("cut ")) {
				l = cut(l, Integer.parseInt(s.substring("cut ".length())));
			} else if (s.startsWith("deal with increment ")) {
				l = increment(l, Integer.parseInt(s.substring("deal with increment ".length())));
			} else if (s.startsWith("deal into new stack")) {
				l = reverse(l);
			}
		}
		System.out.println(l);
		System.out.println(l.indexOf(2019));
	}

	
	void part2(String[] deal0, boolean invert) {
		var m = new Mat(1, 0);
		
		var deal = new ArrayList<String>();
		if (invert)
			for (int i = deal0.length-1; i>=0; --i) deal.add(deal0[i]);
		else
			for (int i = 0; i < deal0.length; ++i) deal.add(deal0[i]);


		for (String s : deal) {
			if (s.startsWith("cut ")) {
				var x = Integer.parseInt(s.substring("cut ".length()));
				var t = invert ? new Mat(1, L + x) : new Mat(1, L - x);
				m = t.mult(m);
			} else if (s.startsWith("deal with increment ")) {
				var x = Integer.parseInt(s.substring("deal with increment ".length()));
				var f = pow(x, L-2, L); // fermat: "division" i/x == i*x^(L-2) [mod L]
				var t = invert ? new Mat(f, 0) : new Mat(x, 0);
				m = t.mult(m);
			} else if (s.startsWith("deal into new stack")) {
				var t = invert ? new Mat(-1, L - 1) : new Mat(-1, L - 1);
				m = t.mult(m);
			}
		}

		System.out.printf("m100 = %d, %d%n", m.a, m.b);

		m = m.pot(ITER);
		System.out.printf("mn = %d, %d%n", m.a, m.b);

		long n = 0;
		System.out.printf("%d -> %d%n", n, m.transform(n));

		n = 1;
		System.out.printf("%d -> %d%n", n, m.transform(n));
		
		n = 2019;
		System.out.printf("%d -> %d%n", n, m.transform(n));

		n = 2020;
		System.out.printf("%d -> %d%n", n, m.transform(n));

		n = 4485;
		System.out.printf("%d -> %d%n", n, m.transform(n));
		
	}


	List<Integer> reverse(List<Integer> l) {
		var r = emptyList();
		for (int i = 0; i <= MAX1; ++i)
			r.set(LEN1 - 1 - i, l.get(i));
		return r;
	}

	List<Integer> increment(List<Integer> l, int inc) {
		var r = emptyList();
		for (int i = 0; i <= MAX1; ++i) {
			r.set((i * inc) % LEN1, l.get(i));
		}
		return r;
	}

	List<Integer> cut(List<Integer> l, int c) {
		var r = emptyList();
		for (int i = 0; i <= MAX1; ++i)
			r.set((i - c + LEN1) % LEN1, l.get(i));
		return r;
	}

	ArrayList<Integer> emptyList() {
		var r = new ArrayList<Integer>(LEN1);
		for (int i = 0; i <= MAX1; ++i)
			r.add(-1);
		return r;
	}
}
