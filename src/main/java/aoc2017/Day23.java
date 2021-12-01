package aoc2017;

import java.util.HashMap;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 23: Coprocessor Conflagration ---
// https://adventofcode.com/2017/day/23

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day23 extends AocPuzzle {

	List<String> data = file2lines("input23.txt");

	void part1() {
		int count = 0;
		var regs = new HashMap<String, Long>();
		long h0=0L;

		int idx = 0;
		while (idx < data.size()) {
			String s = data.get(idx);
			var a = s.split("\\s+");
			var op = a[0];
			var reg = a[1];

			long p = 0;
			if (a.length > 2) {
				if (Character.isAlphabetic(a[2].charAt(0)))
					p = regs.getOrDefault(a[2], 0L);
				else
					p = Integer.parseInt(a[2]);
			}
			
			Long v = regs.getOrDefault(reg, 0L);
			if (!Character.isAlphabetic(reg.charAt(0))) {
				v = Long.parseLong(reg);
			}

			switch (op) {
			case "set" -> regs.put(reg, p);
			case "sub" -> regs.put(reg, regs.getOrDefault(reg, 0L) - p);
			case "mul" -> {
				regs.put(reg, regs.getOrDefault(reg, 0L) * p);
				count++;
			}
			case "jnz" -> {
				if (v != 0L)
					idx = (int) (idx + p - 1);
			}
			default -> {}
			}
			idx++;
			
			long h = regs.getOrDefault("h", 0L);
			if (h!=h0 ) {
				System.out.println(count + " -> " + h);
				h0=h;
			}

		}
		System.out.println(count);

	}

	void part2() {
		int h=0;
		for (int b=109900; b<=109900+17000; b+=17) {
			boolean f = false;
			for (int d=2; d<b; ++d)
				f |= (b%d == 0);
			if (f) h++;
		}
		System.out.println(h);
	}

	public static void main(String[] args) {
	
		System.out.println("=== part 1"); // 9409
		new Day23().part1();
		
		System.out.println("=== part 2"); // 913
		new Day23().part2();
	}

}
