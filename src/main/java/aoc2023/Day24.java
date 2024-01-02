package aoc2023;

import java.util.function.Function;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 24: Never Tell Me The Odds ---
// https://adventofcode.com/2023/day/24
// see https://www.reddit.com/r/adventofcode/comments/18pnycy/comment/keurt2p/?utm_source=share&utm_medium=web2x&context=3

class Day24 extends AocPuzzle {

    public static void main(String[] args) {
        List<String> exampleData = Util.splitLines(example);
        List<String> realData = new Day24(List.empty()).file2lines("input24.txt");

        System.out.println("=== part 1 example");
//        timed(() -> new Day24(exampleData).part1(7, 27));
        System.out.println("=== part 1 real"); // 16172
        timed(() -> new Day24(realData).part1(200000000000000L, 400000000000000L));
        System.out.println("=== part 2 example"); // 47 = 24+13+10
//        timed(() -> new Day24(exampleData).part2(20));
        System.out.println("=== part 2 real"); // 600352360036779
        timed(() -> new Day24(realData).part2(600));
    }

    static double TOLERANCE = 0.00001;
    List<Hailstone> hailstones;

    record D2(double x, double y) {
        boolean parallel(D2 d) {
            return d.y == y * (d.x / x);
        }

        D2 plus(D2 d) {
            return new D2(x + d.x, y + d.y);
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof D2 d) && deq(x, d.x) && deq(y, d.y);
        }

        boolean exists() {
            return !Double.isNaN(x) && !Double.isNaN(y);
        }

        public String toString() {
            return String.format("%.1f/%.1f", x, y);
        }

