package aoc2018;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

	static class Day4Event {
		static int currentGuard;
		
		String day;
		int guard;
		int minute = -1;
		boolean awake;
		
		public Day4Event(String s) {
			if (s.contains("Guard")) {
				Matcher m = Pattern.compile("#(\\d+) ").matcher(s);
				m.find();
				currentGuard = Integer.parseInt(m.group(1));
				minute = 0;
				System.out.println(currentGuard);
			}

			day = s.substring(6, 11);
			minute = (minute == -1) ? Integer.parseInt(s.substring(15, 17)) : minute;
			guard = currentGuard;
			awake = !s.contains("asleep");
		}
	}

	public static void main(String[] args) throws Exception {
		part1();
	}

	private static void part1() throws Exception {
		List<Day4Event> l = 
				Util.lines("aoc2018/day4-test.txt")
				.stream()
				.sorted()
				.map(x -> new Day4Event(x))
				.collect(Collectors.toList());

		System.out.println(l);

	}

	private static void part2() throws Exception {

	}

}
