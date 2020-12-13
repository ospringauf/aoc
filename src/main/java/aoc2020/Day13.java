package aoc2020;

import java.math.BigInteger;

import common.AocPuzzle;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// --- Day 13: Shuttle Search ---
// https://adventofcode.com/2020/day/13

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day13 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 3215
		new Day13().part1();

		System.out.println();
		System.out.println("=== part 2"); // 1001569619313439
		new Day13().part2();
	}

	List<String> data = List.of(input.split("\n"));
//	List<String> data = List.of(example.split("\n"));

	void part1() {
		var arrival = Integer.valueOf(data.get(0));
		String[] times = data.get(1).split(",");
		
		List<Integer> bus = List.of(times)
				.filter(s -> !s.equals("x"))
				.map(Integer::valueOf)
				.toList();

		var depart = Stream.from(arrival).dropWhile(m -> !bus.exists(b -> m % b == 0)).get();

		var myBus = bus.filter(b -> depart % b == 0).get();
		System.out.println("depart = " + depart);
		System.out.println("result = " + (depart - arrival) * myBus);
	}

	void part2() {

		// Chinese remainder solver
		// https://www.dcode.fr/chinese-remainder
		
//		var line1 = "17,x,13,19";
//		var line1 = "7,13,x,x,59,x,31,19";
//		var line1 = "1789,37,47,1889";
		var line1 = data.get(1);

		var times = line1.split(",");
		List<Integer> bus = List.of(times).map(s -> "x".equals(s) ? 0 : Integer.valueOf(s));

		var an = List.range(0, bus.size())
				.filter(i -> bus.get(i) != 0)
				.map(i -> Tuple.of((bus.get(i) - i) % bus.get(i), bus.get(i)));
		
		System.out.println("coeff (a,n) = " + an);

		System.out.println(" T === Ai mod Ni");
		an.forEach(x -> System.out.println(" t === " + x._1 + " mod " + x._2));

		var N = an.map(x -> x._2).product().longValue();
		System.out.println("N = " + N);
		
//		var gcd = coeff.map(c -> Util.gcd(c._2, N/c._2));
//		System.out.println("gcd = " + gcd); // all 1s because departures are all prime --> special case!
		
		var t = an.map(x -> x._1 * (N/x._2) * modInv((N/x._2), x._2)).sum().longValue() % N;
		System.out.println("t = " + t);
	}

	long modInv(long a, long n) {
		return BigInteger.valueOf(a).modInverse(BigInteger.valueOf(n)).longValue();
	}


	static String example = """
			939
			7,13,x,x,59,x,31,19
						""";

	static String input = """
			1015292
			19,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,743,x,x,x,x,x,x,x,x,x,x,x,x,13,17,x,x,x,x,x,x,x,x,x,x,x,x,x,x,29,x,643,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,23
						""";

}
