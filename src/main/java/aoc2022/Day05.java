package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 5: Supply Stacks ---
// https://adventofcode.com/2022/day/5

class Day05 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // VWLCWGSDQ
        timed(() -> new Day05().solve(Crane.CrateMover9000));
        
        System.out.println("=== part 2"); // TCGLQSLPW
        timed(() -> new Day05().solve(Crane.CrateMover9001));
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

    
    List<Character>[] parseStacks(String input) {
        var config = Util.splitLines(input);
        
        // look at the last line containing the stack numbers
        // find max stack number
        String stackNumbers = config.last();
        var numStacks = Util.string2ints(stackNumbers.trim()).max().get();
        System.out.println("stacks: " + numStacks);
        
        // find stack column indexes in input (1, 5, 9, 13 ...)
        var pos = List.rangeClosed(0, numStacks).map(i -> stackNumbers.indexOf(i.toString()));

        // build initial stacks (top = head), reading the corresponding columns
        var stacks = new List[numStacks+1];
        var crates = config.removeLast(x -> true);
        List.rangeClosed(1, numStacks)
        .forEach(s -> stacks[s] = crates.map(line -> line.charAt(pos.get(s))).removeAll(' '));
        
        return stacks;
    }
    
    void solve(Crane crane) {
        var inputBlocks = data.split("\n\n");
        
        var stacks = parseStacks(inputBlocks[0]);
        
        var moves = Util.splitLines(inputBlocks[1]).map(Move::parse);        
        
        for (var move : moves) {
            var crates = stacks[move.from].take(move.num);
            if (crane == Crane.CrateMover9001)
                crates = crates.reverse();
            stacks[move.from] = stacks[move.from].drop(move.num);
            stacks[move.to] = stacks[move.to].pushAll(crates); 
        }
        
        var tops = List.range(1, stacks.length).map(x -> stacks[x].head());
        System.out.println(tops.mkString());
    }
}
