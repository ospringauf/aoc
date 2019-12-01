package aoc2018;

import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) throws Exception {
        part1();
    }
    
    private static void part1() throws Exception {
    	List<Day3Claim> claims = Util.lines("aoc2018/day3.txt").stream().map(x -> new Day3Claim(x)).collect(Collectors.toList());
    	
    	int result = 0;
    	for (int x=0; x<1000; ++x) {
    		for (int y=0; y<1000; ++y) {
    			final int px=x;
    			final int py=y;
    			long s = claims.stream().filter(c -> c.contains(px, py)).count();
    			if (s>1)
    				result++;
    		}
    	}
    	System.out.println(result);
    	
    }

    private static void part2() throws Exception {
    	List<Day3Claim> claims = Util.lines("aoc2018/day3.txt").stream()
    			.map(x -> new Day3Claim(x))
    			.collect(Collectors.toList());
    	
    	for (Day3Claim c : claims) {
    		
    		if (claims.stream()
    				.filter(x -> !x.equals(c))
    				.noneMatch(x -> x.overlap(c)))
    			System.out.println(c);
    	}
    	
    }
    
}
