package aoc2022;

import common.AocPuzzle;
import common.Pos3;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 18: Boiling Boulders ---
// https://adventofcode.com/2022/day/18

class Day18a extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 4192
        new Day18a().part1();
        new Day18a().part1a();
        System.out.println("=== part 2"); // 2520
        new Day18a().part2();
    }

    List<String> data = Util.splitLines(example);
//    List<String> data = file2lines("input18.txt");

    List<Pos3> cubes = data.map(Pos3::parse);

    record Range(int min, int max) {
        int len() {
            return max - min + 1;
        }

        boolean contains(int x) {
            return min <= x && x <= max;
        }
        
        boolean edge(int x) {
            return x==min || x==max;
        }
    }

    record Box(Range xr, Range yr, Range zr) {
        boolean contains(Pos3 p) {
            return xr.contains(p.x()) && yr.contains(p.y()) && zr.contains(p.z());
        }

        public int volume() {
            return xr.len() * yr.len() * zr.len();
        }
        
        boolean edge(Pos3 p) {
            return xr.edge(p.x()) || yr.edge(p.y()) || zr.edge(p.z());
        }
        
        List<Pos3> points() {
            return
            List.rangeClosed(xr.min, xr.max)
            .flatMap(x -> List.rangeClosed(yr.min, yr.max)
                    .flatMap(y -> List.rangeClosed(zr.min, zr.max).map(z -> new Pos3(x,y,z))));
        }

        public List<Pos3> hull(Pos3 p, List<Pos3> blocked) {
            var hull = List.of(p);
            Set<Pos3> next = HashSet.of(p);
            while (next.nonEmpty()) {
                hull = hull.appendAll(next);
                
                next = hull.flatMap(c -> c.neighbors6()).toSet();
                next = next.filter(this::contains);
                next = next.removeAll(blocked);
                next = next.removeAll(hull);
            }
            return hull;
        }
    }

    void part1() {
        System.out.println("checking area of: " + cubes.size());

        var r = cubes.map(c -> 6 - c.neighbors6().count(n -> cubes.contains(n))).sum();
        System.out.println(r);
    }
    
    void part1a() {
        var s = cubes.toSet();
        System.out.println("checking area of: " + s.size());

        var r = s.toList().map(c -> 6 - c.neighbors6().count(n -> s.contains(n))).sum();
        System.out.println(r);
    }


    void part2() {
        var x = cubes.map(c -> c.x());
        var y = cubes.map(c -> c.y());
        var z = cubes.map(c -> c.z());
        var xr = new Range(x.min().get(), x.max().get());
        var yr = new Range(y.min().get(), y.max().get());
        var zr = new Range(z.min().get(), z.max().get());
        var box = new Box(xr, yr, zr);

        System.out.println(box);
        System.out.println("volume: " + box.volume());
        
        var all = box.points();
        var todo = all.removeAll(cubes);
        List<Pos3> bubble = List.empty();
        
        // check all "non-cube" points if they are in a bubble
        while (todo.nonEmpty()) {
            System.out.println(todo.size());
            var p = todo.head();
            var h = box.hull(p, cubes);
            System.out.println("hull" + p + " = " + h.size());
            
            if (! h.exists(c -> box.edge(c))) {
                bubble = bubble.appendAll(h);
            }
            
            todo = todo.remove(p);
            todo = todo.removeAll(h);
        }
        
        System.out.println("bubble: " + bubble.size());
        cubes = cubes.appendAll(bubble);
        part1();
        
    }

    static String example = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
                        """;

}
