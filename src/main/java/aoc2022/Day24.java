package aoc2022;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 24: Blizzard Basin ---
// https://adventofcode.com/2022/day/24

class Day24 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day24().solve());
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input24.txt");
    static int W;
    static int H;
    

    record Blizzard(Point p0, Direction dir) {
        Point position(int t) {
            return switch (dir) {
            case DOWN -> Point.of(p0.x(), (p0.y() - 1 + t) % H + 1);
            case RIGHT -> Point.of((p0.x() - 1 + t) % W + 1, p0.y());
            case UP -> Point.of(p0.x(), ((p0.y() - 1 - t % H) + H) % H + 1);
            case LEFT -> Point.of(((p0.x() - 1 - t % W) + W ) % W + 1, p0.y());
            default -> throw new IllegalArgumentException("Unexpected value: " + dir);
            };
        }
    }

    Set<Point> move(Point p) {
       return HashSet.of(p, p.north(), p.south(), p.east(), p.west()); 
    }

    void solve() {
        PointMap<Character> map = new PointMap<Character>();
        map.read(data);
        var bb = map.boundingBox();
        H = bb.height() - 2;
        W = bb.width() - 2;

        var wall = map.findPoints('#');
        var ground = HashSet.ofAll(map.keySet()).removeAll(wall);
        var start = ground.minBy(Point::y).get(); 
        var goal = ground.maxBy(Point::y).get();
        var blizzards = map
                .findPoints(List.of('^','v','>','<')::contains)
                .map(p -> new Blizzard(p, Direction.parse(map.getOrDefault(p, null))))
                .toSet();

//        map.print();
        System.out.println("start: " + start);
        System.out.println("goal: " + goal);
        
        
        System.out.println("=== part 1"); // 305
        var time = trip(start, goal, blizzards, ground, 0);
        System.out.println(time);
        
        System.out.println("=== part 2"); // 905
        time = trip(goal, start, blizzards, ground, time);
        time = trip(start, goal, blizzards, ground, time);
        System.out.println(time);
    }

    int trip(Point start, Point goal, Set<Blizzard> blizzards, Set<Point> ground, int time) {
        var waypoint = HashSet.of(start);
        while (! waypoint.contains(goal)) {
            time++;
            var t = time;
            waypoint = waypoint
                    .addAll(waypoint.flatMap(this::move))
                    .intersect(ground)
                    .removeAll(blizzards.map(b -> b.position(t)));
        }
        return time;
    }

    static String example1 = """
#.#####
#...v.#
#..>..#
#.....#
#.....#
#.....#
#####.#            
            """;
    
    static String example = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
                        """;

}
