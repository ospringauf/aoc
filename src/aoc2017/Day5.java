package aoc2017;

import java.util.List;

public class Day5 {

    public static void main(String[] args) throws Exception {
        part2();
    }
    
    private static void part1() throws Exception {
    	List<Integer> l = Util.ints("aoc2017/day5.txt");
    	
    	int steps = 0;
    	int p = 0;
    	while (p < l.size()) {
    		int tmp = l.get(p);
    		l.set(p, tmp+1);
    		p += tmp;
    		steps++;
    	}
    	
    	System.out.println(steps);
    }

    private static void part2() throws Exception {
	List<Integer> l = Util.ints("aoc2017/day5.txt");
    	
    	int steps = 0;
    	int p = 0;
    	while (p < l.size()) {
    		int tmp = l.get(p);
    		l.set(p, (tmp >= 3)? tmp-1 : tmp+1);
    		p += tmp;
    		steps++;
    	}
    	
    	System.out.println(steps);
    }

  
    
}
