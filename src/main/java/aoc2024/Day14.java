package aoc2024;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 14: Restroom Redoubt ---
// https://adventofcode.com/2024/day/14

class Day14 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 226236192
        timed(() -> new Day14().part1());
        System.out.println("=== part 2"); // 8168
        timed(() -> new Day14().part2());
    }

    List<String> data = file2lines("input14.txt");
//    List<String> data = Util.splitLines(example);

    static Point parsePoint(String s) {
        var f = s.split("=")[1].split(",");
        return Point.of(Integer.valueOf(f[0]), Integer.valueOf(f[1]));
    }

    record Robot(Point p, Point v) {
        static Robot parse(String s) {
            var f = s.split(" ");
            return new Robot(parsePoint(f[0]), parsePoint(f[1]));
        }

        Robot stepModulo(int mx, int my) {
            var p1 = p.plus(v).modulo(mx, my);
            return new Robot(p1, v);
        }

        int quadrant(int w, int h) {
            var cx = w / 2;
            var cy = h / 2;
            if (p.x() == cx || p.y() == cy)
                return 0;
            var l = p.x() < cx;
            var t = p.y() < cy;

            return l ? (t ? 1 : 2) : (t ? 3 : 4);
        }
    }

    void part1() {
        var robots = data.map(Robot::parse);

        var bb = BoundingBox.of(robots.map(r -> r.p).toJavaList());
        var w = bb.width();
        var h = bb.height();

        for (int i = 0; i < 100; ++i)
            robots = robots.map(r -> r.stepModulo(w, h));

//        var m = new PointMap<Integer>();
//        robots.forEach(r -> m.put(r.p, m.getOrDefault(r.p, 0) + 1));
//        m.print();

        var q = robots.map(r -> r.quadrant(w, h));

        var res = List.of(1, 2, 3, 4).map(i -> q.count(x -> x == i)).product();
        System.out.println(res);
    }

    boolean detectStructure(List<Robot> robots, int w, int h, int threshold) {
        var lines = List.range(0, h).map(y -> robots.count(r -> r.p.y() == y));
        return (lines.max().get() > threshold);
    }

    void part2() {
        var robots = data.map(Robot::parse);

        var w = robots.map(r -> r.p.x()).max().get() + 1;
        var h = robots.map(r -> r.p.y()).max().get() + 1;
        System.out.println(robots);
        System.out.println(Point.of(w, h));

        for (int i = 1; i < 8200; ++i) {
            robots = robots.map(r -> r.stepModulo(w, h));
            if (detectStructure(robots, w, h, 30)) {
                System.out.println();
                System.out.println(i);
                var m = new PointMap<Character>();
                robots.forEach(r -> m.put(r.p, '#'));
                m.print(' ');

            }
        }
    }

    static String example = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """;
}
