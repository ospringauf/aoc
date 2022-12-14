package aoc2022;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

// --- Day 14: Regolith Reservoir ---
// https://adventofcode.com/2022/day/14

class Day14 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 757
        timed(() -> new Day14().solve(false));
        System.out.println("=== part 2"); // 24943
        timed(() -> new Day14().solve(true));
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input14.txt");
    PointMap<Character> map = new PointMap<Character>();


    boolean free(Point p) {
        return map.getOrDefault(p, '.') == '.';
    }
    
    void solve(boolean withFloor) {
        // build rock walls
        for (var l : data) {
            var points = split(l, " -> ").map(s -> Point.parse(s));
            points.sliding(2).flatMap(ab -> Point.straightLine(ab.get(0), ab.get(1))).forEach(p -> map.put(p, '#'));
        }
//        map.print();

        int floor = map.boundingBox().yMax() + 2;
//        for (int x = bb.xMin() - 2*bb.height(); x < bb.xMax() + 2*bb.height(); ++x)
//            m.put(Point.of(x,  floor), '#');

        final var source = Point.of(500, 0);
        boolean full = false;
        boolean overflow = false;
        
        while (!full && !overflow) {
            var s = source;
            boolean falling = false;
            do {
                var rest = withFloor && s.y() == floor-1;
                var down = !rest && free(s.south());
                var left = !rest && free(s.south().west());
                var right = !rest && free(s.south().east());
                s = down ? s.south() : left ? s.south().west() : right ? s.south().east() : s;
                falling = down || left || right;
                overflow = s.y() >= floor;                
            } while (falling && !overflow);
            
            full = s.equals(source);
            if (!overflow)
                map.put(s, 'o');
        }

//        map.print();
        var sand = map.countValues('o');
        System.out.println(sand);        
    }

    static String example = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
                        """;

}
