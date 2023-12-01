package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 2:  ---
// https://adventofcode.com/2023/day/2

class Day02 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day02().part1());
        System.out.println("=== part 2");
        timed(() -> new Day02().part2());
    }

//    List<String> data = file2lines("input00.txt");
	List<String> data = Util.splitLines(example);

    void part1() {
    }

    void part2() {
    }

    static String example = """
""";
}
