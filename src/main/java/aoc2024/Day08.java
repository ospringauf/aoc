package aoc2024;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.*;

//--- Day 8: Resonant Collinearity ---
// https://adventofcode.com/2024/day/8

class Day08 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 256
        timed(() -> new Day08().part1());
        System.out.println("=== part 2"); // 1005
        timed(() -> new Day08().part2());
    }

    List<String> data = file2lines("input08.txt");
//    List<String> data = Util.splitLines(example);

    PointMap<Character> map = new PointMap<>();
    
    interface NodeFunc extends Function1<Tuple2<Point, Point>, Traversable<Point>> {}

    Day08() {
        map.read(data);
    }
    
    List<Point> singleAntinode(Point a, Point b) {
        return List.of(b.plus(b.minus(a)));
    }
    
    List<Point> allAntinodes(Point a, Point b) {
        var delta = b.minus(a);      
        return Stream.iterate(a, p -> p.plus(delta)).takeWhile(p -> map.containsKey(p)).toList();        
    }

    void part1() {
        NodeFunc f = p -> singleAntinode(p._1, p._2);
        solve(f);
    }
    
    void part2() {
       NodeFunc  f = p -> allAntinodes(p._1, p._2);
       solve(f);
    }
    
    void solve(NodeFunc f) {
        Set<Point> antinodes = HashSet.empty();

        var frequencies = HashSet.ofAll(map.values()).remove('.');
        for (var freq : frequencies) {
            var antennas = map.findPoints(freq);
            // all pairs of antennas with same frequency
            var pairs = antennas.crossProduct().reject(t -> t._1==t._2);

            antinodes = antinodes.addAll(pairs.flatMap(f));
        }

        antinodes = antinodes.intersect(HashSet.ofAll(map.keySet()));
        System.out.println(antinodes.size());
    }

    static String example = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """;
}
