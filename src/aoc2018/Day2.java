package aoc2018;

import java.util.List;

public class Day2 {

    public static void main(String[] args) throws Exception {
    	
        part2();
    }
    
    
    /**
     * String contains N occurrences of any char
     */
    static boolean hasN(String s, int n) {
    	return 
    	s.chars()
    		.distinct()
    		.map(c -> (int) s.chars().filter(x -> x == c).count())
    		.filter(x -> x == n) // exactly n times
    		.findAny()
    		.isPresent();
    }

    /**
     * Strings a and b differ by exactly 1 char
     */
    static boolean diffBy1(String a, String b) {
    	if (a.length() != b.length()) 
    		return false;
    	int d = 0;
    	for (int i=0; i<a.length(); ++i) 
    		d += (a.charAt(i) != b.charAt(i))? 1 : 0;
    	return d == 1;
    }
    
    private static void part1() throws Exception {
        //List<String> l = Util.lines("aoc2018/day2-test.txt");
    	List<String> l = Util.lines("aoc2018/day2.txt");
    	
        int maxlen = l.stream().mapToInt(x -> x.length()).max().getAsInt();
        
        long result=1;
        for (int i=2; i<=maxlen; ++i) {
        	final int n = i;
        	long c = l.stream().filter(x -> hasN(x, n)).count();
        	System.out.println("*"+i+" : "+c);
        	if (c>0) result *= c;
        }
        System.out.println(result);
        
    }

    private static void part2() throws Exception {
    	List<String> l = Util.lines("aoc2018/day2.txt");
    	
        for (String a : l) {
			for (String b: l) {
				if (diffBy1(a, b))
					System.out.println(a + " / " + b);
			}
		}
        // ighfbsyijnoumxjlxevacpwqtr
        // ighfbbyijnoumxjlxevacpwqtr
        // ighfb-yijnoumxjlxevacpwqtr
    }
    
}
