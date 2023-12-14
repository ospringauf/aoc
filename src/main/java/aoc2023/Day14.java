package aoc2023;

import java.util.function.Function;

import common.AocPuzzle;
import common.BoundingBox;
import common.CyclicSequence;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 14: Parabolic Reflector Dish ---
// https://adventofcode.com/2023/day/14

class Day14 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day14().part1()); // 109755
        System.out.println("=== part 2");
        timed(() -> new Day14().part2()); // 90928
    }

    List<String> data = file2lines("input14.txt");
//    List<String> data = Util.splitLines(example);

    class RockMap extends PointMap<Character> {

        final BoundingBox bb;
        final Function<Point, Point> rotCw;

        public RockMap(List<String> data) {
            super();
            read(data);
            bb = boundingBox();
            final int max = bb.xMax();
            rotCw = p -> new Point(max - p.y(), p.x());
        }

        int northLoad() {
            var rocks = findPoints('O');
            var y = bb.yMax();
            return rocks.map(p -> y - p.y() + 1).sum().intValue();
        }

        void rollNorth() {
            var h = bb.height();
            var w = bb.width();

            for (int x = 0; x < w; x++) {                
                var to = Point.of(x, 0);
                var from = to.south();
                while (from.y() < h) {
                    while (to.y() < h && get(to) != '.')
                        to = to.south();
                    from = to.south();
                    while (from.y() < h && get(from) == '.')
                        from = from.south();
                    if (from.y() < h) {
                        if (get(from) == 'O') {
                            put(from, '.');
                            put(to, 'O');
                            to = to.south();
                        } else if (get(from) == '#') {
                            to = from.south();
                        }
                    }
                }
            }
        }

        // naive, too slow for part 2
        void rollNorth0() {
            boolean rolling = true;
            var rocks = findPoints('O').toSet();
            do {
                var r = rocks.filter(p -> getOrDefault(p.north(), '?') == '.');
                r.forEach(p -> {
                    put(p, '.');
                    put(p.north(), 'O');
                });
                rocks = rocks.removeAll(r);
                rocks = rocks.addAll(r.map(p -> p.north()));
                rolling = r.nonEmpty();
            } while (rolling);
        }

        void rollCycle() {
            for (int i = 0; i < 4; ++i) {
                rollNorth();
                transformPoints(rotCw);
//                shiftToOrigin();
            }
        }
    }

    void part1() {
        var m = new RockMap(data);
        m.rollNorth();
//        m.print();
        System.err.println(m.northLoad());
    }

    void part2() {
        var m = new RockMap(data);

        int sim = 300;
        var seq = new CyclicSequence();

        System.out.println("simulating " + sim + " cycles ...");
        for (int c = 1; c < sim; ++c) {
            m.rollCycle();
            var nl = m.northLoad();
            seq.add(nl);
            System.out.print(".");
            if (c % 80 == 0)
                System.out.println();
//            System.out.println(c + " --> " + nl + " / " + findCycle(vals));
        }
        System.out.println();

        var cycles = seq.findCycles();
        System.out.println("cycle length: " + cycles);
        int cycleLen = cycles.head();
        int result = seq.extrapolate(1_000_000_000, cycleLen);
        System.err.println(result);
    }

    static String example = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;
}
