package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 8:  ---
// https://adventofcode.com/2023/day/8

class Day08 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day08().part1());
        System.out.println("=== part 2");
        timed(() -> new Day08().part2());
    }

//    List<String> data = file2lines("input08.txt");
	List<String> data = Util.splitLines(example);

    void part1() {
    }

    void part2() {
    }

    static String example = """
""";
}
