package aoc2022;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 19: Not Enough Minerals ---
// https://adventofcode.com/2022/day/19
// TODO part 2 is ugly & slow

class Day19 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1009
        timed(() -> new Day19().part1());
        System.out.println("=== part 2"); // 18816
        timed(() -> new Day19().part2());
    }

//    String data = example.replace(":\n", ": ").replace(".\n", ".");
    String data = file2string("input19.txt");

    enum Typ {
        ore, clay, obsidian, geode;

        static Typ parse(String s) {
            return Enum.valueOf(Typ.class, s);
        }
    }

    record Amounts(int ore, int clay, int obsidian, int geode) {

        static Amounts of(Map<Typ, Integer> c) {
            return new Amounts(c.getOrElse(Typ.ore, 0), c.getOrElse(Typ.clay, 0), c.getOrElse(Typ.obsidian, 0),
                    c.getOrElse(Typ.geode, 0));
        }

        boolean geq(Amounts r) {
            return ore >= r.ore && clay >= r.clay && obsidian >= r.obsidian && geode >= r.geode;
        }

        Amounts minus(Amounts r) {
            return new Amounts(ore - r.ore, clay - r.clay, obsidian - r.obsidian, geode - r.geode);
        }

        Amounts plus(Amounts r) {
            return new Amounts(ore + r.ore, clay + r.clay, obsidian + r.obsidian, geode + r.geode);
        }

        int amount(Typ t) {
            return switch (t) {
            case ore -> this.ore;
            case clay -> this.clay;
            case obsidian -> this.obsidian;
            case geode -> this.geode;
            };
        }
    }

    record Rule(Typ robotTyp, Amounts cost) {
        static Rule parse(String s) {
            var f = s.split(" costs ");
            var rob = Typ.parse(f[0].trim().split(" ")[1]);
            var f1 = f[1].split(" and ");
            Map<Typ, Integer> c = HashMap.empty();
            for (var fc : f1) {
                var t = split(fc.replace(".", ""), " ").to(x -> Tuple.of(Typ.parse(x.s(1)), x.i(0)));
                c = c.put(t);
            }
            return new Rule(rob, Amounts.of(c));
        }

        boolean applicable(Amounts stock) {
            return stock.geq(cost);
        }

        boolean produceable(Amounts robots) {
            return (cost.ore == 0 || robots.ore > 0) && (cost.clay == 0 || robots.clay > 0)
                    && (cost.obsidian == 0 || robots.obsidian > 0);
        }

        int timeToProd(Amounts robots, Amounts stock) {
            int i = 0;
            while (!stock.geq(cost)) {
                stock = stock.plus(robots);
                i++;
            }
            return i;
        }

        Amounts addRobot(Amounts r) {
            return switch (robotTyp) {
            case ore -> r.plus(new Amounts(1, 0, 0, 0));
            case clay -> r.plus(new Amounts(0, 1, 0, 0));
            case obsidian -> r.plus(new Amounts(0, 0, 1, 0));
            case geode -> r.plus(new Amounts(0, 0, 0, 1));
            default -> r;
            };
        }
    }

    record Blueprint(int id, List<Rule> rules) {
        static Blueprint parse(String s) {
            var l = s.split("[:.]");
            int id = split(l[0], " ").i(1);
            var r = List.of(l).tail().map(Rule::parse);
            return new Blueprint(id, r);
        }

        Amounts maxRobots() {
            var rore = rules.map(r -> r.cost.ore).max().get();
            var rclay = rules.map(r -> r.cost.clay).max().get();
            var robs = rules.map(r -> r.cost.obsidian).max().get();
            return new Amounts(rore, rclay, robs, 1000);
        }
    }

    record State(int time, Amounts robots, Amounts stock) {
        State skip() {
            // build nothing
            return new State(time - 1, robots, stock.plus(robots));
        }

        State skip(int t) {
            // build nothing for t minutes
            if (t <= 0)
                return this;
            else
                return skip().skip(t - 1);
        }

        State build(Rule rule) {
            return new State(time - 1, rule.addRobot(robots), stock.plus(robots).minus(rule.cost));
        }
    }

    java.util.Map<State, Integer> cache = new java.util.HashMap<>();

    int solve1(Blueprint b, Amounts rmax, State s) {
        if (s.time <= 0)
            return s.stock.geode;

        if (cache.containsKey(s)) {
            return cache.get(s);
        }

        // what could we build? 
        var rules = b.rules.filter(r -> r.produceable(s.robots));
        rules = rules.filter(r -> s.robots.amount(r.robotTyp) < rmax.amount(r.robotTyp));
        
        List<State> next = List.empty();
        for (var r : rules) {
            // when can we build it?
            var t = r.timeToProd(s.robots, s.stock);
            if (t < s.time)
                next = next.append(s.skip(t).build(r));
        }
        if (next.isEmpty())
            next = next.append(s.skip());

        var res = next.map(n -> solve1(b, rmax, n)).max().get();
        cache.put(s, res);
        return res;
    }


    void part1() {
        var blueprints = Util.splitLines(data).map(Blueprint::parse);

        int q = 0;
        for (var b : blueprints) {
            cache.clear();
            System.out.println("blueprint " + b.id);
            var s0 = new State(24, new Amounts(1, 0, 0, 0), new Amounts(0, 0, 0, 0));

//            var solution = "--c-c-c---oc--o--g--g----";
//            var w = List.ofAll(solution.toCharArray())
//                    .map(c -> (c == 'c') ? Typ.clay : (c == 'o') ? Typ.obsidian : (c == 'g') ? Typ.geode : null);
//            System.out.println(w);

            var geodes = solve1(b, b.maxRobots(), s0);
            System.out.println(geodes);
            q += b.id * geodes;
        }
        System.out.println(q);
    }

    int solve2(Blueprint b, Amounts rmax, State s) {
        if (s.time <= 0)
            return s.stock.geode;

        if (cache.containsKey(s)) {
            return cache.get(s);
        }

        var rules = b.rules.filter(r -> r.produceable(s.robots));
        rules = rules.filter(r -> s.robots.amount(r.robotTyp) < rmax.amount(r.robotTyp));

        // TODO this is ugly but true
        if (s.time <= 5)
            rules = rules.filter(r -> r.robotTyp == Typ.geode);
        List<State> next = List.empty();
        for (var r : rules) {
            var t = r.timeToProd(s.robots, s.stock);
            if (t < s.time)
                next = next.append(s.skip(t).build(r));
        }
        if (next.isEmpty())
            next = next.append(s.skip());

        var res = next.map(n -> solve2(b, rmax, n)).max().get();
        cache.put(s, res);
        return res;
    }

    void part2() {
        var blueprints = Util.splitLines(data).map(Blueprint::parse);

        int q = 1;
        for (var b : blueprints.take(3)) {
            cache.clear();
            System.out.println("blueprint " + b.id);
            var s0 = new State(32, new Amounts(1, 0, 0, 0), new Amounts(0, 0, 0, 0));

            var geodes = solve2(b, b.maxRobots(), s0);
            System.out.println(geodes);
            q *= geodes;
        }
        System.out.println(q);
    }

    static String example = """
            Blueprint 1:
              Each ore robot costs 4 ore.
              Each clay robot costs 2 ore.
              Each obsidian robot costs 3 ore and 14 clay.
              Each geode robot costs 2 ore and 7 obsidian.

            Blueprint 2:
              Each ore robot costs 2 ore.
              Each clay robot costs 3 ore.
              Each obsidian robot costs 3 ore and 8 clay.
              Each geode robot costs 3 ore and 12 obsidian.
                        """;

}
