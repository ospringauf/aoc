package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;
import io.vavr.collection.Map;

//--- Day 8: Haunted Wasteland ---
// https://adventofcode.com/2023/day/8

class Day08 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 16409
        timed(() -> new Day08().part1());
        System.out.println("=== part 2"); // 11795205644011
        timed(() -> new Day08().part2());
    }

    List<String> data = file2lines("input08.txt");
//	List<String> data = Util.splitLines(example2);

    List<Character> instructions;
    Map<String, Node> nodes;

	
	record Node(String name, String left, String right) {
	    static Node parse(String s) {
	        var f = s.split("[ =,()]+");
	        return new Node(f[0], f[1], f[2]);
	    }
	}
	
	Day08() {
        instructions = List.ofAll(data.head().toCharArray());
        nodes = data.tail().tail().map(Node::parse).toMap(x -> x.name, x -> x);
	}
	
    int pathToZ(String pos, List<Character> instr) {
        int steps = 0;
        while (! pos.endsWith("Z")) {
            var c = instr.head();
            instr = instr.rotateLeft(1);
            var node = nodes.getOrElse(pos, null);
            pos = (c == 'L') ? node.left : node.right;
            steps++;
        }
        return steps;
    }

    void part1() {        
        // not quite correct, we check only for ??Z instead of ZZZ,
        // but it works due to the special structure of the input data
        var d = pathToZ("AAA", instructions);
        System.out.println(d);
    }
    
    void part2() {
        var anodes = nodes.keySet().filter(x -> x.endsWith("A"));
        var dist = anodes.map(p -> pathToZ(p, List.ofAll(instructions)));
        
//        // all the path lengths are multiples of the instruction length - really!?! what a nice coincidence.
//        var il = instructions.size() * 1.0;
//        System.out.println(dist.map(x -> x/il));
        
        System.out.println(dist.toJavaList());
        
        // clumsy conversion to int[] as input for the LCM function
        var v = dist.toJavaList().stream().mapToInt(x->x).toArray();
        
        long lcm = Util.lcm(v);
        System.out.println(lcm);
    }

    static String example1 = """
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)            
            """;

    static String example2 = """
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
""";
    
}
