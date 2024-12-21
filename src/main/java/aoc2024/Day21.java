package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function2;
import io.vavr.Tuple2;
import io.vavr.collection.*;

//--- Day 21: Keypad Conundrum ---
// https://adventofcode.com/2024/day/21

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
        Point pos;

        Map<Tuple2<Character, Character>, List<String>> paths = HashMap.empty();

        Robot(PointMap<Character> kb) {
            this.kb = kb;
            this.pos = kb.findPoint('A');

            var keys = List.ofAll(kb.values()).remove('x');
            for (var t : keys.crossProduct()) {
                var p = paths(t);
                paths = paths.put(t, p);
            }
        }

        List<String> paths(Tuple2<Character, Character> t) {
            Point k1 = kb.findPoint(t._1);
            Point k2 = kb.findPoint(t._2);
            var dist = k1.manhattan(k2);
            var options = List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).crossProduct(dist)
                    .toList();

            List<String> r = List.empty();
            for (var p : options) {
                var keys = walk(k1, p);
                if (keys.last().equals(t._2) && !keys.contains('x'))
                    r = r.append(p.map(d -> d.symbol()).mkString());
            }
            return r;
        }

        List<Character> walk(Point k1, List<Direction> dirs) {
            var l = dirs.foldLeft(List.of(k1), (k, d) -> k.append(k.last().translate(d)));
            return l.map(x -> kb.getOrDefault(x, 'x'));
        }

        List<String> sequences(String s) {
            var x = pairs(s).map(t -> paths.get(t).get()).toList();
            var y = expand(x);
            return y;
        }

        List<String> expand(List<List<String>> x) {
            var r = x.reduceLeft((l1, l2) -> l1.flatMap(s1 -> l2.map(s2 -> s1 + "A" + s2)));
            r = r.map(si -> si + 'A');
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
                
                var min = Long.MAX_VALUE;
                var options = paths.get(keys).get();
                for (var p : options) {
                    var pairs = pairs("A" + p + "A");
                    var c = pairs.map(t -> memCost.apply(t, nrobots - 1)).sum().longValue();
                    min = (c < min) ? c : min;
                }
                return min;
            }
        }

        Function2<Tuple2<Character, Character>, Integer, Long> memCost = Function2.of(this::cost).memoized();
    }

    static int numeric(String input) {
        return Integer.valueOf(input.substring(0, 3));
    }

    static List<Tuple2<Character, Character>> pairs(String s) {
        return List.ofAll(s.toCharArray())
                .sliding(2)
                .map(l -> new Tuple2<Character, Character>(l.get(0), l.get(1)))
                .toList();
    }

    void solve(int nrobots) {
        PointMap<Character> kb0 = new PointMap<>();
        kb0.read(Util.splitLines(KEY0));

        PointMap<Character> kb1 = new PointMap<>();
        kb1.read(Util.splitLines(KEY1));

        var r0 = new Robot(kb0);
        var r1 = new Robot(kb1);

        long result = 0;

        for (String input : Util.splitLines(real)) {
//            System.out.println(pairs(input));

            var s0 = r0.sequences("A" + input);
//            System.out.println(s0);

            long min = Long.MAX_VALUE;
            for (var s : s0) {
                s = "A" + s;
                var c = pairs(s).map(t -> r1.memCost.apply(t, nrobots));
//                System.out.println(s + " -> " + c + " -> " + c.sum());
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
            x0A
            """;

    static String KEY1 = """
            x^A
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
