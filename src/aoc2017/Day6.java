package aoc2017;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {

    public static void main(String[] args) throws Exception {
        part1();
    }
    
    private static void part1() throws Exception {
//    	String input = "0 2 7 0";
    	String input = "10	3	15	10	5	15	5	15	9	2	5	8	5	2	3	6";

    	
    	List<Integer> banks = Util.splitLine(input).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
    	
    	// string representation of the banks
    	Function<List<Integer>, String> rep = (l) -> l.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" "));
    	
    	HashMap<String, Integer> seen = new HashMap<String, Integer>();
    	String cfg = rep.apply(banks);
    	
    	int cycle = 0;
    	while (!seen.containsKey(cfg)) {
    		
    		seen.put(cfg, cycle);
    		int b = IntStream.range(0, banks.size()) // bank indexes
    				.mapToObj(i -> i)
    				.sorted((b1, b2) -> banks.get(b2) - banks.get(b1)) // fullest bank first
    				.findFirst().get()
    				;
    		System.out.println(cfg + " --> pick " + b);
    		
    		Integer dist = banks.get(b); // blocks to re-distribute
    		banks.set(b, 0); // clear bank
    		
    		while (dist > 0) {
    			b = (b+1) % banks.size(); // round robin
    			banks.set(b, banks.get(b) + 1);
    			dist--;
    		}
    		cfg = rep.apply(banks); 
    		cycle++;
    	}
    	
    	System.out.println("at cycle " + cycle);
    	System.out.println("after " + (cycle - seen.get(cfg)) + " cycles");
    }

    private static void part2() throws Exception {
    }

  
    
}
