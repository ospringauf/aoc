package aoc2017;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 {

    public static void main(String[] args) throws Exception {
        part2();
    }
    
    private static void part1() throws Exception {
    	List<String> l = Util.lines("aoc2017/day4.txt");
    	
    	System.out.println(l.stream().filter(x -> valid1(x)).count());
    }

    private static void part2() throws Exception {
    	List<String> l = Util.lines("aoc2017/day4.txt");
    	
    	System.out.println(l.stream().filter(x -> valid2(x)).count());
    }

    private static boolean valid1(String x) {
    	List<String> l = Util.splitLine(x);
    	return l.size() == l.stream().distinct().count();
	}

    private static boolean valid2(String x) {
    	List<String> l = Util.splitLine(x);
    	for (int i=0; i<l.size(); ++i)
    		for (int j=i+1; j<l.size(); ++j)
    			if (anagram(l.get(i), l.get(j)))
    				return false;
    	return true;
	}
    
    private static boolean anagram(String a, String b) {
    	int[] ca = a.chars().sorted().toArray();
    	int[] cb = b.chars().sorted().toArray();
    	return Arrays.equals(ca, cb);
		
	}
    
}
