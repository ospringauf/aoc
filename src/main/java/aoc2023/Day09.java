package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 9: Mirage Maintenance ---
// https://adventofcode.com/2023/day/9

class Day09 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1921197370
        timed(() -> new Day09().part1());
        System.out.println("=== part 2"); // 1124
        timed(() -> new Day09().part2());
    }

    List<String> data = file2lines("input09.txt");
//	List<String> data = Util.splitLines(example);

    int extrapolateLast(List<Integer> l) {
        if (l.forAll(x -> x==0)) {
            return 0;
        } else {
            return l.last() + extrapolateLast(diffs(l));
        }
    }

    int extrapolateFirst(List<Integer> l) {
        if (l.forAll(x -> x==0)) {
            return 0;
        } else {
            return l.head() - extrapolateFirst(diffs(l));
        }
    }

    List<Integer> diffs(List<Integer> n) {
        return n.sliding(2).map(x -> x.get(1) - x.get(0)).toList();
    }

    void part1() {
        var r = data.map(Util::string2ints).map(this::extrapolateLast);
        System.out.println(r.sum());
    }

    void part2() {
        var r = data.map(Util::string2ints).map(this::extrapolateFirst);
        System.out.println(r.sum());
    }

    static String example = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;
}
