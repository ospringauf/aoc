package aoc2016;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 19: An Elephant Named Joseph ---
// https://adventofcode.com/2016/day/19

class Day19 extends AocPuzzle {

    int N = 3004953;

    void part1() {
        var elf = new int[N];
        Arrays.fill(elf, 1);

        int i = 0;
        while (true) {
            if (elf[i] != 0) {
                var j = (i + 1) % N;
                while (elf[j] == 0)
                    j = (j + 1) % N;
                elf[i] += elf[j];
                elf[j] = 0;
                if (elf[i] == N)
                    break;
            }
            i = (i + 1) % N;
        }
        System.out.println(i + 1);
    }

    void part2() {
        HashMap<Integer, Integer> next = new HashMap<>();
        HashMap<Integer, Integer> prev = new HashMap<>();
        List.range(0, N).forEach(i -> next.put(i, (i + 1) % N));
        List.range(0, N).forEach(i -> prev.put(i, (i - 1 + N) % N));

        var i = N / 2;
        var elves = N;
        while (next.get(i) != i) {
            // System.out.println("steal from " + (i+1));
            var p = prev.get(i);
            var n = next.get(i);
            next.put(p, n);
            prev.put(n, p);
            i = n;
            elves--;
            if (elves % 2 == 0)
                i = next.get(i);
        }
        System.out.println(i + 1);
    }

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1815603
        timed(() -> new Day19().part1());

        System.out.println("=== part 2"); // 1410630
        timed(() -> new Day19().part2());
    }
}
