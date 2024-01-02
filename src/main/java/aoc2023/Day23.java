package aoc2023;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 23: A Long Walk ---
// https://adventofcode.com/2023/day/23

class Day23 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 2358
        timed(() -> new Day23().part1());

        System.out.println("=== part 2"); // 6586
        timed(() -> new Day23().part2());
    }

    List<String> data = file2lines("input23.txt");
//    List<String> data = Util.splitLines(example);

    record Path(Set<Point> points, Point last, int len) {
        Path add(Point p) {
            return new Path(points.add(p), p, len+1);
        }

        Path add(Edge e) {
            return new Path(points.add(e.b), e.b, len + e.dist + 1);
        }

        boolean contains(Point p) {
            return points.contains(p);
        }

        public int size() {
            return points.size() - 1;
        }
    }


    class HikeMap extends PointMap<Character> {
        Point start;
        Point dest;

        public HikeMap(List<String> data) {
            read(data);
            findPoints('#').forEach(this::remove);

            start = List.ofAll(findPoints('.')).minBy(p -> p.y()).get();
            dest = List.ofAll(findPoints('.')).maxBy(p -> p.y()).get();
            System.out.println("find paths from " + start + " to " + dest);
        }

        List<Path> nextPaths(Path path) {
            List<Path> r = List.empty();
            var p = path.last();

            Point px;
            px = p.north();
            if (!path.contains(px)) {
                var c = getOrDefault(px, '?');
                if (c == '.' || c == '^')
                    r = r.append(path.add(px));
            }
            px = p.south();
            if (!path.contains(px)) {
                var c = getOrDefault(px, '?');
                if (c == '.' || c == 'v')
                    r = r.append(path.add(px));
            }
            px = p.west();
            if (!path.contains(px)) {
                var c = getOrDefault(px, '?');
                if (c == '.' || c == '<')
                    r = r.append(path.add(px));
            }
            px = p.east();
            if (!path.contains(px)) {
                var c = getOrDefault(px, '?');
                if (c == '.' || c == '>')
                    r = r.append(path.add(px));
            }

            return r;
        }

        public List<Edge> traceEdges(Point p, Set<Point> poi) {
//            System.out.println("tracing simple edges from " + p);
            var p0 = new Path(HashSet.of(p), p, 0);
            var paths = List.of(p0);
            List<Edge> result = List.empty();
            var points = HashSet.ofAll(keySet());

            while (!paths.isEmpty()) {
                List<Path> p1 = List.empty();
                for (var pt : paths) {
                    var next = pt.last.neighbors().filter(points::contains).removeAll(pt.points);
                    for (var n : next) {
                        if (poi.contains(n)) {
                            result = result.append(new Edge(p, n, pt.size()));
                        } else {
                            p1 = p1.append(pt.add(n));
                        }
                    }
                }
                paths = p1;
            }

            return result;
        }
    }

    record Edge(Point a, Point b, int dist) {
    }

    void part1() {
        var m = new HikeMap(data);
        var p0 = new Path(HashSet.of(m.start), m.start, 0);
        var paths = List.of(p0);
        List<Path> result = List.empty();

        while (!paths.isEmpty()) {
            var next = paths.flatMap(p -> m.nextPaths(p));
            var done = next.filter(p -> p.last.equals(m.dest));
            result = result.appendAll(done);
            paths = next.removeAll(done);
        }

        System.err.println(result.map(p -> p.size()));
        System.err.println(result.map(p -> p.size()).max());
    }

    void part2() {
        var m = new HikeMap(data);
        var points = List.ofAll(m.keySet());

        // reduce graph to "interesting" nodes (having more than 2 edges, ie. intersections)
        var intersections = points.filter(p -> p.neighbors().count(n -> m.containsKey(n)) > 2);
        var poi = intersections.append(m.start).append(m.dest).toSet();
        var edges = poi.flatMap(x -> m.traceEdges(x, poi.remove(x)));
//        
        System.out.println("poi: " + poi.size());
        System.out.println("edges: " + edges.size());

        var paths = HashSet.of(new Path(HashSet.of(m.start), m.start, 1));
        var edgeMap = poi.toMap(it -> it, it -> edges.filter(e -> it.equals(e.a)));

        // brute force, enumerate all paths
        int longest = 0;
        while (!paths.isEmpty()) {
            HashSet<Path> nextPaths = HashSet.empty();
            for (var p : paths) {
                var nextEdges = edgeMap.get(p.last).get().filter(e -> !p.contains(e.b));

                for (var e : nextEdges) {
                    if (e.b.equals(m.dest)) {
                         longest = Math.max(longest, p.len + e.dist);
                    } else {
                        nextPaths = nextPaths.add(p.add(e));
                    }
                }
            }
            paths = nextPaths;
            System.out.println(paths.size() + " -> " + longest);
        }
    }

    static String example = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """;
}
