package aoc2019;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.lambda.Seq;

/*
 * Day 8: Space Image Format
 * https://adventofcode.com/2019/day/8
 * alternative / more functional solution
 */
public class Day08f {

	private static final int COLS = 25;
	private static final int ROWS = 6;
	private static final int LAYER_SIZE = COLS * ROWS;
	
    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int TRANSPARENT = 2;


	public static void main(String[] args) throws Exception {
		new Day08f().solve();
	}

	private int[] input;

	void solve() throws Exception {
		System.out.println("=== input ===");

		input = Util.lines("input08.txt").get(0).chars().map(n -> n - '0').toArray();
		int layers = input.length / LAYER_SIZE;
		System.out.println(String.format("found %d layers", layers));

		System.out.println("=== part1 ===");
		
		int minZeroes = Seq.range(0, layers).minBy(l -> layer(l).count(x -> x==0)).get();
		System.out.println("min zeroes on layer " + minZeroes);

		long ones = layer(minZeroes).count(x -> x==1);
		long twos = layer(minZeroes).count(x -> x==2);
		var result = ones * twos;
		System.out.println(result);

		System.out.println("=== part2 ===");

		BiFunction<Integer, Integer, Integer> overlay = (lower, upper) -> (upper == TRANSPARENT) ? lower : upper;

		var lowest = layer(layers - 1);
		Seq<Integer> merged = Seq
				.range(0, layers - 1)
				.reverse()
				.map(i -> layer(i))
				.foldLeft(lowest, (lower, upper) -> lower.zip(upper, overlay));

		var img = merged.mapToInt(n -> n).toArray();
		Seq.range(0, ROWS).forEach(row -> System.out.println(line(img, row)));
	}
	
	Seq<Integer> segment(int[] arr, int segSize, int n) {
		return Seq.seq(Arrays.stream(arr)).skip(n*segSize).limit(segSize);
	}

	Seq<String> line(int[] img, int line) {
		Function<Integer, String> printable = n -> (n == WHITE) ? "#" : " ";
		return segment(img, COLS, line).map(printable);
	}

	Seq<Integer> layer(int n) {
		return segment(input, LAYER_SIZE, n);
	}
}
