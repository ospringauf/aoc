package aoc2023;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 21:  ---
// https://adventofcode.com/2023/day/21
// TODO implement https://www.youtube.com/watch?v=9UOMZSL0JTg

class Day21 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3660
        timed(() -> new Day21().part1());
        System.out.println("=== part 2"); // 605492675373144
//        timed(() -> new Day21().part2a());
//        timed(() -> new Day21().part2b());
//        timed(() -> new Day21().part2c());
        timed(() -> new Day21().part2());

    }

    List<String> data = file2lines("input21.txt");
//    List<String> data = Util.splitLines(example);

    PointMap<Character> map;
    Point start;

    static final Point C = Point.of(65, 65);
    static final Point N = Point.of(65, 0);
    static final Point S = Point.of(65, 130);
    static final Point E = Point.of(130, 65);
    static final Point W = Point.of(0, 65);
    static final Point NE = Point.of(130, 0);
    static final Point SE = Point.of(130, 130);
    static final Point NW = Point.of(0, 0);
    static final Point SW = Point.of(0, 130);

    Day21() {
        map = new PointMap<Character>();
        map.read(data);
        start = map.findPoint('S');
        System.out.println(start);
        map.put(start, '.');
//        m.print();
    }

    void part1() {
        int steps = 64;
        var seen = HashSet.of(start);

        while (steps > 0) {
            seen = seen.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, '-') == '.');
            steps--;
//            System.out.println(seen.size());
        }
        System.err.println(seen.size());
    }

    int solve1Grid(Point initial, int steps) {
        if (steps < 0)
            return 0;
        while (steps >= 261 + 65)
            steps -= 66;
        int step = 0;
        var next = HashSet.of(initial);
        var seen = next;
        var s = new PointMap<Integer>();
//        System.out.println("initial: " + initial);

        int even = seen.size();
        int odd = 0;

        while (next.nonEmpty() && step < steps) {
            step++;
            next = next.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, '-') == '.');
            next = next.removeAll(seen);
            seen = seen.addAll(next);
            for (var p : next)
                s.put(p, step);
            if (step % 2 == 0)
                even += next.size();
            else
                odd += next.size();
//            System.out.println(seen.size());
        }
        var r = (steps % 2 == 0) ? even : odd;
        System.out.println(initial + ", " + steps + " steps --> " + r);
        return r;
    }

    void part2c() {
        int r = 0;
        // r += = solve1Grid(HashSet.of(C), 64);

        // 100
//        r += solve1Grid(HashSet.of(C), 100);        
//        int rem = 100-66;
//        r += solve1Grid(HashSet.of(S), rem);
//        r += solve1Grid(HashSet.of(N), rem);
//        r += solve1Grid(HashSet.of(E), rem);
//        r += solve1Grid(HashSet.of(W), rem);

//        // 196
//        int rem = 196;
//        r += solve1Grid(HashSet.of(C), rem);
//        rem -= 65 + 1;       
//        r += solve1Grid(HashSet.of(S), rem);
//        r += solve1Grid(HashSet.of(N), rem);
//        r += solve1Grid(HashSet.of(E), rem);
//        r += solve1Grid(HashSet.of(W), rem);
//        
//        rem -= 65 + 1;
//        r += solve1Grid(HashSet.of(SW), rem);
//        r += solve1Grid(HashSet.of(NW), rem);
//        r += solve1Grid(HashSet.of(SE), rem);
//        r += solve1Grid(HashSet.of(NE), rem);
//        
//        rem -= 65 + 1;
//        r += solve1Grid(HashSet.of(S), rem);
//        r += solve1Grid(HashSet.of(N), rem);
//        r += solve1Grid(HashSet.of(E), rem);
//        r += solve1Grid(HashSet.of(W), rem);

//        int d = 0;
//        int rem = 200; // 33417
////        int rem = 200; // 34926
//        int off=66;
//        System.out.println(rem/66.0);
//        r += solve1Grid(HashSet.of(C), rem+d);
//        rem -= off; d=0;       
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        rem -= off; d=0;      
//        r += solve1Grid(HashSet.of(SW), rem+d);
//        r += solve1Grid(HashSet.of(NW), rem+d);
//        r += solve1Grid(HashSet.of(SE), rem+d);
//        r += solve1Grid(HashSet.of(NE), rem+d);
//        rem -= off; d=1;      
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);

