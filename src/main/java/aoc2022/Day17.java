package aoc2022;

import java.io.PrintWriter;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;

// --- Day 17: Pyroclastic Flow ---
// https://adventofcode.com/2022/day/17
// TODO too slow

class Day17 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3069
//        new Day17().part1();
        System.out.println("=== part 2"); // 1523167155404
        new Day17().part2();
    }

    private static final int WIDTH = 7;

//    String data = example;
    String data = file2string("input17.txt");
    static PointMap<Character> cave = new PointMap<>();

    record Rock(Point pos, PointMap<Character> shape) {

        static Rock parse(String a) {
            var s = new PointMap<Character>();
            s.read(a.split("\n"), c -> c);
            s.findPoints('.').forEach(p -> s.remove(p));
            return new Rock(Point.of(0, 0), s);
        }

        Rock move(Point p) {
            return new Rock(p, shape);
        }

        Rock left() {
            return move(pos.west());
        }

        Rock right() {
            return move(pos.east());
        }

        boolean collision() {
            for (var p : shape.keySet()) {
                var t = p.translate(pos.x(), pos.y());
                if (cave.containsKey(t))
                    return true;
            }
            return false;
        }

        boolean collision(java.util.Set<Point> s) {
            for (var p : shape.keySet()) {
                var t = p.translate(pos.x(), pos.y());
                if (s.contains(t))
                    return true;
            }
            return false;
        }

        void materialize(char c) {
            for (var p : shape.keySet()) {
                var t = p.translate(pos.x(), pos.y());
                cave.put(t, c);
            }
        }

    }

    int topy = 0;
    int jet = 0;

    void dropRock(Rock rock) {
        var bb = rock.shape.boundingBox();
        int y = topy - 3 - bb.height();
        rock = rock.move(Point.of(3, y));

//        print(rock);
        boolean falling = true;
        while (falling) {

            java.util.HashSet<Point> m = new java.util.HashSet(cave.keySet());
            var r0 = rock;
            m.removeIf(p -> p.y() < r0.pos.y() - 0 || p.y() > r0.pos.y() + 4);

            var d = data.charAt(jet % data.length());
            var h = (d == '<') ? rock.left() : rock.right();
            if (!h.collision(m))
                rock = h;
            var n = rock.move(rock.pos.south());
            falling = !n.collision(m);
            if (falling)
                rock = n;
            jet++;
        }
        rock.materialize('#');
        topy = Math.min(topy, rock.pos.y());
    }

    void print(Rock r) {
        if (r != null)
            r.materialize('@');
        cave.print();
        System.out.println();

        cave.findPoints('@').forEach(p -> cave.remove(p));
    }

    void drawWalls() {
        int h = topy;
        List.rangeClosed(h - 6, h).forEach(y -> cave.put(Point.of(0, y), '|'));
        List.rangeClosed(h - 6, h).forEach(y -> cave.put(Point.of(WIDTH + 1, y), '|'));
        cave.put(Point.of(0, 0), '+');
    }

    void part1() {
        var rocks = split(rockshapes, "\n\n").map(Rock::parse);

        List.range(1, WIDTH + 1).forEach(x -> cave.put(Point.of(x, 0), '-'));
        cave.put(Point.of(0, 0), '+');
        drawWalls();
        print(null);

        List<Integer> tops = List.empty();

        int rounds = 20000;
        for (int i = 0; i < rounds; ++i) {
            tops = tops.append(topy);
            drawWalls();
            var rock = rocks.get(i % 5);
            dropRock(rock);
            if (i % 100 == 0)
                System.out.println(i);
        }
//        print(null);

        System.out.println(-topy);

        save(tops);

    }

    void save(List<Integer> tops) {
        try {
            try (PrintWriter out = new PrintWriter("src/main/java/aoc2022/output17.txt")) {
                var s = tops.mkString("\n");
                out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void part2() {
        long rounds = 1_000_000_000_000L / 5;
        var h = file2ints("output17.txt").map(x -> -x);
        System.out.println(h.take(30));

        var delta = h.sliding(6, 5).map(l -> l.last()-l.head()).toList();
        System.out.println("delta: " + delta.take(30));
        
        var max = delta.max().get();
        System.out.println("max: " + max);
        int initialrounds = delta.indexOf(max);
        int initialh = delta.take(initialrounds).sum().intValue();
        System.out.println("initial: " + initialrounds + " / " +initialh);
        
        var maxes = List.range(0, delta.size()).filter(i -> delta.get(i)==max);
        System.out.println("maxes: " + maxes.take(20));
        var periods = maxes.sliding(2).map(l -> l.last()-l.head()).toList();
        System.out.println("periods: " + periods.take(10));
        int period = periods.head();
        
        var repeat = delta.drop(initialrounds).take(period).toList();
        var len = repeat.sum().intValue();
        System.out.println("len: " + len);
        
        var y0 = initialh + ((rounds-initialrounds)/period)*len;
        int rem = (int) ((rounds-initialrounds)%period);
        var dy = repeat.take(rem).sum().longValue();
        System.out.println("y0 = " + y0);
        System.out.println("y = " + (y0+dy));
    }
    
    
    static String example = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";

    static String rockshapes = """
            ####

            .#.
            ###
            .#.

            ..#
            ..#
            ###

            #
            #
            #
            #

            ##
            ##
                        """;
}
