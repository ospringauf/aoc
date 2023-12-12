package aoc2023;

import java.util.HashMap;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 12: Hot Springs ---
// https://adventofcode.com/2023/day/12

class Day12 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== test");
        new Day12().test();
        System.out.println("=== part 1");
        timed(() -> new Day12().part1()); // 7674
        System.out.println("=== part 2");
        timed(() -> new Day12().part2()); // 4443895258186
    }

//    List<String> data = file2lines("input12.txt");
    List<String> data = Util.splitLines(example);
    boolean debug = data.size() < 10;

    record Problem(String s, List<Integer> l) {
        static Problem parse(String x) {
            var f = x.split(" ");
            var s = f[0];
            var dmg = Util.string2ints(f[1].replaceAll(",", " "));
            return new Problem(s, dmg);
        }

        Problem unfold() {
            var s5 = s + "?" + s + "?" + s + "?" + s + "?" + s;
            var l5 = l.appendAll(l).appendAll(l).appendAll(l).appendAll(l);
            return new Problem(s5, l5);
        }

        public Problem skip1() {
            return new Problem(s.substring(1), l);
        }

        public Problem consumeGroup() {
            var damaged = l.head();
            var s2 = (damaged >= s.length()) ? "" : s.substring(damaged + 1); // also skip the trailing "."
            return new Problem(s2, l.tail());
        }

        public boolean nextGroupMatches() {
            var damaged = l.head();

            // check if s starts with a group of damaged springs like "####."
            for (int i = 0; i < damaged; ++i) {
                if (i >= s.length() || s.charAt(i) == '.')
                    return false;
            }
            return (damaged == s.length() || s.charAt(damaged) != '#');
        }
    }

    int minDamaged(String s) {
        int r = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '#')
                r++;
        }
        return r;
    }

    HashMap<Problem, Long> cache = new HashMap<>();

    // caching wrapper around "solve"
    long csolve(Problem p) {
//        var r = cache.computeIfAbsent(p, this::solve);
//        return r;
        var cr = cache.getOrDefault(p, null);
        if (cr != null)
            return cr;

        var r = solve(p);
        cache.put(p, r);
        return r;
    }

    long solve(Problem p) {
        // no more groups - all remaining positions must be "not damaged"
        if (p.l.isEmpty()) {
            return minDamaged(p.s) == 0 ? 1 : 0;
        }
        // no more positions
        if (p.s.length() == 0) {
            return 0;
        }

        var c = p.s.charAt(0);

        // .
        if (c == '.')
            return csolve(p.skip1());

        // next group of damaged springs like "####." matches the current position?
        var match = p.nextGroupMatches();

        // #
        if (c == '#')
            return match ? csolve(p.consumeGroup()) : 0;

        // unknown, could be operational or damaged
        // ? = .
        var r1 = csolve(p.skip1());
        // ? = #
        var r2 = match ? csolve(p.consumeGroup()) : 0;
        return r1 + r2;
    }

    long solve(String s, List<Integer> l) {
        return solve(new Problem(s, l));
    }

    void test() {
//      var x = solve("????????", List.of(2,1));
//      var x = solve("##", List.of(1));
//      var x = solve("????", List.of(1,1));
//      var x = solve("?????", List.of(2,1));
        var x = solve("?###????????", List.of(3, 2, 1));
        System.out.println(x);
    }

    void part1() {
        long sum = 0L;
        for (var line : data) {
            var p = Problem.parse(line);
            var x = solve(p);
            if (debug)
                System.out.println(p + " --> " + x);
            sum += x;
        }
        System.out.println(sum);
    }

    void part2() {
        long sum = 0L;
        for (var line : data) {
            var p0 = Problem.parse(line);
            var p = p0.unfold();
            var x = solve(p);
            if (debug)
                System.out.println(p0 + " --> " + x);
            sum += x;
        }
        System.out.println(sum);
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
