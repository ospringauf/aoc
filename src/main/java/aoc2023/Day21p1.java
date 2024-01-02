package aoc2023;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 21:  ---
// https://adventofcode.com/2023/day/21

class Day21p1 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3660
//        timed(() -> new Day21().part1());
        System.out.println("=== part 2");
        timed(() -> new Day21p1().part2());
    }

//    List<String> data = file2lines("input21.txt");
	List<String> data = Util.splitLines(example);

    void part1() {
        var m = new PointMap<Character>();
        m.read(data);
        var start = m.findPoint('S');
        m.put(start, '.');
        
        m.print();
        int steps = 64;
        var tgt = HashSet.of(start);
        
        while (steps > 0) {
            System.out.println(steps);
            var next = tgt.flatMap(p -> p.neighbors()).filter(p -> m.getOrDefault(p, '?')=='.');
            tgt = next;
            steps--;
        }
        System.err.println(tgt.size());        
    }

    void part2() {
        int maxsteps = 100;
        var m = new PointMap<Character>();
        m.read(data);
        var start = m.findPoint('S');
        m.put(start, '.');
        var bb = m.boundingBox();
        var w = bb.width();
        var h = bb.height();
        
        var tgt = HashSet.of(start);
        var garden = HashSet.ofAll(m.findPoints('.'));
        
        var am = HashSet.ofAll(m.keySet());
        var a1 = am.map(p -> p.translate(Direction.EAST, w));
        var a2 = a1.map(p -> p.translate(Direction.EAST, w));
        var a3 = a2.map(p -> p.translate(Direction.EAST, w));
        var a4 = a3.map(p -> p.translate(Direction.SOUTH, h));
        
        int step = 0;
        while (step < maxsteps) {
            tgt = tgt.flatMap(p -> p.neighbors()).filter(p -> garden.contains(p.modulo(w, h)));
            var tgt0 = tgt;
            var c = List.of(am, a1, a2, a3, a4).map(a -> tgt0.count(a::contains));
            System.out.println(step + " --> " + c);
            step++;
        }
        System.err.println(tgt.size());      
    }

    static String example = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
""";
}
