package aoc2018;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {
	static class Day6Area {
		int x, y;
		int area;
		boolean infinite;
		public Day6Area(String s) {
			x = Integer.parseInt(s.split(", ")[0]);
			y = Integer.parseInt(s.split(", ")[1]);
		}
		
		int dist(int px, int py) {
			return Math.abs(x-px) + Math.abs(y-py);
		}
		
		@Override
		public String toString() {			
			return "area " + x + "/" + y + "  size=" + area + "  inf:" + infinite;
		}
	}
	
	static Day6Area dummy = new Day6Area("0, 0");

	private static List<Day6Area> input;

	private static int mx;

	private static int my;

	public static void main(String[] args) throws Exception {
	    input = Util.stringStreamOf("aoc2018/day6.txt").map(Day6Area::new).collect(Collectors.toList());
		mx = input.stream().mapToInt(a -> a.x).max().getAsInt();
		my = input.stream().mapToInt(a -> a.y).max().getAsInt();

		System.out.println("=== part 1 ===");
		part1();
		System.out.println("=== part 2 ===");
		part2();
	}

	
	static Day6Area closest(int px, int py) {
		Integer[] c = IntStream.range(0, input.size())
				.mapToObj(i -> i)
				.sorted((i1, i2) -> input.get(i1).dist(px, py) - input.get(i2).dist(px, py))
				.toArray(l -> new Integer[l]);
		// tie?
		Day6Area a1 = input.get(c[0]);
		Day6Area a2 = input.get(c[1]);

		return (a1.dist(px, py) == a2.dist(px, py)) ? dummy : a1;
	}
	
	static int sumDist(int px, int py) {
		return input.stream().mapToInt(a -> a.dist(px, py)).sum();
	}
	
	private static void part1() throws Exception {
		for (int x=0; x<=mx; ++x) {
			for (int y=0; y<=my; ++y) {
				closest(x,y).area++;
			}
		}
		
//		IntStream.range(0,  mx).forEach(x -> IntStream.range(0, my).forEach(y -> closest(x, y).area++));
		
		// mark infinite areas
		IntStream.range(-10000, 10000).forEach(i -> closest(i, -10000).infinite = true);
		IntStream.range(-10000, 10000).forEach(i -> closest(i,  10000).infinite = true);
		IntStream.range(-10000, 10000).forEach(i -> closest(-10000, i).infinite = true);
		IntStream.range(-10000, 10000).forEach(i -> closest( 10000, i).infinite = true);

	
		input.stream().forEach(System.out::println);
		
		// max finite area?
		Optional<Day6Area> result = input.stream()
			.filter(a -> !a.infinite)
			.max((a1, a2) -> a1.area - a2.area)
			;
		System.out.println("--> result: " + result);
	}

	private static void part2() throws Exception {
		int r = 0;
		for (int x=0; x<=mx; ++x) {
			for (int y=0; y<=my; ++y) {
				if (sumDist(x, y) < 10000)
					r++;
			}			
		}
		System.out.println(r);
	}

}
