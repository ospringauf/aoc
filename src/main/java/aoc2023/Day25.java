package aoc2023;

import java.util.HashMap;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 25: Snowverload ---
// https://adventofcode.com/2023/day/25

class Day25 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 606062
        timed(() -> new Day25().part1());
    }

    List<String> data = file2lines("input25.txt");
//    List<String> data = Util.splitLines(example);
    private Set<Conn> edges;
    private Set<String> nodes;

    record Conn(String c1, String c2) {
        static List<Conn> parse(String s) {
            var f = List.of(s.replace(":", "").split(" "));
            return f.tail().map(x -> Conn.of(f.head(), x));
        }

        static Conn of(String a, String b) {
            if (a.compareTo(b) > 0)
                return of(b, a);
            else
                return new Conn(a, b);
        }

        boolean contains(String c) {
            return c1.equals(c) || c2.equals(c);
        }

        public String other(String s) {
            return c1.equals(s) ? c2 : c1;
        }
    }

    void part1() {
        edges = data.flatMap(Conn::parse).toSet();
        nodes = edges.flatMap(x -> List.of(x.c1, x.c2)).distinct();
        System.out.println("nodes: " + nodes.size());
        System.out.println("edges: " + edges.size());

        HashMap<Conn, Integer> eflow = new HashMap<>();

        // pick some (10..100) random test nodes
        // calc all shortest paths from each test node
        // sum flow over each edge on all shortest path
        
        Set<String> testnodes = HashSet.empty();
        while (testnodes.size() < 15)
            testnodes = testnodes.add(List.ofAll(nodes).get((int) (Math.random() * nodes.size())));        
        
        for (var start : testnodes) {
            System.out.println("dfs from " + start);
            var p = dfs(start, edges);
            for (var node : p.keySet()) {
                // trace back path to node, increase flow on each edge
                Conn e = null;
                var tgt = node;
                do {
                    e = p.getOrDefault(tgt, null);
                    if (e != null) {
                        eflow.put(e, 1 + eflow.getOrDefault(e, 0));
                        tgt = e.other(tgt);
                    }

                } while (e != null);
            }
        }

        // pick 3 edges with highest flow (most travelled)
        var we = List.ofAll(edges).sortBy(e -> eflow.getOrDefault(e, 0));
        for (var e : we.reverse().take(10))
            System.out.println(e + " -> " + eflow.get(e));

        var forbidden = we.reverse().take(3);
        var c1 = forbidden.head().c1;
        var c2 = forbidden.head().c2;
        
        var p1 = dfs(c1, edges.removeAll(forbidden));
        System.out.println(p1.size());
        
        var p2 = dfs(c2, edges.removeAll(forbidden));
        System.out.println(p2.size());

        System.err.println((p1.size() + 1) * (p2.size() + 1));
    }
    

    HashMap<String, Conn> dfs(String start, Set<Conn> allowed) {
        int INF = Integer.MAX_VALUE / 2;
        HashMap<String, Integer> d = new HashMap<>();
        HashMap<String, Conn> pred = new HashMap<>();

        var nedges = nodes.toMap(n -> n, n -> allowed.filter(e -> e.contains(n)).toList());
        
        d.put(start, 0);

        boolean better = true;
        while (better) {
            better = false;
            var knownd = List.ofAll(d.keySet());
            for (var node : knownd) {
                var dn = d.get(node);
                for (var e : nedges.get(node).get()) {
                    var n = e.other(node);
                    if (d.getOrDefault(n, INF) > dn + 1) {
                        d.put(n, dn + 1);
                        pred.put(n, e);
                        better = true;
                    }
                }
            }
        }

        return pred;
    }

    static String example = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
            """;
}
