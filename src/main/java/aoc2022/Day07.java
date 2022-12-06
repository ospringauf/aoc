package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 7:  ---
// https://adventofcode.com/2022/day/7

class Day07 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new Day07().part1();
        System.out.println("=== part 2");
        new Day07().part2();
    }

    List<String> data = Util.splitLines(example);
//    List<String> data = file2lines("input07.txt");

    void part1() {
        System.out.println(data.size());
    }

    void part2() {
    }

    static String example = """

            """;

}
