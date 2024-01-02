package aoc2021;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;
import io.vavr.control.Option;

// --- Day 24: Arithmetic Logic Unit ---
// https://adventofcode.com/2021/day/24

class Day24a extends AocPuzzle {

    List<String> input = file2lines("input24.txt");
//    List<String> input = Util.splitLines(inp0);

    enum Op {
        inp, add, mul, div, mod, eql;
        
        public String toString() {
            return switch (this) {
            case add -> "+";
            case mul -> "*";
            case div -> "/";
            case mod -> "%";
            case eql -> "==";
            case inp -> "?";
            };
        }
    };

    static int inputNr = 0;
    
    static class Cmd {
        Cmd(Op op, char reg, Object arg) {
            this.op = op;
            this.reg = reg;
            this.arg = arg;
        }

        char reg;
        Op op;
        Object arg;
        
        @Override
        public String toString() {
            return String.format("(%c %s %s)", reg, op, arg);
        }

//        Cmd expand(List<Cmd> l) {
//            if (arg instanceof Character c) {
//                while (l.head().reg != c) l = l.tail();
//                this.arg = l.head().expand(l.tail());                
//            }
//            return this;
//        }
        
        static List<Cmd> find(char r, List<Cmd> l) {
            while (l.nonEmpty() && l.head().reg != r) l = l.tail();
            if (l.isEmpty() && r=='z')
                return List.of(new Cmd(Op.inp, 'z', "Z"));
            return l;            
        }
        
//        static Map<Cmd, Object> cache = HashMap.empty();
        Object expanded = null;
        
        
        Object expand(Option<List<Cmd>> option) {
            if (expanded != null)
                return expanded;
            
//            System.out.println("expand " + this);
            if (op == Op.inp) 
                return "s[" + arg + "]";
            
            if (option.isEmpty())
                return Integer.valueOf(0);
            
            var l1 = find(reg, option.get());
            var a1 = l1.isEmpty() ? 0 : l1.head().expand(l1.tailOption());
            
            Expr expr = null;
            if (arg instanceof Character a) {
                List<Cmd> l2 = find(a, option.get());
                var a2 = l2.isEmpty() ? 0 : l2.head().expand(l2.tailOption());
                expr = new Expr(op, a1, a2); 
            } else {
                expr = new Expr(op, a1, arg);
            }
            expanded = expr.simplify();
            return expanded;
        }
    }
    
    record Expr(Op op, Object l, Object r) {
        public String toString() {
            return String.format("(%s %s %s)", l, op, r);
        }

        Object simplify() {
            
            if (op == Op.mul) {
                if (l instanceof Integer i && i == 0)
                    return 0;
                if (r instanceof Integer i && i == 0)
                    return 0;
                if (l instanceof Integer i && i == 1)
                    return r;
                if (r instanceof Integer i && i == 1)
                    return l;

            }
            
            if (op == Op.div) {
                if (l instanceof Integer i && i == 0)
                    return 0;
                if (r instanceof Integer i && i == 1)
                    return l;
            }

            if (op == Op.add) {
                if (l instanceof Integer i && i == 0)
                    return r;
                if (r instanceof Integer i && i == 0)
                    return l;
            }

            if (op == Op.mod) {
                if (l instanceof Integer i && i == 0)
                    return 0;
                if (l instanceof Expr e && e.op == Op.eql)
                    return l;
            }
            
            if (op == Op.eql) {
                if (l instanceof String && r instanceof Integer i && i >= 10)
                    return 0;
                if (r instanceof String && l instanceof Integer i && i >= 10)
                    return 0;
                if (l instanceof String && r instanceof Integer i && i < 0)
                    return 0;
                if (r instanceof String && l instanceof Integer i && i < 0)
                    return 0;
                if (l instanceof Integer li && r instanceof Integer ri && li == ri)
                    return 1;
            }


            return this;
        }
        
        boolean depends(String inp) {
            if (l instanceof String s && s.equals(inp)) return true;
            if (r instanceof String s && s.equals(inp)) return true;
            var d = false;
            d |= (l instanceof Expr e && e.depends(inp));
            d |= (r instanceof Expr e && e.depends(inp));
            return d;
        }

    }

    
    Cmd parse(String s) {
        var f = s.split(" ");
        if ("inp".equals(f[0])) {
            return new Cmd(Op.inp, 'w', Integer.valueOf(inputNr++));
        }
        if (List.of('x','y','z','w').contains(f[2].charAt(0))) {
            return new Cmd(Enum.valueOf(Op.class, f[0]), f[1].charAt(0), f[2].charAt(0));
        } else {
            return new Cmd(Enum.valueOf(Op.class, f[0]), f[1].charAt(0), Integer.valueOf(f[2]));
        }
    }

    public static void main(String[] args) {
        new Day24a().solve();
    }

    void solve() {
        var cmds = input.map(this::parse);
        cmds = List.of(new Cmd(Op.mul, 'z', 0)).appendAll(cmds);
        var blocks = blocks(cmds);
                
//        cmds = cmds.reverse();
//        Expr z = (Expr) cmds.head().expand(cmds.tailOption());
        
        for (var b : blocks) {
            var cb = b.reverse();
            var z = cb.head().expand(cb.tailOption());
            System.out.println(z);
            System.out.println("---");
        }

//        System.out.println(z);
//        for (int i=0; i<15; ++i) {
//            var s = String.format("s[%d]", i);
//            System.out.println(s + " -> " + z.depends(s));
//        }
    }

    List<List<Cmd>> blocks(List<Cmd> cmds) {
        List<List<Cmd>> r = List.empty();
        
        while (cmds.nonEmpty()) {
        var t = cmds.tail().splitAt(c -> c.op == Op.inp);
        r = r.append(List.of(cmds.head()).appendAll(t._1));
        cmds = t._2;
        }
        return r;
                
        
    }

    static String inp0 = """
inp w
mul x 0
add x z
mod x 26
div z 1
add x 13
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 0
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 11
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 3
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 14
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 8
mul y x
add z y
            """;
}
