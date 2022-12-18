package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

// --- Day 16: Proboscidea Volcanium ---
// https://adventofcode.com/2022/day/16
// TODO too slow

class Day16a extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 2080
//        timed(() -> new Day16().part1());
        System.out.println("=== part 2"); // 2752
        new Day16a().part2();
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input16.txt");
    
    List<Valve> valves = data.map(Valve::parse);

    java.util.Map<String, Valve> m = valves.toMap(v -> v.id, v -> v).toJavaMap();
    List<Valve> todo = valves.filter(v -> v.flowRate > 0);
    java.util.Map<Valve, java.util.HashMap<Valve, Integer>> dst = valves.toMap(v -> v, v -> dists(v).toJavaMap())
            .toJavaMap();

    record Valve(String id, int flowRate, List<String> tunnels) {
        static Valve parse(String s) {
            var f = split(s, "[ ;,=]+");
            return new Valve(f.s(1), f.i(5), f.toList().drop(10));
        }
        
        public String toString() {
            return id;
        }
    }

    record Path(Valve pos, int min, Map<Valve, Integer> open) {
        int release(int total) {
            var y = open.map(x -> (total - x._2) * x._1.flowRate).sum();
            return y.intValue();
        }

        String desc() {
            return open.map(t -> " " + t._2 + ":" + t._1.id).mkString();
        }
    }

    HashMap<Valve, Integer> dists(Valve s) {
        HashMap<Valve, Integer> d = HashMap.of(s, 0);
        var m = valves.toMap(v -> v.id, v -> v);

        boolean repeat = true;
        while (repeat) {
            HashMap<Valve, Integer> next = HashMap.empty();
            for (var v : d.keySet()) {
                for (var t : v.tunnels.map(x -> m.get(x).get())) {
                    if (d.getOrElse(t, 10000) > d.get(v).get() + 1) {
                        next = next.put(t, d.get(v).get() + 1);
                    }
                }
            }
            repeat = !next.isEmpty();

            for (var v : next.keySet())
                d = d.put(v, next.get(v).get());
        }
        return d;
    }

    record State(int time, Valve pos, Set<Valve> open) {
        int flow() {
            return open.map(v -> v.flowRate).sum().intValue();
        }
    }

    java.util.HashMap<State, Integer> cache = new java.util.HashMap<>();

    final int TIME=26;
    
    int best(State s, List<Valve> todo) {
//      System.out.println(s + " / flow=" + s.flow());
        if (s.time > TIME)
            return 0;

        if (cache.containsKey(s))
            return cache.get(s);

        List<Integer> bests = List.empty();
        var next = todo.removeAll(s.open);

        if (!s.open.contains(s.pos) && s.pos.flowRate>0) {
            var x = new State(s.time + 1, s.pos, s.open.add(s.pos));
            bests = bests.append(best(x, todo.remove(s.pos)) + s.flow());
        }
        else for (var n : next) {
            int d = dst.get(s.pos).get(n);
            if (s.time + d <= TIME) {
                var x = new State(s.time + d, n, s.open);
                bests = bests.append(best(x, todo.remove(s.pos)) + d*s.flow());
            }
        }
        if (bests.isEmpty())
            bests = bests.append(((TIME+1)-s.time)*s.flow());

        var r = bests.max().get();
        cache.put(s, r);
//        System.out.println("result t=" + s.time + " -> " + r);
        return r;
    }

    
    void part1() {
        Valve aa = m.get("AA");
//        todo = List.of("DD", "BB", "JJ", "HH", "EE", "CC").map(s -> m.get(s));
        var r = best(new State(1, aa, HashSet.empty()), todo);
        System.out.println(r);
    }

    void part2() {
        System.out.println(todo.size());

        Valve aa = m.get("AA");
        System.out.println(todo.combinations().size());
        
        List<Integer> bests = List.empty();
        
        for (int k = 0; k<=todo.size()/2; ++k) {
            for (var my : todo.combinations(k)) {
                var ele = todo.removeAll(my);
                
                cache.clear();
                var b1 = best(new State(1, aa, HashSet.empty()), my);
                cache.clear();
                var b2 = best(new State(1, aa, HashSet.empty()), ele);
                bests = bests.append(b1+b2);
            }
            System.out.println("# " + k + " -> " + bests.max().get());
        }

    }


    static String example = """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II
                        """;

}
