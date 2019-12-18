package aoc2019;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Day 18: Many-Worlds Interpretation
 * https://adventofcode.com/2019/day/18
 *
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        System.out.println("=== part 1 ===");
        new Day18().part1();

//        System.out.println("=== part 2 ===");
//        new Day18().part2();
    }

    static final char WALL = '#'; 
    static final char CLEAR = '.';
	
    PointMap<Character> map;

    void part1() throws Exception {
        map = new PointMap<>();
        map.read(Util.linesArray("input18a.txt"), c->c);

        map.print();
        
        var entrance = map.findPoint('@');
        var pos = entrance;
        map.put(pos, CLEAR);
        var keys = map.values().stream().filter(v -> v >= 'a' && v <= 'z').collect(Collectors.toSet());
//        var doors = map.values().stream().filter(v -> v >= 'A' && v <= 'Z').collect(Collectors.toSet());
//        System.out.println(keys);
//        System.out.println(doors);
//        System.out.println(pos);
        
//        var collectedKeys = new HashSet<Character>();
//        var openDoors = new HashSet<Character>();
        
        var steps = bestPath(pos, keys); 
        
        map.print();
        System.out.println(steps);        
    }

	private int bestPath(Point pos, Set<Character> keys) {
		var steps = 0;
        if ( ! keys.isEmpty())  
        {
        	var locked = keys.stream().map(k -> doorOf(k)).collect(Collectors.toSet());
        	var dist = map.minDistances(pos, c -> c!=WALL && !locked.contains(c));
        	var reachKeys = keys.stream().filter(k -> dist.containsKey(map.findPoint(k))).collect(Collectors.toList());
        	
        	var key = reachKeys.get(0);
        	System.out.println("select key: " + key + " from " + reachKeys);
//        	pos = map.findPoint(key); 
//        	steps += dist.get(pos);
//        	keys.remove(key);
        	var next = map.findPoint(key);
        	var s = bestPath(next, except(keys, key));
        	steps += dist.get(next) + s;

//        	map.put(pos, '.');
//        	var dk = map.findPoint(doorOf(key));
//        	if (dk != null) map.put(dk, '.');
        }
		return steps;
	}
	
	

	Set<Character> except(Set<Character> keys, Character key) {
		var s = new HashSet<Character>(keys);
		s.remove(key);
		return s;
	}

	static char doorOf(char key) {
		return (char) (key + ('A'-'a'));
	}
}

