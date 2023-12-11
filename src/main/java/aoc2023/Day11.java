package aoc2023;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 11: Cosmic Expansion ---
// https://adventofcode.com/2023/day/11

class Day11 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 9521550
        timed(() -> new Day11().solve(1));
        System.out.println("=== part 2"); // 298932923702
        timed(() -> new Day11().solve(999999));
    }

    List<String> data = file2lines("input11.txt");
//	List<String> data = Util.splitLines(example);

    void solve(int expansion) {
        var m = new PointMap<Character>();
        m.read(data);

        Set<Point> galaxies = HashSet.ofAll(m.findPoints('#'));
        BoundingBox bb = m.boundingBox();
        
        var rows = galaxies.map(p -> p.y()).toSet();
        var emptyRows = List.rangeClosed(0, bb.yMax()).removeAll(rows);
        
        var cols = galaxies.map(p -> p.x()).toSet();
        var emptyCols = List.rangeClosed(0, bb.xMax()).removeAll(cols);
        
        for (var x : emptyCols.reverse()) {
            var gx = galaxies.filter(g -> g.x() > x);
            var gx2 = gx.map(p -> p.translate(expansion, 0));
            galaxies = galaxies.removeAll(gx).addAll(gx2);
        }
        
        for (var y : emptyRows.reverse()) {
            var gy = galaxies.filter(g -> g.y() > y);
            var gy2 = gy.map(p -> p.translate(0, expansion));
            galaxies = galaxies.removeAll(gy).addAll(gy2);
        }

        var pairs = Stream.ofAll(Generator.combination(galaxies.toJavaList()).simple(2));
//        System.out.println("pairs: " + pairs.size());

        var d = pairs.map(p -> p.get(1).manhattan(p.get(0))).sum();
        System.out.println(d);
    }

    static String example = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;
}
