package aoc2023;

import common.AocPuzzle;
import common.BoundingBox;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

//--- Day 18: Lavaduct Lagoon ---
// https://adventofcode.com/2023/day/18

class Day18 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 35991
        timed(() -> new Day18().part1());
        System.out.println("=== part 2"); // 54058824661845
        timed(() -> new Day18().part2());
    }

    List<String> data = file2lines("input18.txt");
//	List<String> data = Util.splitLines(example);
	final boolean DEBUG = false;
	

    record Cmd(Direction dir, int steps) {

        static Cmd parse1(String s) {
            var f = split(s, " ");
            var dir = Direction.parse(f.c(0));
            var steps = f.i(1);
            return new Cmd(dir, steps);
        }

        static Cmd parse2(String s) {
            var f = s.split("[ #()]")[4];
            var dir = switch (f.charAt(5)) {
            case '0' -> Direction.RIGHT;
            case '1' -> Direction.DOWN;
            case '2' -> Direction.LEFT;
            case '3' -> Direction.UP;
            default -> throw new IllegalArgumentException("Unexpected value: " + f.charAt(5));
            };

            var steps = Integer.parseUnsignedInt(f.substring(0, 5), 16);

            return new Cmd(dir, steps);
        }
    }

    record Leg(Point a, Point b) {
        
        // make sure that a is left of/above b
        public Leg(Point a, Point b) {
            if (a.x() == b.x()) {
                if (a.y() < b.y()) {
                    this.a = a;
                    this.b = b;                               
                } else {
                    this.a = b;
                    this.b = a;
                }                    
            } else {
                if (a.x() < b.x()) {
                    this.a = a;
                    this.b = b;                               
                } else {
                    this.a = b;
                    this.b = a;
                }
            }
        }
        
        boolean vertical() {
            return a.x() == b.x();
        }

        boolean containsY(int y) {
            return a.y() <= y && y < b.y();
        }

        int len() {
            return a.manhattan(b);
        }
    }

    void part1() {
        var cmd = data.map(Cmd::parse1);
        solve(cmd);
    }

    void part2() {
        var cmd = data.map(Cmd::parse2);
        solve(cmd);
    }

    void print(BoundingBox highlight) {
        var m = new PointMap<Character>();

        var p = Point.of(0, 0);
        m.put(p, '#');
        for (var l : data) {
            var f = split(l, " ");
            var d = Direction.parse(f.c(0));
            var n = f.i(1);
            for (int i = 0; i < n; ++i) {
                p = p.translate(d);
                m.put(p, '#');
            }
        }
        
        if (highlight != null) {
            highlight.generatePoints().forEach(it -> m.put(it, 'B'));
        }
        
        m.print();
    }

    void solve(List<Cmd> cmd) {
        var p1 = Point.of(0, 0);

        List<Leg> legs = List.empty();
        for (var c : cmd) {
            var p2 = p1.translate(c.dir, c.steps);
            legs = legs.append(new Leg(p1, p2));
            p1 = p2;
        }

//        print();
        
        var ys = legs.flatMap(l -> List.of(l.a.y(), l.b.y())).distinct().sorted();

        List<BoundingBox> boxes = List.empty();
        for (var y12 : ys.sliding(2, 1)) {
            var y1 = y12.get(0);
            var y2 = y12.get(1);
            var xs = legs.filter(l -> l.vertical() && l.containsY(y1)).map(l -> l.a.x()).distinct().sorted();

            var bb = xs.sliding(2, 2).map(xx -> new BoundingBox(xx.get(0), xx.get(1), y1, y2)).toList();
            if (DEBUG) {
                for (var b : bb) {
                    print(b);
                    System.out.println();
                }
            }
            boxes = boxes.appendAll(bb);
        }
        var vol = boxes.map(b -> (long) b.width() * b.height()).sum().longValue();
        System.out.println("boxed volume: " + vol);

        var overlap = boxes.crossProduct(boxes).filter(t -> t._1 != t._2).map(t -> overlap(t._1, t._2)).sum()
                .longValue();
        System.out.println("overlap: " + overlap / 2);

        System.err.println("==> " + (vol - overlap / 2));
    }

    long overlap(BoundingBox b1, BoundingBox b2) {
        // if 2 boxes touch vertically, the common points on the border have been counted twice 
        if (b1.yMax() != b2.yMin() && b1.yMin() != b2.yMax())
            return 0;
        int r = 0;
        var xs = List.of(b1.xMin(), b1.xMax(), b2.xMin(), b2.xMax()).distinct().sorted();
        for (int i = 0; i < xs.size() - 1; ++i) {
            var x = xs.get(i);
            if (x >= b1.xMin() && x >= b2.xMin() && x < b1.xMax() && x < b2.xMax())
                r += xs.get(i + 1) - x + 1;
        }
        return r;
    }
    
    
// here are the boxes for the example 
// O means overlap, must be removed
//    
//    BBBBBBB
//    BBBBBBB
//    BBBBBBB
//    ··#···#
//    ··#···#
//    ###·###
//    #···#··
//    ##··###
//    ·#····#
//    ·######
//
//    #######
//    #·····#
//    ##OOOOO
//    ··BBBBB
//    ··BBBBB
//    ##BBBBB
//    #···#··
//    ##··###
//    ·#····#
//    ·######
//
//    #######
//    #·····#
//    ###···#
//    ··#···#
//    ··#···#
//    BBOOO##
//    BBBBB··
//    BBBBB##
//    ·#····#
//    ·######
//
//    #######
//    #·····#
//    ###···#
//    ··#···#
//    ··#···#
//    ###·###
//    #···#··
//    #OOOOBB
//    ·BBBBBB
//    ·BBBBBB    

    static String example = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """;
}
