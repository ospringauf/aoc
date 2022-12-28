package aoc2022;

import java.util.function.Function;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Pose;
import common.Util;
import common.Vec3;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 22: Monkey Map ---
// https://adventofcode.com/2022/day/22
// improved version, determines cube face orientation and rotation
// TODO move some features to Point/Pose/Vec3 
// TODO improve part 1

class Day22 extends AocPuzzle {

    public static void main(String[] args) {
        new Day22().analyzeFacemap();
        System.out.println("=== part 1"); // 1484 (6032)
        new Day22().part1();
        System.out.println("=== part 2"); // 142228 (5031)
        new Day22().part2();
    }

    String data;
    static int A;
    final PointMap<Integer> faceMap = new PointMap<>();
    static PointMap<Character> map = new PointMap<>();
    List<String> instr;

    static final boolean TEST = false;

    {
        if (TEST) {
            A = 4;
            data = example;
            faceMap.read(faceMapExample.split("\n"), c -> c - '0');
        } else {
            A = 50;
            data = file2string("input22.txt");
            faceMap.read(faceMapReal.split("\n"), c -> c - '0');
        }

        var blocks = data.split("\n\n");
        map.read(blocks[0].split("\n"), c -> c);
        map.findPoints(c -> (c != '.') && (c != '#')).forEach(p -> map.remove(p));
        instr = Util.splitWithDelimiters(blocks[1].trim(), "RL");
    }

    List<Point> rect() {
        return List.range(0, A).flatMap(y -> List.range(0, A).map(x -> Point.of(x, y)));
    }

    Function<Vec3, Vec3> id = p -> p;
    static Function<Vec3, Vec3> td = Vec3::rotxr;
    static Function<Vec3, Vec3> tu = Vec3::rotx;
    static Function<Vec3, Vec3> tl = Vec3::rotyr;
    static Function<Vec3, Vec3> tr = Vec3::roty;
    static final Vec3 V1 = Vec3.of(0, 0, 1); // normal of face 1
    static final Vec3 N = Vec3.of(0, -1, 0);
    static final Vec3 S = Vec3.of(0, 1, 0);
    static final Vec3 E = Vec3.of(1, 0, 0);
    static final Vec3 W = Vec3.of(-1, 0, 0);

    /**
     * represents the "ant" walking on the cube surface.
     * origin is the position of the face in the original map.
     * pos is relative to the face origin! (0..A-1)
     */
    record AntPose(Face face, Direction heading, Point pos) {
        
        AntPose absolute() {
            return new AntPose(face, heading, pos.plus(face.origin));
        }

        AntPose wrap() {
            var p = switch (heading) {
            case NORTH -> Point.of(pos.x(), ((pos.y() - 1) % A + A) % A);
            case SOUTH -> Point.of(pos.x(), (pos.y() + 1) % A);
            case WEST -> Point.of(((pos.x() - 1) % A + A) % A, pos.y());
            case EAST -> Point.of((pos.x() + 1) % A, pos.y());
            default -> throw new IllegalArgumentException("Unexpected value: " + heading);
            };
            return new AntPose(face, heading, p);
        }
        
        AntPose rot(Direction dir, Face next) {
            var d = heading;
            var p = pos;
            var c = Point.of(0, A-1);

            while (d != dir) {
                p = p.rotLeft().plus(c);
                d = d.left();
            }
            return new AntPose(next, d, p);
        }

        AntPose ahead() {
            var p = new Pose(heading, pos).ahead();
            return new AntPose(face, p.heading(), p.pos());
        }

        boolean valid() {
            return 0 <= pos.x() && pos.x() < A && 0 <= pos.y() && pos.y() < A;
        }
    }

    record Face(int id, Function<Vec3, Vec3> transform, Point origin) {
        Vec3 normal() {
            return transform.apply(V1);
        }
        
        public String toString() {
            return String.format("%d:%s", id, origin);
        }
    }

    class Cube {
        HashMap<Vec3, Face> faces = HashMap.empty();

        static Map<Vec3, Direction> dirs = HashMap.of(N, Direction.NORTH, S, Direction.SOUTH, E, Direction.EAST, W,
                Direction.WEST);

        Face getFace(Vec3 normal) {
            return faces.get(normal).get();
        }

        Face getFace(int f) {
            return faces.values().find(it -> it.id == f).get();
        }

