package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function2;
import io.vavr.collection.List;

//--- Day 11: Plutonian Pebbles ---
// https://adventofcode.com/2024/day/11

class Day11 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 199986
        timed(() -> new Day11().part1());
        System.out.println("=== part 2"); // 236804088748754
        timed(() -> new Day11().part2());
    }

    List<Long> stones = Util.string2longs(file2string("input11.txt"));
//	List<Long> stones = Util.string2longs(example);

    void part1() {
        System.out.println(stones);

        for (int i = 0; i < 25; ++i) {
            stones = stones.flatMap(this::blink);
        }
        System.out.println(stones.size());
    }

    List<Long> blink(Long stone) {
        if (stone == 0L)
            return List.of(1L);
        var s = Long.toString(stone);
        var sl = s.length();
        if (sl % 2 == 0) {
            return List.of(Long.valueOf(s.substring(0, sl / 2)), Long.valueOf(s.substring(sl / 2)));
        }
        return List.of(stone * 2024);
    }


    long stoneCount(long stone, int blinks) {
        if (blinks == 0)
            return 1;

        var blinkOnce = blink(stone);
        var r = blinkOnce.map(s -> stoneCountMem.apply(s, blinks - 1)).sum();
        return r.longValue();
    }
    
    Function2<Long, Integer, Long> stoneCountMem  = Function2.of(this::stoneCount).memoized();

    void part2() {
        System.out.println(stones);

        var r = stones.map(s -> stoneCountMem.apply(s, 75)).sum();
        System.out.println(r);
    }

    static String example = "125 17";

}
