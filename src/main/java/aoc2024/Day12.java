package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 12: Garden Groups ---
// https://adventofcode.com/2024/day/12

class Day12 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1387004
        timed(() -> new Day12().part1());
        System.out.println("=== part 2"); // 844198
        timed(() -> new Day12().part2());
    }

    List<String> data = file2lines("input12.txt");
//    List<String> data = Util.splitLines(example); // 140 / 80
//    List<String> data = Util.splitLines(example3); // 1930 / 1206

    PointMap<Character> map = new PointMap<>();

    void part1() {
        map.read(data);

        int price = 0;
        var plots = HashSet.ofAll(map.keySet());
        
        for (var p : plots) {
            var plant = map.get(p);
            if (plant == '.')
                continue;

            var a = map.connectedArea(p, c -> c == plant).toList();
            var f = a.map(x -> fence(x, plant)).sum().intValue();

            a.forEach(x -> map.put(x, '.'));

            price += (a.size() * f);
//            System.out.println(plant + " -> " + (a.size() * f));
        }
        System.out.println(price);
    }

    int fence(Point x, Character plant) {
        return x.neighbors().count(n -> map.getOrDefault(n, null) != plant);
    }

    
    void part2() {
        map.read(data);

        int price = 0;
        var plots = HashSet.ofAll(map.keySet());
        
        for (var p : plots) {
            var plant = map.get(p);
            if (plant == '.')
                continue;

            var a = map.connectedArea(p, c -> c == plant);
            var f = fronts(a);
            
            a.forEach(x -> map.put(x, '.'));

            price += (a.size() * f);
//            System.out.println(plant + " -> " + (a.size() * f));
        }
        System.out.println(price);
    }

    int fronts(Set<Point> plots) {
        var l = front(front(plots, Direction.WEST), Direction.NORTH).size();
        var r = front(front(plots, Direction.EAST), Direction.NORTH).size();
        var t = front(front(plots, Direction.NORTH), Direction.WEST).size();
        var b = front(front(plots, Direction.SOUTH), Direction.WEST).size();
        return l + r + t + b;
    }
    
    Set<Point> front(Set<Point> plots, Direction d) {
        return plots.reject(x -> plots.contains(x.translate(d)));
    }
    
    static String example = """
            AAAA
            BBCD
            BBCC
            EEEC
            """;

    static String example3 = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;
}
