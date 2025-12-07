package aoc2024;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 24:  ---
// https://adventofcode.com/2024/day/24

// half adder
// z0 = xor(x0,y0)
// c0 = and(x0,y0)

// full adder
// zn = xor(xor(xn,yn), cin)
// cn = or( and(xor(xn,yn), cin), and(xn,yn))

class Day24a extends AocPuzzle {

    public static void main(String[] args) {
//        System.out.println("=== part 1"); // 43942008931358
//        timed(() -> new Day24().part1());
//        System.out.println("=== part 2");
//        timed(() -> new Day24().part2());
        new Day24a().part2();
    }

    String data = file2string("input24.txt");
//	String data = example2;

    enum Op {
        AND, OR, XOR
    }

    List<Gate> gates;
    List<String> signals = List.empty();
    List<String> inputs = List.empty();

    record Gate(String in1, String in2, String out, Op op) {

        static Gate parse(String s) {
            var f = s.split("[ \\-\\>]+");
            return new Gate(f[0], f[2], f[3], Op.valueOf(f[1]));
        }

        Gate swap(String s1, String s2) {
            return new Gate(
                    in1, 
//                     s1.equals(in1) ? s2 : s2.equals(in1) ? s1 : in1,
                    in2, 
//                     s1.equals(in2) ? s2 : s2.equals(in2) ? s1 : in2,
                    s1.equals(out) ? s2 : s2.equals(out) ? s1 : out, op);
        }

        List<String> inputs() {
            return List.of(in1, in2);
        }

        boolean hasInput(String s) {
            return s.equals(in1) || s.equals(in2);
        }

        int eval(Map<String, Integer> value) {
            var v1 = value.get(in1);
            var v2 = value.get(in2);

            return switch (op) {
            case AND -> v1 & v2;
            case OR -> v1 | v2;
            case XOR -> v1 ^ v2;
            };
        }

        void print(Map<String, String> signame) {
            if (inx()) {
                System.out.println(in1 + " -> " + op + "_" + out);
                System.out.println(in2 + " -> " + op + "_" + out);
//                System.out.println(op + "_" + out + " -> " + out);
            } else {
                System.out.println(signame.get(in1) + " -> " + signame.get(out));
                System.out.println(signame.get(in2) + " -> " + signame.get(out));
            }
        }

        boolean inx() {
            return in1.startsWith("x") || in2.startsWith("x");
        }

        
    }

    Map<String, Integer> value = new HashMap<String, Integer>();
    private Map<String, String> signame;

    Day24a() {
        var blocks = data.split("\n\n");
        var init = Util.splitLines(blocks[0]);
        init.map(s -> s.split(": ")).forEach(b -> value.put(b[0], Integer.valueOf(b[1])));
        gates = Util.splitLines(blocks[1]).map(Gate::parse);

        signals = gates.flatMap(g -> List.of(g.in1, g.in2, g.out)).distinct();
        inputs = signals.filter(s -> s.startsWith("x") || s.startsWith("y"));
    }

    void part1() {
        eval();

        var r = z();
        System.out.println(r);
    }

    // wrong: z39, z10
    void part2() {

        gates = gates.map(g -> g.swap("z10", "vcf"));
        gates = gates.map(g -> g.swap("z17", "fhg"));
        gates = gates.map(g -> g.swap("z39", "tnc"));
        gates = gates.map(g -> g.swap("fsq", "dvb"));
        
        signame = gates.toMap(g -> g.out, g -> g.op + "_" + g.out).toJavaMap();

        System.out.println("digraph {");
        System.out.print("subgraph {\n   rank=same; ");
        List.range(0, 45).forEach(i -> System.out.print(String.format("x%02d;", i)));
        System.out.println("\n}");
        System.out.print("subgraph {\n   rank=same; ");
        List.range(0, 45).forEach(i -> System.out.print(String.format("y%02d;", i)));
        System.out.println("\n}");
        
        System.out.print("subgraph {\n   rank=same; ");
        List.range(0, 46).forEach(i -> System.out.print(signame.get(String.format("z%02d", i)) + ";"));
        System.out.println("\n}");
        
        var x1 = gates.filter(g -> g.op==Op.XOR && g.inx() && !"z00".equals(g.out));
        System.out.print("subgraph {\n   rank=same; ");
        x1.forEach(i -> System.out.print(signame.get(i.out) + ";"));
        System.out.println("\n}");
        

        System.out.println();
        for (var g : gates)
            g.print(signame);

        System.out.println("\n}");
        
        var r = List.of("z10","vcf","z17","fhg","z39","tnc","fsq","dvb").sorted().mkString(",");
        System.out.println(r);
        
////		gates = gates.map(g -> g.swap("z39", "vcf"));
//		var x1 = List.range(0, 45).map(i -> String.format("x%02d", i))
//				.flatMap(s -> gates.filter(g -> g.op==Op.XOR && g.hasInput(s)));
//		var x2 = gates.filter(g->g.op==Op.XOR).removeAll(x1);
//		var x1o = x1.map(g -> g.out);
//		
//		System.out.println(x2.filter(g -> !(x1o.contains(g.in1) || x1o.contains(g.in2))));
//		
//		var w1 = gates.filter(g -> g.op==Op.XOR);
//		
//		for (int i=0; i<=45; ++i) {
//			var sz = String.format("z%02d", i);
//			System.out.println(sz + " -> " + dep(sz,2).sorted());
//		}
    }

