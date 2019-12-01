package aoc2019;

import org.junit.jupiter.api.Assertions;

/*
 * https://adventofcode.com/2019/day/1
 */
public class Day01Classic {

    public static void main(String[] args) throws Exception {
        new Day01Classic().part1();
        new Day01Classic().part2();
    }
    
    
    int fuel(int mass) { 
    	return mass/3 - 2;
    }
    
    void part1() throws Exception {      
    	var input = Util.intListOf("input01.txt");
    	       
    	int result = 0;
    	for (int mass : input) {
    		result += fuel(mass);
    	}
        System.out.println(result);
    }

    int totalFuel(int mass) {
    	int result = 0;
    	do {
    		int f = Math.max(0, fuel(mass));
    		result += f;
    		mass = f;
    	} while (mass > 0);
    	return result;
    }
    
    void part2() throws Exception {
    	var input = Util.intListOf("input01.txt");
    	
    	Assertions.assertEquals(50346, totalFuel(100756));

    	int result = 0;
    	for (int mass : input) {
    		result += totalFuel(mass);
    	}
        System.out.println(result);
    }

}
