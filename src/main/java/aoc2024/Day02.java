package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 2: Red-Nosed Reports ---
// https://adventofcode.com/2024/day/2

class Day02 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 269
        timed(() -> new Day02().part1());
        System.out.println("=== part 2"); // 337
        timed(() -> new Day02().part2());
    }

    List<String> data = file2lines("input02.txt");
//    List<String> data = Util.splitLines(example);
    
    List<List<Integer>> reports = data.map(s -> Util.string2ints(s));

    boolean safe(List<Integer> d) {
        var dec = d.sliding(2).forAll(x -> x.get(0) > x.get(1));
        var inc = d.sliding(2).forAll(x -> x.get(0) < x.get(1));
        var dst = d.sliding(2).map(x -> Math.abs(x.get(0) - x.get(1))).toList();
        var d1 = dst.forAll(x -> x >= 1);
        var d3 = dst.forAll(x -> x <= 3);
        return (dec || inc) && d1 && d3;
    }

    boolean safe2(List<Integer> d) {
        var dd = List.range(0, d.size()).map(i -> d.removeAt(i));
        return dd.exists(this::safe);
    }

    void part1() {
        var r = reports.count(this::safe);
        System.out.println(r);
    }

    void part2() {
        var r = reports.count(this::safe2);
        System.out.println(r);
    }

    static String example = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;
}
