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

class Day21 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 154208
        timed(() -> new Day21().solve(2));
        System.out.println("=== part 2"); // 188000493837892
        timed(() -> new Day21().solve(25));
    }

    List<String> data = Util.splitLines(real);
//    List<String> data = Util.splitLines(example);
    
    Robot robot0; // numerical keypad
    Robot robot1; // directional keypad


    static class Robot {
        PointMap<Character> keypad = new PointMap<>();
        Map<Tuple2<Character, Character>, List<String>> maneuvers = new HashMap<>();

        Robot(String layout) {
            keypad.read(Util.splitLines(layout));
            keypad.findPoints('g').forEach(keypad::remove); // delete "gaps"

            var keys = List.ofAll(keypad.values());
            for (var t : keys.crossProduct()) {
                maneuvers.put(t, allManeuvers(t));
            }
        }

        List<String> allManeuvers(Tuple2<Character, Character> t) {
            Point p1 = keypad.findPoint(t._1);
            Point p2 = keypad.findPoint(t._2);
            var dist = p1.manhattan(p2);
            var options = List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
                    .crossProduct(dist);

            // find the [^<>v]* input sequences that start at k1, stay on allowed keys and end at
            // k2 (with Activate), e.g. 
            // (2, 7) -> List(^^<A, ^<^A, <^^A)
            // (9, A) -> List(vvvA)
            List<String> m = List.empty();
            for (var seq : options) {
                var path = seq.scanLeft(p1, (k, d) -> k.translate(d));
                if (path.forAll(keypad::containsKey) && path.last().equals(p2)) {                    
                    String input = seq.map(Direction::symbol).mkString() + "A";
                    m = m.append(input);
                }
            }
            return m;
        }
    }

    static int numeric(String input) {
        return Integer.valueOf(input.substring(0, 3));
    }

    static List<Tuple2<Character, Character>> pairs(String s) {
        return List.ofAll(s.toCharArray()).sliding(2).map(l -> new Tuple2<Character, Character>(l.get(0), l.get(1)))
                .toList();
    }

    long cost(Tuple2<Character, Character> keys, int nrobots) {
        var robot = robot0.maneuvers.containsKey(keys) ? robot0 : robot1;

        if (nrobots == 0) {
            var p1 = robot.keypad.findPoint(keys._1);
            var p2 = robot.keypad.findPoint(keys._2);
            return p1.manhattan(p2) + 1;
            
        } else {
            if (keys._1 == keys._2)
                return 1; // just press A

            Function1<Tuple2<Character, Character>, Long> nextCost = t -> memCost.apply(t, nrobots - 1);

            // for all possible maneuvers from k1 to k2, find the minmal total cost at the "last" robot in the chain
            var options = robot.maneuvers.get(keys);
            var c = options.map(seq -> pairs("A" + seq).map(nextCost));
            var min = c.map(x -> x.sum().longValue()).min().get();

            return min;
        }
    }
    
    Function2<Tuple2<Character, Character>, Integer, Long> memCost = Function2.of(this::cost).memoized();

    
    void solve(int nrobots) {
        robot0 = new Robot(KEYPAD0);
        robot1 = new Robot(KEYPAD1);

        long result = 0;

        for (String input : data) {
            var seq = "A" + input;
//            System.out.println(pairs(seq));

            var c = pairs(seq).map(t -> cost(t, nrobots));
//            System.out.println("   " + seq + " -> " + c + " -> " + c.sum());
            
            result += numeric(input) * c.sum().longValue();
        }
        System.out.println(result);
    }


    static String KEYPAD0 = """
            789
            456
            123
            g0A
            """;

    static String KEYPAD1 = """
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
            964A            
            """;


}
