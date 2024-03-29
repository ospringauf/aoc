package aoc2022;

import java.util.function.LongFunction;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 11: Monkey in the Middle ---
// https://adventofcode.com/2022/day/11

class Day11 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 54054
        timed(() -> new Day11().solve(20, 3));
        System.out.println("=== part 2"); // 14314925001
        timed(() -> new Day11().solve(10000, 1));
    }

//    String data = example;
    String data = file2string("input11.txt");
    List<Monkey> monkeys = List.of(data.split("\n\n")).map(s -> Monkey.parse(s));

    
    record Monkey(int id, List<Long> initialItems, LongFunction<Long> op, int divider, int trueTo, int falseTo) {
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
                op = x -> x + Integer.valueOf(arg2);
            else
                op = x -> x * Integer.valueOf(arg2);
            ;
            int divider = Integer.valueOf(l[3].split(" by ")[1]);
            var trueTo = Integer.valueOf(l[4].split("monkey ")[1]);
            var falseTo = Integer.valueOf(l[5].split("monkey ")[1]);
            return new Monkey(id, items, op, divider, trueTo, falseTo);
        }
    }

    void solve(int rounds, int worryDivisor) {
        var items = monkeys.toMap(m -> m.id, m -> m.initialItems).toJavaMap();
        var inspections = monkeys.map(m -> 0);

        // TODO: could calc real LCM, but since all dividers are primes ... never mind
        var lcm = monkeys.map(m -> m.divider).product().longValue();

        for (int round = 0; round < rounds; ++round)
            for (var m : monkeys) {
                var monkeyItems = items.put(m.id, List.empty());
                for (var wlevel : monkeyItems) {
                    inspections = inspections.update(m.id, inspections.get(m.id) + 1);
                    wlevel = m.op.apply(wlevel) % lcm; // LCM doesn't hurt for part 1 
                    wlevel = wlevel / worryDivisor;
                    int to = (wlevel % m.divider == 0) ? m.trueTo : m.falseTo;
                    items.put(to, items.getOrDefault(to, List.empty()).append(wlevel));
                }
            }
//        System.out.println(items);
//        System.out.println(inspections);
        Number monkeyBusiness = inspections.sorted().reverse().take(2).product();
        System.out.println(monkeyBusiness);
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
