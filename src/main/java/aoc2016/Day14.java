package aoc2016;

import java.security.MessageDigest;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;
import io.vavr.control.Try;

// --- Day 14: One-Time Pad ---
// https://adventofcode.com/2016/day/14

class Day14 extends AocPuzzle {

    String salt = "ahsbgdzn";
    // String salt = "abc";
    MessageDigest md = Try.of(() -> MessageDigest.getInstance("MD5")).get();

    String hash(int idx) {
        md.update((salt + Integer.toString(idx)).getBytes());
        return Util.encodeHexString(md.digest());
    }

    String hash(int idx, int rounds) {
        var x = salt + Integer.toString(idx);
        for (int i = 0; i < rounds; ++i) {
            md.update((x).getBytes());
            x = Util.encodeHexString(md.digest());
        }
        return x;
    }

    void solve(int rounds) {
        int found = 0;
        int i = 0;
        int j = 0;
        List<String> buf = List.empty();
        while (found < 64) {

            while (buf.size() < 1001)
                buf = buf.append(hash(j++, rounds));

            var h = buf.head();
            buf = buf.pop();
            var foak = fives(threes(h));
            var match = buf.take(1000).exists(x -> x.contains(foak));
            if (match) {
                found++;
                System.out.print("   " + i);
            }
            i++;
        }
        System.out.println();
        System.out.println(i - 1);
    }

    String fives(Character c) {
        return "" + c + c + c + c + c;
    }

    Character threes(String h) {
        return List.ofAll(h.toCharArray()).sliding(3).filter(l -> l.distinct().size() == 1).map(
                l -> l.head()).headOption().getOrElse('x');
    }

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day14().solve(1));

        System.out.println("=== part 2");
        timed(() -> new Day14().solve(2017));
    }
}
