package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 25: Combo Breaker ---
// https://adventofcode.com/2020/day/25

// Diffe-Hellman key exchange, public/private keys

// calculate modular power and modular logarithm (!) 
// numbers are small -> brute force search will do 

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day25 extends AocPuzzle {

	private static final long M = 20201227;

	/**
	 * O(n) naive implementation 
	 */
	long modPowNaive(final long subject, final long n) {
		long value = 1L;
		for (long i = 0; i < n; ++i) {
			value = (value * subject) % M;
		}
		return value;
	}

	/**
	 * O(log n) implementation
	 */
	long modPow(final long subject, final long n) {
		// could also use
//		 return BigInteger.valueOf(subject).modPow(BigInteger.valueOf(loops), BigInteger.valueOf(N)).longValue();

		if (n == 0)
			return 1L;
		var mp2 = modPow(subject, n / 2);
		var mp = (mp2 * mp2) % M;
		if (n % 2 == 1)
			mp = (mp * subject) % M;
		return mp;
	}

	/**
	 * discrete logarithm, brute force search
	 * 
	 * alternatives:
	 * https://programmingpraxis.com/2016/05/06/baby-steps-giant-steps/
	 * https://programmingpraxis.com/2016/05/27/pollards-rho-algorithm-for-discrete-logarithms/
	 */
	long modLog(final long n, final long subject) {
		long value = 1;
		long loops = 0;
		while (value != n) {
			loops++;
			value = (value * subject) % M;
		}
		return loops;
	}

	void part1(long cardPub, long doorPub) {

		Long doorLoop = modLog(doorPub, 7);
		Long cardLoop = modLog(cardPub, 7);

		System.out.println("door loops: " + doorLoop);
		System.out.println("card loops: " + cardLoop);

//		long encKey = modPow(cardPub, doorLoop);
		long encKey = modPow(doorPub, cardLoop);
		System.out.println("=> enc key: " + encKey);
	}

	void test() {
		System.out.println(List.range(1, 20).map(n -> modPowNaive(7, n)).mkString(" "));
		System.out.println(List.range(1, 20).map(n -> modPow(7, n)).mkString(" "));

		var keys = List.range(1, 20).map(n -> modPow(7, n));
		System.out.println(keys.map(n -> modLog(n, 7)).mkString(" "));

		long cardPub = 5764801;
		long doorPub = 17807724;

		long doorLoop = modLog(doorPub, 7L);
		long cardLoop = modLog(cardPub, 7L);

		assertEquals(8, cardLoop);
		assertEquals(11, doorLoop);

		long encKey = modPow(cardPub, doorLoop);
		assertEquals(14897079L, encKey);
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day25().test();

		System.out.println("=== part 1"); // door loop: 3794585 enc key: 5025281
		new Day25().part1(10943862, 12721030);
	}

}
