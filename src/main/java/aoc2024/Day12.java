package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 12: Garden Groups ---
// https://adventofcode.com/2024/day/12

class Day12 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 1387004
		timed(() -> new Day12().part1());
		System.out.println("=== part 2"); // 844198
		timed(() -> new Day12().part2());
	}

	List<String> data = file2lines("input12.txt");
//    List<String> data = Util.splitLines(example); // 140 / 80
//    List<String> data = Util.splitLines(example3); // 1930 / 1206

	PointMap<Character> map = new PointMap<>();

	void part1() {
		map.read(data);
		var regions = findRegions();
		var price = regions.map(this::price1).sum();
		System.out.println(price);
	}

	List<Set<Point>> findRegions() {
		List<Set<Point>> regions = List.empty();
		var plots = HashSet.ofAll(map.keySet());
		Set<Point> done = HashSet.empty();
		
		for (var p : plots) {
			if (done.contains(p))
				continue;

			var plant = map.get(p);
			var region = map.connectedArea(p, c -> c == plant);
			regions = regions.append(region);
			done = done.addAll(region);
		}
		return regions;
	}

	int perimeter(Point p) {
		var plant = map.get(p);
		return p.neighbors().count(n -> map.getOrDefault(n, null) != plant);
	}

	int price1(Set<Point> region) {
		var perim = region.toList().map(this::perimeter).sum().intValue();
		return region.size() * perim;
	}

	void part2() {
		map.read(data);
		var regions = findRegions();
		var price = regions.map(this::price2).sum();
		System.out.println(price);
	}

	int price2(Set<Point> region) {
		return region.size() * countFronts(region);
	}

	int countFronts(Set<Point> region) {
		var west = front(front(region, Direction.WEST), Direction.NORTH).size();
		var east = front(front(region, Direction.EAST), Direction.NORTH).size();
		var north = front(front(region, Direction.NORTH), Direction.WEST).size();
		var south = front(front(region, Direction.SOUTH), Direction.WEST).size();
		return west + east + north + south;
	}

	Set<Point> front(Set<Point> region, Direction d) {
		return region.reject(x -> region.contains(x.translate(d)));
	}

	static String example = """
			AAAA
			BBCD
			BBCC
			EEEC
			""";

	static String example3 = """
			RRRRIICCFF
			RRRRIICCCF
			VVRRRCCFFF
			VVRCCCJFFF
			VVVVCJJCFE
			VVIVCCJJEE
			VVIIICJJEE
			MIIIIIJJEE
			MIIISIJEEE
			MMMISSJEEE
			""";
}
