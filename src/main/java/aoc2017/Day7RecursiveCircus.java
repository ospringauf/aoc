package aoc2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7RecursiveCircus {

	private static Map<String, List<String>> stacked;
	private static Map<String, Integer> weight;

	public static void main(String[] args) throws Exception {
		List<String> l = Util.lines("../src/aoc2017/day7-test.txt");
		
		// fwft (72) -> ktlj, cntj, xhth
		//Pattern.compile("(\\w+) \\((\\d+)\\)( -> ((\\w)( ,)?)+)?");
		
		stacked = new HashMap<>();
		weight = new HashMap<>();
		
		for (String s: l) {
			String p1 = s.split(" ")[0];
			
			int w = Integer.parseInt(s.split(" \\(")[1].split("\\)")[0]);
			weight.put(p1, w);
			
			if (s.contains(" -> ")) {
				List<String> pp = Arrays.asList(s.split(" -> ")[1].split(", "));
				stacked.put(p1, pp);
			}					
		}
		
		List<String> right = stacked.values().stream().flatMap(v -> v.stream()).collect(Collectors.toList());
		Optional<String> bottom = stacked.keySet().stream().filter(k -> !right.contains(k)).findFirst();
		
		System.out.println(bottom);
		
		for (String p : stacked.keySet()) {
			for (String pp : stacked.get(p)) {
				System.out.println(p + " - " + pp + " " + sumWeight(pp));
			}
		}
		
	}
	
	static int sumWeight(String s) {
		int w = weight.get(s);
		if (stacked.containsKey(s))
			w += stacked.get(s).stream().mapToInt(p -> sumWeight(p)).sum();
		return w;
	}
}
