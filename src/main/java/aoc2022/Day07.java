package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

// --- Day 7: No Space Left On Device ---
// https://adventofcode.com/2022/day/7

class Day07 extends AocPuzzle {

    public static void main(String[] args) {
        new Day07().solve();
    }

    // List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input07.txt");

    void solve() {
        List<String> path = List.empty();
        HashMap<String, Integer> size = HashMap.empty();

        String current = "";
        for (var l : data) {
            var s = split(l, " ");

            if ("$".equals(s.s(0))) {
                if ("cd".equals(s.s(1))) {
                    var into = s.s(2);
                    if ("..".equals(into)) {
                        // cd up
                        var subdirSize = size.getOrElse(current, 0);
                        path = path.pop();
                        current = path.head();
                        size = size.put(current, size.getOrElse(current, 0) + subdirSize);
                    } else {
                        // cd down
                        current = current + into + "/"; // "/" becomes "//", so what
                        path = path.push(current);
                    }
                }
            } else if ("dir".equals(s.s(0))) {

            } else {
                // ls output for "normal" file
                size = size.put(current, size.getOrElse(current, 0) + s.i(0));
            }
        }

        // climb back up to root in order to sum up the dir sizes
        while (path.size() > 1) {
            var subdirSize = size.getOrElse(current, 0);
            path = path.pop();
            current = path.head();
            size = size.put(current, size.getOrElse(current, 0) + subdirSize);
        }

        // System.out.println(size);
        System.out.println("=== part 1"); // 1182909

        var result1 = size.filter((d, s) -> s <= 100000).map(t -> t._2).sum();
        System.out.println(result1);

        System.out.println("=== part 2"); // 2832508

        var total = 70000000;
        var update = 30000000;

        var free = total - size.getOrElse("//", 0);
        var need = update - free;
        System.out.println("must delete >= " + need);

        var result2 = size.filter((d, s) -> s >= need)
                .map(t -> t._2)
                .sorted()
                .head();
        var dir = size.find(t -> t._2 == result2).get();
        System.out.println("delete: " + dir);
    }

    static String example = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
                        """;

}
