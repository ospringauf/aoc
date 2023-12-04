package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 4: Scratchcards ---
// https://adventofcode.com/2023/day/4

class Day04 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 28538
        timed(() -> new Day04().part1());
        System.out.println("=== part 2"); // 9425061
        timed(() -> new Day04().part2());
    }

    List<String> data = file2lines("input04.txt");
//	List<String> data = Util.splitLines(example);

    record Card(List<Integer> have, List<Integer> winning) {
        static Card parse(String s) {
            var f = split(s, "[:|]");
            return new Card(Util.string2ints(f.s(1)), Util.string2ints(f.s(2)));
        }

        int val() {
            return (1 << matches()) / 2;
        }

        int matches() {
            return have.filter(winning::contains).size();
        }
    }

    void part1() {
        var cards = data.map(Card::parse);
        var points = cards.map(c -> c.val());
        System.out.println(points.sum());
    }

    void part2() {
        var cards = data.map(Card::parse).toArray();
        var copies = cards.map(c -> 1L);

        for (int i = 0; i < cards.size(); ++i) {
            var m = cards.get(i).matches();
            for (int j = 1; j <= m; ++j)
                copies = copies.update(i + j, copies.get(i + j) + copies.get(i));
        }

        System.out.println(copies.sum());
    }

    
    static String example = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;
}
