package aoc2022;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

// --- Day 12: Hill Climbing Algorithm ---
// https://adventofcode.com/2022/day/12

class Day12 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day12().solve());
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input12.txt");

    boolean canClimb(Character from, Character to) {
        return (from != null) && (to != null) && (to <= from+1);
    }
    
    boolean canDescend(Character from, Character to) {
        return canClimb(to, from);
    }
    
    void solve() {
        PointMap<Character> map = new PointMap<Character>();
        map.read(data);
        
        Point S = map.findPoint('S');
        Point E = map.findPoint('E');
        map.put(S, 'a');
        map.put(E, 'z');
        
        var dst = map.minDistances(E, c -> true, this::canDescend);
        
        System.out.println("=== part 1"); // 437
        System.out.println(dst.get(S));
        
        System.out.println("=== part 2"); // 430
        System.out.println(map.findPoints('a').map(p -> dst.getOrDefault(p, Integer.MAX_VALUE)).min());
    }

    static String example = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
                        """;

}
