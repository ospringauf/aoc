package aoc2022;

import java.util.function.LongFunction;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 11: Monkey in the Middle ---
// https://adventofcode.com/2022/day/11

class Day11a extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 54054
        new Day11a().solve(20, 3);
        System.out.println("=== part 2"); // 14314925001
        new Day11a().solve(10000, 1);
    }

//    String data = example;
    String data = file2string("input11.txt");
    List<Monkey> monkeys = List.of(data.split("\n\n")).map(s -> Monkey.parse(s));

    record Monkey(int id, int trueTo, int falseTo, List<Long> initialItems, LongFunction<Long> op, int divider) {

        static Monkey parse(String s) {
            var l = s.split("\n");
            var id = split(l[0], "[ :]").i(1);
            var items = Util.strings2longs(l[1].split(": ")[1].split(", "));
            var expr = l[2].split(" = ")[1];
            var arg2 = expr.split(" ")[2];
            LongFunction<Long> op = null;
            if (expr.startsWith("old * old"))
                op = x -> x * x;
            else if (expr.startsWith("old +"))
                op = x -> x + Integer.parseInt(arg2);
            else
                op = x -> x * Integer.parseInt(arg2);
            ;
            int divider = Integer.parseInt(l[3].split(" by ")[1]);
            var trueTo = Integer.parseInt(l[4].split("monkey ")[1]);
            var falseTo = Integer.parseInt(l[5].split("monkey ")[1]);
            return new Monkey(id, trueTo, falseTo, items, op, divider);
        }
    }

    void solve(int rounds, int worryDivisor) {
        var items = monkeys.toMap(m -> m.id, m -> m.initialItems);
        var inspect = monkeys.map(m -> 0);

        var p = monkeys.map(m -> m.divider).product().longValue();

        for (int round = 0; round < rounds; ++round)
            for (var m : monkeys) {
                var it = items.getOrElse(m.id, List.empty());
                items = items.put(m.id, List.empty());
                for (var i : it) {
                    inspect = inspect.update(m.id, inspect.get(m.id) + 1);
                    i = m.op.apply(i) % p;
                    i = i / worryDivisor;
                    int to = (i % m.divider == 0) ? m.trueTo : m.falseTo;
                    items = items.put(to, items.getOrElse(to, List.empty()).append(i));
                }
            }
//        System.out.println(items);
//        System.out.println(inspect);
        System.out.println(inspect.sorted().reverse().take(2).product());
    }

    static String example = """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3

            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0

            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3

            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
                        """;

}
