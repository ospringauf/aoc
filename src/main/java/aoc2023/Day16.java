package aoc2023;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Pose;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

//--- Day 16: The Floor Will Be Lava ---
// https://adventofcode.com/2023/day/16

class Day16 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 8389
        timed(() -> new Day16().part1());
        System.out.println("=== part 2"); // 8564
        timed(() -> new Day16().part2());
    }

    List<String> data = file2lines("input16.txt");
//    List<String> data = file2lines("input16_1.txt");

    class BeamMap extends PointMap<Character> {

        List<Pose> next(Pose p) {
            var c = get(p.pos());            
            var ns = p.heading() == Direction.NORTH || p.heading() == Direction.SOUTH;
            
            List<Pose> n = switch (c) {
            case '-' -> ns ? List.of(p.turnLeft(), p.turnRight()) : List.of(p);
            case '|' -> ns ? List.of(p) : List.of(p.turnLeft(), p.turnRight());
            case '/' -> ns ? List.of(p.turnRight()) : List.of(p.turnLeft());
            case '\\' -> ns ? List.of(p.turnLeft()) : List.of(p.turnRight());
            default -> List.of(p); // empty tile
            };

            return n.map(x -> x.ahead()).filter(x -> containsKey(x.pos()));
        }
        
        int energizeGrid(Pose p) {
            boolean stable = false;
            var heads = HashSet.of(p);
            var seen = heads;
            while (!stable) {
                var next = heads.flatMap(this::next);
                stable = seen.containsAll(next);
                heads = next.removeAll(seen);
                seen = seen.addAll(next);
            }
            var energized = seen.map(Pose::pos);
            return energized.size();
        }
    }
    
    void part1() {
        var m = new BeamMap();
        m.read(data);
        var p = new Pose(Direction.EAST, Point.of(0, 0));
        System.err.println(m.energizeGrid(p));
    }

    void part2() {
        var m = new BeamMap();
        m.read(data);

        var bb = m.boundingBox();
        var n = List.rangeClosed(0, bb.xMax()).map(x -> new Pose(Direction.SOUTH, Point.of(x, 0)));
        var s = List.rangeClosed(0, bb.xMax()).map(x -> new Pose(Direction.NORTH, Point.of(x, bb.yMax())));
        var w = List.rangeClosed(0, bb.yMax()).map(y -> new Pose(Direction.EAST, Point.of(0, y)));
        var e = List.rangeClosed(0, bb.yMax()).map(y -> new Pose(Direction.WEST, Point.of(bb.xMax(), y)));

        var start = n.appendAll(s).appendAll(e).appendAll(w);
        System.out.println("simulation size: " + start.size());
        var max = start.map(m::energizeGrid).max();
        System.err.println(max);
    }

}
