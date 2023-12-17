package aoc2023;

import java.util.HashMap;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

//--- Day x:  ---
// https://adventofcode.com/2023/day/x

class Day17p2 extends AocPuzzle {

    public static void main(String[] args) {
        // 1074 too low
        // 1109 too low
        // 1115 too high
        
        // part 2
        // 1304 too high

        // TODO: minimum of four blocks in that direction before it can turn (or even before it can stop at the end)
        
        System.out.println("=== part 1"); // 1110
        timed(() -> new Day17p2().part1());
        System.out.println("=== part 2"); // 1294
        timed(() -> new Day17p2().part2());
    }

    List<String> data = file2lines("input17.txt");
//    List<String> data = Util.splitLines(example2);

    class HeatMap extends PointMap<Integer> {

        public void bfs(Xpose start, Point dest) {
            final int INF = Integer.MAX_VALUE / 2;
            var poses = List.of(start);
            var dist = new HashMap<Xpose, Integer>();
            dist.put(start, 0);

            while (!poses.isEmpty()) {
                System.out.println("poses: " + poses.size());
                List<Xpose> poses2 = List.empty();
                
                for (var p : poses) {
                    
                    var d = dist.get(p);
                    var nx = p.next().filter(x -> containsKey(x.pos()));
                    for (var n : nx) {
                        var c = get(n.pos());
                        var dn = dist.getOrDefault(n, INF);
                        if (d + c < dn) {
                            dist.put(n, d+c);
                            poses2 = poses2.append(n);
                        }
                    }
                    
                }
                poses = poses2.distinct();
            }

            var l = List.ofAll(dist.keySet()).filter(y -> y.pos().equals(dest));
            for (var x : l) {
                System.err.println(x + " --> " + dist.get(x));
            }
            
            System.err.println(l.map(x -> dist.get(x)).min());
        }

    }

    void part1() {
        var m = new HeatMap();
        m.read(data, c -> c - '0');
        m.print();

        var bb = m.boundingBox();
        var start = Point.of(0, 0);
        var dest = Point.of(bb.xMax(), bb.yMax());
        System.out.println(dest);

        //var p0 = new Xpose(Direction.EAST, Point.of(0, 0), 0);
        var p0 = new Xpose(Direction.SOUTH, Point.of(0, 0), 0);
        System.out.println(p0.next());

        m.bfs(p0, dest);
    }

    void part2() {
    }

    static String example = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """;
    
    static String example2 = """
111111111111
999999999991
999999999991
999999999991
999999999991            
            """;
}
