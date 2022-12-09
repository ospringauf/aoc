package aoc2022;

import static common.Direction.DOWN;
import static common.Direction.LEFT;
import static common.Direction.RIGHT;
import static common.Direction.UP;

import common.AocPuzzle;
import common.BoundingBox;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

// --- Day 8: Treetop Tree House ---
// https://adventofcode.com/2022/day/8

class Day08 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1803
        timed(() -> new Day08().part1());
        System.out.println("=== part 2"); // 268912
        timed(() -> new Day08().part2());
    }

    // List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input08.txt");

    PointMap<Integer> map = new PointMap<Integer>();
    BoundingBox bb;
    List<Point> trees;

    {
        map.read(data, c -> c - '0');
        bb = map.boundingBox();
        trees = List.ofAll(map.keySet());
    }

    int height(Point p) {
        return map.getOrDefault(p, null);
    }

    // enum all points between p and edge (inclusive), starting with the closest point
    List<Point> lineOfSight(Point p, Direction dir) {
        int distToEdge = switch (dir) {
            case UP -> p.y() - bb.yMin();
            case DOWN -> bb.yMax() - p.y();
            case RIGHT -> bb.xMax() - p.x();
            case LEFT -> p.x() - bb.xMin();
            default -> 0;
        };
        return List.rangeClosed(1, distToEdge).map(i -> p.translate(dir, i));
    }

    boolean isVisibleFrom(Point p, Direction dir) {
        var los = lineOfSight(p, dir);
        var h = height(p);
        return los.isEmpty() || los.forAll(t -> height(t) < h);
    }

    boolean isVisible(Point p) {
        return isVisibleFrom(p, UP) || isVisibleFrom(p, DOWN) || isVisibleFrom(p, RIGHT) || isVisibleFrom(p, LEFT);
    }

    void part1() {
        var result = trees.count(this::isVisible);
        System.out.println(result);
    }

    int viewingDistance(Point p, Direction dir) {
        var los = lineOfSight(p, dir);
        var h = height(p);
        // var d = line.takeWhile(t -> height(t) < h).size();
        // if (d < line.size())
        // d++; // include the last visible tree
        var d = los.splitAtInclusive(t -> height(t) >= h)._1.size();
        return d;
    }

    int scenicScore(Point p) {
        return viewingDistance(p, UP) * viewingDistance(p, DOWN) * viewingDistance(p, RIGHT) * viewingDistance(p, LEFT);
    }

    void part2() {
        var result = trees.map(this::scenicScore).max();
        System.out.println(result.get());
    }

    static String example = """
            30373
            25512
            65332
            33549
            35390
                        """;

}
