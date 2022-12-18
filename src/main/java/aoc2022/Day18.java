package aoc2022;

import common.AocPuzzle;
import common.Pos3;
import common.Range;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;

// --- Day 18: Boiling Boulders ---
// https://adventofcode.com/2022/day/18

class Day18 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 4192 (64)
        timed(() -> new Day18().part1());
        System.out.println("=== part 2"); // 2520 (58)
        timed(() -> new Day18().part2());
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input18.txt");

    Set<Pos3> lava = data.map(Pos3::parse).toSet();

    record Box(Range xr, Range yr, Range zr) {
        boolean contains(Pos3 p) {
            return xr.contains(p.x()) && yr.contains(p.y()) && zr.contains(p.z());
        }

        public int volume() {
            return xr.size() * yr.size() * zr.size();
        }

        boolean isEdge(Pos3 p) {
            return xr.isEdge(p.x()) || yr.isEdge(p.y()) || zr.isEdge(p.z());
        }

        List<Pos3> points() {
            return xr.values().flatMap(x -> yr.values().flatMap(y -> zr.values().map(z -> new Pos3(x, y, z))));
        }

        Set<Pos3> floodFill(Pos3 p, Set<Pos3> blocked) {
            Set<Pos3> next = HashSet.of(p);
            Set<Pos3> filled = HashSet.empty();
            while (next.nonEmpty()) {
                filled = filled.addAll(next);
                next = filled.flatMap(Pos3::neighbors6);
                next = next.filter(this::contains);
                next = next.removeAll(blocked);
                next = next.removeAll(filled);
            }
            return filled;
        }

        static Box around(Set<Pos3> s) {
            var xr = Range.of(s.map(c -> c.x()));
            var yr = Range.of(s.map(c -> c.y()));
            var zr = Range.of(s.map(c -> c.z()));
            return new Box(xr, yr, zr);
        }
    }

    void part1() {
        System.out.println(exposedSurface(lava));
    }

    long exposedSurface(Set<Pos3> droplets) {
        System.out.println("checking exposed area of " + droplets.size() + " droplets");

        return droplets.toList().map(d -> 6 - d.neighbors6().count(n -> droplets.contains(n))).sum().longValue();
    }

    void part2() {
        var box = Box.around(lava);
        System.out.println(box);
        System.out.println("volume: " + box.volume());

        // Idea: check all non-lava points in the box. Flood-fill around each point;
        // if the filled area touches the box edges, none of the points is in a
        // "bubble". Otherwise, all the points are in an enclosed bubble.

        var all = box.points();
        var todo = all.removeAll(lava);
        List<Pos3> bubbles = List.empty();

        // check all "non-cube" points if they are in a bubble
        while (todo.nonEmpty()) {
            var p = todo.head();

            var reachable = box.floodFill(p, lava);
//            System.out.println("hull" + p + " = " + reachable.size());

            if (!reachable.exists(box::isEdge)) {
                bubbles = bubbles.appendAll(reachable);
            }

            todo = todo.tail().removeAll(reachable);
        }

        System.out.println("bubble: " + bubbles.size());
        System.out.println(exposedSurface(lava.addAll(bubbles)));
    }

    static String example = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
                        """;

}
