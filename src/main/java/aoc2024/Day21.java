package aoc2024;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple2;
import io.vavr.collection.List;

//--- Day 21: Keypad Conundrum ---
// https://adventofcode.com/2024/day/21
// 
// It works, don't ask my why

class Day21 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 154208
        timed(() -> new Day21().solve(2));
        System.out.println("=== part 2"); // 188000493837892
        timed(() -> new Day21().solve(25));
    }

//    List<String> data = file2lines("input21.txt");
    List<String> data = Util.splitLines(real);

    static class Robot {
        PointMap<Character> kb;

        Map<Tuple2<Character, Character>, List<String>> maneuvers = new HashMap<>();

        Robot(PointMap<Character> kb) {
            this.kb = kb;

            var keys = List.ofAll(kb.values());
            for (var t : keys.crossProduct()) {
                maneuvers.put(t, allManeuvers(t));
            }
        }

        List<String> allManeuvers(Tuple2<Character, Character> t) {
            Point k1 = kb.findPoint(t._1);
            Point k2 = kb.findPoint(t._2);
            var dist = k1.manhattan(k2);
            var options = List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).crossProduct(dist)
                    .toList();

            // find the [^<>v]* sequences that start at k1, stay on allowed keys and end at
            // k2
            List<String> r = List.empty();
            for (var seq : options) {
                var points = seq.scanLeft(k1, (k, d) -> k.translate(d));
                if (points.forAll(kb::containsKey) && points.last().equals(k2))
                    r = r.append(seq.map(Direction::symbol).mkString() + "A");
            }
            return r;
        }

        // all possible combinations of pairwise key maneuvers for the given key
        // sequence (s)
        List<String> sequences(String s) {
            var pwm = pairs(s).map(maneuvers::get);
            var r = pwm.reduceLeft((l1, l2) -> l1.flatMap(s1 -> l2.map(s2 -> s1 + s2)));

            return r;
        }

        long cost(Tuple2<Character, Character> keys, int nrobots) {
            if (nrobots == 1) {
                var k1 = kb.findPoint(keys._1);
                var k2 = kb.findPoint(keys._2);
                return k1.manhattan(k2) + 1;
            } else {
                if (keys._1 == keys._2)
                    return 1; // just press A

                Function1<Tuple2<Character, Character>, Long> cost = t -> memCost.apply(t, nrobots-1);
                
                var options = maneuvers.get(keys);
                var c = options.map(seq -> pairs("A" + seq).map(cost));
                var min = c.map(x -> x.sum().longValue()).min().get();
                
//                var min = Long.MAX_VALUE;
//                for (var m : maneuvers.get(keys)) {
//                    var pairs = pairs("A" + m);
//                    var c = pairs.map(t -> memCost.apply(t, nrobots - 1)).sum().longValue();
//                    min = (c < min) ? c : min;
//                }
                return min;
            }
        }

        Function2<Tuple2<Character, Character>, Integer, Long> memCost = Function2.of(this::cost).memoized();
    }

    static int numeric(String input) {
        return Integer.valueOf(input.substring(0, 3));
    }

    static List<Tuple2<Character, Character>> pairs(String s) {
        return List.ofAll(s.toCharArray()).sliding(2).map(l -> new Tuple2<Character, Character>(l.get(0), l.get(1)))
                .toList();
    }

    void solve(int nrobots) {
        PointMap<Character> kb0 = new PointMap<>();
        kb0.read(Util.splitLines(KEY0));
        kb0.findPoints('g').forEach(kb0::remove);

        PointMap<Character> kb1 = new PointMap<>();
        kb1.read(Util.splitLines(KEY1));
        kb1.findPoints('g').forEach(kb1::remove);

        var r0 = new Robot(kb0);
        var r1 = new Robot(kb1);
        Function1<Tuple2<Character, Character>, Long> cost = t -> r1.memCost.apply(t, nrobots);

        long result = 0;

        for (String input : Util.splitLines(real)) {
//            System.out.println(pairs(input));

            var options = r0.sequences("A" + input);
            System.out.println(input + " sequences: " + options);


//            var c = options.map(seq -> pairs("A" + seq).map(cost));
//            System.out.println(c);
//            var min = c.map(x -> x.sum().longValue()).min().get();

            long min = Long.MAX_VALUE;
            for (var seq : options) {
                seq = "A" + seq;
                var c = pairs(seq).map(t -> r1.memCost.apply(t, nrobots));
                System.out.println("   " + seq + " -> " + c + " -> " + c.sum());
                var sum = c.sum().longValue();
                min = (sum < min) ? sum : min;
            }
            result += numeric(input) * min;
        }

        System.out.println(result);
    }

    static String KEY0 = """
            789
            456
            123
            g0A
            """;

    static String KEY1 = """
            g^A
            <v>
            """;

    static String example = """
            029A
            980A
            179A
            456A
            379A
            """;

    static String real = """
            140A
            143A
            349A
            582A
            964A            """;

}
