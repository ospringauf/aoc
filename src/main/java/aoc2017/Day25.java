package aoc2017;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 25: The Halting Problem ---
// https://adventofcode.com/2017/day/25

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day25 extends AocPuzzle {

	record Rule(int curr, int write, int move, Character nextState) {
	}

	record State(Character name, Rule rule0, Rule rule1) {
		Rule rule(int curr) {
			return (curr == 0) ? rule0 : rule1;
		}
	}

	void part1() {
		var stateA = new State('A', new Rule(0, 1, 1, 'B'), new Rule(1, 0, 1, 'F'));
		var stateB = new State('B', new Rule(0, 0, -1, 'B'), new Rule(1, 1, -1, 'C'));
		var stateC = new State('C', new Rule(0, 1, -1, 'D'), new Rule(1, 0, 1, 'C'));
		var stateD = new State('D', new Rule(0, 1, -1, 'E'), new Rule(1, 1, 1, 'A'));
		var stateE = new State('E', new Rule(0, 1, -1, 'F'), new Rule(1, 0, -1, 'D'));
		var stateF = new State('F', new Rule(0, 1, 1, 'A'), new Rule(1, 0, -1, 'E'));
		int checksum = 12425180;
		var states = List.of(stateA, stateB, stateC, stateD, stateE, stateF).toMap(s -> s.name, s -> s);
		runTuring(stateA, states, checksum);
	}

	java.util.Map<Integer, Integer> tape = new java.util.HashMap<>();

	void test() {
		var stateA = new State('A', new Rule(0, 1, 1, 'B'), new Rule(1, 0, -1, 'B'));
		var stateB = new State('B', new Rule(0, 1, -1, 'A'), new Rule(1, 1, 1, 'A'));
		var states = List.of(stateA, stateB).toMap(s -> s.name, s -> s);
		runTuring(stateA, states, 6);
	}

	void runTuring(State state, Map<Character, State> states, int checksum) {
		int cursor = 0;
		for (int i = 0; i < checksum; ++i) {
			var r = state.rule(tape.getOrDefault(cursor, 0));
			tape.put(cursor, r.write);
			cursor += r.move;
			state = states.get(r.nextState).get();
		}

		System.out.println(List.ofAll(tape.values()).sum());
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day25().test();

		System.out.println("=== part 1"); // 3099
		new Day25().part1();
	}

	static String example = """
			Begin in state A.
			Perform a diagnostic checksum after 6 steps.

			In state A:
			  If the current value is 0:
			    - Write the value 1.
			    - Move one slot to the right.
			    - Continue with state B.
			  If the current value is 1:
			    - Write the value 0.
			    - Move one slot to the left.
			    - Continue with state B.

			In state B:
			  If the current value is 0:
			    - Write the value 1.
			    - Move one slot to the left.
			    - Continue with state A.
			  If the current value is 1:
			    - Write the value 1.
			    - Move one slot to the right.
			    - Continue with state A.
						""";

}
