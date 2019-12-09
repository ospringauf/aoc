package aoc2019;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;

/*
 * Day 8: Space Image Format
 * https://adventofcode.com/2019/day/8
 */
public class Day08 {

	private static final int COLS = 25;
	private static final int ROWS = 6;
    private static final int LAYER_SIZE = COLS*ROWS;

    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int TRANSPARENT = 2;
    
	public static void main(String[] args) throws Exception {
        new Day08().solve();
    }

	private int[] input;

    void solve() throws Exception {
    	System.out.println("=== input ===");
    	
        input = Util.lines("input08.txt").get(0).chars().map(n -> n-'0').toArray();
        int layers = input.length/LAYER_SIZE;
        System.out.println(String.format("found %d layers", layers));
        
        System.out.println("=== part1 ===");
        
        Function<Integer, Long> zeroes = n -> layer(n).filter(i -> i==0).count();        
        int minZeroes = IntStream.range(0, layers).boxed().min(Comparator.comparing(zeroes)).get();  
        System.out.println("min zeroes on layer " + minZeroes);
        
        long ones = layer(minZeroes).filter(i -> i==1).count();
        long twos = layer(minZeroes).filter(i -> i==2).count();
        var result = ones*twos;
        System.out.println(result);
        
        System.out.println("=== part2 ===");

//        int i = layers-1;
//        var img = layer(i).toArray();
//        for (; i>=1; --i) merge(img, layer(i-1).toArray());
        
        // all layer arrays in reversed order
        var allLayers = IntStream.range(0, layers).mapToObj(n -> layer(layers-n-1).toArray());
        var img = allLayers.reduce(new int[LAYER_SIZE], (lower, upper) -> merge(lower, upper));
        
        printImage(img);
    }

	void printImage(int[] img) {
		for (int r=0; r<ROWS; ++r) {
        	for (int c=0; c<COLS; ++c) System.out.print(img[r*COLS+c] == WHITE? '#':' ');
        	System.out.println();
        }
	}
   
	IntStream layer(int n) {
		return Arrays.stream(input).skip(n*LAYER_SIZE).limit(LAYER_SIZE);
	}
	
	int overlay(int lower, int upper) {
		return (upper == TRANSPARENT)? lower: upper;
	}
	
	int[] merge(int[] lower, int[] upper) {
		for (int i=0; i<upper.length; ++i) lower[i] = overlay(lower[i], upper[i]); 
		return lower;
	}
}
