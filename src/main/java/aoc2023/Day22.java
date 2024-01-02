package aoc2023;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import common.Vec3;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import static java.lang.Math.*;


//--- Day 22: Sand Slabs ---
// https://adventofcode.com/2023/day/22

class Day22 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 418
        timed(() -> new Day22().part1());
        System.out.println("=== part 2"); // 70702
        timed(() -> new Day22().part2());
    }

    List<String> data = file2lines("input22.txt");
//    List<String> data = Util.splitLines(example);
    List<Brick> bricks;

    record Brick(Axis axis, Set<Vec3> cubes) {

        public static List<Brick> all;

        enum Axis {
            X, Y, Z
        };

        public static Brick build(Vec3 a, Vec3 b) {
            var axis = a.x() != b.x() ? Axis.X : a.y() != b.y() ? Axis.Y : Axis.Z;
            return new Brick(axis, points(axis, a, b));
        }

        static Brick parse(String s) {
            var a = s.split("~");
            return Brick.build(Vec3.parse(a[0]), Vec3.parse(a[1]));
        }

        static Set<Vec3> points(Axis axis, Vec3 a, Vec3 b) {
            return switch (axis) {
            case X -> range(a.x(), b.x()).map(x -> Vec3.of(x, a.y(), a.z())).toSet();
            case Y -> range(a.y(), b.y()).map(y -> Vec3.of(a.x(), y, a.z())).toSet();
            case Z -> range(a.z(), b.z()).map(z -> Vec3.of(a.x(), a.y(), z)).toSet();
            };
        }

        static List<Integer> range(int a, int b) {
            var r = List.rangeClosed(min(a, b), max(a, b));
            return r;
        }

        int minZ() {
            return cubes.map(it -> it.z()).min().get();
        }

        public Brick drop(Integer h) {
            return new Brick(axis, cubes.map(c -> c.minus(Vec3.of(0, 0, h))));
        }

        public boolean sitsOn(Brick b) {
            var v1 = Vec3.of(0, 0, 1);
            return cubes.exists(c -> b.cubes.contains(c.minus(v1)));
        }

//        public String toString() {
//            return "" + (char)('A' + all.indexOf(this));
//        }
    }

    static Point xy(Vec3 v) {
        return Point.of(v.x(), v.y());
    }

    void dropAllBricks() {
        bricks = data.map(Brick::parse);
        System.out.println(
                "brick sizes: " + bricks.groupBy(b -> b.cubes.size()).map(t -> Tuple.of(t._1, t._2.size())).toList());

        System.out.println("dropping bricks");
        bricks = bricks.sortBy(Brick::minZ);
        var ground = new PointMap<Integer>();
        List<Brick> restingBricks = List.empty();
        for (var b : bricks) {
            var height = b.cubes.map(c -> (c.z() - 1) - ground.getOrDefault(xy(c), 0)).min().get();
            // System.out.println("dropping " + height + " -> " + b);
            Brick droppedBrick = b.drop(height);
            // update ground map
            for (var c : droppedBrick.cubes) {
                var p = xy(c);
                ground.put(p, max(ground.getOrDefault(p, 0), c.z()));
            }
            restingBricks = restingBricks.append(droppedBrick);
        }
        bricks = restingBricks;
        Brick.all = bricks;
    }

    void part1() {
        dropAllBricks();

        System.out.println("finding safe bricks");

        var siton = bricks.crossProduct().filter(t -> t._1 != t._2).filter(t -> t._1.sitsOn(t._2)).toList();
        // map: brick -> supporting bricks
        var support = bricks.toMap(b -> b, b -> siton.filter(t -> t._1 == b).map(t -> t._2).toList()).toJavaMap();

        int countSafe = 0;
        for (var b : bricks) {
            // is there another brick that is only supported by b (and would now fall)?
            var safe = !bricks.exists(it -> support.get(it).equals(List.of(b)));
            if (safe) {
                countSafe++;
            }
        }
        System.err.println("safe to disintegrate => " + countSafe);
    }

    void part2() {
        dropAllBricks();

        // (b1,b2) = brick 1 sits directly on brick 2
        var siton = bricks.crossProduct().filter(t -> t._1 != t._2).filter(t -> t._1.sitsOn(t._2)).toList();

        System.out.println("counting chain reactions");
        var result = 0;
        int i = 0;
        for (var b : bricks) {
            var falling = chainReaction(b, siton);
            result += falling.size();
            if (i++ % 100 == 0)
                System.out.println(".");
        }
        System.err.println(result);
    }

    Set<Brick> chainReaction(Brick b, List<Tuple2<Brick, Brick>> siton0) {
        Set<Brick> found = HashSet.empty();

        var grounded = bricks.filter(it -> it.minZ() == 1).toSet();
        var candidates = bricks.removeAll(grounded).toSet();
        var siton = siton0.reject(t -> t._2 == b);

        boolean repeat = true;
        while (repeat) {
            var supported = siton.map(t -> t._1).toSet();
            var unsupported = candidates.removeAll(supported);
            siton = siton.reject(t -> unsupported.contains(t._2));
            candidates = supported;

            found = found.addAll(unsupported);
            repeat = unsupported.nonEmpty();
        }
        return found;
    }

    static String example = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;
}
