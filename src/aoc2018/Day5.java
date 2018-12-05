package aoc2018;

import java.util.OptionalInt;
import java.util.Stack;
import java.util.stream.IntStream;

public class Day5 {
    static String input = "dabAcCaCBAcCcaDA";

	public static void main(String[] args) throws Exception {
	    input = Util.lines("aoc2018/day5.txt").get(0);
		part2();
	}

	private static int react(String input) {
        int offset = 'a'-'A';
        Stack<Integer> s = new Stack<>();
        input
            .chars()
            .forEach(c -> { 
                if (!s.empty() && Math.abs(s.peek() - c) == offset) 
                    s.pop(); // destroy
                else 
                    s.push(c); // keep
            });
        return s.size();
    }
	
	private static void part1() throws Exception {
	    System.out.println(react(input));
	}

	private static void part2() throws Exception {
	    OptionalInt min = IntStream
	        .rangeClosed('a', 'z')
	        .mapToObj(c -> "[" + (char)c + Character.toString((char)Character.toUpperCase(c)) + "]") // pattern [aA]
	        .map(pattern -> input.replaceAll(pattern, "")) // remove all aA
	        .mapToInt(Day5::react)
	        .min()
	        ;
	    
	    System.out.println(min.getAsInt());
	}

}
