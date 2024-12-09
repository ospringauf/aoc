package aoc2024;

import java.util.function.BiFunction;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 7: Bridge Repair ---
// https://adventofcode.com/2024/day/7

class Day07 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3598800864292
        timed(() -> new Day07().part1());
        System.out.println("=== part 2"); // 340362529351427
        timed(() -> new Day07().part2());
    }

    List<String> data = file2lines("input07.txt");
//    List<String> data = Util.splitLines(example);

    interface Bif extends BiFunction<Long, Long, Long> {}
    
    static Bif ADD = (a, b) -> a + b;
    static Bif MUL = (a, b) -> a * b;
    static Bif CONC = (a, b) -> Long.valueOf(Long.toString(a) + Long.toString(b));

    record Equation(Long r, List<Long> v) {

        static Equation parse(String l) {
            var f = l.split(": ");
            return new Equation(Long.valueOf(f[0]), Util.string2longs(f[1]));
        }

        // check if the equation is satisfiable with the given operators
        boolean sat(Bif... ops) {            
            var op = List.of(ops).crossProduct(v.length() - 1);
            return op.exists(x -> r.equals(eval(x)));
        }

        // apply the given operators to the values
        // eg. (1, (ADD,2), (MUL,3), (ADD,4))
        Long eval(List<Bif> ops) {
            var c = ops.zip(v.tail());
            return c.foldLeft(v.head(), (r, op) -> op._1.apply(r, op._2));
        }
    }

    void part1() {
        var sum = data.map(Equation::parse).filter(e -> e.sat(ADD, MUL)).map(e -> e.r).sum();
        System.out.println(sum);
    }

    void part2() {
        var sum = data.map(Equation::parse).filter(e -> e.sat(ADD, MUL, CONC)).map(e -> e.r).sum();
        System.out.println(sum);
    }

    static String example = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;
}
