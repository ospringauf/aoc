package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 20: Race Condition ---
// https://adventofcode.com/2024/day/20

class Day20 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1363
        timed(() -> new Day20().part1());
        System.out.println("=== part 2"); // 1007186
        timed(() -> new Day20().part2());
    }

    List<String> data = file2lines("input20.txt");
//	List<String> data = Util.splitLines(example);
    
    PointMap<Character> map = new PointMap<>();
    PointMap<Integer> dst;

    Day20() {
        map.read(data);
        
        var S = map.findPoint('S');
        var E = map.findPoint('E');
        map.put(S, '.');
        map.put(E, '.');
        
        dst = distances(S, E);
        
        System.out.println("distance S->E: " + dst.get(E));        
    }
    
    PointMap<Integer> distances(Point s, Point e) {
        var allowed = map.findPoints('.').toSet();
        var d = new PointMap<Integer>();
        d.put(s, 0);
        var p = s;
        do {
            for (var n : p.neighbors().filter(allowed::contains)) {
                if (! d.containsKey(n)) {
                    d.put(n, d.get(p) + 1);
                    p = n;
                }
            }
        } while (! p.equals(e));
        return d;
    }
    
    int save(Point w) {
        var dir = List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        for (var d : dir) {
            var a = w.translate(d, -1);
            var b = w.translate(d, 1);
            if (dst.containsKey(a) && dst.containsKey(b))
                return Math.abs(dst.get(a) - dst.get(b)) - 2;
        }
        return 0;        
    }
    
    void part1() {
        var walls = map.findPoints('#');
        var saving = walls.map(this::save);
        
//        System.out.println(saving.distinct().toMap(c -> c, c -> saving.count(it -> it==c)));
        System.out.println(saving.filter(c -> c >= 100).size());
    }


    void part2() {
        var cheats = List.ofAll(map.findPoints('.'))
                .crossProduct()
                .filter(t -> t._1.manhattan(t._2) <= 20)
                .filter(t -> dst.get(t._2) > dst.get(t._1))
                ;
        var saving = cheats.map(t -> dst.get(t._2) - dst.get(t._1) - t._2.manhattan(t._1));
        System.out.println(saving.filter(s -> s >= 100).size());
    }

    
    static String example = """
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
""";
    
    
}
