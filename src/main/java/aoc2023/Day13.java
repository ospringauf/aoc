package aoc2023;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.Tuple;
import io.vavr.collection.List;

//--- Day 13: Point of Incidence ---
// https://adventofcode.com/2023/day/13

class Day13 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 35360
        timed(() -> new Day13().part1());
        System.out.println("=== part 2"); // 36755
        timed(() -> new Day13().part2());
    }

//    String data = example;
    String data = file2string("input13.txt");
    boolean debug = data.length() < 1000;

    class MirrorMap extends PointMap<Character> {

        public boolean isMirrorCol(int c, int smudge) {
            var right = List.ofAll(keySet()).filter(p -> p.x() > c);
            var pairs = right.map(p -> Tuple.of(p, mirrorX(p, c))).filter(t -> containsKey(t._2));
            var vals = pairs.map(t -> Tuple.of(get(t._1), get(t._2)));

            var diff = vals.count(t -> t._1 != t._2);
            return diff == smudge;
        }

        public boolean isMirrorRow(int c, int smudge) {
            var below = List.ofAll(keySet()).filter(p -> p.y() > c);
            var pairs = below.map(p -> Tuple.of(p, mirrorY(p, c))).filter(t -> containsKey(t._2));
            var vals = pairs.map(t -> Tuple.of(get(t._1), get(t._2)));

            var diff = vals.count(t -> t._1 != t._2);
            return diff == smudge;
        }

        Point mirrorX(Point p, int c) {
            return Point.of(mirror(p.x(), c), p.y());
        }

        Point mirrorY(Point p, int c) {
            return Point.of(p.x(), mirror(p.y(), c));
        }

        int mirror(int x, int c) {
            return 2 * c - x + 1;
        }

        int mirrorValue(int smudge) {
            var bb = boundingBox();
            var c = List.range(0, bb.xMax()).filter(x -> isMirrorCol(x, smudge)).min();
            var r = List.range(0, bb.yMax()).filter(y -> isMirrorRow(y, smudge)).min();
            if (debug) {
                System.out.println("col: " + c);
                System.out.println("row: " + r);
            }
            var ci = c.map(x -> x + 1).getOrElse(0);
            var ri = r.map(x -> x + 1).getOrElse(0);
            return ci + 100 * ri;
        }
    }

    void solve(int smudge) {
        var blocks = data.split("\n\n");

        var sum = 0L;
        for (var b : blocks) {
            MirrorMap m = new MirrorMap();
            m.read(b.split("\n"), c -> c);
            int mirrval = m.mirrorValue(smudge);
            sum += mirrval;
            if (debug) {
                System.out.println("value: " + mirrval);
                m.print();
                System.out.println();                
            }
        }
        System.out.println(sum);
    }

    void part1() {
        solve(0);
    }

    void part2() {
        solve(1);
    }

    static String example = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """;
}
