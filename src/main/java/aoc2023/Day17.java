package aoc2023;

import java.util.HashMap;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 17: Clumsy Crucible  ---
// https://adventofcode.com/2023/day/17

class Day17 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1110
        timed(() -> new Day17().part1());
        System.out.println("=== part 2"); // 1294
        timed(() -> new Day17().part2());
    }

    List<String> data = file2lines("input17.txt");
//    List<String> data = Util.splitLines(example);
    
    record Crucible(Direction heading, Point pos, int steps) {

        public Crucible turnRight() {
            return new Crucible(heading.right(), pos, 0);
        }

        public Crucible turnLeft() {
            return new Crucible(heading.left(), pos, 0);
        }
        
        public List<Crucible> next() {
            if (steps == 3)
                return List.of(turnLeft().ahead(), turnRight().ahead());
            else
                return List.of(ahead(), turnLeft().ahead(), turnRight().ahead());
        }
        
        public List<Crucible> next2() {
            if (steps == 10)
                return List.of(turnLeft().ahead(), turnRight().ahead());
            else if (steps < 4)
                return List.of(ahead());
            else 
                return List.of(ahead(), turnLeft().ahead(), turnRight().ahead());
        }

        public Crucible ahead() {
            return new Crucible(heading, pos.translate(heading, 1), steps+1);
        }

        public String toString() {
            return String.format("%s-%d@%s", heading, steps, pos);
        }
    }

    class HeatMap extends PointMap<Integer> {

        public void bfs(Set<Crucible> start, Point dest, int part) {
            final int INF = Integer.MAX_VALUE / 2;
            var poses = HashSet.ofAll(start);
            var dist = new HashMap<Crucible, Integer>();
            start.forEach(c -> dist.put(c, 0));
            var seen = HashSet.ofAll(start);

            while (!poses.isEmpty()) {
                System.out.println("poses: " + poses.size());
                HashSet<Crucible> poses2 = HashSet.empty();
                
                for (var p : poses) {
                    var dp = dist.get(p);
                    var next = (part == 1) ? p.next() : p.next2();
                    next = next.filter(x -> containsKey(x.pos()));
                    for (var n : next) {
                        var c = get(n.pos());
                        var dn = dist.getOrDefault(n, INF);
                        if (dp + c < dn) {
                            dist.put(n, dp+c);
                            poses2 = poses2.add(n);
                        }
                    }
                }
                poses = poses2;
//                poses = poses2.removeAll(seen);
//                seen = seen.addAll(poses);
            }
            
            // TODO: part 2, can't stop before 4 blocks
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

        var bb = m.boundingBox();
        var dest = Point.of(bb.xMax(), bb.yMax());
        var start = HashSet.of(new Crucible(Direction.EAST, Point.of(0, 0), 0));

        m.bfs(start, dest, 1);
    }

    void part2() {
    	 var m = new HeatMap();
         m.read(data, c -> c - '0');

         var bb = m.boundingBox();
         var dest = Point.of(bb.xMax(), bb.yMax());
         var start = HashSet.of(
        		 new Crucible(Direction.EAST, Point.of(0, 0), 0),
        		 new Crucible(Direction.SOUTH, Point.of(0, 0), 0));

         m.bfs(start, dest, 2);
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
}
