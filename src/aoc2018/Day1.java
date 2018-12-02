package aoc2018;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

    public static void main(String[] args) throws Exception {
        part1();
    }
    
    private static void part1() throws Exception {
        System.out.println(Util.intStreamOf("aoc2018/day1.txt").sum());
    }

    private static void part2() throws Exception {
//        System.out.println("--- test");
        Set<Integer> seen = new HashSet<Integer>();
        int freq = 0;
        
        List<Integer> l = Util.ints("aoc2018/day1.txt");
        boolean found = false;
        while (!found) {
        	for (Integer i : l) {
				freq += i;
				found = seen.contains(freq);
				if (found) break;
				seen.add(freq);
			}
        }
        
        System.out.println(freq);
    }
    
}
