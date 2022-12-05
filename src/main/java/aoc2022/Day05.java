package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 5: Supply Stacks ---
// https://adventofcode.com/2022/day/5

class Day05 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // VWLCWGSDQ
        new Day05().solve(Crane.CrateMover9000);
        
        System.out.println("=== part 2"); // TCGLQSLPW
        new Day05().solve(Crane.CrateMover9001);
    }

    static enum Crane { CrateMover9000, CrateMover9001 }; 

    record Move(int num, int from, int to) {
        static Move parse(String s) {
            var sr = split(s, " ");
            return new Move(sr.i(1), sr.i(3), sr.i(5));
        }
    }
    
//    String data = file2string("input05_example.txt");
    String data = file2string("input05.txt");

    void solve(Crane crane) {
        var inputParts = data.split("\n\n");
        var config = Util.splitLines(inputParts[0]).reverse();
        
        // find max stack number
        String stackIndex = config.head();
        var numStacks = Util.string2ints(stackIndex.trim()).max().get();
        System.out.println("stacks: " + numStacks);
        
        // find stack index positions in input (1+4*s)
        var pos = List.rangeClosed(0, numStacks).map(i -> stackIndex.indexOf(i.toString()));

        // build initial stacks (top = head)
        var stacks = new List[numStacks+1];        
        List.rangeClosed(1, numStacks)
        .forEach(s -> stacks[s] = config.drop(1).map(x -> x.charAt(pos.get(s))).filter(c -> c != ' ').reverse());
        
        
        var moves = Util.splitLines(inputParts[1]).map(Move::parse);        
        
        for (var move : moves) {
            var crates = stacks[move.from].take(move.num);
            if (crane == Crane.CrateMover9001)
                crates = crates.reverse();
            stacks[move.from] = stacks[move.from].drop(move.num);
            stacks[move.to] = stacks[move.to].pushAll(crates); 
        }
        
        var tops = List.rangeClosed(1, numStacks).map(x -> stacks[x].headOption().getOrElse(' '));
        System.out.println(tops.mkString());
    }
}
