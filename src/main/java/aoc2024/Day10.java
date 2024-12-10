package aoc2024;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 10: Hoof It ---
// https://adventofcode.com/2024/day/10

class Day10 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 535
        timed(() -> new Day10().part1());
        System.out.println("=== part 2"); // 1186
        timed(() -> new Day10().part2());
    }

    List<String> data = file2lines("input10.txt");
//	List<String> data = Util.splitLines(example);

    PointMap<Integer> m = new PointMap<Integer>();

    void part1() {
        m.read(data, c -> c - '0');

        var trailheads = m.findPoints(0);
        var r = trailheads.map(x -> score(x)).sum();
        System.out.println(r);
    }

    void part2() {
        m.read(data, c -> c - '0');

        var trailheads = m.findPoints(0);
        var r = trailheads.map(x -> rating(x)).sum();
        System.out.println(r);
    }

    int score(Point s) {
        Predicate<Point> until = p -> m.get(p) == 9;
        BiPredicate<Point, Point> via = (a, b) -> m.get(b) == m.get(a) + 1;
        var ap = m.allPaths(s, until, via);
        return ap.map(p -> p.head()).distinct().size();
    }

    int rating(Point s) {
        Predicate<Point> until = p -> m.get(p) == 9;
        BiPredicate<Point, Point> via = (a, b) -> m.get(b) == m.get(a) + 1;
        var ap = m.allPaths(s, until, via);
        return ap.size();
    }

    static String example = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """;
}
