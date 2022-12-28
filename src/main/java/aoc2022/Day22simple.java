package aoc2022;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Vec3;
import common.Pose;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 22: Monkey Map ---
// https://adventofcode.com/2022/day/22
// TODO rewrite part 2 

class Day22simple extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1484 (6032)
        new Day22simple().part1();
        System.out.println("=== part 2"); // 142228 (5031)
        new Day22simple().part2();
    }


    String data;
    static int A;
    final PointMap<Integer> faceMap = new PointMap<>();
    static PointMap<Character> map = new PointMap<>();
    final List<String> instr;
    final List<Rule> rules;
    
    static final boolean TEST = false;
    
    {
        if (TEST) {
            A = 4;
            data = example;
            faceMap.read(faceMapExample.split("\n"), c -> c-'0');
            rules = Util.splitLines(wrapRulesExample).map(Rule::parse);
        } else {
            A = 50;
            data = file2string("input22.txt");
            faceMap.read(faceMapReal.split("\n"), c -> c-'0');
            rules = Util.splitLines(wrapRulesReal).map(Rule::parse);
        }
        
        var blocks = data.split("\n\n");
        map.read(blocks[0].split("\n"), c -> c);
        map.findPoints(c -> (c != '.') && (c != '#')).forEach(p -> map.remove(p));
        instr = Util.splitWithDelimiters(blocks[1].trim(), "RL");
    }
    
    
    Pose ahead1(Pose pose, HashSet<Point> k) {
        var next = pose.ahead();
        if (k.contains(next.pos()))
            return next;

        var n = switch (next.heading()) {
        case RIGHT -> k.filter(e -> e.y() == next.pos().y()).minBy(e -> e.x());
        case LEFT -> k.filter(e -> e.y() == next.pos().y()).maxBy(e -> e.x());
        case DOWN -> k.filter(e -> e.x() == next.pos().x()).minBy(e -> e.y());
        case UP -> k.filter(e -> e.x() == next.pos().x()).maxBy(e -> e.y());

        default -> throw new IllegalArgumentException("Unexpected value: " + next.heading());

        };

        return new Pose(next.heading(), n.get());
    }

    int face(Point p) {
        if (!map.containsKey(p)) return 0;
        return faceMap.getOrDefault(Point.of(p.x()/A, p.y()/A), 0);
    }

    void part1() {
        var k = HashSet.ofAll(map.keySet());

        var p = new Pose(Direction.RIGHT, Point.of(0, 0));
        p = ahead1(p, k);

        for (var i : instr) {

            if (Character.isDigit(i.charAt(0))) {
                var d = Integer.valueOf(i);
                for (int s = 0; s < d; ++s) {
                    var a = ahead1(p, k);
                    if (map.getOrDefault(a.pos(), '?') == '#')
                        break;
                    map.put(p.pos(), p.heading().symbol());
                    p = a;
                }

            } else {
                p = p.turn("R".equals(i));
            }
        }
//        map.print();

        System.out.println(p);
        System.out.println(password(p));
    }

    int password(Pose p) {
        var facing = switch (p.heading()) {
        case RIGHT -> 0;
        case LEFT -> 2;
        case UP -> 3;
        case DOWN -> 1;
        default -> throw new IllegalArgumentException("Unexpected value: " + p.heading());
        };
        return 1000 * (p.pos().y() + 1) + 4 * (p.pos().x() + 1) + facing;
    }

    Point origin(int side) {
        var p = faceMap.findPoint(side);
        return Point.of(A*p.x(), A*p.y());
    }    

    void part2() {
        var p = new Pose3(1, Direction.RIGHT, origin(1));
        System.out.println("start: " + p);

        for (var i : instr) {

            if (Character.isDigit(i.charAt(0))) {
                var d = Integer.valueOf(i);
                for (int s = 0; s < d; ++s) {
                    var a = ahead2(p);
                    if (map.getOrDefault(a.pos(), '?') == '#')
                        break;
//                    map.put(p.pos(), dirc(p.heading));
//                    map.printS(c -> (c==null)? " ":(String) Character.toString(c));
//                    System.out.println();
                    p = a;
                }
            } else {
                boolean right = "R".equals(i);
                p = new Pose3(p.face, p.heading.turn(right), p.pos);
            }
        }
        System.out.println("last: " + p);
        System.out.println(password(new Pose(p.heading, p.pos)));

    }

    Pose3 ahead2(Pose3 p) {
        var next = new Pose(p.heading, p.pos).ahead();
        var nextface = face(next.pos());
        if (nextface == p.face)
            return new Pose3(p.face, p.heading, next.pos());

        var rule = rules.find(r -> r.match(p.face, p.heading)).get();
        var p1 = rule.wrap(p.pos.minus(origin(p.face)));
        p1 = p1.plus(origin(p1.face));

        return p1;
    }

    record Pose3(int face, Direction heading, Point pos) {

        Pose3 plus(Point origin) {
            return new Pose3(face, heading, pos.plus(origin));
        }
    }

    record Rule(int fromFace, Direction fromDir, int toFace, Direction toDir, String xm, String ym) {
        static Rule parse(String s) {
            var f = split(s, " ");
            return new Rule(f.i(0), Direction.parse(f.s(1)), f.i(2), Direction.parse(f.s(3)), f.s(4), f.s(5));
        }

        boolean match(int face, Direction d) {
            return fromFace == face && fromDir == d;
        }

        Pose3 wrap(Point p) {
            var x = transform(p, xm);
            var y = transform(p, ym);
            return new Pose3(toFace, toDir, Point.of(x, y));
        }

        static int transform(Point p, String tr) {
            return switch (tr) {
            case "0" -> 0;
            case "a" -> A - 1;
            case "x" -> p.x();
            case "a-x" -> A - 1 - p.x();
            case "y" -> p.y();
            case "a-y" -> A - 1 - p.y();
            default -> throw new IllegalArgumentException("Unexpected value: " + tr);
            };
        }
    }
    
    static String transform = """
            L R 0 a-y
            L L a y
            L U a-y a
            L D y 0
            R R 0 y
            R L a a-y
            R U y a
            R D a-y 0
            U R 0 x
            U L a-x a
            U U x a
            U D a-x 0
            D R 0 a-x
            D L a x
            D U a-x a
            D D x 0
            """;

    static String wrapRulesExample = """
            1 L 3 D y 0
            1 R 6 L a a-y
            1 U 2 D a-x 0
            1 D 4 D x 0
            2 L 6 U a-y a
            2 R 3 R 0 y
            2 U 1 D a-x 0
            2 D 5 U a-x a
            3 L 2 L a y
            3 R 4 R 0 y
            3 U 1 R 0 x
            3 D 5 R 0 a-x
            4 L 3 L a y
            4 R 6 D a-y 0
            4 U 1 U x a
            4 D 5 D x 0
            5 L 3 U a-y a
            5 R 6 R 0 y
            5 U 4 U x a
            5 D 2 U a-x a
            6 L 5 L a y
            6 R 1 L a a-y
            6 U 4 L a-x a
            6 D 2 R 0 a-x
            """;
    
    static String wrapRulesReal = """
            1 L 4 R 0 a-y
            1 R 2 R 0 y
            1 U 6 R 0 x
            1 D 3 D x 0
            2 L 1 L a y
            2 R 5 L a a-y
            2 U 6 U x a
            2 D 3 L a x
            3 L 4 D y 0
            3 R 2 U y a
            3 U 1 U x a
            3 D 5 D x 0
            4 L 1 R 0 a-y
            4 R 5 R 0 y
            4 U 3 R 0 x
            4 D 6 D x 0
            5 L 4 L a y          
            5 R 2 L a a-y
            5 U 3 U x a
            5 D 6 L a x
            6 L 1 D y 0
            6 R 5 U y a
            6 U 4 U x a
            6 D 2 D x 0
            """;


    static String example = """
                    ...#
                    .#..
                    #...
                    ....
            ...#.......#
            ........#...
            ..#....#....
            ..........#.
                    ...#....
                    .....#..
                    .#......
                    ......#.

            10R5L5R10L4R5L5
                        """;
    
    static String faceMapExample = """
            0010
            2340
            0056
            """;
    
    static String faceMapReal = """
            012
            030
            450
            600
            """;

}
