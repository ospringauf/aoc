package aoc2022;

import java.util.Stack;

import org.scijava.parsington.ExpressionParser;
import org.scijava.parsington.Group;
import org.scijava.parsington.SyntaxTree;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 13: Distress Signal ---
// https://adventofcode.com/2022/day/13

class Day13 extends AocPuzzle {

    static final ExpressionParser PARSER = new ExpressionParser(java.util.List.of(new Group("[", "]", 10)));

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 5882
        new Day13().part1();
        System.out.println("=== part 2"); // 24948
        new Day13().part2();
    }

//    String data = example;
    String data = file2string("input13.txt");

    void part1() {
        int sum = 0;
        int idx = 1;

        for (var block : data.split("\n\n")) {
            String[] pair = block.split("\n");
            var l = pair[0];
            var r = pair[1];

            int cmp = comparePacket(parse(l), parse(r));
            if (cmp < 0) {
                sum += idx;
            }
            idx++;
        }
        System.out.println(sum);
    }

    Object toPacket(SyntaxTree tree) {
        var token = tree.token();
        if (token instanceof Integer n) {
            return n;
        } else if (token instanceof Group) {
            return List.range(0, tree.count()).map(i -> toPacket(tree.child(i)));
        }
        return null;
    }

    Object parse(String s) {
        return toPacket(PARSER.parseTree(s));
//        return myParse(s);
    }

    /**
     * just for exercise: own implementation of a stack parser
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    Object myParse(String s) {
        var tokens = Util.splitWithDelimiters(s, "[,]");

        var stack = new Stack<List>();
        stack.push(List.empty());

        for (var t : tokens) {
            switch (t) {
            case "[" -> {
                stack.push(List.empty());
            }
            case "]" -> {
                var list = stack.pop();
                stack.push(stack.pop().append(list));
            }
            case "," -> {
            }
            default -> {
                stack.push(stack.pop().append(Integer.valueOf(t)));
            }
            }
        }

        return stack.pop().single();
    }

    /**
     * convenience: string representation of a packet
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    String toString(Object packet) {
        if (packet instanceof List l)
            return l.map(this::toString).mkString("[", ",", "]");
        else
            return packet.toString();
    }

    @SuppressWarnings("rawtypes")
    int comparePacket(Object l, Object r) {
        if ((l instanceof Integer il) && (r instanceof Integer ir))
            return Integer.compare(il, ir);

        if ((l instanceof List ll) && (r instanceof List lr)) {
            if (ll.isEmpty() && lr.isEmpty())
                return 0;
            if (ll.isEmpty())
                return -1;
            if (lr.isEmpty())
                return 1;
            var ch = comparePacket(ll.head(), lr.head());
            if (ch == 0)
                return comparePacket(ll.tail(), lr.tail());
            else
                return ch;
        }

        if ((l instanceof Integer il) && (r instanceof List lr)) {
            return comparePacket(List.of(il), lr);
        }

        if ((l instanceof List ll) && (r instanceof Integer ir)) {
            return comparePacket(ll, List.of(ir));
        }

        return 0;
    }

    void part2() {
        var divider1 = parse("[[2]]");
        var divider2 = parse("[[6]]");

        var packets = Util.splitLines(data)
                .filter(s -> !s.isEmpty())
                .map(this::parse)
                .appendAll(List.of(divider1, divider2));

        packets = packets.sorted(this::comparePacket);

        var decoderKey = (packets.indexOf(divider1) + 1) * (packets.indexOf(divider2) + 1);
        System.out.println(decoderKey);
    }

    static String example = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
                        """;

}
