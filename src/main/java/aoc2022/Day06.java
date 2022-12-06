package aoc2022;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 6: Tuning Trouble ---
// https://adventofcode.com/2022/day/6

class Day06 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new Day06().part1();
        System.out.println("=== part 2");
        new Day06().part2();
    }

    String data = file2string("input06.txt");
    // String data = example;

    int findMarker(int len) {
        var buf = List.ofAll(data.toCharArray());
        var p = List.range(0, buf.length()).find(i -> isMarker(buf, i, len));
        return p.get() + len;
    }

    boolean isMarker(List<Character> buf, int pos, int len) {
        return buf.drop(pos)
                .take(len)
                .distinct()
                .length() == len;
    }

    void part1() {
        System.out.println(findMarker(4));
    }

    void part2() {
        System.out.println(findMarker(14));
    }

    static String example = """
            nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg
                        """;

}
