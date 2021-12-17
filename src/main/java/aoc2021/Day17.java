package aoc2021;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

// --- Day 17: Trick Shot ---
// https://adventofcode.com/2021/day/17

class Day17 extends AocPuzzle {

    static BoundingBox example = new BoundingBox(20, 30, -10, -5);

    // target area: x=192..251, y=-89..-59
    static BoundingBox myTarget = new BoundingBox(192, 251, -89, -59);
    
    static BoundingBox target = myTarget;

    
    record Probe(Point s, Point v) {
        
        Probe nextPos() {
            int vx = v.x();
            vx = (int) (vx == 0 ? 0 : (Math.abs(vx) - 1) * Math.signum(vx));
            int vy = v.y() - 1;
            return new Probe(s.translate(v.x(), v.y()), Point.of(vx, vy));
        }
        
        boolean overshoot() {
            // past the target, won't hit anymore
            return s.x() > target.xMax() || (s.y() < target.yMin() && v.y() < 0);
        }
        
        static List<Probe> trajectory(int vx, int vy) {
            var p0 = new Probe(Point.of(0, 0), Point.of(vx, vy));
            var t = Stream.iterate(p0, Probe::nextPos).takeUntil(Probe::overshoot).toList();
            return t;
        }
    }


    boolean hitTarget(List<Probe> trajectory) {
        return trajectory.exists(p -> target.contains(p.s));
    }
    
    int top(List<Probe> trajectory) {
        return trajectory.map(p -> p.s.y()).max().get();
    }

    void part1() {
        // TODO better bounds for initial velocity? not really worth the effort ...
        var r = List.range(0, 300) // vx
                .crossProduct(List.range(-100, 200)) // vy
                .map(v -> Probe.trajectory(v._1, v._2))
                .filter(t -> hitTarget(t))
                .map(t -> top(t))
                .max();

        System.out.println(r);
    }

    void part2() {
        var r = List.range(0, 300) // vx
                .crossProduct(List.range(-100, 200)) // vy
                .map(v -> Probe.trajectory(v._1, v._2))
                .filter(t -> hitTarget(t))
                .size();

        System.out.println(r);
    }

    public static void main(String[] args) {

        System.out.println("=== part 1"); // 3916
        new Day17().part1();

        System.out.println("=== part 2"); // 2986
        new Day17().part2();
    }
}
