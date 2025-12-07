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

class Day24 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 43942008931358
        timed(() -> new Day24().part1());
        System.out.println("=== part 2");
        timed(() -> new Day24().part2());
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

        Gate swapOut(String s1, String s2) {
            return new Gate(in1, in2, s1.equals(out) ? s2 : s2.equals(out) ? s1 : out, op);
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

    Day24() {
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

    void part2() {

        var r = List.of("z10", "vcf", "z17", "fhg", "z39", "tnc", "fsq", "dvb").sorted().mkString(",");
        System.out.println(r);
//        gates = gates.map(g -> g.swapOut("z10", "vcf"));
//        gates = gates.map(g -> g.swapOut("z17", "fhg"));
//        gates = gates.map(g -> g.swapOut("z39", "tnc"));
//        gates = gates.map(g -> g.swapOut("fsq", "dvb"));

        var ha1 = gates.filter(g -> g.inx());
        System.out.println(ha1.map(g -> g.op).distinct() + " / " + ha1.size());
        var ha2 = gates.removeAll(ha1).filter(g -> g.op==Op.AND || g.op == Op.XOR);
        System.out.println(ha2.map(g -> g.op).distinct() + " / " + ha2.size());

        Map<String, Integer> bit = new HashMap<>();
        List.range(0, 45).forEach(i -> bit.put(String.format("x%02d", i), i));
        List.range(0, 45).forEach(i -> bit.put(String.format("y%02d", i), i));
//        List.range(0, 45).forEach(i -> bit.put(String.format("x%02d", i), i));

        boolean more = true;
        while (more) {
            more = false;
            for (var g : gates) {
                var l = g.inputs().map(i -> bit.get(i)).removeAll((Integer)null).distinct();
//                System.out.println(g + " -> " + l);
                if (l.size() == 1 && !bit.containsKey(g.out)) {
                    more = true;
                    bit.put(g.out, l.head());
                }
            }
        }
        
        var rem = gates.reject(g -> bit.containsKey(g.out));
        System.out.println(rem);
        for (var g : rem) {
            var l = g.inputs().map(i -> bit.get(i)).removeAll((Integer)null).distinct();
            if (Math.abs(l.get(0)-l.get(1)) > 1)
                System.out.println(g + " -> " + l);
        }
        
        var ha1a = ha1.filter(g -> g.op == Op.AND);
        for (var g : ha1a) {
            var nf = gates.filter(x -> x.inputs().contains(g.out));
            System.out.println(g + " --> " + nf.map(x -> x.op).distinct());
        }

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

    boolean testz(int n) {
        var ok = true;
        for (long x = 0; x <= 1; ++x) {
            for (long y = 0; y <= 1; ++y) {
                var xx = x << n;
                var yy = y << n;
                var z = sim(xx, yy);
                if (z != xx + yy) {
                    System.out.println("x=" + x + " y=" + y + " z=" + (z >> n));
                    ok = false;
                }
            }
        }
        return ok;
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
