package aoc2020;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 7: Handy Haversacks ---
// recursive approach
// https://adventofcode.com/2020/day/7

@SuppressWarnings({ "deprecation", "preview" })
class Day07recursive extends AocPuzzle {

    public static void main(String[] args) throws Exception {

        System.out.println("=== part 1"); // 192
        new Day07recursive().part1();

        System.out.println("=== part 2"); // 12128
        new Day07recursive().part2();
    }

    private Set<Rule> rules;

    static record Bags(int amount, String color) {
        static Bags parse(String s) {
            var x = s.split(" ");
            if (x[0].equals("no"))
                // no other bags
                return null;
            else
                // 2 muted yellow bags
                return new Bags(Integer.parseInt(x[0]), x[1] + " " + x[2]);
        }

        boolean isColor(String c) {
            return c.equals(color);
        }
    }

    static record Rule(String color, List<Bags> inner) {

        static Rule parse(String s) {
            var a = s.split(" bags contain ");
            var inner = a[1].split(", ");
            return new Rule(a[0], List.of(inner).map(Bags::parse).filter(b -> b != null));
        }

        public boolean containsColor(String c) {
            return inner.exists(i -> i.color.equals(c));
        }

        public List<Bags> multiply(int ntimes) {
            return inner.map(bag -> new Bags(bag.amount * ntimes, bag.color));
        }
    }

    Day07recursive() throws Exception {
        // var input = List.of(example.split("\\n"));
        var input = lines("input07.txt");

        rules = input.map(Rule::parse).toSet();
    }

    private Set<Rule> findContainingRules(Rule r) {
        return rules.filter(rule -> rule.containsColor(r.color));
    }

    private Rule findRule(String color) {
        return rules.find(rule -> rule.color.equals(color)).get();
    }

    private Set<Rule> expandOuter(Rule r) {
        return findContainingRules(r)
                .flatMap(x -> HashSet.of(x).addAll(expandOuter(x)));
    }

    private List<Bags> expandInner(Bags b) {
        return findRule(b.color)
                .multiply(b.amount)
                .flatMap(x -> List.of(x).appendAll(expandInner(x)));
    }

    private void part1() throws Exception {

        Rule shinyGold = findRule("shiny gold");
        var found = expandOuter(shinyGold);

        System.out.println(found.map(r -> r.color));
        System.out.println(found.size());
    }

    private void part2() throws Exception {

        Bags shinyGold = new Bags(1, "shiny gold");
        var expanded = expandInner(shinyGold);

        System.out.println(expanded);
        System.out.println(expanded.map(b -> b.amount).sum());
    }

    static String example = """
            light red bags contain 1 bright white bag, 2 muted yellow bags.
            dark orange bags contain 3 bright white bags, 4 muted yellow bags.
            bright white bags contain 1 shiny gold bag.
            muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
            shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
            dark olive bags contain 3 faded blue bags, 4 dotted black bags.
            vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
            faded blue bags contain no other bags.
            dotted black bags contain no other bags.
                        """;
}
