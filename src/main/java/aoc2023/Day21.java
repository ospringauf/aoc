package aoc2023;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

//--- Day 21: Step Counter ---
// https://adventofcode.com/2023/day/21

class Day21 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3660
        timed(() -> new Day21().part1(64));
        System.out.println("=== part 2"); // 605492675373144
        timed(() -> new Day21().part2(26501365));

    }

    List<String> data = file2lines("input21.txt");
//    List<String> data = Util.splitLines(example);

    final PointMap<Character> map;
    final Point start;
    final int gridSize;
    final Point C, N, S, E, W, NE, SE, NW, SW;

    Day21() {
        map = new PointMap<Character>();
        map.read(data);
        start = map.findPoint('S');
        map.put(start, '.');
        var bb = map.boundingBox();
        gridSize = bb.width();
        System.out.println("start point: " + start);
        System.out.println("grid size: " + bb);
        var m = bb.width() - 1;
        var c = m / 2;

        C = Point.of(c, c);
        N = Point.of(c, 0);
        S = Point.of(c, m);
        E = Point.of(m, c);
        W = Point.of(0, c);
        NE = Point.of(m, 0);
        SE = Point.of(m, m);
        NW = Point.of(0, 0);
        SW = Point.of(0, m);
    }

    void part1(int steps) {
        var seen = HashSet.of(start);

        while (steps > 0) {
            seen = seen.flatMap(p -> p.neighbors()).filter(n -> map.getOrDefault(n, '-') == '.');
            steps--;
        }
        System.err.println(seen.size());
    }

    // spreading pattern across grid instances:
    //
    // ......... ......... ....6...
    // ......... ......... ...545..
    // ....2.... ...323... ..53235.
    // ...212... ...212... .6421246
    // ....2.... ...323... ..53235.
    // ......... ......... ...545..
    // ......... ......... ....6...

    // starting points for 2,4,6...: center of N/E/S/W border
    // starting points for 3,5,7...: NE/SE/NW/SW corner

    void part2(int steps) {
        // pre-calc spreading pattern for each type of grid 
        for (var p : List.of(C, N, S, E, W, NE, NW, SE, SW)) {
            System.out.println("pre-calc " + p);
            precalc.put(p, precalcPlots(p));
        }

        long result = 0;
        int delta = gridSize / 2;

        int radius = (int) Math.ceil((double) steps / delta);
        System.out.println("radius: " + radius);

        int remaining = steps;

        // 1
        result += solve(C, remaining);
        radius -= 1;

        // 2
        remaining -= delta + 1; // <-- still not sure why one extra step?
        result += solve(S, remaining);
        result += solve(N, remaining);
        result += solve(E, remaining);
        result += solve(W, remaining);
        radius -= 1;

        long f = 1;
        while (radius > 0) {
            // 3, 5, 7, ...
            remaining -= delta + 1; // <-- same here
            result += f * solve(SW, remaining);
            result += f * solve(NW, remaining);
            result += f * solve(SE, remaining);
            result += f * solve(NE, remaining);
            radius -= 1;
            
            // 4, 6, 8, ...
            remaining -= delta;
            result += solve(S, remaining);
            result += solve(N, remaining);
            result += solve(E, remaining);
            result += solve(W, remaining);
            radius -= 1;

            f++; // each diagonal contains one more grid than the one before
        }

        System.err.println(result);
    }

    PointMap<List<Integer>> precalc = new PointMap<>();

    // calculate the number of reachable plots for each step.
    // after max 2*gridSize, the number keeps oscillating between 2 values for
    // even/odd step counts
    List<Integer> precalcPlots(Point startPos) {
        List<Integer> counts = List.of(1);
        int steps = 2 * gridSize;
        var plots = HashSet.of(startPos);

        while (steps > 0) {
            plots = plots.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, 'X') == '.');
            steps--;
            counts = counts.append(plots.size());
        }
        return counts;
    }

    // get the number of reachable plots for a single grid
    // by simple lookup in pre-calculated lists
    long solve(Point startPos, int steps) {
        if (steps <= 0)
            return 0;
        else {
            var v = precalc.get(startPos);
            if (steps > 2 * gridSize) {
                if (steps % 2 == 0)
                    return v.last();
                else
                    return v.get(v.size() - 2);
            } else
                return v.get(steps);
        }
    }

    // some solutions (by brute force calculation)
//            65 --> odd: 3744, even: 3660
//            100 --> odd: 8650, even: 8857
//            131 --> odd: 15007, even: 14784
//            196 --> odd: 33159, even: 33417
//            200 --> odd: 34750, even: 34926
//            262 --> odd: 59158, even: 59603
//            300 --> odd: 77293, even: 78001
//            327 --> odd: 92680, even: 92248
//            393 --> odd: 133789, even: 133122
//            400 --> odd: 137818, even: 138474
//            458 --> odd: 180927, even: 181533
//            500 --> odd: 215745, even: 216565
//            524 --> odd: 236676, even: 237565
//            589 --> odd: 299976, even: 299196
//            1244 --> odd: 1334391, even: 1336041
//            1300 --> odd: 1457284, even: 1459286
//            1310 --> odd: 1479390, even: 1481611
//            1375 --> odd: 1632024, even: 1630200
//            1400 --> odd: 1689980, even: 1693484
//            1441 --> odd: 1792517, even: 1790074
//            2400 --> odd: 4962882, even: 4968844
//            2423 --> odd: 5065128, even: 5061912
//            2489 --> odd: 5345005, even: 5340786
//            3209 --> odd: 8882736, even: 8878476
//            3275 --> odd: 9252151, even: 9246600
//            3471 --> odd: 10391992, even: 10387384
//            3500 --> odd: 10561111, even: 10569516
//            3537 --> odd: 10791253, even: 10785258

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
