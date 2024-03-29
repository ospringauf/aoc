package aoc2020;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// --- Day 13: Shuttle Search ---
// https://adventofcode.com/2020/day/13

class Day13 extends AocPuzzle {

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 3215
		new Day13().part1();

		System.out.println();
		System.out.println("=== part 2 (crt)"); // 1001569619313439
		new Day13().part2();

		System.out.println();
		System.out.println("=== part 2 (alu)"); // 1001569619313439
		new Day13().part2alu();
	}

	List<String> data = List.of(input.split("\n"));
//	List<String> data = List.of(example.split("\n"));

	void part1() {
		var arrival = Integer.valueOf(data.get(0));
		String[] times = data.get(1).split(",");

		List<Integer> bus = List.of(times).filter(s -> !s.equals("x")).map(Integer::valueOf).toList();

		var depart = Stream.from(arrival).dropWhile(m -> !bus.exists(b -> m % b == 0)).get();

		var myBus = bus.filter(b -> depart % b == 0).get();
		System.out.println("depart = " + depart);
		System.out.println("result = " + (depart - arrival) * myBus);
	}

	void part2() {

		// Chinese Remainder Theorem
		// solver: https://www.dcode.fr/chinese-remainder

//		var line1 = "17,x,13,19";
//		var line1 = "7,13,x,x,59,x,31,19";
//		var line1 = "1789,37,47,1889";
		var line1 = data.get(1);

		Long[] bus = List.of(line1.split(",")).map(s -> "x".equals(s) ? 0 : Long.valueOf(s)).toJavaArray(Long.class);

		var an = List.range(0, bus.length).filter(i -> bus[i] != 0).map(i -> Tuple.of((10*bus[i] - i) % bus[i], bus[i]));

		System.out.println("modular congruences: t === a_i mod n_i");
		System.out.println("coeff (a,n) = " + an);
		an.forEach(x -> System.out.println(" t === " + x._1 + " mod " + x._2));

		var N = an.map(x -> x._2).product().longValue(); // product of all n_i
		System.out.println("N = " + N);

//		var gcd = an.map(c -> Util.gcd(c._2, N / c._2));
//		System.out.println("gcd = " + gcd); // all 1's because departures are all prime --> special case!

		// a_i * N/n_i * invMod(N/n_i, n_i)
		var t = an.map(x -> x._1 * (N / x._2) * Util.modInv(N / x._2, x._2)).sum().longValue() % N;
		System.out.println("t = " + t);
	}

	record Bus(long offset, long id) {};
	
	void part2a() {

//		var line1 = "17,x,13,19".split(",");
//		var line1 = "7,13,x,x,59,x,31,19".split(",");
//		var line1 = "1789,37,47,1889".split(",");
		String[] line1 = data.get(1).split(",");

		List<Bus> bus = List.range(0, line1.length)
				.filter(i -> ! "x".equals(line1[i]))
				.map(i -> new Bus(i, Long.valueOf(line1[i])));
		
		bus.forEach(b -> System.out.format("bus: %3d -> %3d  [%3d]\n", b.id, b.offset, b.offset % b.id));

//		long start = -19;
//		long step = 19 * 743 * 29 * 37;
		
		Bus bus0 = bus.get(0);

		List<Long> factors = bus.filter(b -> (b.offset % b.id) == bus0.id).map(d -> d.id);

		// meeting at minute 19 (bus0 id)
		long step = factors.product().longValue() * bus0.id;
		System.out.println("steps: n * " + step + " - " + bus0.id);
		
		Predicate<Long> found = t -> bus.forAll(b -> (t + b.offset) % b.id == 0);

		long t = 0 * step - bus0.id;
		while (!found.test(t)) {
			t += step;
		}
	
		System.out.println("t = " + t);
	}
	
	// ALu solution from https://github.com/alu82/adventofcode/blob/master/2020/13/day13.py
	void part2alu() {

        String[] line1 = data.get(1).split(",");

        List<Bus> bus = List.range(0, line1.length)
                .filter(i -> ! "x".equals(line1[i]))
                .map(i -> new Bus(i, Long.valueOf(line1[i])));
        
        long t = 0;
        long step = 1;
        
        for (var b : bus) {        
            while (t < b.id * step) {
            	// b departs at the correct minute?
                if ((t + b.offset) % b.id == 0) { 
                    step = step * b.id; // works for primes, else lcm(step, b.id)
                    break;
                } else {
                    t += step;
                }
            }            
        }
        
        System.out.println(t);
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