        public boolean isInteger() {
            return deq(x, Math.round(x)) && deq(y, Math.round(y));
        }
    }

    record D3(double x, double y, double z) {
        static D3 parse(String[] f) {
            return new D3(Double.parseDouble(f[0].trim()), Double.parseDouble(f[1].trim()),
                    Double.parseDouble(f[2].trim()));
        }

        D2 xy() {
            return new D2(x, y);
        }

        D2 xz() {
            return new D2(x, z);
        }

        D2 zy() {
            return new D2(z, y);
        }

        D3 plus(D3 d) {
            return new D3(x + d.x, y + d.y, z + d.z);
        }

        public String toString() {
            return String.format("%f,%f,%f", x, y, z);
        }

    }

    record Hailstone(D3 pos, D3 v) {
        static Hailstone parse(String s) {
            var f = s.split(" @ ");
            return new Hailstone(D3.parse(f[0].split(", ")), D3.parse(f[1].split(", ")));
        }

        Hailstone plusV(D3 dv) {
            return new Hailstone(pos, v.plus(dv));
        }

        Hailstone2d xy() {
            return new Hailstone2d(pos.xy(), v.xy());
        }

        Hailstone2d xz() {
            return new Hailstone2d(pos.xz(), v.xz());
        }

        Hailstone2d zy() {
            return new Hailstone2d(pos.zy(), v.zy());
        }
    }

    record Hailstone2d(D2 pos, D2 v) {
        Hailstone2d plusV(D2 dv) {
            return new Hailstone2d(pos, v.plus(dv));
        }
    }

    boolean intersectInArea(Hailstone h1, Hailstone h2, long A, long B) {
        var cross = checkIntersect(h1.xy(), h2.xy());
        if (cross) {
            var p = pointOfIntersection(h1.xy(), h2.xy());
            return (A <= p.x && p.x <= B) && (A <= p.y && p.y <= B);
        } else
            return false;
    }

    boolean checkIntersect(Hailstone2d h1, Hailstone2d h2) {
        var p1 = h1.pos;
        var p2 = h2.pos;
        var v1 = h1.v;
        var v2 = h2.v;
        var u = (p1.y * v2.x + v2.y * p2.x - p2.y * v2.x - v2.y * p1.x) / (v1.x * v2.y - v1.y * v2.x);
        var v = (p1.x + v1.x * u - p2.x) / v2.x;
        return Double.isFinite(u) && Double.isFinite(v) && u > 0 && v > 0;
    }

    D2 pointOfIntersection(Hailstone2d h1, Hailstone2d h2) {
        var p1 = h1.pos;
        var p2 = h2.pos;
        var v1 = h1.v;
        var v2 = h2.v;

        D2 p1End = p1.plus(v1); // another point in line p1->n1
        D2 p2End = p2.plus(v2); // another point in line p2->n2

        var m1 = (p1End.y - p1.y) / (p1End.x - p1.x); // slope of line p1->n1
        var m2 = (p2End.y - p2.y) / (p2End.x - p2.x); // slope of line p2->n2

        var b1 = p1.y - m1 * p1.x; // y-intercept of line p1->n1
        var b2 = p2.y - m2 * p2.x; // y-intercept of line p2->n2

        var px = (b2 - b1) / (m1 - m2); // collision x
        var py = m1 * px + b1; // collision y

        return new D2(px, py); // return statement
    }

    Day24(List<String> data) {
        hailstones = data.map(Hailstone::parse);        
    }
    
    void part1(long A, long B) {
        var res = hailstones.crossProduct(2).count(l -> intersectInArea(l.get(0), l.get(1), A, B));
        System.err.println(res / 2);
    }

    public static boolean deq(double a, double b) {
        return Math.abs(b - a) < TOLERANCE;
    }

    void part2(int range) {
        Function<Hailstone, Hailstone2d> toXy = it -> it.xy();
        Function<Hailstone, Hailstone2d> toXz = it -> it.xz();
        Function<Hailstone, Hailstone2d> toZy = it -> it.zy();

        System.out.println("find intersections in x/y");
        var cxy = findIntersections2D(range, toXy);
        System.out.println("find intersections in x/z");
        var cxz = findIntersections2D(range, toXz);
        System.out.println("find intersections in z/y");
        var czy = findIntersections2D(range, toZy);

        var px = cxy.map(it -> it.pos.x).toSet();
        var py = cxy.map(it -> it.pos.y).toSet();
        cxz = cxz.filter(it -> px.contains(it.pos.x));
        czy = czy.filter(it -> py.contains(it.pos.y));

        System.out.println("matching 2d projections");
        for (var c1 : cxy) {
            for (var c2 : cxz) {
                for (var c3 : czy) {
                    var match = true;
                    match &= deq(c1.pos.x, c2.pos.x) && deq(c1.v.x, c2.v.x);
                    match &= deq(c1.pos.y, c3.pos.y) && deq(c1.v.y, c3.v.y);
                    match &= deq(c2.pos.y, c3.pos.x) && deq(c2.v.y, c3.v.x);

                    if (match) {
                        System.out.println(c1 + " | " + c2 + " | " + c3);
                        System.err.println((long) (c1.pos.x + c1.pos.y + c2.pos.y));
                    }
                }
            }
        }

    }

    Set<Hailstone2d> findIntersections2D(int range, Function<Hailstone, Hailstone2d> to2d) {
        Set<Hailstone2d> result = HashSet.empty();
        for (int v1 = -range; v1 < 0; ++v1) {
            for (int v2 = -range; v2 < 0; ++v2) {
                var dv = new D2(v1, v2);
                var hh = hailstones.map(to2d).map(i -> i.plusV(dv));

                var p0 = Generator.combination(hh.take(3).toJavaList()).simple(2);
                var pairs = List.ofAll(p0).map(l -> Tuple.of(l.get(0), l.get(1)));

//                var pairs = hh.take(5).crossProduct().filter(t -> t._1 != t._2);

                var poi = pairs
                        .filter(t -> checkIntersect(t._1, t._2))
                        .map(t -> pointOfIntersection(t._1, t._2))
                        .filter(it -> it.exists())
                        .filter(it -> it.isInteger())
                        .toSet();

                if (poi.size() == 1) {
                    Hailstone2d rock = new Hailstone2d(poi.head(), dv);
                    result = result.add(rock);
//                    System.out.println(rock);
                }                
            }            
        }
        System.out.println("-  found: " + result.size());
        return result;
    }

    static String example = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
            """;
}
