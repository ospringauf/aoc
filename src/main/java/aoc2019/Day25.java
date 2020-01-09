package aoc2019;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.paukov.combinatorics3.Generator;

/*
 * Day 25: Cryostasis
 * https://adventofcode.com/2019/day/25
 *
 */
public class Day25 {

	static final long[] PROGRAM = Util.readIntProg("input25.txt");

	static final String[] ITEMS = { "spool of cat6", "space law space brochure", "asterisk", "jam", "shell",
			"astronaut ice cream", "space heater", "klein bottle" };

	class Droid extends IntComputer {

		List<Integer> data = new ArrayList<>();

		public Droid(long[] program) {
			super(program);
			this.input = this::supplyInput;
		}

		synchronized void command(String cmd) {
//			System.out.println("sending: " + cmd);
			cmd = cmd + "\n";
			cmd.chars().forEach(data::add);
			notify();
		}

		synchronized Long supplyInput() {
			try {
				if (data.isEmpty())
					wait();
				var d = data.remove(0);
				System.out.print((char)(int)d);
				return (long) d;
			} catch (Exception e) {
				e.printStackTrace();
				return (long) '\n';
			}
		}

		@Override
		void output(long value) {
			System.out.print((char) value);
		}
	}

	public static void main(String[] args) throws Exception {
		new Day25().part1();
	}

	void part1() throws Exception {
		var droid = new Droid(PROGRAM);
		new Thread(() -> droid.run()).start();

		// collect items
		Util.lines("walk25.txt").forEach(droid::command);

		// try combinations -->
		// asterisk + astronaut ice cream + space heater + klein bottle
		// 2105377
		for (var c : Generator.subset(ITEMS).simple()) {
			Arrays.stream(ITEMS).forEach(it -> droid.command("drop " + it));
			c.forEach(it -> droid.command("take " + it));
			droid.command("south");
		}

		var console = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			var cmd = console.readLine();
			droid.command(cmd);
		}
	}
}
