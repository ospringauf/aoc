package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day x:  ---
// https://adventofcode.com/2022/day/x

class Day22 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new Day22().part1();
        System.out.println("=== part 2");
        new Day22().part2();
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
