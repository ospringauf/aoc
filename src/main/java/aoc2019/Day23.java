package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Day 23: Category Six
 * https://adventofcode.com/2019/day/23
 *
 */
public class Day23 {

	static final long[] PROGRAM = Util.readIntProg("input23.txt");

	class NIC extends IntComputer {

		long myAdr;
		long noInput = 0;
		boolean exit = false;

		List<Long> outbuf = new ArrayList<>();
		List<Long> data = new ArrayList<>();

		NIC(long nicAddr, long[] program) {
			super(program);
			myAdr = nicAddr;
			data.add(myAdr);
			input = () -> supplyInput();
		}

		@Override
		boolean complete() {
			return exit;
		}

		synchronized long supplyInput() {
			if (!data.isEmpty()) {
				noInput = 0;
				return data.remove(0);
			}
			noInput++;
			return -1L;
		}

		boolean idle() {
			return data.isEmpty() && noInput > 100;
		}

		@Override
		synchronized void output(long value) {
			outbuf.add(value);
			if (outbuf.size() == 3) {
				int tgt = outbuf.get(0).intValue();
				var x = outbuf.get(1);
				var y = outbuf.get(2);
				outbuf.clear();

				System.out.printf("%d sends %d,%d to %d%n", myAdr, x, y, tgt);
				if (tgt == 255) {
//					System.out.println("--> " + y);
					nat.receive(x, y);
				} else {
					comp[tgt].receive(x, y);
				}
			}
		}

		synchronized void receive(Long x, Long y) {
			data.add(x);
			data.add(y);
		}
	}

	class NAT implements Runnable {
		Long x;
		Long y;
		Long lastY = null;

		synchronized void receive(Long x, Long y) {
			this.x = x;
			this.y = y;
			System.out.printf("--> NAT received %d,%d%n", x, y);
		}

		@Override
		public void run() {
			boolean done = false;
			while (!done) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				var networkIdle = Arrays.stream(comp).allMatch(c -> c.idle());
				if (networkIdle && x != null) {
					System.out.printf("<-- NAT sends %d,%d%n", x, y);
					comp[0].receive(x, y);
					done = y.equals(lastY);
					lastY = y;
					x = null;
					y = null;
				}
			}

			System.out.printf("NAT sent Y=%d twice%n", lastY);
			shutdown();
		}

		void shutdown() {
			for (var c : comp) c.exit = true;
		}
	}

	class NAT0 extends NAT {
		@Override
		synchronized void receive(Long x, Long y) {
			super.receive(x, y);
			shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		// 22829
		System.out.println("=== part 1 ===");
		new Day23().part1();

		// 15678
		System.out.println("=== part 2 ===");
		new Day23().part2();

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}

	NIC[] comp = new NIC[50];
	NAT nat;

	void part1() throws Exception {
		for (int i = 0; i < 50; ++i) {
			comp[i] = new NIC(i, PROGRAM);
			comp[i].run1();
		}
		
		nat = new NAT0();
		var threads = IntStream.range(0, 50).mapToObj(i -> new Thread(() -> comp[i].run())).collect(Collectors.toList());
		
		for (var t : threads) t.start();
		for (var t : threads) t.join();
	}

	void part2() throws Exception {
		for (int i = 0; i < 50; ++i) {
			comp[i] = new NIC(i, PROGRAM);
			comp[i].run1();
		}

		nat = new NAT();
		var natThread = new Thread(nat);
		natThread.start();
		IntStream.range(0, 50).forEach(i -> new Thread(() -> comp[i].run()).start());
		
		natThread.join();
	}
}
