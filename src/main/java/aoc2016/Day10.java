package aoc2016;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 10: Balance Bots ---
// https://adventofcode.com/2016/day/10

class Day10 extends AocPuzzle {

	Map<Integer, List<Integer>> botChips = new HashMap<>();
	Map<Integer, List<Integer>> output = new HashMap<>();
	
	void solve() {
		var instr = file2lines("day10.txt");
		process(instr.filter(s -> s.startsWith("value")));
		
		for (int i=0; i<100; ++i) {
			process(instr.filter(s -> ! s.startsWith("value")));
		}
		
		System.out.println(List.range(0, 3).flatMap(i -> output.get(i)).product());
	}

	private void process(List<String> instr) {
		for (var i : instr) {
			var r = split(i, " ");
			switch (r.s(0)) {
			case "bot" -> {
				// bot 2 gives low to bot 1 and high to bot 0
				var bot = r.i(1);
				var lowTo = r.s(5);
				var low = r.i(6);
				var high = r.i(11);
				var highTo = r.s(10);
				
				var chips = botChips.getOrDefault(bot, List.empty());
				
				if (chips.length() == 2) {
					if (chips.contains(61) && chips.contains(17)) {
						System.out.println("bot comparing 61 and 17: " + bot);
					}
					var lowTgt = ("bot".equals(lowTo)) ? botChips : output;
					lowTgt.put(low, lowTgt.getOrDefault(low, List.empty()).append(chips.min().get()));
					var highTgt = ("bot".equals(highTo)) ? botChips : output;
					highTgt.put(high, highTgt.getOrDefault(high, List.empty()).append(chips.max().get()));
					botChips.put(bot, List.empty());
				}						
			}
			case "value" -> {
				// value 29 goes to bot 7
				var val = r.i(1);
				var bot = r.i(5);
				botChips.put(bot, botChips.getOrDefault(bot, List.empty()).append(val));
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + r.s(0));
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day10().solve());
	}
}
