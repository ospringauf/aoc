package aoc2019;

import static java.lang.Math.max;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2019/day/1
 */
public class Day01 {

    public static void main(String[] args) throws Exception {
        new Day01().part1();
        new Day01().part2();
    }
    
    void part1() throws Exception {      
    	var input = Util.intStreamOf("input01.txt");
    	
        IntUnaryOperator fuel = mass -> mass/3 - 2;
        
		var result = input.map(fuel).sum();
        System.out.println(result);
    }

    void part2() throws Exception {
    	var input = Util.intStreamOf("input01.txt");
    	
    	IntUnaryOperator fuel = mass -> max(0, mass/3 - 2);
    	IntUnaryOperator totalFuel = mass -> IntStream.iterate(mass, x -> x>0, fuel).skip(1).sum();
    	
    	assertEquals(50346, totalFuel.applyAsInt(100756));
    	
        var result = input.map(totalFuel).sum();
        System.out.println(result);
    }

}
