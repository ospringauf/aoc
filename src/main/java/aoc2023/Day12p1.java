package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 12:  ---
// https://adventofcode.com/2023/day/12

class Day12p1 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day12p1().part1());
        System.out.println("=== part 2");
        timed(() -> new Day12p1().part2());
    }

    List<String> data = file2lines("input12.txt");
//    List<String> data = Util.splitLines(example);

    List<Integer> counts(String s) {
        List<Integer> r = List.empty();
        int i = 0;
        int l = s.length();
        while (i < l) {
            while (i < l && s.charAt(i) == '.')
                i++;
            if (i >= l)
                break;
            int j = i;
            while (i < l && s.charAt(i) == '#')
                i++;
            r = r.append(i - j);
            if (i >= l)
                break;
        }
        return r;
    }

    void part1() {
        System.out.println();
        
        System.out.println("---");
        
        int sum =0;
        for (var l : data) {
            var f = l.split(" ");
            var springs = f[0];
            var dmg = Util.string2ints(f[1].replace(',', ' '));

            // System.out.println(springs + " -> " + dmg);
//            System.out.println(springs + " -> " + counts(springs) + " / " + dmg.eq(counts(springs)));

            var unk = unknown(springs);
            int match = 0;
            for (int x = 0; x < (1 << unk); ++x) {
                var sx = fill(springs, x);
                if (dmg.eq(counts(sx))) match++;
            }
            
            System.out.println(springs + " -> " + match);
            sum+=match;
        }
        
        System.out.println(sum);
    }

    private String fill(String s, int x) {
        var r = "";
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '?') {
                r = r + ((x % 2 == 1) ? "#" : ".");
                x = x / 2;
            } else {
                r = r + s.charAt(i);
            }
        }

        return r;
    }

    private int unknown(String s) {
        int r = 0;
        for (int i = 0; i < s.length(); ++i)
            if (s.charAt(i) == '?')
                r++;
        return r;
    }

    void part2() {
    }

    static String example0 = """
            #.#.### 1,1,3
            .#...#....###. 1,1,3
            .#.###.#.###### 1,3,1,6
            ####.#...#... 4,1,1
            #....######..#####. 1,6,5
            .###.##....# 3,2,1
                        """;

    static String example = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;
}
