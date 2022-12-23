package aoc2021;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 24: Arithmetic Logic Unit ---
// https://adventofcode.com/2021/day/24

class Day24a extends AocPuzzle {

    List<String> input = file2lines("input24.txt");

    enum Op {
        inp, add, mul, div, mod, eql
    };

    static int inputNr = 0;

    static class Cmd {
        Cmd(Op op) {
            this.op = op;
        }

        Op op;
    }

    static class Inp extends Cmd {
        public Inp(int i) {
            super(Op.inp);
            nr=i;
        }

        int nr;
    }

//    static class Cmd1 extends Cmd {
//        char reg;
//        long arg;
//    }
//
//    static class Cmd2 extends Cmd {
//        char reg;
//        char arg;
//    }
//    
//    Cmd parse(String s) {
//        var f = s.split(" ");
//        if ("inp".equals(f[0])) {
//            return new Inp(inputNr++);
//        }
//    }

    public static void main(String[] args) {
        new Day24a().solve();
    }

    private void solve() {
        // TODO Auto-generated method stub

    }

}
