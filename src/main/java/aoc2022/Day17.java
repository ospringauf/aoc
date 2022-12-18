package aoc2022;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 17: Pyroclastic Flow ---
// https://adventofcode.com/2022/day/17

class Day17 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 3069
        timed(() -> new Day17().part1(2022));

        System.out.println("=== part 2"); // 1523167155404
        var heights = new Day17().part1(10000);
        new Day17().part2(heights);
    }

    private static final int WIDTH = 7;

//    String jets = example;
    String jets = file2string("input17.txt");

    PointMap<Character> cave = new PointMap<>();
    int top = 0;
    int jetIndex = 0;

    record Rock(Point pos, Set<Point> shape) {
        static Rock parse(String a) {
            var s = new PointMap<Character>();
            s.read(a.split("\n"), c -> c);
            return new Rock(Point.of(0, 0), s.findPoints('#').toSet());
        }

        int height() {
            return shape.map(p -> p.y()).max().get() + 1;
        }

        Rock move(Point p) {
            return new Rock(p, shape.map(x -> x.translate(p.x() - pos.x(), p.y() - pos.y())));
        }

        Rock left() {
            return move(pos.west());
        }

        Rock right() {
            return move(pos.east());
        }

        Rock down() {            
            return move(pos.south());
        }
        
        boolean collision(PointMap cave) {
            return shape.exists(p -> cave.containsKey(p));
        }

        void materialize(PointMap cave, char c) {
            shape.forEach(p -> cave.put(p,  c));
        }
    }

    void dropRock(Rock rock) {
        int y = top - 3 - rock.height();
        rock = rock.move(Point.of(3, y));

//        print(rock);
        boolean falling = true;
        while (falling) {

            var dir = jets.charAt(jetIndex % jets.length());

            var r = (dir == '<') ? rock.left() : rock.right();
            if (!r.collision(cave))
                rock = r;

            r = rock.down();
            falling = !r.collision(cave);
            if (falling)
                rock = r;

            jetIndex++;
        }
        rock.materialize(cave, '#');
        top = Math.min(top, rock.pos.y());
    }

    void print(Rock r) {
        if (r != null)
            r.materialize(cave, '@');
        cave.print();
        System.out.println();

        cave.findPoints('@').forEach(p -> cave.remove(p));
    }

    void addWalls() {
        int h = top;
        List.rangeClosed(h - 6, h).forEach(y -> cave.put(Point.of(0, y), '|'));
        List.rangeClosed(h - 6, h).forEach(y -> cave.put(Point.of(WIDTH + 1, y), '|'));
        cave.put(Point.of(0, 0), '+');
    }

    List<Integer> part1(int rounds) {
        var rocks = split(rockshapes, "\n\n").map(Rock::parse);

        List.range(1, WIDTH + 1).forEach(x -> cave.put(Point.of(x, 0), '-'));
        cave.put(Point.of(0, 0), '+');
        addWalls();
//        print(null);

        List<Integer> heights = List.empty();

        for (int i = 0; i < rounds; ++i) {
            heights = heights.append(-top);
            addWalls();
            var rock = rocks.get(i % 5);
            dropRock(rock);
            if (i % 1000 == 0)
                System.out.println(".. " + i);
        }
//        print(null);

        System.out.println(-top);

        return heights;
    }

    void part2(List<Integer> height) {
        long rounds = 1_000_000_000_000L / 5;
        System.out.println("height: " + height.take(30));

        var delta5 = height.sliding(6, 5).map(l -> l.last() - l.head()).toList();
        System.out.println("delta5: " + delta5.take(30));

        var max = delta5.max().get();
        System.out.println("max: " + max);
        int initialrounds = delta5.indexOf(max);
        int initialh = delta5.take(initialrounds).sum().intValue();
        System.out.println("initial: " + initialrounds + " / " + initialh);

        var maxes = List.range(0, delta5.size()).filter(i -> delta5.get(i) == max);
        System.out.println("maxima: " + maxes.take(20));

        var maxDistances = maxes.sliding(2).map(l -> l.last() - l.head()).toList();
        System.out.println("maxima distances: " + maxDistances.take(10));
        int cycle = maxDistances.head();

        var repeat = delta5.drop(initialrounds).take(cycle).toList();
        var deltaCycle = repeat.sum().intValue();
        System.out.println("delta/cycle: " + deltaCycle);

        var y0 = initialh + ((rounds - initialrounds) / cycle) * deltaCycle;
        int rem = (int) ((rounds - initialrounds) % cycle);
        var dy = repeat.take(rem).sum().longValue();
        System.out.println("y0 = " + y0);
        System.out.println("y = " + (y0 + dy));
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