//        int d = 0;
//        int rem = 300; // 78001
//        int off=66;
//        System.out.println(rem/66.0);
//        // 1
//        r += solve1Grid(HashSet.of(C), rem+d);
//        // 2
//        rem -= off; d=0;       
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 3
//        rem -= off; d=0;      
//        r += solve1Grid(HashSet.of(SW), rem+d);
//        r += solve1Grid(HashSet.of(NW), rem+d);
//        r += solve1Grid(HashSet.of(SE), rem+d);
//        r += solve1Grid(HashSet.of(NE), rem+d);
//        //4
//        rem -= off; d=1;
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 5
//        rem -= off; d=1;      
//        r += 2*solve1Grid(HashSet.of(SW), rem+d);
//        r += 2*solve1Grid(HashSet.of(NW), rem+d);
//        r += 2*solve1Grid(HashSet.of(SE), rem+d);
//        r += 2*solve1Grid(HashSet.of(NE), rem+d);

//        int d = 0, f=1;
//        int rem = 400; // odd: 137818, even: 138474
//        int off=66;
//        System.out.println(rem/66.0);
//        // 1
//        r += solve1Grid(C, rem+d);
//        // 2
//        rem -= off; d=0;       
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 3
//        rem -= off; d=0;      
//        r += solve1Grid(HashSet.of(SW), rem+d);
//        r += solve1Grid(HashSet.of(NW), rem+d);
//        r += solve1Grid(HashSet.of(SE), rem+d);
//        r += solve1Grid(HashSet.of(NE), rem+d);
//        //4
//        rem -= off; d=1;
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 5
//        rem -= off; d=1; f=2;      
//        r += f*solve1Grid(HashSet.of(SW), rem+d);
//        r += f*solve1Grid(HashSet.of(NW), rem+d);
//        r += f*solve1Grid(HashSet.of(SE), rem+d);
//        r += f*solve1Grid(HashSet.of(NE), rem+d);
//        //6
//        rem -= off; d=2;
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 7
//        rem -= off; d=2; f=3;      
//        r += f*solve1Grid(HashSet.of(SW), rem+d);
//        r += f*solve1Grid(HashSet.of(NW), rem+d);
//        r += f*solve1Grid(HashSet.of(SE), rem+d);
//        r += f*solve1Grid(HashSet.of(NE), rem+d);
//

        System.out.println(r);
    }
