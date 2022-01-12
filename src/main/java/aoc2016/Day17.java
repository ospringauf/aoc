package aoc2016;

import java.security.MessageDigest;

import common.AocPuzzle;
import common.Point;
import common.Util;
import io.vavr.collection.List;
import io.vavr.control.Try;

// --- Day 17: Two Steps Forward ---
// https://adventofcode.com/2016/day/17

class Day17 extends AocPuzzle {

    String passcode = "yjjvjgan";
    MessageDigest md = Try.of(() -> MessageDigest.getInstance("MD5")).get();

    Point next(Point p, Character d) {
        return switch (d) {
        case 'U' -> p.north();
        case 'D' -> p.south();
        case 'L' -> p.west();
        case 'R' -> p.east();
        default -> throw new IllegalArgumentException("Unexpected value: " + d);
        };
    }

    List<Character> open(Point p, String path) {
        md.update((passcode + path).getBytes());
        var hash = Util.encodeHexString(md.digest());

        List<Character> l = List.empty();
        var open = List.of('b', 'c', 'd', 'e', 'f');
        if (open.contains(hash.charAt(0)) && p.y() > 0)
            l = l.append('U');
        if (open.contains(hash.charAt(1)) && p.y() < 3)
            l = l.append('D');
        if (open.contains(hash.charAt(2)) && p.x() > 0)
            l = l.append('L');
        if (open.contains(hash.charAt(3)) && p.x() < 3)
            l = l.append('R');
        return l;
    }

    int best = Integer.MAX_VALUE;
    static final Point TARGET = Point.of(3, 3);

    String walk1(Point p, String path) {
        if (p.equals(TARGET)) {
            if (path.length() < best) {
                System.out.println("best: " + path);
                best = path.length();
            }
            return path;
        }

        if (path.length() > best)
            return null;

        var doors = open(p, path);
        var x = doors.map(dir -> walk1(next(p, dir), path + dir)).filter(w -> w != null).minBy(w -> w.length());
        return x.getOrElse((String) null);
    }

    String walk2(Point p, String path) {
        if (p.equals(TARGET)) {
            return path;
        }

        var doors = open(p, path);
        var x = doors.map(dir -> walk2(next(p, dir), path + dir)).filter(w -> w != null).maxBy(w -> w.length());
        return x.getOrElse((String) null);
    }

    void part1() {
        System.out.println(walk1(Point.of(0, 0), ""));
    }

    void part2() {
        System.out.println(walk2(Point.of(0, 0), "").length());
    }

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day17().part1());

        System.out.println("=== part 2");
        timed(() -> new Day17().part2());
    }
}
