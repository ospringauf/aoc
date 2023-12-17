package aoc2023;

import java.util.HashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import common.PointMap.PathResult;
import io.vavr.collection.*;

//--- Day x:  ---
// https://adventofcode.com/2023/day/x

class Day17p1 extends AocPuzzle {

    public static void main(String[] args) {
        // 1074 too low
        // 1109 too low
        // 1115 too high

        System.out.println("=== part 1"); // 1110
        timed(() -> new Day17p1().part1());
        System.out.println("=== part 2");
        timed(() -> new Day17p1().part2());
    }

    List<String> data = file2lines("input17.txt");
//    List<String> data = Util.splitLines(example);

    class HeatMap extends PointMap<Integer> {

        public void bfs(Mpose start, Point dest) {
            final int INF = Integer.MAX_VALUE / 2;
            var poses = List.of(start);
            var dist = new HashMap<Mpose, Integer>();
            dist.put(start, 0);

            while (!poses.isEmpty()) {
                System.out.println("poses: " + poses.size());
                List<Mpose> poses2 = List.empty();
                
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
                poses = poses2;
            }

            var l = List.ofAll(dist.keySet());
            for (var x : l.filter(y -> y.pos().equals(dest))) {
                System.err.println(x + " --> " + dist.get(x));
            }
        }

    }

//        public void bfs(Mpose start, Point dest) {
//            final int INF = Integer.MAX_VALUE / 2;
//
//            var dist0 = new PointMap<Integer>(size());
//            var dist1 = new PointMap<Integer>(size());
//            var dist2 = new PointMap<Integer>(size());
//            var dist3 = new PointMap<Integer>(size());
//            dist0.put(start.pos(), 0);
//
//            var poses = List.of(start);
//
//            boolean better = true;
//            while (!poses.isEmpty()) {
//
//                System.out.println("poses: " + poses.size());
//                better = false;
//                List<Mpose> poses2 = List.empty();
//                for (var p : poses) {
////                    var d = switch (p.steps()) {
////                        case 1 -> dist1.get(p.pos());
////                        case 2 -> dist2.get(p.pos());
////                        case 3 -> dist3.get(p.pos());
////                        default -> dist0.get(p.pos());
////                    };
//
////                    var d = List.of(
////                            dist0.getOrDefault(p.pos(), INF), 
////                            dist1.getOrDefault(p.pos(), INF), 
////                            dist2.getOrDefault(p.pos(), INF), 
////                            dist3.getOrDefault(p.pos(), INF)).min().get();
//                    int d = 0;
//                    if (p.steps() == 0) d = dist0.getOrDefault(p.pos(), INF);
//                    if (p.steps() == 1) d = dist1.getOrDefault(p.pos(), INF);
//                    if (p.steps() == 2) d = dist2.getOrDefault(p.pos(), INF);
//                    if (p.steps() == 3) d = dist3.getOrDefault(p.pos(), INF);
//                    
//                    var nx = p.next().filter(x -> containsKey(x.pos()));
//                    for (var n : nx) {
//                        var c = get(n.pos());
//
//                          var dn = List.of(
//                          dist0.getOrDefault(n.pos(), INF), 
//                          dist1.getOrDefault(n.pos(), INF), 
//                          dist2.getOrDefault(n.pos(), INF), 
//                          dist3.getOrDefault(n.pos(), INF)).min().get();
//                        
//                        if (n.steps() == 0) {
//                            //if (d + c < dist0.getOrDefault(n.pos(), INF)) {
//                            if (d + c < dn) {
//                                poses2 = poses2.append(n);
//                                dist0.put(n.pos(), d + c);
//                                better = true;
//                            }
//                        }
//                        if (n.steps() == 1) {
////                            if (d + c < dist1.getOrDefault(n.pos(), INF)) {
//                            if (d + c < dn) {
//                                poses2 = poses2.append(n);
//                                dist1.put(n.pos(), d + c);
//                                better = true;
//                            }
//                        }
//
//                        if (n.steps() == 2) {
////                            if (d + c < dist2.getOrDefault(n.pos(), INF)) {
//                            if (d + c < dn) {
//                                poses2 = poses2.append(n);
//                                dist2.put(n.pos(), d + c);
//                                better = true;
//                            }
//                        }
//
//                        if (n.steps() == 3) {
////                            if (d + c < dist3.getOrDefault(n.pos(), INF)) {
//                            if (d + c < dn) {
//                                poses2 = poses2.append(n);
//                                dist3.put(n.pos(), d + c);
//                                better = true;
//                            }
//                        }
//
//                    }
//                }
//                poses = poses2.distinct();
//            }
//
//            System.err.println(dist0.get(dest));
//            System.err.println(dist1.get(dest));
//            System.err.println(dist2.get(dest));
//            System.err.println(dist3.get(dest));
//        }
//
//    }

//	    public void bfs(Mpose start, Point dest) {
//	        final int INF = Integer.MAX_VALUE / 2;
//	        
//	        var dist = new PointMap<Integer>(size());
//	        dist.put(start.pos(), 0);
//	        
//	        var poses = List.of(start);	        
//	        
//	        boolean better = true;
//	        while (!poses.isEmpty()) {
//	            
//	            System.out.println("poses: " + poses.size());
//	            better = false;
//	            List<Mpose> poses2 = List.empty();
//	            for (var p : poses) {
//	                var nx = p.next().filter(x -> containsKey(x.pos()));
//	                for (var n : nx) {
//	                    var c = get(n.pos());
//	                    var d = dist.get(p.pos());
//	                    if (d + c < dist.getOrDefault(n.pos(), INF)) {
//	                        poses2 = poses2.append(n);
//	                        dist.put(n.pos(), d+c);
//	                        better = true;
//	                    }
//	                }
//	            }
//	            poses = poses2.distinct();	            
//	        }
//	        
//	        System.err.println(dist.get(dest));
//	    }
//	    
//	}
//	
    void part1() {
        var m = new HeatMap();
        m.read(data, c -> c - '0');
        m.print();

        var bb = m.boundingBox();
        var start = Point.of(0, 0);
        var dest = Point.of(bb.xMax(), bb.yMax());
        System.out.println(dest);

        var p0 = new Mpose(Direction.EAST, Point.of(0, 0), 0);
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
}
