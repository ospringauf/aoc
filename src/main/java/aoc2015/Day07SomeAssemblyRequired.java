package aoc2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

public class Day07SomeAssemblyRequired {

	static class Gate {
		String name;
		String in1, in2;

		IntSupplier gateFunction;

		static int valueOf(String input) {
			if (input.matches("\\d+")) {
				return Integer.parseInt(input);
			} else {
				return eval(input);
			}
		}
		

		Gate(String s) {
			name = s.split(" -> ")[1];
			String[] tokens = s.split(" -> ")[0].split(" ");

			if (tokens.length == 1) {
				in1 = tokens[0];
				gateFunction = () -> valueOf(in1);
			} else if (tokens.length == 2) {
				// must be NOT
				in1 = tokens[1];
				gateFunction = () -> ~ valueOf(in1);
			} else if (tokens.length == 3) {
				in1 = tokens[0];
				in2 = tokens[2];
				
				switch (tokens[1]) {
				case "AND":
					gateFunction = () -> valueOf(in1) & valueOf(in2);
					break;
				case "OR":
					gateFunction = () -> valueOf(in1) | valueOf(in2);
					break;
				case "LSHIFT":
					gateFunction = () -> valueOf(in1) << Integer.parseInt(in2);
					break;
				case "RSHIFT":
					gateFunction = () -> valueOf(in1) >> Integer.parseInt(in2);
					break;

				default:
					throw new IllegalArgumentException(s);
				}
			}
		}
	}

	static Map<String, Gate> wire = new HashMap<>();
	static Map<String, Integer> result = new HashMap<>();

	public static void main(String[] args) throws Exception {
	    List<String> l = Files.readAllLines(Paths.get("src/main/java/aoc2015/day07.txt"));
		for (String s : l) {
			Gate w = new Gate(s);
			wire.put(w.name, w);
		}
		
		// part 2
//		wire.put("b", new Gate("16076 -> b"));
//
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
		Integer r = w.gateFunction.getAsInt();
		result.put(s,  r);
		return r;
	}
}
