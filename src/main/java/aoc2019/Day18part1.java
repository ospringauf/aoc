package aoc2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import aoc2019.PointMap.PathResult;

/*
 * Day 18: Many-Worlds Interpretation
 * https://adventofcode.com/2019/day/18
 *
 */
public class Day18part1 {

	static final String inputFile = "input18.txt";

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("=== part 1 ===");
		new Day18part1().part1();
		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}
	
	long keyMask(char key) {
		return (key=='@')? 1L : 1L<<(key-'a'+1);
	}
	
	long keyMask(Set<Character> keys) {
		long m = 0;
		for (var k : keys) m = m | keyMask(k);
		return m;
	}
	
	static final char WALL = '#';
	static final char CLEAR = '.';
	static final int INF = 1000000;

	PointMap<Character> map;

	int bound = Integer.MAX_VALUE / 2;
	Map<Character, Point> poi;
	Point entrance;
	Set<Character> keys;
	Map<Character, PathResult> shortest = new HashMap<>();

	void part1() throws Exception {
		map = new PointMap<>();
		map.read(Util.linesArray(inputFile), c -> c);
		map.print();

		entrance = map.findPoint('@');
		map.put(entrance, CLEAR);
		keys = map.values().stream().filter(v -> v >= 'a' && v <= 'z').collect(Collectors.toSet());
		System.out.println(keys);
		var steps = 0;

		// key/door => point of interest
		poi = map.entrySet().stream().filter(e -> e.getValue() != WALL && e.getValue() != CLEAR)
				.collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
		poi.put('@', entrance);

		// reduce map: remove walls, keep only points on shortest paths between keys
		map.findPoints(WALL).collect(Collectors.toList()).forEach(w -> map.remove(w));
		printMap("without walls: ", entrance);
		shortestPathsBetweenKeys();
		printMap("shortest paths only: ", entrance);

		steps = collectKeys('@', keys);

		System.out.println(steps);
	}
	
	Map<Character, Map<Long, Integer>> cache = new HashMap<Character, Map<Long,Integer>>();

	
	int collectKeys(Character currentKey, Set<Character> keys) {
		if (keys.isEmpty()) {
			return 0;
		}

		// cache lookup
		var m = keyMask(keys);
		var c1 = cache.get(currentKey);
		if (c1 != null) {
			var c2 = c1.get(m);
			if (c2 != null) return c2;
		}
		
		
		var locked = keys.stream().map(Day18part1::doorOf).collect(Collectors.toSet());
		Predicate<Character> via = c -> !keys.contains(c);
		var pos = poi.get(currentKey);
		var dist = map.minDistances(pos, c -> !locked.contains(c), via);
		var reachKeys = keys.stream().filter(k -> dist.containsKey(poi.get(k))).collect(Collectors.toList());


		var best = 1000000;
		for (var k : reachKeys) {
			var nextPos = poi.get(k);
			int dn = dist.get(nextPos);
			var s = dn + collectKeys(k, except(keys, k));
			best = Math.min(best, s);
		}
		
		c1 = (c1 == null) ? new HashMap<Long, Integer>() : c1;
		cache.putIfAbsent(currentKey, c1);
		c1.put(m, best);

		return best;
	}

	void shortestPathsBetweenKeys() {
		System.out.println("calculating shortest paths between keys");
		var pathpoints = new HashSet<Point>();
		var start = new HashSet<Character>(keys);
		start.add('@');
		
		for (var k : start) {
			var result = map.calPaths(poi.get(k), x->true, x->true);
			shortest.put(k, result);
			for (var k2 : keys) {
				var p = poi.get(k2);
				while (p != null) {
					pathpoints.add(p);
					p = result.predecessor.get(p);
				}
			}
		}

		map.keySet().stream()
		.filter(p -> !pathpoints.contains(p))
		.collect(Collectors.toList())
		.forEach(p -> map.remove(p));
	}

	void printMap(String header, Point pos) {
		System.out.println(header + " @" + pos);
		map.boundingBox().print(p -> (p.equals(pos) ? '@' : map.getOrDefault(p, ' ')));
	}

	Set<Character> except(Set<Character> keys, Character... remove) {
		var s = new HashSet<Character>(keys);
		for (var k : remove)
			s.remove(k);
		return s;
	}

	static char doorOf(char key) {
		return (char) (key + ('A' - 'a'));
	}

}
