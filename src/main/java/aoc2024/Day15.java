package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 15: Warehouse Woes ---
// https://adventofcode.com/2024/day/15

class Day15 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1495147
        timed(() -> new Day15().part1());
        System.out.println("=== part 2"); // 1524905
        timed(() -> new Day15().part2());
    }

    String data = file2string("input15.txt");
    boolean DEBUG = false;
//    String data = example1b;
//    boolean DEBUG = true;

    PointMap<Character> map = new PointMap<Character>();

    void part1() {
        var blocks = data.split("\n\n");
        map.read(Util.splitLines(blocks[0]));
        String b1 = Util.splitLines(blocks[1]).mkString();
        var cmd = List.ofAll(b1.toCharArray()).map(Direction::parse);

        if (DEBUG)
            map.print();

        var robot = map.findPoint('@');
        map.put(robot, '.');

        while (cmd.nonEmpty()) {
            var d = cmd.head();
            cmd = cmd.tail();

            var rnext = robot.translate(d);
            var obj = map.get(rnext);

            robot = switch (obj) {
            case '.' -> rnext;
            case '#' -> robot;
            case 'O' -> pushBoxes1(rnext, d) ? rnext : robot;
            default -> robot;
            };

            if (DEBUG) {
                System.out.println();
                map.print();
            }
        }

        var boxes = map.findPoints('O');
        var r = boxes.map(box -> 100 * box.y() + box.x());
        System.out.println(r.sum());
    }

    boolean pushBoxes1(Point p0, Direction dir) {
        var p = p0;
        while (map.get(p) == 'O')
            p = p.translate(dir);

        if (map.get(p) == '.') {
            map.put(p, 'O');
            map.put(p0, '.');
            return true;
        } else
            return false;
    }

    
    void part2() {
        var blocks = data.split("\n\n");

        // stretch map
        var m2 = HashMap.of('#', "##", '.', "..", 'O', "[]", '@', "@.");
        var lines = Util.splitLines(blocks[0]);
        lines = lines.map(l -> List.ofAll(l.toCharArray()).map(c -> m2.get(c).get()).mkString());
        map.read(lines);

        String b1 = Util.splitLines(blocks[1]).mkString();
        var cmd = List.ofAll(b1.toCharArray()).map(Direction::parse);

        if (DEBUG)
            map.print();

        var robot = map.findPoint('@');
        map.put(robot, '.');

        while (cmd.nonEmpty()) {
            var dir = cmd.head();
            cmd = cmd.tail();

            var rnext = robot.translate(dir);
            var obj = map.get(rnext);

            robot = switch (obj) {
            case '.' -> rnext;
            case '#' -> robot;
            case '[' -> pushBoxes2(HashSet.of(rnext, rnext.east()), dir) ? rnext : robot;
            case ']' -> pushBoxes2(HashSet.of(rnext, rnext.west()), dir) ? rnext : robot;
            default -> robot;
            };

            if (DEBUG) {
                map.put(robot, '@');
                System.out.println();
                map.print();
                map.put(robot, '.');
            }
        }

        var boxes = map.findPoints('[');
        var r = boxes.map(box -> 100 * box.y() + box.x());
        System.out.println(r.sum());

    }

    Set<Point> cone(Set<Point> c0, Direction dir) {
        var cone = c0;

        boolean more = true;
        while (more) {
            var next = cone.map(x -> x.translate(dir)).filter(p -> map.get(p) == '[' || map.get(p) == ']');
            var w = next.filter(p -> map.get(p) == ']').map(p -> p.west());
            var e = next.filter(p -> map.get(p) == '[').map(p -> p.east());
            next = next.addAll(w).addAll(e);

            var cone1 = cone.addAll(next);
            more = cone1.size() > cone.size();
            cone = cone1;
        }

        return cone;
    }

    boolean pushBoxes2(Set<Point> p, Direction dir) {
        var c = cone(p, dir);

        // blocked?
        var ahead = c.map(x -> map.get(x.translate(dir)));
        if (ahead.contains('#'))
            return false;

        // move cone
        var mem = c.toMap(x -> x.translate(dir), x -> map.get(x));
        c.forEach(x -> map.put(x, '.'));
        mem.forEach(e -> map.put(e._1, e._2()));
        return true;
    }

    static String example1a = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
                        """;

    static String example1b = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
            """;

    static String example2 = """
            #######
            #...#.#
            #.....#
            #..OO@#
            #..O..#
            #.....#
            #######

            <vv<<^^<<^^
                        """;

}
