package aoc2016;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Stream;

// --- Day 15: Timing is Everything ---
// https://adventofcode.com/2016/day/15

class Day15 extends AocPuzzle {

    record Disc(int nr, int positions, int initial) {
        static Disc parse(String s) {
            var r = split(s, "\\W");
            return new Disc(r.i(2), r.i(4), r.i(15));
        }

        boolean pass(int t) {
            return (initial + t + nr) % positions == 0;
        }
    }

    void part1() {
        var discs = Util.splitLines(input).map(Disc::parse);
        var r = Stream.iterate(0, i -> i + 1).find(i -> discs.forAll(d -> d.pass(i)));
        System.out.println(r);
    }

    void part2() {
        var discs0 = Util.splitLines(input).map(Disc::parse);
        var discs = discs0.append(new Disc(7, 11, 0));
        var r = Stream.iterate(0, i -> i + 1).find(i -> discs.forAll(d -> d.pass(i)));
        System.out.println(r);
    }

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day15().part1());

        System.out.println("=== part 2");
        timed(() -> new Day15().part2());
    }

    String example = """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.
            	        """;

    String input = """
            Disc #1 has 17 positions; at time=0, it is at position 1.
            Disc #2 has 7 positions; at time=0, it is at position 0.
            Disc #3 has 19 positions; at time=0, it is at position 2.
            Disc #4 has 5 positions; at time=0, it is at position 0.
            Disc #5 has 3 positions; at time=0, it is at position 0.
            Disc #6 has 13 positions; at time=0, it is at position 5.
            	        """;
}
