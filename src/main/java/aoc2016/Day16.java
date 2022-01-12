package aoc2016;

import common.AocPuzzle;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

// --- Day 16: Dragon Checksum ---
// https://adventofcode.com/2016/day/16

class Day16 extends AocPuzzle {

    void part1() {
        // System.out.println(checksum(fill("10000", 20)));
        System.out.println(checksum(fillDisk("10001110011110000", 272)));
    }

    void part2() {
        System.out.println(checksum(fillDisk("10001110011110000", 35651584)));
    }

    private List<Character> fillDisk(String s, int size) {
        var all = List.ofAll(s.toCharArray());
        while (all.size() < size) {
            all = all.append('0').appendAll(all.reverse().map(c -> (c == '1') ? '0' : '1'));
            System.out.println(100*all.size()/size);
        }

        return all.take(size);
    }

    String checksum(Seq<Character> all) {
        do {
            all = all.grouped(2).map(g -> (g.distinct().size() == 1) ? '1' : '0').toList();
        } while (all.size() % 2 == 0);
        return all.mkString();
    }

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 10010101010011101
        timed(() -> new Day16().part1());

        System.out.println("=== part 2"); // 01100111101101111
        timed(() -> new Day16().part2());
    }
}
