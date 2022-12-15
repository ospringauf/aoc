package aoc2022;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import common.AocPuzzle;

//--- Day 1: Calorie Counting ---
// https://adventofcode.com/2022/day/1

class Day01 extends AocPuzzle {

    public static void main(String[] args) {
        new Day01().solve();
        
//        new Day01().solveJava();
    }

    String input = file2string("input01.txt");

    void solve() {
        var l = split(input, "\n\n").map(block -> lines(block).map(Integer::parseInt));

        System.out.println("=== part 1"); // 69693
        var sums = l.map(elf -> elf.sum());
        System.out.println(sums.max());

        System.out.println("=== part 2"); // 200945
        var top3 = sums.sorted().reverse().take(3);
        System.out.println(top3.sum());
    }

    // same in pure java syntax
    void solveJava() {
        String[] blocks = input.split("\n\n");
        List<Integer> sums = Arrays.stream(blocks).map(b -> sum(b.split("\n"))).toList();

        Optional<Integer> max = sums.stream().max(Integer::compareTo);
        System.out.println(max);

        IntStream top3 = sums.stream().sorted(Comparator.reverseOrder()).mapToInt(x -> x).limit(3);
        System.out.println(top3.sum());
    }

    int sum(String[] lines) {
        return Arrays.stream(lines).mapToInt(Integer::parseInt).sum();
    }

}