//    
//    void part2c400() {
//        int r = 0;
//        
//        int d = 0, f=1;
//        int rem = 400; // odd: 137818, even: 138474
//        int off=65;
//        System.out.println(rem/66.0);
//        // 1
//        r += solve1Grid(C, rem+d);
//        // 2
//        rem -= off+1; d=0;       
//        r += solve1Grid(S, rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 3
//        rem -= off+1; d=0;      
//        r += solve1Grid(HashSet.of(SW), rem+d);
//        r += solve1Grid(HashSet.of(NW), rem+d);
//        r += solve1Grid(HashSet.of(SE), rem+d);
//        r += solve1Grid(HashSet.of(NE), rem+d);
//        //4
//        rem -= off; d=0;
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 5
//        rem -= off+1; d=0; f=2;      
//        r += f*solve1Grid(HashSet.of(SW), rem+d);
//        r += f*solve1Grid(HashSet.of(NW), rem+d);
//        r += f*solve1Grid(HashSet.of(SE), rem+d);
//        r += f*solve1Grid(HashSet.of(NE), rem+d);
//        //6
//        rem -= off; d=0;
//        r += solve1Grid(HashSet.of(S), rem+d);
//        r += solve1Grid(HashSet.of(N), rem+d);
//        r += solve1Grid(HashSet.of(E), rem+d);
//        r += solve1Grid(HashSet.of(W), rem+d);
//        // 7
//        rem -= off+1; d=0; f=3;      
//        r += f*solve1Grid(HashSet.of(SW), rem+d);
//        r += f*solve1Grid(HashSet.of(NW), rem+d);
//        r += f*solve1Grid(HashSet.of(SE), rem+d);
//        r += f*solve1Grid(HashSet.of(NE), rem+d);
//        
//        System.out.println(r);
//    }

    void part2d(int steps) {
        int r = 0;

        int f = 1;
        int rem = steps;
        int delta = 65;

        double rounds = rem / 66.0;
        System.out.println(rounds);
        // 1
        r += solve1Grid(C, rem);
        rounds -= 1;
        // 2
        rem -= delta + 1;
        r += solve1Grid(S, rem);
        r += solve1Grid(N, rem);
        r += solve1Grid(E, rem);
        r += solve1Grid(W, rem);
        rounds -= 1;

        f = 1;
        while (rounds > 0) {
            // 3
            rem -= delta + 1;
            r += f * solve1Grid(SW, rem);
            r += f * solve1Grid(NW, rem);
            r += f * solve1Grid(SE, rem);
            r += f * solve1Grid(NE, rem);
            // 4
            rem -= delta;
            r += solve1Grid(S, rem);
            r += solve1Grid(N, rem);
            r += solve1Grid(E, rem);
            r += solve1Grid(W, rem);

            f++;
            rounds -= 2;
        }

        System.out.println(r);
    }

    void part2() {
        gridValues(N);
        part2d(393); // 133122

    }

    // brute force calc locations for the first ~3000 steps
    void part2b() {
        var bb = map.boundingBox();
        var w = bb.width();
        var h = bb.height();
        var s = new PointMap<Integer>();

        var st = start;

        int steps = 0;
        var seen = HashSet.of(st);
        var next = seen;

        int even = 1;
        int odd = 0;

        while (next.nonEmpty() && steps < 6400) {
            steps++;
            next = next.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p.modulo(w, h), '-') == '.');
            next = next.removeAll(seen);
            var steps0 = steps;
            next.forEach(p -> s.put(p, steps0));
            seen = seen.addAll(next);
            if (steps % 2 == 0)
                even += next.size();
            else
                odd += next.size();
            if (steps % 131 == 0 || (steps - 65) % 131 == 0 || steps % 100 == 0)
                System.out.println(steps + " --> odd: " + odd + ", even: " + even);

        }
        System.out.println(steps + " --> odd: " + odd + ", even: " + even);
    }

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
//            600 --> odd: 310545, even: 312046
//            655 --> odd: 370931, even: 369820
//            700 --> odd: 422245, even: 422893
//            720 --> odd: 447055, even: 448009
//            786 --> odd: 532554, even: 533887
//            800 --> odd: 551344, even: 552835
//            851 --> odd: 625632, even: 624504
//            900 --> odd: 698508, even: 700119
//            917 --> odd: 726433, even: 724878
//            982 --> odd: 831543, even: 832845
//            1000 --> odd: 863098, even: 863832
//            1048 --> odd: 946792, even: 948569
//            1100 --> odd: 1041690, even: 1044374
//            1113 --> odd: 1069648, even: 1068172
//            1179 --> odd: 1200295, even: 1198296
//            1200 --> odd: 1240927, even: 1242888
//            1244 --> odd: 1334391, even: 1336041
//            1300 --> odd: 1457284, even: 1459286
//            1310 --> odd: 1479390, even: 1481611
//            1375 --> odd: 1632024, even: 1630200
//            1400 --> odd: 1689980, even: 1693484
//            1441 --> odd: 1792517, even: 1790074
//            1500 --> odd: 1938762, even: 1940060
//            1506 --> odd: 1955599, even: 1957597
//            1572 --> odd: 2130348, even: 2133013
//            1600 --> odd: 2205880, even: 2208915
//            1637 --> odd: 2312760, even: 2310588
//            1700 --> odd: 2491186, even: 2494392
//            1703 --> odd: 2503099, even: 2500212
//            1768 --> odd: 2695167, even: 2697513
//            1800 --> odd: 2794666, even: 2796842
//            1834 --> odd: 2899666, even: 2902775
//            1899 --> odd: 3111856, even: 3109336
//            1900 --> odd: 3111856, even: 3117034
//            1965 --> odd: 3332041, even: 3328710
//            2000 --> odd: 3447279, even: 3449781
//            2030 --> odd: 3553095, even: 3555789
//            2096 --> odd: 3787344, even: 3790897
//            2100 --> odd: 3801422, even: 3805143
//            2161 --> odd: 4029312, even: 4026444
//            2200 --> odd: 4173291, even: 4177446
//            2227 --> odd: 4279343, even: 4275568
//            2292 --> odd: 4529383, even: 4532425
//            2300 --> odd: 4563443, even: 4565328
//            2358 --> odd: 4793382, even: 4797379
//            2400 --> odd: 4962882, even: 4968844
//            2423 --> odd: 5065128, even: 5061912
//            2489 --> odd: 5345005, even: 5340786
//            2500 --> odd: 5387074, even: 5391050
//            2554 --> odd: 5624031, even: 5627421
//            2600 --> odd: 5828727, even: 5832953
//            2620 --> odd: 5917780, even: 5922221
//            2685 --> odd: 6219304, even: 6215740
//            2700 --> odd: 6285382, even: 6292306
//            2751 --> odd: 6529027, even: 6524364
//            2800 --> odd: 6758327, even: 6760209
//            2816 --> odd: 6837039, even: 6840777
//            2882 --> odd: 7160538, even: 7165423
//            2900 --> odd: 7248854, even: 7254279
//            2947 --> odd: 7491840, even: 7487928
//            3000 --> odd: 7759453, even: 7765223
//            3013 --> odd: 7831409, even: 7826302
//            3078 --> odd: 8168407, even: 8172493
//            3100 --> odd: 8287736, even: 8290704
//            3144 --> odd: 8521656, even: 8526985
//            3200 --> odd: 8823591, even: 8831424
//            3209 --> odd: 8882736, even: 8878476
//            3275 --> odd: 9252151, even: 9246600
//            3300 --> odd: 9386776, even: 9392047
//            3340 --> odd: 9618135, even: 9622569
//            3400 --> odd: 9966534, even: 9971777
//            3406 --> odd: 10001134, even: 10006907
//            3471 --> odd: 10391992, even: 10387384
//            3500 --> odd: 10561111, even: 10569516
//            3537 --> odd: 10791253, even: 10785258

    List<Integer> gridValues(Point start) {
        List<Integer> vals = List.of(1);
        int steps = 262;
        var seen = HashSet.of(start);

        while (steps > 0) {
            seen = seen.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, '-') == '.');
            steps--;
            vals = vals.append(seen.size());
        }
        System.err.println(vals.reverse().take(2));
        System.err.println(vals.size() % 2 == 0);
        System.err.println(vals.take(10));
        return vals;

