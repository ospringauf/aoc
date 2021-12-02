package aoc2017;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 18: Duet ---
// https://adventofcode.com/2017/day/18

class Day18 extends AocPuzzle {

	void part1() {
//		var data = List.of(example.split("\n"));
		var data = file2lines("input18.txt");

		var regs = new HashMap<String, Long>();
		long sound = 0;
		Long recovered = null;

		int idx = 0;
		while (recovered == null) {
			String s = data.get(idx);
			var a = s.split("\\s+");
//			System.out.println(idx + " " + s + " sound=" + sound);
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
			case "add" -> regs.put(reg, regs.getOrDefault(reg, 0L) + p);
			case "mul" -> regs.put(reg, regs.getOrDefault(reg, 0L) * p);
			case "mod" -> regs.put(reg, regs.getOrDefault(reg, 0L) % p);
			case "snd" -> sound = v;
			case "rcv" -> {
				if (v != 0L)
					recovered = sound;
			}
			case "jgz" -> {
				if (v > 0L)
					idx = (int) (idx + p - 1);
			}
			default -> {}
			}
			idx++;

		}

		System.out.println(recovered);
	}

	void run2(int id, BlockingQueue<Long> in, BlockingQueue<Long> out, List<String> data) {
		try {
			int count = 0;
			var regs = new HashMap<String, Long>();
			regs.put("p", (long) id);
			long sound = 0;
			Long recovered = null;

			int idx = 0;
			while (recovered == null) {
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
				case "add" -> regs.put(reg, regs.getOrDefault(reg, 0L) + p);
				case "mul" -> regs.put(reg, regs.getOrDefault(reg, 0L) * p);
				case "mod" -> regs.put(reg, regs.getOrDefault(reg, 0L) % p);
				case "snd" -> {
					out.offer(v, 3, TimeUnit.SECONDS);
					count++;
					if (id == 1)
						System.out.println(id + " #" + count + " --> " + v);
				}
				case "rcv" -> regs.put(reg, in.poll(3, TimeUnit.SECONDS));
				case "jgz" -> {
					if (v > 0L)
						idx = (int) (idx + p - 1);
				}
				default -> {}
				}
				idx++;

			}

			System.out.println(recovered);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void part2() {
//		var data = List.of(example2.split("\n"));
		var data = file2lines("input18.txt");
		var q01 = new ArrayBlockingQueue<Long>(100);
		var q10 = new ArrayBlockingQueue<Long>(100);

		new Thread(() -> run2(0, q10, q01, data)).start();
		new Thread(() -> run2(1, q01, q10, data)).start();

	}

	void test() {

	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day18().test();

		System.out.println("=== part 1"); // 4601
		new Day18().part1();

		System.out.println("=== part 2"); // 6858
		new Day18().part2();
	}

	static String example = """
			set a 1
			add a 2
			mul a a
			mod a 5
			snd a
			set a 0
			rcv a
			jgz a -1
			set a 1
			jgz a -2
						""";

	static String example2 = """
			snd 1
			snd 2
			snd p
			rcv a
			rcv b
			rcv c
			rcv d
			""";
}
