package aoc2025;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 6: Trash Compactor ---
// https://adventofcode.com/2025/day/6

class Day06 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day06().part1()); // 4580995422905
        System.out.println("=== part 2"); // 10875057285868
        timed(() -> new Day06().part2());
    }

    List<String> data = file2lines("input06.txt");
//    List<String> data = Util.splitLines(example);

    void part1() {
        data = data.reverse();
        var ops = Util.splitFields(data.head().replaceAll("#", " "));

        System.out.println(ops);

        var arg0 = data.tail().map(s -> Util.string2ints(s));
        var arg = List.range(0, ops.length()).map(i -> arg0.map(a -> a.get(i)));

        System.out.println(arg.head());
        long r = 0;
        for (int i = 0; i < ops.length(); ++i) {
            long v = 0;
            if (ops.get(i).equals("+"))
                v = arg.get(i).sum().longValue();
            else
                v = arg.get(i).product().longValue();
//            System.out.println(ops.get(i) + " / " + arg.get(i) + " -> " + v);
            r += v;
        }
        System.out.println(r);
    }

    List<Integer> extract(List<String> l) {
        var c = l.head().length();
        return List.range(0, c).map(i -> l.map(x -> x.charAt(i)).mkString().strip()).map(Integer::valueOf);
    }
    
    void part2() {
        data = data.reverse();
        
        var s = data.head();
        System.out.println(s);
        var pos = List.range(0, s.length()).filter(i -> s.charAt(i)!=' ');
        System.out.println(pos);
        var len = pos.sliding(2).map(l -> l.get(1)-l.get(0)-1).toList();
        System.out.println(len);
     
        var work = data.tail().reverse();
        var cols = pos.length()-1;
        var w1 = List.range(0, cols).map(i -> work.map(x -> (x+"    ").substring(pos.get(i), pos.get(i)+len.get(i))));
        System.out.println(w1);
        var w2 = w1.map(x -> extract(x));
        System.out.println(w2);
        
        var ops = Util.splitFields(s);
        Long r = 0L;
        for (int i = 0; i < cols; ++i) {
            long v = 0;
            if (ops.get(i).equals("+"))
                v = w2.get(i).sum().longValue();
            else
                v = w2.get(i).product().longValue();
            r += v;
        }
        System.out.println(r);

    }

    static String example = """
123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +   #""";
}