//        var s = new PointMap<Integer>();
//        var first = HashSet.of(start);
//        List<Integer> vals = List.of(1);
//
//        int steps = 0;
//        // var seen = HashSet.of(st);
//        var seen = first;
//        var next = seen;
//
//
//        while (next.nonEmpty() && steps < 929) {
//            steps++;
//            next = next.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, '-') == '.');
//            var off = next.flatMap(p -> p.neighbors()).filter(p -> !map.containsKey(p));
//
//            next = next.removeAll(seen);
//            var steps0 = steps;
//            next.forEach(p -> s.put(p, steps0));
//            seen = seen.addAll(next);
//            vals = vals.append(seen.size());
//        }
//        System.out.println("steps: " + steps);
//
//        var odd = List.ofAll(s.entrySet()).count(e -> e.getValue() % 2 == 1);
//        var even = List.ofAll(s.entrySet()).count(e -> e.getValue() % 2 == 0);
//        System.out.println("odd: " + odd + ", even: " + even);
//        System.out.println(vals.reverse().take(2));
//
//        return vals;
    }

    // analyze spreading to neighbor maps
    void part2a() {
        var s = new PointMap<Integer>();
        var garden = map.findPoints('.').toSet();

        var first = HashSet.of(SE);

        int steps = 0;
        // var seen = HashSet.of(st);
        var seen = first;
        var next = seen;

        PointMap<Integer> offgrid = new PointMap<>();

        while (next.nonEmpty() && steps < 929) {
            steps++;
            next = next.flatMap(p -> p.neighbors()).filter(p -> map.getOrDefault(p, '-') == '.');
            var off = next.flatMap(p -> p.neighbors()).filter(p -> !map.containsKey(p));

            var ogk = HashSet.ofAll(offgrid.keySet());
            for (var p : off) {
                var newx = !ogk.map(it -> it.x()).contains(p.x());
                var newy = !ogk.map(it -> it.y()).contains(p.y());

//                var newx = ogk.count(it -> it.x() == p.x()) < 3;
//                var newy = ogk.count(it -> it.y() == p.y()) < 3;

//                var newx = true; 
//                var newy = true;

                if (newx && newy) {
                    System.out.println(p + " - " + steps);
                    offgrid.put(p, steps);
                }
            }

            next = next.removeAll(seen);
            var steps0 = steps;
            next.forEach(p -> s.put(p, steps0));
            seen = seen.addAll(next);
        }
        System.out.println("steps: " + steps);

        var odd = List.ofAll(s.entrySet()).count(e -> e.getValue() % 2 == 1);
        var even = List.ofAll(s.entrySet()).count(e -> e.getValue() % 2 == 0);
        System.out.println("odd: " + odd + ", even: " + even);

        seen.forEach(p -> map.put(p, 's'));
        map.print();

//        offgrid.keySet().forEach(p -> map.put(p, 'X'));
//        map.print();
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

    static String example2 = """
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##...####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##..S####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##...####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            """;
}
