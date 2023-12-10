package aoc2023;

import java.util.function.Function;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

//--- Day 10: Pipe Maze ---
// https://adventofcode.com/2023/day/10

class Day10 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day10().solve()); // 6738, 579
    }

//    List<String> data = file2lines("input10.txt"); boolean debug=false;
//    List<String> data = Util.splitLines(example3); boolean debug = true;
//    List<String> data = Util.splitLines(example4); boolean debug = true;
    List<String> data = Util.splitLines(example5); boolean debug = true;
//    List<String> data = Util.splitLines(example6); boolean debug = true;


    static boolean canStep(PointMap<Character> m, Point a, Point b) {
        var dir = dir(a, b);
        var ca = m.get(a);
        var cb = m.getOrDefault(b, '.');
        return canStep(dir, ca, cb);
    }

    static boolean canStep(Direction dir, Character ca, Character cb) {
        return switch (dir) {
        case EAST -> "S-LF".indexOf(ca) >= 0 && "S-7J".indexOf(cb) >= 0;
        case WEST -> "S-J7".indexOf(ca) >= 0 && "S-FL".indexOf(cb) >= 0;
        case SOUTH -> "S|7F".indexOf(ca) >= 0 && "S|JL".indexOf(cb) >= 0;
        case NORTH -> "S|JL".indexOf(ca) >= 0 && "S|7F".indexOf(cb) >= 0;
        default -> throw new IllegalArgumentException("Unexpected value: " + dir);
        };
    }

    static Direction dir(Point a, Point b) {
        if (b.equals(a.west()))
            return Direction.WEST;
        if (b.equals(a.east()))
            return Direction.EAST;
        if (b.equals(a.south()))
            return Direction.SOUTH;
        if (b.equals(a.north()))
            return Direction.NORTH;
        throw (new RuntimeException("not neighbors"));
    }

    static void prettyPrintMap(PointMap<Character> m) {
        m.print(c -> switch (c) {
        case null -> '?';
        case '7' -> '┐';
        case 'L' -> '└';
        case 'J' -> '┘';
        case 'F' -> '┌';
        case '-' -> '─';
        case '|' -> '│';
        default -> c;
        });
    }

    PointMap<List<Point>> buildStepMap(PointMap<Character> m) {
        var n = new PointMap<List<Point>>();
        for (var p : m.keySet()) {
            n.put(p, p.neighbors().filter(b -> canStep(m, p, b)));
        }
        return n;
    }

    void solve() {
        PointMap<Character> m = new PointMap<Character>();
        m.read(data);
        Point start = m.findPoint('S');

        System.out.println("=== part 1");

        System.out.println("-- build neighbor map");
        var nmap = buildStepMap(m);
        m.neigbors = nmap::get;
        System.out.println("-- find loop");
        var dist = m.dijkstraAll(start, c -> true).distance;
        System.out.println(List.ofAll(dist.values()).max().get());

        System.out.println("=== part 2");
        var loop = dist.keySet();

        System.out.println("-- expanding map");
        var xm = new PointMap<Character>();
        Function<Point, Point> xpoint = p -> Point.of(2 * p.x(), 2 * p.y());
        loop.forEach(p -> xm.put(xpoint.apply(p), m.get(p)));
        
        // fill gaps between connected pipes
        for (var p : xm.boundingBox().generatePoints()) {
            var cw = xm.getOrDefault(p.west(), '?');
            var ce = xm.getOrDefault(p.east(), '?');
            if (canStep(Direction.EAST, cw, ce))
                xm.put(p, '-');

            var cn = xm.getOrDefault(p.north(), '?');
            var cs = xm.getOrDefault(p.south(), '?');
            if (canStep(Direction.SOUTH, cn, cs))
                xm.put(p, '|');
        }
        // add empty border to find paths from the outside
        var inside = '·';
        var bb = xm.boundingBox().expand(1, 1);
        bb.generatePoints().forEach(p -> xm.put(p, xm.getOrDefault(p, inside)));
        if (debug)
            prettyPrintMap(xm);

        System.out.println("-- finding paths from outside");
        dist = xm.dijkstraAll(Point.of(bb.xMin(), bb.yMin()), c -> c == inside).distance;
        dist.keySet().forEach(p -> xm.put(p, 'O'));

        System.out.println("-- shrinking map");
        m.keySet().forEach(p -> m.put(p, xm.getOrDefault(xpoint.apply(p), ' ')));
        if (debug)
            prettyPrintMap(m);

        System.out.println("-- enclosed points: " + m.findPoints(inside).size());
    }
    
    

    static String example1 = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
            """;

    static String example2 = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
            """;

    static String example3 = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
                        """;

    static String example4 = """
            ..........
            .S------7.
            .|F----7|.
            .||....||.
            .||....||.
            .|L-7F-J|.
            .|..||..|.
            .L--JL--J.
            ..........
                        """;

    static String example5 = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
                        """;

    static String example6 = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
                        """;

}
