package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.function.Function;

import common.AocPuzzle;
import common.PointMap;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.IndexedSeq;
import io.vavr.collection.Seq;

// --- Day 17: Conway Cubes ---
// https://adventofcode.com/2020/day/17

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day17 extends AocPuzzle {

    static final char ACTIVE = '#';
    static final char INACTIVE = '.';

    /**
     * point in 4-dim space
     */
    static record P4(int x, int y, int z, int w) {
        
        // neighbor delta in any direction
        static Array<Integer> offset = Array.of(-1,0,+1);

        P4 add(IndexedSeq<Integer> delta) {
            return new P4(x+delta.get(0), y+delta.get(1), z+delta.get(2), w+delta.get(3));
        }
        
        Seq<P4> neighbors3d() {
            return offset.crossProduct(3)
                    .map(c -> c.append(0)) // [dx, dy, dz, 0]
                    .map(c -> this.add(c))
                    .toList()
                    .remove(this);
        }

        Seq<P4> neighbors4d() {
            return offset.crossProduct(4).map(c -> this.add(c)).toList().remove(this);
        }
    }
    
    /**
     * map: point -> current status (ACTIVE/INACTIVE)
     */
    static class CubeMap extends HashMap<P4, Character> {
        
        void expand(Function<P4, Seq<P4>> neighbors) {
            var source = HashSet.ofAll(keySet());
            
            // optimization: expand only active points 
            source = source.filter(p -> get(p) == ACTIVE);
            
            var newNeighbors = source
                    .flatMap(p -> neighbors.apply(p))
                    .removeAll(keySet());
            
            putAll(newNeighbors.toMap(p->p, p->INACTIVE).toJavaMap());
        }        
    }
    
    public static void main(String[] args) {
        
        System.out.println("=== test");
        new Day17().test();
        
        var t0 = System.currentTimeMillis();
        
        System.out.println("=== part 1"); // 291
        new Day17().part1();
        System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
        
        System.out.println("=== part 2"); // 1524
        new Day17().part2();
        System.out.format("=== end (%d ms)\n", System.currentTimeMillis() - t0);
    }
    
    void part1() {
        conway(input, 6, P4::neighbors3d);
    }

    void part2() {
        conway(input, 6, P4::neighbors4d);
    }

    void conway(String initial, int cycles, Function<P4, Seq<P4>> neighbors) {

        // read initial 2d configuration
        var start = new PointMap<Character>();
        start.read(initial.split("\n"), c -> c);

        // transform to 2d-slice of n-dim map
        var map = new CubeMap();
        for (var p : start.keySet())
            map.put(new P4(p.x(), p.y(), 0, 0), start.get(p));

        for (int i = 1; i <= cycles; ++i) {
            map.expand(neighbors);
            CubeMap last = map;
            CubeMap next = new CubeMap();

            for (P4 p : last.keySet()) {

                int activeNeighbors = neighbors.apply(p).count(n -> last.getOrDefault(n, '?') == ACTIVE);

                if (last.get(p) == ACTIVE) { 
                    next.put(p, (activeNeighbors == 2 || activeNeighbors == 3) ? ACTIVE : INACTIVE);
                } else {
                    next.put(p, (activeNeighbors == 3) ? ACTIVE : INACTIVE);
                }
            }
            map = next;
            
            var totalActive = map.values().stream().filter(v -> v == ACTIVE).count();
            System.out.println(i + " --> " + totalActive);
        }
    }
    

    void test() {       
        var diff = Array.of(-1,0,1);
        var c1 = diff.crossProduct(diff);
        System.out.println(c1.toList());
        
        var c3 = diff.crossProduct(3).toList();
        assertEquals(27, c3.size());

        var c4 = diff.crossProduct(4).toList();
        assertEquals(81, c4.size());

        var p0 = new P4(0, 0, 0, 0);
        assertEquals(26, p0.neighbors3d().size());
        assertEquals(80, p0.neighbors4d().size());
        System.out.println("passed");
    }

    static String example = """
            .#.
            ..#
            ###
            			""";

    static String input = """
            ##.#....
            ...#...#
            .#.#.##.
            ..#.#...
            .###....
            .##.#...
            #.##..##
            #.####..
            			""";

}
