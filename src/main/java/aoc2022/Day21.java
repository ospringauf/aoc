package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 21: Monkey Math ---
// https://adventofcode.com/2022/day/21

class Day21 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 309248622142100
        timed(() -> new Day21().part1());

        System.out.println("=== part 2"); // 3757272361782
        timed(() -> new Day21().part2());
    }

//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("input21.txt");

    static enum Op {
        add, sub, mul, div, lit, x;

        static Op parse(String s) {
            return switch (s) {
            case "+" -> add;
            case "-" -> sub;
            case "*" -> mul;
            case "/" -> div;
            default -> throw new IllegalArgumentException("Unexpected value: " + s);
            };
        }
    }

    record Expr(Op op, Long val, Expr left, Expr right) {
        static Expr parse(String s, Function1<String, String> resolve) {
            var f = s.split(" ");
            if ("x".equals(s)) {
                return new Expr(Op.x, null, null, null);
            } else if (f.length == 1) {
                return new Expr(Op.lit, Long.valueOf(s), null, null);
            } else {
                var l = parse(resolve.apply(f[0]), resolve);
                var op = Op.parse(f[1]);
                var r = parse(resolve.apply(f[2]), resolve);
                return new Expr(op, null, l, r);
            }
        }

        static Expr add(Expr left, Expr right) {
            return new Expr(Op.add, null, left, right);
        }

        static Expr sub(Expr left, Expr right) {
            return new Expr(Op.sub, null, left, right);
        }

        static Expr mul(Expr left, Expr right) {
            return new Expr(Op.mul, null, left, right);
        }

        static Expr div(Expr left, Expr right) {
            return new Expr(Op.div, null, left, right);
        }

        long eval() {
            return switch (op) {
            case lit -> val;
            case add -> left.eval() + right.eval();
            case sub -> left.eval() - right.eval();
            case mul -> left.eval() * right.eval();
            case div -> left.eval() / right.eval();
            default -> throw new IllegalArgumentException("Unexpected value: " + op);
            };
        }

        boolean containsX() {
            return switch (op) {
            case lit -> false;
            case x -> true;
            default -> left.containsX() || right.containsX();
            };
        }
    }

    void part1() {
        var e = data
                .map(s -> s.split(": "))
                .toMap(a -> a[0], a -> a[1]);
        
        Function1<String, String> resolve = name -> e.getOrElse(name, null);
        var root = Expr.parse(e.getOrElse("root", null), resolve);
        System.out.println(root.eval());
    }

    Expr solveEquation(Expr f, Expr c) {
        // transform "f(x) = C" to final form "x = C" 
        // (where C is an expression that does not contain x)

        if (f.containsX() && c.containsX())
            throw new RuntimeException("I was not programmed for this");

        if (c.containsX())
            return solveEquation(c, f);

        while (f.op != Op.x) {

            if (f.left.containsX()) {
                c = switch (f.op) {
                case add -> Expr.sub(c, f.right); // x+r=c -> x=c-r
                case sub -> Expr.add(c, f.right); // x-r=c -> x=c+r
                case mul -> Expr.div(c, f.right); // x*r=c -> x=c/r
                case div -> Expr.mul(c, f.right); // x/r=c -> x=c*r
                default -> throw new IllegalArgumentException("Unexpected value: " + f.op);
                };
                f = f.left;
            } else {
                c = switch (f.op) {
                case add -> Expr.sub(c, f.left); // l+x=c -> x=c-l
                case sub -> Expr.sub(f.left, c); // l-x=c -> x=l-c
                case mul -> Expr.div(c, f.left); // l*x=c -> x=c/l
                case div -> Expr.div(f.left, c); // l/x=c -> x=l/c
                default -> throw new IllegalArgumentException("Unexpected value: " + f.op);
                };
                f = f.right;
            }
        }

        return c;
    }

    void part2() {
        var e = data
                .map(s -> s.split(": "))
                .toMap(a -> a[0], a -> a[1])
                .put("humn", "x");

        Function1<String, String> resolve = name -> e.getOrElse(name, null);
        var root = Expr.parse(e.getOrElse("root", null), resolve);
        Expr x = solveEquation(root.left, root.right);
        long humn = x.eval();
        System.out.println(humn);
    }

    static String example = """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32
                        """;

}
