package aoc2022;

import common.AocPuzzle;
import common.Point;
import common.Range;
import common.Util;
import io.vavr.collection.List;

// --- Day 15: Beacon Exclusion Zone ---
// https://adventofcode.com/2022/day/15

class Day15 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 5112034
        timed(() -> new Day15().part1(TEST ? 10 : 2_000_000));
        timed(() -> new Day15().part1fast(TEST ? 10 : 2_000_000));

        System.out.println("=== part 2"); // 13172087230812
        timed(() -> new Day15().part2(TEST ? 20 : 4_000_000));
    }

    static final boolean TEST = false;
    List<String> data = TEST ? Util.splitLines(example) : file2lines("input15.txt");
    List<Sensor> sensors = data.map(Sensor::parse);

    record Sensor(Point pos, Point beacon) {
        static Sensor parse(String s) {
            // "Sensor at x=2, y=18: closest beacon is at x=-2, y=15"
            var f = s.split(": closest beacon is at ");
            var pos = parsePoint(f[0].split(" at ")[1]);
            var b = parsePoint(f[1]);
            return new Sensor(pos, b);
        }

        static Point parsePoint(String s) {
            // "x=-2, y=15"
            return split(s, "[xy= ,]+").to(f -> Point.of(f.i(1), f.i(2)));
        }

        Range exclusionZone(final int y) {
            var d = beacon.manhattan(this.pos);
            var dy = Math.abs(y - pos.y());
            var dx = d - dy; // width of exclusion zone
            if (dx >= 0)
                return new Range(pos.x() - dx, pos.x() + dx);
            else
                return Range.empty();
        }
    }

    void part1(final int y) {
        var zones = sensors.map(s -> s.exclusionZone(y));

        // count all values of x covered by any exclusion zone
        var xmin = zones.map(r -> r.min()).min().get();
        var xmax = zones.map(r -> r.max()).max().get();
        var covered = List.rangeClosed(xmin, xmax).count(x -> zones.exists(r -> r.contains(x)));

        // subtract known beacons at y
        var beacons = sensors.map(s -> s.beacon).distinct().count(b -> b.y() == y);

        System.out.println(covered - beacons);
    }

    /**
     * much faster but less readable, 
     * uses similar approach to part2 (jump to the edges of the exclusion zones
     * instead of enumerating all x values)
     */
    void part1fast(final int y) {
        var zones = sensors.map(s -> s.exclusionZone(y));

        // count all values of x covered by any exclusion zone
        var xmin = zones.map(r -> r.min()).min().get();
        var xmax = zones.map(r -> r.max()).max().get();
        int covered = 0;
        int x = xmin;
        do {
            final int fx = x;
            var zone = zones.find(z -> z.contains(fx));
            if (zone.isDefined()) {
                // add exclusion zone width, continue after zone
                int b = zone.get().max() + 1;
                covered += b - x;
                x = b;
            } else {
                // uncovered area, jump to next zone
                x = zones.map(z -> z.min()).filter(za -> za>=fx).min().getOrElse(xmax);
            }
        } while (x <= xmax);

        // subtract known beacons at y
        var beacons = sensors.map(s -> s.beacon).distinct().count(b -> b.y() == y);

        System.out.println(covered - beacons);
    }

    
    void part2(final int area) {
        for (int y = 0; y <= area; ++y) {
            final int fy = y;
            var zones = sensors.map(s -> s.exclusionZone(fy));
            int x = 0;
            do {
                // sweep x, jump over exclusion zones
                final int fx = x;
                var zone = zones.find(z -> z.contains(fx));
                if (zone.isDefined())
                    // continue after this exclusion zone
                    x = zone.get().max() + 1;
                else {
                    // found an uncovered point - we're done
                    System.out.println(Point.of(x, y));
                    long frequency = x * 4_000_000L + y;
                    System.out.println(frequency);
                    return;
                }
            } while (x <= area);
        }
    }

    static String example = """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
                        """;

}
