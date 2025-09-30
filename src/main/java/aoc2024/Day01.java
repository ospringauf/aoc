package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 01: Historian Hysteria ---
// https://adventofcode.com/2024/day/1

class Day01 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1506483
        timed(() -> new Day01().part1());
        System.out.println("=== part 2"); // 23126924
        timed(() -> new Day01().part2());
    }

    List<String> data = file2lines("input01.txt");
//    List<String> data = Util.splitLines(example);

    List<Integer> left = List.empty();
    List<Integer> right = List.empty();

    {
        left = data.map(s -> split(s, "   ")).map(s -> s.i(0));
        right = data.map(s -> split(s, "   ")).map(s -> s.i(1));
    }

    void part1() {
        var result = left.sorted()
                .zip(right.sorted())
                .map(t -> t._1 - t._2)
                .map(Math::abs)
                .sum();
        System.out.println(result);
    }

    void part2() {
        var occur = left.map(x -> right.count(r -> r.equals(x)));
        var result = left.zip(occur).map(x -> x._1 * x._2).sum();
        System.out.println(result);
    }

    static String example = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;
}
