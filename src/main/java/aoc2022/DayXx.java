package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day x:  ---
// https://adventofcode.com/2022/day/x

class DayXx extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new DayXx().part1();
        System.out.println("=== part 2");
        new DayXx().part2();
    }

    List<Integer> data = file2ints("input0x.txt");

    void part1() {
        System.out.println(data.size());
    }

    void part2() {
    }

    static String example = """

            """;

}
