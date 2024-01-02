package aoc2023;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 21:  ---
// https://adventofcode.com/2023/day/21

class Day21ex2 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3660
//        timed(() -> new Day21().part1());
        System.out.println("=== part 2");
//        timed(() -> new Day21().part2());
        new Day21ex2().part2a();
    }

    List<String> data = file2lines("input21.txt");
//    List<String> data = Util.splitLines(example);

    void part1() {
        var m = new PointMap<Character>();
        m.read(data);
        var start = m.findPoint('S');
        m.put(start, '.');

        m.print();
        int steps = 64;
        var tgt = HashSet.of(start);

        while (steps > 0) {
            System.out.println(steps);
            var next = tgt.flatMap(p -> p.neighbors()).filter(p -> m.getOrDefault(p, '?') == '.');
            tgt = next;
            steps--;
        }
        System.err.println(tgt.size());
    }

    class Area {
        HashSet<Point> points;
        Point rep;
        List<Integer> reach = List.empty();

        int offset;
        int type;

        void analyse() {
            if (reach.contains(42)) {
                offset = reach.indexWhere(c -> c > 0);
                reach = reach.dropWhile(c -> c == 0).takeWhile(c -> c != 42);
//                reach = reach.dropWhile(c -> c == 0).take(30);
            } else {
                offset = 0;
                reach = List.empty();
            }
        }
    }

    record Atype(int off, List<Integer> start) {

        int val(int step) {
            if (step < off)
                return 0;
            step -= off;
            if (step < start.size())
                return start.get(step);
            return ((step - start.length()) % 2 == 0) ? 39 : 42;
        }

        long val(int d, int step) {
            step = step-1;
            int off1 = off + (d - 1) * 11;
            if (step < off1)
                return 0;
            step -= off1;
            if (step < start.length())
                return start.get(step);
            return ((step - start.length()) % 2 == 0) ? 42 : 39;
        }
    }

    void part2a() {
        var t1 = new Atype(0, List.of(1, 2, 3, 4, 7, 6, 9, 10, 14, 14, 18, 21, 26, 29, 32, 33, 36, 36, 40, 39));
        var t2 = new Atype(10, List.of(2, 4, 6, 9, 14, 14, 18, 20, 24, 25, 29, 31, 35, 36, 41, 39));
        var t3 = new Atype(0, List.of(1, 2, 3, 5, 7, 9, 11, 14, 17, 18, 21, 21, 25, 26, 33, 33, 38, 37, 41, 39));
        var t4 = new Atype(10, List.of(3, 8, 11, 12, 16, 16, 19, 21, 25, 26, 31, 32, 37, 37, 41, 39));
        var t5 = new Atype(6, List.of(1, 4, 7, 7, 10, 11, 14, 15, 18, 18, 22, 25, 31, 33, 38, 39));
        var t6 = new Atype(10, List.of(2, 4, 6, 8, 14, 14, 17, 20, 26, 27, 33, 34, 39, 38));
        var t7 = new Atype(8, List.of(1, 2, 5, 6, 9, 10, 14, 14, 17, 20, 26, 27, 33, 34, 39, 38));
        var t8 = new Atype(0, List.of(2, 4, 6, 9, 13, 16, 21, 25, 29, 33, 35, 40, 39));
        var t9 = new Atype(7, List.of(1, 3, 5, 7, 7, 9, 10, 13, 14, 17, 19, 25, 26, 30, 30, 35, 35, 38, 38, 41, 39));
        var ta = new Atype(10, List.of(1, 2, 4, 5, 9, 11, 15, 16, 22, 23, 29, 30, 35, 35, 38, 38, 41, 39));
        var tb = new Atype(0, List.of(1, 2, 3, 4, 7, 9, 10, 14, 18, 20, 24, 25, 29, 29, 34, 33, 38, 37, 41, 39));
        var tc = new Atype(8, List.of(1, 3, 6, 7, 12, 13, 18, 19, 24, 25, 28, 31, 33, 34, 38, 37, 40, 39));
        var td = new Atype(4, List.of(1, 2, 4, 5, 8, 9, 12, 12, 17, 18, 23, 24, 29, 31, 34, 35, 37, 37, 41, 39));
        var te = new Atype(10, List.of(1, 2, 5, 7, 11, 13, 18, 18, 24, 25, 29, 31, 34, 35, 39, 38, 41, 39));
        var tf = new Atype(10, List.of(1, 2, 4, 5, 9, 11, 15, 15, 21, 23, 27, 28, 33, 34, 38, 38, 41, 39));

        // 1048656158 too low
        // 470149643712798 too low?
        // 470149643712799 no
        // 470149643712800 no
        // 470149643712801 ?
        // 470149643712802 ?
        // 470149643712803 no
        // 470149643712804 wrong **
        // 470149643712805 ?

        // 100000   6694148697
        // 1000000  669420421436
        // 10000000 66942142148697
        // 2650136  4701493329192
        // 26501365 470149643712804
        // 26501365047014966756408794
//        int s = 26501365;
        int s = 5000;
//        
//        for (int i=0; i<s; ++i)
//            System.out.println(i + " -> " + t8.val(1,i));

//        int r = 0;
//        r += t8.val(1, s);
//        // N
//        r += t7.val(1, s) + t6.val(2, s) + t6.val(3, s) + t6.val(4, s);
//        // S
//        r += t9.val(1, s) + ta.val(2, s) + ta.val(3, s) + ta.val(4, s) + ta.val(5, s);
//        // W
//        r += t5.val(1, s) + t4.val(2, s) + t2.val(3, s) + t2.val(4, s) + t2.val(5, s);
//        // E
//        r += tc.val(1, s) + te.val(2, s) + tf.val(3, s) + tf.val(4, s);
//
//        // NE
//        r += 1 * tb.val(2, s) + 2 * tb.val(3, s) + 3 * tb.val(4, s) + 4 * tb.val(5, s);
//        // NW
//        r += 1 * t1.val(2, s) + 2 * t1.val(3, s) + 3 * t1.val(4, s)+ 4 * t1.val(5, s);
//        // SW
//        r += 1 * t3.val(2, s) + 2 * t3.val(3, s) + 3 * t3.val(4, s) + 4 * t3.val(5, s);
//        // SE
//        r += 1 * td.val(2, s) + 2 * td.val(3, s) + 3 * td.val(4, s) + 4 * td.val(5, s);
//
//        System.err.println(r);
        
        var f = 5;
        long r = 0;
        r += t8.val(1, s);
        // N
        r += t7.val(1, s); // + t6.val(2, s) + t6.val(3, s) + t6.val(4, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=t6.val(x, s);
        // S
        r += t9.val(1, s); // + ta.val(2, s) + ta.val(3, s) + ta.val(4, s) + ta.val(5, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=ta.val(x, s);

        // W
        r += t5.val(1, s) + t4.val(2, s); // + t2.val(3, s) + t2.val(4, s) + t2.val(5, s);
        for (int x=3; 11*x<f*s; x=x+1)
            r+=t2.val(x, s);

        // E
        r += tc.val(1, s) + te.val(2, s); // + tf.val(3, s) + tf.val(4, s);
        for (int x=3; 11*x<f*s; x=x+1)
            r+=tf.val(x, s);


        // NE
//        r += 1 * tb.val(2, s) + 2 * tb.val(3, s) + 3 * tb.val(4, s) + 4 * tb.val(5, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=(x-1)* tb.val(x, s);

        // NW
        //r += 1 * t1.val(2, s) + 2 * t1.val(3, s) + 3 * t1.val(4, s)+ 4 * t1.val(5, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=(x-1)* t1.val(x, s);

        // SW
        //r += 1 * t3.val(2, s) + 2 * t3.val(3, s) + 3 * t3.val(4, s) + 4 * t3.val(5, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=(x-1)* t3.val(x, s);

        // SE
        // r += 1 * td.val(2, s) + 2 * td.val(3, s) + 3 * td.val(4, s) + 4 * td.val(5, s);
        for (int x=2; 11*x<f*s; x=x+1)
            r+=(x-1)* td.val(x, s);


        System.err.println(r);

    }

    void part2() {
        int maxsteps = 100;
        var m = new PointMap<Character>();
        m.read(data);
        var start = m.findPoint('S');
        m.put(start, '.');
        var bb = m.boundingBox();
        var w = bb.width();
        var h = bb.height();

        var tgt = HashSet.of(start);
        var garden = HashSet.ofAll(m.findPoints('.'));
        var am = HashSet.ofAll(m.keySet());

        List<Area> areas = List.empty();
        int dist = 8;
        for (int x = -dist; x <= dist; ++x) {
            for (int y = -dist; y < dist; ++y) {
                Area a = new Area();
                a.rep = Point.of(x, y);
                var x0 = x;
                var y0 = y;
                a.points = am.map(p -> p.translate(x0 * w, y0 * h));
                areas = areas.append(a);
            }
        }

        int step = 0;
        while (step < maxsteps) {
            tgt = tgt.flatMap(p -> p.neighbors()).filter(p -> garden.contains(p.modulo(w, h)));
            var tgt0 = tgt;
//            var c = List.of(am, a1, a2, a3, a4).map(a -> tgt0.count(a::contains));
//            System.out.println(step + " --> " + c);

            for (var a : areas) {
                var c = tgt.count(p -> a.points.contains(p));
                a.reach = a.reach.append(c);
            }

            System.out.println(maxsteps - step);
            step++;
        }

        areas.forEach(a -> a.analyse());
        var types = areas.map(a -> a.reach).distinct();

        areas.forEach(a -> a.type = types.indexOf(a.reach));

        System.out.println("areas: " + areas.size());
        System.out.println("types: " + types.size());

        types.forEach(t -> System.out.println(t));

        var anamap = new PointMap<Integer>();
        areas.forEach(a -> anamap.put(a.rep, a.type));
        var p0 = Point.of(0, 0);
        anamap.put(p0, 0);
        anamap.print(c -> ".123456789ABCDEFghij".charAt(c));

//        for (int t = 1; t < types.size(); ++t) {
//            System.out.println("== type " + ".123456789ABCDEFghij".charAt(t));
//            int t0 = t;
//            var at = areas.filter(a -> a.type == t0);
//            at.forEach(a -> System.out.println(a.rep.manhattan(p0) + " / " + a.offset));
//        }

        // type B
        // offset = (m-1)*11

        // type 3
        // offset = (m-1)*11

        // type 1
        // offset = (m-1)*11

        // type D
        // offset = 4 + 11*(m-1)

        // type 6
        // offset = 10 + 11*(m-1)

//        System.err.println(tgt.size());      
    }

    static String example = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """;
}
