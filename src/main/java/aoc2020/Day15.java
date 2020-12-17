package aoc2020;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 15: Rambunctious Recitation ---
// https://adventofcode.com/2020/day/15

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day15 extends AocPuzzle {

    public static void main(String[] args) {
    	var t0 = System.currentTimeMillis();

        System.out.println("=== part 1"); // 763
        new Day15().part1();
        System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);

        System.out.println("=== part 2"); // 1876406
        new Day15().part2();
        
        System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
    }

    // List<Integer> data = List.of(0, 3, 6);
    List<Integer> data = List.of(0, 14, 1, 3, 7, 9);

    void part1() {
        // use (linked) list, last spoken number is first in list
        var spoken = data.reverse();

        int end = 2020;
        var last = spoken.head();
        int i = spoken.size();

        while (i < end) {
            int next = 0;
            if (spoken.tail().contains(last)) {
                var a = spoken.indexOf(last);
                int age = spoken.subSequence(a+1).indexOf(last) + 1;
                next = age;
            }

            spoken = spoken.prepend(next);
            last = next;
            i++;
        }
        System.out.println(i + " --> " + last);
    }

    void part2() {

        int end = 30000000;
        // int end = 2020; // -> 763

        Map<Integer, Integer> spokenLast = new HashMap<>();
        Map<Integer, Integer> spokenBefore = new HashMap<>();

        int i = 0;
        int last = 0;

        while (i < end) {
            int next = 0;

            if (i < data.size())
                next = data.get(i);
            else {
                // last was spoken before?
                int sb = spokenBefore.getOrDefault(last, -1);
                if (sb >= 0) {
                    int sl = spokenLast.get(last);
                    next = sl - sb;
                }
            }

            // shift "last" -> "before last"
            spokenBefore.put(next, spokenLast.getOrDefault(next, -1));
            spokenLast.put(next, i);
            last = next;
            i++;
        }
        System.out.println(end + " --> " + last);
    }
    

//    void part2a() {
//
//        int end = 30000000;
//        // int end = 2020; // -> 763
//
//        int[] spokenLast = new int[end];
//        int[] spokenBefore = new int[end];
//
//        int i = 0;
//        int last = 0;
//
//        while (i < data.size()) {
//            int next = data.get(i);
//
//            // shift "last" -> "before last"
//            spokenBefore[next] = spokenLast[next];
//            spokenLast[next] = i+1;
//            last = next;
//            i++;
//        }
//
//        while (i < end) {
//            int next = 0;
//
//            // last was spoken before?
//            int sb = spokenBefore[last] -1;
//            if (sb >= 0) {
//                int sl = spokenLast[last]-1;
//                next = sl - sb;
//            }
//
//            // shift "last" -> "before last"
//            spokenBefore[next] = spokenLast[next];
//            spokenLast[next] = i+1;
//            last = next;
//            i++;
//        }
//        System.out.println(end + " --> " + last);
//    }
}
