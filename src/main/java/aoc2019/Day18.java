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
        
        var steps = bestPath(pos, keys, 0); 
        
        map.print();
        System.out.println(steps);        
    }

    int bound = Integer.MAX_VALUE / 2;
    
	private int bestPath(Point pos, Set<Character> keys, int fre) {
//		var steps = 0;
        if (keys.isEmpty()) {
            if (fre < bound) bound = fre;
            return fre;
        }
        
    	var locked = keys.stream().map(Day18::doorOf).collect(Collectors.toSet());
    	var dist = map.minDistances(pos, c -> c != WALL && !locked.contains(c));
    	var reachKeys = keys.stream().filter(k -> dist.containsKey(map.findPoint(k))).collect(Collectors.toList());
    	
    	var best = 1000000; //Integer.MAX_VALUE;
    	for (var key : reachKeys) {
//                System.out.println("select key: " + key + " from " + reachKeys);
            var nextPos = map.findPoint(key);
            int dn = dist.get(nextPos);
            if (fre + dn > bound) continue;
            var s = bestPath(nextPos, except(keys, key), fre + dn);
            best = Math.min(best, s);
    	}
        var steps = fre + best;                
    
		return steps;
	}
	
	private int bestPath0(Point pos, Set<Character> keys) {
        var steps = 0;
        if ( ! keys.isEmpty())  
        {
            var locked = keys.stream().map(k -> doorOf(k)).collect(Collectors.toSet());
            var dist = map.minDistances(pos, c -> c!=WALL && !locked.contains(c));
            var reachKeys = keys.stream().filter(k -> dist.containsKey(map.findPoint(k))).collect(Collectors.toList());
            
//          for (var key : )
            var key = reachKeys.get(0);
            System.out.println("select key: " + key + " from " + reachKeys);
//          pos = map.findPoint(key); 
//          steps += dist.get(pos);
//          keys.remove(key);
            var next = map.findPoint(key);
            var s = bestPath0(next, except(keys, key));
            steps += dist.get(next) + s;

//          map.put(pos, '.');
//          var dk = map.findPoint(doorOf(key));
//          if (dk != null) map.put(dk, '.');
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

