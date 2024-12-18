package aoc2024;

import java.util.HashMap;
import java.util.function.BiFunction;

import common.AocPuzzle;
import common.Direction;
import common.PointMap;
import common.Pose;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.List;

//--- Day 16: Reindeer Maze ---
// https://adventofcode.com/2024/day/16

class Day16 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day16().solve());
    }

    List<String> data = file2lines("input16.txt");
//    List<String> data = Util.splitLines(example1b);

    PointMap<Character> map = new PointMap<>();

    void solve() {
        map.read(data);

        var S = map.findPoint('S');
        var E = map.findPoint('E');
        map.put(S, '.');
        map.put(E, '.');

        System.out.println("=== part 1"); // 73432
        var r = new Pose(Direction.EAST, S);
        System.out.println("paths from " + r);
        var ds = minDistances(r);
        var allPoses = List.ofAll(ds.keySet());
        var best = allPoses.filter(p -> E.equals(p.pos())).minBy(ds::get).get();

        var bestCost = ds.get(best);
        System.out.println(best + " -> cost " + bestCost);

        System.out.println();
        System.out.println("=== part 2"); // 496
        r = best.opposite();
        System.out.println("opposite paths from " + r);
        var de = minDistances(r);
        
        System.out.println("tiles on best paths");
        var tiles = allPoses.filter(x -> ds.get(x) + de.get(x.opposite()) == bestCost);
        var bestSeats = tiles.map(t -> t.pos()).distinct();
        System.out.println(bestSeats.size());
        
//        map.print();
//        tiles.forEach(t -> map.put(t.pos(), 'O'));
//        map.print();
    }

    // shortest paths search
    // stripped down version of PointMap.dijkstraAll, but with Poses instead of Points
    HashMap<Pose, Integer> minDistances(Pose start) {
        final int INF = Integer.MAX_VALUE / 2;

        var dist = new HashMap<Pose, Integer>(map.size());
        dist.put(start, 0);

        var allDirs = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        var poses = map.findPoints('.').flatMap(p -> allDirs.map(d -> new Pose(d,p)));
        
        BiFunction<Pose, Pose, Integer> cost = (a,b) -> (a.pos().equals(b.pos())) ? 1000 : 1;
        
        boolean better = true;
        while (better) {
            better = false;
            for (var current : poses) {
                // best neighbor distance?
            	var predecessors = List.of(current.behind(), current.turnLeft(), current.turnRight());
                var bestPred = predecessors.minBy(n -> dist.getOrDefault(n, INF)); 

                if (bestPred.isEmpty())
                    continue;

                var pred = bestPred.get();
                int dp = dist.getOrDefault(pred, INF);
                int dc = dist.getOrDefault(current, INF);
                int c = cost.apply(pred, current);
                if (dp + c < dc) {
                    better = true;
                    dist.put(current, dp + c);
                }
            }
        }

        return dist;
    }
    

    static String example1a = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """;

    static String example1b = """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """;
}