    Long z() {
        var zs = List.ofAll(value.keySet()).filter(s -> s.startsWith("z")).sorted();
        var out = zs.map(s -> value.get(s));
//		System.out.println(out);

        var r = out.reverse().foldLeft(0L, (n, v) -> 2 * n + v);
        return r;
    }

    void eval() {
        boolean more = true;
        while (more) {
            var tbd = gates
                    .filter(g -> value.containsKey(g.in1) && value.containsKey(g.in2) && !value.containsKey(g.out));
            tbd.forEach(g -> value.put(g.out, g.eval(value)));
            more = tbd.nonEmpty();
        }
    }

    Set<String> zucks = HashSet.empty();

    boolean testz(int n) {
        zucks = HashSet.empty();
        var ok = true;
        for (long x = 0; x <= 1; ++x) {
            for (long y = 0; y <= 1; ++y) {
                var xx = x << n;
                var yy = y << n;
                var z = sim(xx, yy);
                zucks = zucks.addAll(signals.filter(s -> value.getOrDefault(s, 0) != 0));
                // if ((z&3) != ((xx + yy)&3)) {
                if (z != xx + yy) {
                    System.out.println("x=" + x + " y=" + y + " z=" + (z >> n));
                    ok = false;
                }
            }
        }
//		zucks = zucks.addAll(value.keySet());
        zucks = zucks.distinct();
        return ok;
    }

    void part2a() {
//		var d = dep("z03");
//		System.out.println(d.distinct());

        List<Tuple2<String, String>> swap = List.empty();
        Set<String> ok = HashSet.empty();

        for (int n = 0; n < 46; ++n) {
            value.clear();
            String zsig = String.format("z%02d", n);
            String zsig1 = String.format("z%02d", n + 1);
            System.out.println("testing " + zsig);
            if (testz(n)) {
                ok = ok.addAll(dep(zsig));
                System.out.println("ok: " + ok);
            } else {
                System.out.println("error in " + n);

                var s1 = zucks.removeAll(inputs).toList();
                var s2 = dep(zsig).removeAll(ok).removeAll(inputs).append(zsig1).distinct().toList();
                System.out.println(s1);
                System.out.println(s2);

//				s1 = List.of("z10");
//				s2 = List.of("z11");

                for (var t : s2.crossProduct(s1)) {
                    var g0 = gates;
                    gates = gates.map(g -> g.swap(t._1, t._2));
                    boolean tsz = testz(n);
                    if (tsz)
                        System.out.println("... " + t + " -> " + tsz);
                    gates = g0;
                }
                break;
            }
        }

//		var z = sim(0L<<8, 1L<<8);
//		System.out.println(z);
    }

    Long sim(long x, long y) {
        value.clear();
        for (int i = 0; i < 45; ++i) {
            value.put(String.format("x%02d", i), (int) (x % 2));
            x = x / 2;
        }
        for (int i = 0; i < 45; ++i) {
            value.put(String.format("y%02d", i), (int) (y % 2));
            y = y / 2;
        }

        eval();
        return z();
    }

    List<String> dep(String s, int d) {
        if (d == 0)
            return List.of(s);

        var g = gates.find(x -> x.out.equals(s));
        if (g.isDefined()) {
            var gg = g.get();
            return List.of(gg.in1, gg.in2).appendAll(dep(gg.in1, d - 1)).appendAll(dep(gg.in2, d - 1)).append(s)
                    .distinct();
        } else
            return List.of(s);
    }

    List<String> dep(String s) {
        var g = gates.find(x -> x.out.equals(s));
        if (g.isDefined()) {
            var gg = g.get();
            return List.of(gg.in1, gg.in2).appendAll(dep(gg.in1)).appendAll(dep(gg.in2)).append(s);

        } else
            return List.of(s);
    }

    static String example = """
            x00: 1
            x01: 1
            x02: 1
            y00: 0
            y01: 1
            y02: 0

            x00 AND y00 -> z00
            x01 XOR y01 -> z01
            x02 OR y02 -> z02
            """;

    static String example2 = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
            """;

}
