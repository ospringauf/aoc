package aoc2024;

import common.AocPuzzle;
import io.vavr.collection.*;

//--- Day 17: Chronospatial Computer ---
// https://adventofcode.com/2024/day/17

class Day17 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 4,1,5,3,1,5,3,5,7
        timed(() -> new Day17().part1());
        System.out.println("=== part 2"); // 164542125272765
        timed(() -> new Day17().part2());
    }

    record State(long a, long b, long c, Array<Integer> prg) {
    }

    State example1 = new State(729, 0, 0, Array.of(0, 1, 5, 4, 3, 0));
    State example2 = new State(2024, 0, 0, Array.of(0, 3, 5, 4, 3, 0));
    State real = new State(56256477, 0, 0, Array.of(2, 4, 1, 1, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0));
    State data = real;

    long a = 0;
    long b = 0;
    long c = 0;

    void part1() {
        var out = run1(data);

        System.out.println("output: " + out.mkString(","));
        System.out.println("registers: " + a + ", " + b + ", " + c);
    }

    void part2() {

        int minMatch = 0;
        int knownBits = 0; // trailing bits
        List<Long> trailingBits = List.of(0L);

        while (true) {
            List<Long> cand = List.empty();
            
            for (long i = 0; i <= (1 << 8); ++i) {
                for (var suffix : trailingBits) {
                    long a = (i << knownBits) | suffix;
                    var match = run2(new State(a, 0, 0, data.prg));
                    if (match > minMatch) {
//                        System.out.println(Long.toHexString(a0) + " -> " + match);
                        cand = cand.append(a);
                    }
                    if (match == data.prg.size()) {
                        System.out.println("found: " + a);
                        return;
                    }
                }
            }
            knownBits += 8;
            minMatch += 2;
            trailingBits = cand;
            
//            System.out.println(cand.map(x -> "0x" + Long.toHexString(x) + "L"));
//            System.out.println(cand.size());
        }
    }


    int run2(State initial) {
        a = initial.a;
        b = initial.b;
        c = initial.c;
        var prg = initial.prg;
        int match = 0;

        do {
            b = a % 8; // 2,4
            b = b ^ 1; // 1,1
            c = a / (1 << b); // 7,5
            b = b ^ 5; // 1,5
            a = a / (1 << 3); // 0,3
            b = b ^ c; // 4,3

            int out = (int) (b % 8);
            if (out != prg.head())
                return match;
            match++;
            prg = prg.tail();
            if (prg.isEmpty())
                return match;

        } while (a != 0); // 3,0
        return match;
    }

    long combo(int op) {
        return (op < 4) ? op : (op == 4) ? a : (op == 5) ? b : c;
    }

    Array<Integer> run1(State initial) {
        int iptr = 0;
        a = initial.a;
        b = initial.b;
        c = initial.c;
        var prg = initial.prg;
        List<Integer> out = List.empty();

        while (iptr < prg.size()) {
            var cmd = prg.get(iptr);
            var op = prg.get(iptr + 1);

            switch (cmd) {
            // adv
            case 0: {
                a = a / (1 << combo(op));
                iptr += 2;
                break;
            }
            // bxl
            case 1: {
                b = b ^ op;
                iptr += 2;
                break;
            }
            // bst
            case 2: {
                b = combo(op) % 8;
                iptr += 2;
                break;
            }
            // jnz
            case 3: {
                if (a != 0)
                    iptr = op;
                else
                    iptr += 2;
                break;
            }
            // bxc
            case 4: {
                b = b ^ c;
                iptr += 2;
                break;
            }
            // out
            case 5: {
                out = out.append((int) (combo(op) % 8));
                iptr += 2;
                break;
            }
            // bdv
            case 6: {
                b = a / (1 << combo(op));
                iptr += 2;
                break;
            }
            // cdv
            case 7: {
                c = a / (1 << combo(op));
                iptr += 2;
                break;
            }
            default:
                throw new RuntimeException("unknown cmd " + cmd);
            }
        }
        return out.toArray();
    }
}
