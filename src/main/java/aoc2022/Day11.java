package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 11:  ---
// https://adventofcode.com/2022/day/11

class Day11 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new Day11().part1();
        System.out.println("=== part 2");
        new Day11().part2();
    }

    List<String> data = Util.splitLines(example);
//    List<String> data = file2lines("input0x.txt");

    void part1() {
        System.out.println(data.size());
    }

    void part2() {
    }

    static String example = """

            """;

}
