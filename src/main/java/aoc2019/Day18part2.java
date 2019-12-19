package aoc2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.paukov.combinatorics3.Generator;

import aoc2019.PointMap.PathResult;

/*
 * Day 18: Many-Worlds Interpretation
 * https://adventofcode.com/2019/day/18
 *
 */
public class Day18part2 {

	static final String inputFile = "input18_2.txt";

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("=== part 2 ===");
		new Day18part2().part2();
		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}
	
	long keyMask(char key) {
		return 1L<<(key-'a'+1);
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
	Set<Character> allKeys;
	Map<Character, PathResult> shortest = new HashMap<>();

	void part2() throws Exception {
		map = new PointMap<>();
		map.read(Util.linesArray(inputFile), c -> c);
		map.print();

		allKeys = map.values().stream().filter(v -> v >= 'a' && v <= 'z').collect(Collectors.toSet());
		System.out.println(allKeys);

		// key/door => point of interest
		poi = map.entrySet().stream().filter(e -> e.getValue() != WALL && e.getValue() != CLEAR)
				.collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
		for (char e = '1'; e<= '4'; ++e) {
			var start = map.findPoint(e);
			poi.put(e, start);
			map.put(start, CLEAR);
		}		

		// reduce map: remove walls, keep only points on shortest paths between keys
		map.findPoints(WALL).collect(Collectors.toList()).forEach(w -> map.remove(w));
		printMap("without walls: ");

		shortestPathsBetweenKeys();
		printMap("shortest paths only: ");
		
		var steps = collectKeys('1', '2', '3', '4', allKeys);

		System.out.println(steps);
	}
	
	Map<Long, Map<Long, Integer>> cache = new HashMap<Long, Map<Long,Integer>>();

	
	int collectKeys(char ck1, char ck2, char ck3, char ck4, Set<Character> remainingKeys) {
		if (remainingKeys.isEmpty()) {
			return 0;
		}

		// cache lookup
		var m1 = ((long)ck1<<24) | ((long)ck2<<16) | ((long)ck3<<8) | ((long)ck4);
		var m2 = keyMask(remainingKeys);
		var c1 = cache.get(m1);
		if (c1 != null) {
			var c2 = c1.get(m2);
			if (c2 != null) return c2;
		}
		
		var reach1 = reachableKeys(ck1, remainingKeys);
		var reach2 = reachableKeys(ck2, remainingKeys);
		var reach3 = reachableKeys(ck3, remainingKeys);
		var reach4 = reachableKeys(ck4, remainingKeys);

		var best = 1000000;
		var options = Generator.cartesianProduct(reach1, reach2, reach3, reach4);
		for (var x : options) {
			System.out.println(x);
			
			char k1 = x.get(0); 
			char k2 = x.get(1);
			char k3 = x.get(2);
			char k4 = x.get(3);
			var d1 = shortest.get(ck1).distance.get(poi.get(k1));
			var d2 = shortest.get(ck2).distance.get(poi.get(k2));
			var d3 = shortest.get(ck3).distance.get(poi.get(k3));
			var d4 = shortest.get(ck4).distance.get(poi.get(k4));
			
			var s = d1 + d2 + d3 + d4 + collectKeys(k1, k2, k3, k4, except(remainingKeys, k1, k2, k3, k4));
			best = Math.min(best, s);			
		}
		
		// cache update
		c1 = (c1 == null) ? new HashMap<Long, Integer>() : c1;
		c1.put(m2, best);
		cache.putIfAbsent(m1, c1);

		return best;
	}

	private List<Character> reachableKeys(char currentKey, Set<Character> remainingKeys) {
		Predicate<Character> via = c -> !remainingKeys.contains(c);
		var locked = remainingKeys.stream().map(Day18part2::doorOf).collect(Collectors.toSet());
		var pos = poi.get(currentKey);
		var dist = map.minDistances(pos, c -> !locked.contains(c), via);
		var reachKeys = remainingKeys.stream().filter(k -> dist.containsKey(poi.get(k))).collect(Collectors.toList());
		if (reachKeys.isEmpty()) reachKeys.add(currentKey);
		return reachKeys;
	}

	
	void shortestPathsBetweenKeys() {
		System.out.println("calculating shortest paths between keys");
		var pathpoints = new HashSet<Point>();
		var start = new HashSet<Character>(allKeys);
		start.add('1');
		start.add('2');
		start.add('3');
		start.add('4');
		
		for (var k : start) {
			var result = map.calPaths(poi.get(k), x->true, x->true);
			shortest.put(k, result);
			for (var k2 : allKeys) {
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

	void printMap(String header) {
		System.out.println(header);
		map.boundingBox().print(p -> map.getOrDefault(p, ' '));
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
