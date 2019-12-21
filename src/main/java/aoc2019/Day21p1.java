package aoc2019;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Day 21: Springdroid Adventure
 * https://adventofcode.com/2019/day/21
 *
 */
public class Day21p1 {

	static final long[] PROGRAM = Util.readIntProg("input21.txt");
	Springdroid droid;

	static class Springdroid extends IntComputer {

		private List<Integer> data;

		public Springdroid(long[] program, String cmd, String... script) {
			super(program);

			String s = "";
			int i = 0;
			while (!script[i].isBlank())
				s += script[i++] + "\n";

			data = (s + cmd + "\n").chars().boxed().collect(Collectors.toList());
			input = () -> data.remove(0);
		}

		@Override
		void output(long value) {
			if (value > 255) {
				System.out.println(value);
			} else {
				System.out.print((char) value);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		System.out.println("=== part 1 ===");
		new Day21p1().part1();

		System.out.println("=== part 2 ===");
		new Day21p1().part2();

		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}

	void part1() throws Exception {
		String[] script = Util.linesArray("jumpscript1.txt");
		droid = new Springdroid(PROGRAM, "WALK", script);
		droid.run();
	}

	int ahead = 9;

	void part2() throws Exception {
		String[] script = Util.linesArray("jumpscript2.txt");
		droid = new Springdroid(PROGRAM, "RUN", script);
		droid.run();

		for (int p = 0; p < (1 << ahead); ++p) {
			if (solve(p,0) && jump(p)) 
			print(p);
		}

	}

	boolean solve(int p, int i) {
		return (i >= ahead) || 
				(ground(p, i) && solve(p, i + 1)) || // dont jump
				(ground(p, i + 3) && solve(p, i + 4)); // jump
	}

	boolean jump(int p) {
		// return (ground(p, 3) && solve(p, 4)) && !(ground(p,1) && solve(p, 1)); //
		// must jump
		return (ground(p, 3) && solve(p, 4)); // can jump
	}

	boolean mustJump(int p) {
		return (ground(p, 3) && solve(p, 4)) && !(ground(p, 0) && solve(p, 1)); // must jump
	}

	boolean ground(int p, int i) {
		return (i >= ahead) || (p & (1 << i)) > 0;
	}

	void print(int p) {
		System.out.printf("%4d ", p);
		for (int i = 0; i < ahead; ++i)
			System.out.print(ground(p, i) ? "#" : ".");
		System.out.println("  " + solve(p, 0) + "  " + jump(p) + "  " + mustJump(p));
//		System.out.println("  " + solve(p,0));
	}

}
