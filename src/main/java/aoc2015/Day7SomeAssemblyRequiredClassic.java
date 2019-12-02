package aoc2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc2017.Util;

public class Day7SomeAssemblyRequiredClassic {

	static class Gate {
		String name;
		String gateFunction;
		String in1, in2;

		static int valueOf(String input) {
			if (input.matches("\\d+")) {
				return Integer.parseInt(input);
			} else {
				return eval(input);
			}
		}

		int calc() {
			switch (gateFunction) {
			case "ID":
				return valueOf(in1);
			case "NOT":
				return ~valueOf(in1);
			case "AND":
				return valueOf(in1) & valueOf(in2);
			case "OR":
				return valueOf(in1) | valueOf(in2);
			case "LSHIFT":
				return valueOf(in1) << Integer.parseInt(in2);
			case "RSHIFT":
				return valueOf(in1) >> Integer.parseInt(in2);
			default:
				throw new IllegalArgumentException(gateFunction);
			}
		}

		Gate(String s) {
			name = s.split(" -> ")[1];
			String[] tokens = s.split(" -> ")[0].split(" ");

			if (tokens.length == 1) {
				in1 = tokens[0];
				gateFunction = "ID";
			} else if (tokens.length == 2) {
				// must be NOT
				in1 = tokens[1];
				gateFunction = tokens[0];
			} else if (tokens.length == 3) {
				in1 = tokens[0];
				in2 = tokens[2];
				gateFunction = tokens[1];

			}
		}
	}

	static Map<String, Gate> wire = new HashMap<>();
	static Map<String, Integer> result = new HashMap<>();

	public static void main(String[] args) throws Exception {
		List<String> l = Util.lines("../src/aoc2015/day7.txt");
		for (String s : l) {
			Gate w = new Gate(s);
			wire.put(w.name, w);
		}

		// part 2
//		wire.put("b", new Gate("16076 -> b"));

//		for (String sig: wire.keySet()) {
//			int result = eval(sig);
//			System.out.println(sig + " = " + ((result < 0) ? result + 65536 : result));
//		}

		String sig = "a";
		int result = eval(sig);
		System.out.println(sig + " = " + ((result < 0) ? result + 65536 : result));

	}

	static int eval(String s) {
		if (result.containsKey(s))
			return result.get(s);
		Gate w = wire.get(s);
		Integer r = w.calc();
		result.put(s, r);
		return r;
	}
}
