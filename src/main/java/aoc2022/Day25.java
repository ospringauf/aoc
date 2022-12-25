package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 25: Full of Hot Air ---
// https://adventofcode.com/2022/day/25

class Day25 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        new Day25().part1();
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input25.txt");

    long decimal(String s) {
        var digits = List.ofAll(s.toCharArray()).reverse();
        long v = 1;
        long result = 0;
        while (!digits.isEmpty()) {
            Character d = digits.head();
            result += switch (d) {
            case '-' -> -v;
            case '=' -> -2 * v;
            default -> (d - '0') * v;
            };
            digits = digits.tail();
            v *= 5;
        }
        return result;
    }

    String snafu(long n) {
        String result = "";

        while (n != 0) {
            int d = (int) (n % 5);
            if (d >= 3) {
                d -= 5;
                n += 5;
            }
            n = n / 5;
            var c = switch (d) {
            case -2 -> '=';
            case -1 -> '-';
            default -> (char) ('0' + d);
            };
            result = c + result;
        }
        return result;
    }

    void part1() {
//        System.out.println(decimal("2=-01"));
//        System.out.println(decimal("1=-0-2"));
//        System.out.println(snafu(12345));
        
        var sum = data.map(this::decimal).sum().longValue();
        System.out.println(sum);
        System.out.println(snafu(sum));
    }

    static String example = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122
                        """;

}
