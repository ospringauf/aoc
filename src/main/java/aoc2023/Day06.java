package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 6: Wait For It ---
// https://adventofcode.com/2023/day/6

class Day06 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== example");
        timed(() -> new Day06().example());
        System.out.println("=== part 1"); // 840336
        timed(() -> new Day06().part1());
        System.out.println("=== part 2"); // 41382569
        timed(() -> new Day06().part2());
    }

//    List<String> data = file2lines("input06.txt");
	List<String> data = Util.splitLines(example);

	long ways2win(long time, long best) {
	    long wins = 0;
	    for (int hold=0; hold<=time;++hold) {
	        var speed = hold;
	        var d = (time-hold)*speed;
	        if (d > best) wins++;
	    }
	    return wins;
	}

    void example() {
        System.out.println(ways2win(7, 9));        
        System.out.println(ways2win(7, 9) * ways2win(15, 40) * ways2win(30, 200));
        System.out.println(ways2win(71530, 940200));        
    }
    
    void part1() {
        System.out.println(
                ways2win(62, 553) 
                * ways2win(64, 1010) 
                * ways2win(91, 1473) 
                * ways2win(90, 1074));
    }

    void part2() {
        System.out.println(ways2win(62649190L, 553101014731074L));
    }

    static String example = """
Time:      7  15   30
Distance:  9  40  200            
""";
    
    static String input = """
Time:        62     64     91     90
Distance:   553   1010   1473   1074            
            """;
}
