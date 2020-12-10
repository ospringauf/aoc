package aoc2020;

import java.util.HashMap;
import java.util.Map;

import io.vavr.collection.List;

// --- Day 10: Adapter Array ---
// https://adventofcode.com/2020/day/10

@SuppressWarnings({ "deprecation", "preview" })
class Day10 extends AocPuzzle {

    public static void main(String[] args) {

        System.out.println("=== part 1"); // 1984
        new Day10().part1();

        System.out.println("=== part 1a"); // 1984
        new Day10().part1a();

        System.out.println("=== part 2"); // 3543369523456
        new Day10().part2();
    }

//    final List<Integer> adapters = List.of(example2.split("\n")).map(Integer::valueOf);
    final List<Integer> adapters = ints("input10.txt");

    void part1() {
        int target = adapters.max().get() + 3;

        int curr = 0;
        List<Integer> chain = List.of(curr);
        while (curr < target - 3) {
            int last = curr;
            curr = adapters.filter(a -> a > last & a <= last + 3).min().get();
            chain = chain.append(curr);
        }
        chain = chain.append(target);
        // System.out.println(adapters);

        var diffs = chain.sliding(2).map(w -> w.get(1) - w.get(0)).toList();
        System.out.println(diffs);
        var d1 = diffs.count(x -> x == 1);
        var d3 = diffs.count(x -> x == 3);
        System.out.println(d1 * d3);

    }

    void part1a() {
        var chain = List.of(0).appendAll(adapters).sorted();

        var diffs = chain.sliding(2).map(w -> w.get(1) - w.get(0)).toList();
        var d1 = diffs.count(x -> x == 1);
        var d3 = diffs.count(x -> x == 3) + 1;
        System.out.println(d1 * d3);
    }

    void part2() {
        var target = adapters.max().get();

        long n = countOptions(0, target);
//        System.out.println(cache);
        System.out.println(n);
    }


    final Map<Integer, Long> cache = new HashMap<>();

    long countOptions(int current, int target) {
        if (cache.containsKey(current))
            return cache.get(current);

        long n;
        if (current == target) 
            n = 1;
        else
            n = adapters
            	.filter(a -> a > current && a <= current + 3)
            	.map(a -> countOptions(a, target))
            	.sum().longValue();

        cache.put(current, n);
        return n;
    }

    static String example2 = """
            28
            33
            18
            42
            31
            14
            46
            20
            48
            47
            24
            23
            49
            45
            19
            38
            39
            11
            1
            32
            25
            35
            8
            17
            7
            9
            4
            2
            34
            10
            3
            			""";

    static String example1 = """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            		""";

}