        void buildFaces(Point p, Function<Vec3, Vec3> t) {
            var n = faceMap.getOrDefault(p, 0);
            if (n == 0)
                return;
            if (faces.values().exists(it -> it.id == n))
                return;
            Face f = new Face(n, t, Point.of(A * p.x(), A * p.y()));
            faces = faces.put(f.normal(), f);
            buildFaces(p.south(), td.andThen(t));
            buildFaces(p.north(), tu.andThen(t));
            buildFaces(p.west(), tl.andThen(t));
            buildFaces(p.east(), tr.andThen(t));
        }

        Face neighbor(Face f, Direction d) {
            var t = switch (d) {
            case NORTH -> tu;
            case SOUTH -> td;
            case EAST -> tr;
            case WEST -> tl;
            default -> throw new IllegalArgumentException("Unexpected value: " + d);
            };
            var v = t.andThen(f.transform).apply(V1);
            return getFace(v);
        }

        Direction wrap(Face f, Direction heading) {
            // wrap direction vector to neighbor face, then transform to neighbor
            // coordinates and compare to neighbor NWSE
            var n = neighbor(f, heading);

//            var v = switch (heading) { // Eclipse compiler bug!
            Vec3 v = switch (heading) {
            case NORTH -> tu.andThen(f.transform).apply(N);
            case SOUTH -> td.andThen(f.transform).apply(S);
            case EAST -> tr.andThen(f.transform).apply(E);
            case WEST -> tl.andThen(f.transform).apply(W);
            default -> throw new IllegalArgumentException("Unexpected value: " + heading);
            };

            var dn = List.of(N, W, S, E).find(dir -> n.transform.apply(dir).equals(v)).get();
            return dirs.get(dn).get();
        }
    }

    void analyzeFacemap() {
        var cube = new Cube();
        cube.buildFaces(faceMap.findPoint(1), id);

        // print face orientations
        for (Face f : cube.faces.values()) {
            System.out.println("face " + f.id + " " + f.normal());

            for (var d : cube.dirs.values()) {
                var n = cube.neighbor(f, d);
                var dn = cube.wrap(f, d);
                System.out.println("   " + d + ": " + n.id + ", " + dn);
            }
        }
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
        if (!map.containsKey(p))
            return 0;
        return faceMap.getOrDefault(Point.of(p.x() / A, p.y() / A), 0);
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

        System.out.print("password for " + p + ": ");
        System.out.println(password(p));
    }

    int password(Pose p) {        
        var facing = switch (p.heading()) {
        case RIGHT, EAST -> 0;
        case LEFT, WEST -> 2;
        case UP, NORTH -> 3;
        case DOWN, SOUTH -> 1;
        default -> throw new IllegalArgumentException("Unexpected value: " + p.heading());
        };
        return 1000 * (p.pos().y() + 1) + 4 * (p.pos().x() + 1) + facing;
    }

    Point origin(int face) {
        var p = faceMap.findPoint(face);
        return Point.of(A * p.x(), A * p.y());
    }

    void part2() {

        // flatten instructions to 1 / L / R
        instr = instr.flatMap(
                x -> List.of("L", "R").contains(x) ? List.of(x) : List.range(0, Integer.valueOf(x)).map(n -> "1"));

        var cube = new Cube();
        cube.buildFaces(faceMap.findPoint(1), id);

        var p = new AntPose(cube.getFace(1), Direction.EAST, Point.of(0, 0));
        System.out.println("start: " + p);

        for (var i : instr) {
//            System.out.println("instr: " + i);
//            System.out.println(p.absolute());

            if (Character.isDigit(i.charAt(0))) {
                var a = p.ahead();
                if (!a.valid()) {
                    // move to other face
                    var nextface = cube.neighbor(p.face, p.heading);
                    var nextheading = cube.wrap(p.face, p.heading);
                    a = p.wrap();
                    a = a.rot(nextheading, nextface);
                }
                if (map.getOrDefault(a.pos().plus(a.face.origin), '?') != '#') {
                    p = a;
                } else {
//                        System.out.println("blocked");
                }
            } else {
                p = new AntPose(p.face, p.heading.turn("R".equals(i)), p.pos);
            }
        }
        
        Pose pose = new Pose(p.heading, p.pos.plus(p.face.origin));
        System.out.print("password for " + pose + ": ");
        System.out.println(password(pose));

    }

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
