package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Pose;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 6: Guard Gallivant ---
// https://adventofcode.com/2024/day/6

class Day06 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 5318
        timed(() -> new Day06().part1());
        System.out.println("=== part 2"); // 1831
        timed(() -> new Day06().part2()); 
//        System.out.println("=== part 2a"); // 1831
//        timed(() -> new Day06().part2a()); 
    }

    List<String> data = file2lines("input06.txt");
//	List<String> data = Util.splitLines(example);
    
    PointMap<Character> map;
    Pose guard0;

    Day06() {
        map = new PointMap<Character>();
        map.read(data);

        var p0 = map.findPoint('^');
        map.put(p0, '.');
        guard0 = new Pose(Direction.NORTH, p0);       
    }
    
    Set<Point> part1() {
    	var g = guard0;
        Set<Point> path = HashSet.empty();
        do {
            path = path.add(g.pos());
        	var next = g.ahead().pos();
        	if (! map.containsKey(next)) break;
        	
            if (map.get(next) == '#')
                g = g.turnRight();
            else
                g = g.ahead();
        } while (map.containsKey(g.pos()));
        
        System.out.println("visited positions: " + path.size());
        return path;
    }

    void part2() {
    	var g = guard0;

        Set<Point> obstacle = HashSet.empty();
        Set<Point> path = HashSet.empty();
        
        do {
        	path = path.add(g.pos());
        	var next = g.ahead().pos();
        	if (! map.containsKey(next)) break;
        	
            if (map.get(next) == '#')
                g = g.turnRight();
            else {
            	// simulate obstacle ahead, but only if we haven't been there!
            	if (!path.contains(next)) {
            		map.put(next, '#');
            		if (loops(g))
            			obstacle = obstacle.add(next);
            		map.put(next, '.');
            	}
                g = g.ahead();
            }
        } while (map.containsKey(g.pos()));

//        o.forEach(x -> map.put(x, 'O'));
//        m.print();
        System.out.println("obstacles: " + obstacle.size());
    }
    
    void part2a() {
        var g = guard0;
        Set<Point> obstacle = HashSet.empty();

        //var t = map.findPoints('.');
        var t = part1();
        for (var p : t) {
        	map.put(p, '#');
        	if (loops(g)) {
        		obstacle = obstacle.add(p);
        	}
        	map.put(p, '.');
        }
        
        obstacle = obstacle.remove(guard0.pos());

//        o.forEach(x -> map.put(x, 'O'));
//        m.print();
        System.out.println("obstacles: " + obstacle.size());
    }

    boolean loops(Pose g) {
    	Set<Pose> seen = HashSet.of(g);
        do {
            if (map.getOrDefault(g.ahead().pos(), '.') == '#')
                g = g.turnRight();
            else
                g = g.ahead();
            if (seen.contains(g))
            	return true;
            seen = seen.add(g);
        } while (map.containsKey(g.pos()));
        
        return false;
	}

	static String example = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """;
}
