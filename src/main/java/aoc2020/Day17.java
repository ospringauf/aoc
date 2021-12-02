package aoc2020;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.function.Function;

import common.AocPuzzle;
import common.PointMap;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;

// --- Day 17: Conway Cubes ---
// https://adventofcode.com/2020/day/17

class Day17 extends AocPuzzle {

    static final char ACTIVE = '#';
    static final char INACTIVE = '.';

    /**
     * point in 4-dim space
     */
    static record Cube(int x, int y, int z, int w) {

        static final Cube ZERO = new Cube(0,0,0,0); 
        
        // neighbor delta in any direction
        static final Array<Integer> OFFSET = Array.of(-1,0,+1);
        
        // pre-calc relative neighbor vectors in 3d
        static final Array<Cube> VECTORS_3D = 
                OFFSET
                .crossProduct(3)
                .map(v -> v.append(0)) // set w=0 fixed
                .map(Cube::new)
                .toArray()
                .remove(ZERO);

        // pre-calc relative neighbor vectors in 4d as (-1,0,+1) x ... x (-1,0,+1)
        static final Array<Cube> VECTORS_4D = OFFSET.crossProduct(4).map(Cube::new).toArray().remove(ZERO);
        
        private Cube(Array<Integer> v) {
            this(v.get(0), v.get(1), v.get(2), v.get(3));
        }

        Cube shift(Cube delta) {
            return new Cube(x+delta.x, y+delta.y, z+delta.z, w+delta.w);
        }
        
        Seq<Cube> neighbors3d() {
            return VECTORS_3D.map(this::shift);
        }

        Seq<Cube> neighbors4d() {
            return VECTORS_4D.map(this::shift);
        }
    }
    
    
    /**
     * map: cube -> current status (ACTIVE/INACTIVE)
     */
    static class ConwayPocket extends HashMap<Cube, Character> {
        
        void expand(Function<Cube, Seq<Cube>> neighbors) {
            var source = HashSet.ofAll(keySet());
            
            // optimization: expand only active points 
            source = source.filter(p -> get(p) == ACTIVE);
            
            var newNeighbors = source
                    .flatMap(p -> neighbors.apply(p))
                    .removeAll(keySet());
            
            putAll(newNeighbors.toMap(p->p, p->INACTIVE).toJavaMap());
        }        
        
        /**
         * single conway cycle, produces a new map (configuration)
         */
        ConwayPocket singleCycle(Function<Cube, Seq<Cube>> neighbors) {
            
            expand(neighbors);
            ConwayPocket next = new ConwayPocket();

            for (Cube p : keySet()) {
                int activeNeighbors = neighbors.apply(p).count(n -> this.getOrDefault(n, '?') == ACTIVE);

                if (this.get(p) == ACTIVE) { 
                    next.put(p, (activeNeighbors == 2 || activeNeighbors == 3) ? ACTIVE : INACTIVE);
                } else {
                    next.put(p, (activeNeighbors == 3) ? ACTIVE : INACTIVE);
                }
            }
            return next;            
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
        solve(input, 6, Cube::neighbors3d);
    }

    void part2() {
        solve(input, 6, Cube::neighbors4d);
    }

    void solve(String initial, int cycles, Function<Cube, Seq<Cube>> neighbors) {

        // read initial 2d configuration
        var start = new PointMap<Character>();
        start.read(initial.split("\n"), c -> c);

        // transform to 2d-slice of n-dim map
        var map = new ConwayPocket();
        for (var p : start.keySet())
            map.put(new Cube(p.x(), p.y(), 0, 0), start.get(p));

        // simulate (6) cycles
        for (int i = 1; i <= cycles; ++i) {
            map = map.singleCycle(neighbors);
            
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

        assertEquals(26, Cube.ZERO.neighbors3d().size());
        assertEquals(80, Cube.ZERO.neighbors4d().size());
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
