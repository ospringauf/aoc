package aoc2024;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.*;

//--- Day 19: Linen Layout ---
// https://adventofcode.com/2024/day/19

class Day19 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 353
        timed(() -> new Day19().part1());
        System.out.println("=== part 2"); // 880877787214477
        timed(() -> new Day19().part2());
    }

    List<String> data = file2lines("input19.txt");
//    List<String> data = Util.splitLines(example);

    List<String> towels;
    List<String> designs;

    Day19() {
        towels = List.of(data.head().split(", "));
        designs = data.tail().tail();
    }

    void part1() {
        var r = designs.count(memPossible);
        System.out.println(r);
    }

    void part2() {
        var r = designs.map(memCount);
        System.out.println(r.sum());
    }

    Predicate<String> memPossible = Function1.of(this::possible).memoized()::apply;
    Function1<String, Long> memCount = Function1.of(this::count).memoized();

    boolean possible(String d) {
        if (d.length() == 0)
            return true;

        List<String> rem = towels.filter(d::startsWith).map(t -> d.substring(t.length()));
        return rem.exists(memPossible);
    }

    long count(String d) {
        if (d.length() == 0)
            return 1;

        List<String> rem = towels.filter(d::startsWith).map(t -> d.substring(t.length()));
        return rem.map(memCount).sum().longValue();
    }

    static String example = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """;
}
