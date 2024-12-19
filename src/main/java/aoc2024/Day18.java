package aoc2024;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 18: RAM Run ---
// https://adventofcode.com/2024/day/18

class Day18 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day18().solve());
    }

    List<String> data = file2lines("input18.txt");
    int NBYTES = 1024;

//    List<String> data = Util.splitLines(example);
//    int NBYTES = 12;

    void solve() {
        var bytes = data.map(s -> Point.parse(s.replace(',', ' ')));
        var max = bytes.flatMap(p -> List.of(p.x(), p.y())).max().get();
        System.out.println(max);

        PointMap<Character> map = new PointMap<>();
        List.rangeClosed(0, max).crossProduct().forEach(t -> map.put(Point.of(t), '.'));

        var start = Point.of(0, 0);
        var target = Point.of(max, max);
        
        bytes.take(NBYTES).forEach(b -> map.put(b, '#'));

        var r = map.dijkstraAll(start, c -> c == '.');
        Integer dist = r.distance.get(target);
        var path = r.path(start, target).toSet();

        System.out.println("=== part 1"); // 286
//        path.forEach(p -> map.put(p, 'O'));
//        map.print();
        System.out.println(dist);
        
        
        System.out.println("=== part 2"); // 20,64
        bytes = bytes.drop(NBYTES);
        
        while (dist != null) {
            var b = bytes.head();
            bytes = bytes.tail();
            map.put(b, '#');
            
            if (path.contains(b)) {
                r = map.dijkstraAll(start, c -> c == '.');
                dist = r.distance.get(target);
                if (dist != null)
                    path = r.path(start, target).toSet();                
                System.out.println(b + " -> " + dist);
            } else {
//                System.out.println(b);
            }
        }
    }

    static String example = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
            """;
}
