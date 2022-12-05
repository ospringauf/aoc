package aoc2022;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

// --- Day 4: Camp Cleanup ---
// https://adventofcode.com/2022/day/4

class Day04 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 464
        new Day04().part1();
        System.out.println("=== part 2"); // 770
        new Day04().part2();
    }

    List<String> data = file2lines("input04.txt");
//    List<String> data = Util.splitLines(example);

    record Pair(Seq<Integer> range1, Seq<Integer> range2) {
        static Pair parse(String s) {
            var sp = split(s, "[-,]");
            return new Pair(
                    List.rangeClosed(sp.i(0), sp.i(1)), 
                    List.rangeClosed(sp.i(2), sp.i(3)));
        }

        boolean contained() {
            return range1.containsAll(range2) || range2.containsAll(range1);
        }

        boolean overlapping() {
            return range1.toSet().intersect(range2.toSet()).nonEmpty();
        }
    }

    void part1() {
        var result = data.map(Pair::parse).count(Pair::contained);
        System.out.println(result);
    }

    void part2() {
        var result = data.map(Pair::parse).count(Pair::overlapping);
        System.out.println(result);
    }

    static String example = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
                        """;

}
