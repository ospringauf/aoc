package aoc2020;

import common.AocPuzzle;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 7: Handy Haversacks ---
// fixpoint iteration / Convex hull approach
// https://adventofcode.com/2020/day/7

@SuppressWarnings({ "deprecation", "preview" })
class Day07 extends AocPuzzle {

    public static void main(String[] args) throws Exception {

        System.out.println("=== part 1"); // 192
        new Day07().part1();

        System.out.println("=== part 2"); // 12128
        new Day07().part2();              
    }

    private List<Rule> rules = lines("input07.txt").map(Rule::parse);
//    private List<Rule> rules = List.of(example.split("\\n"));

 
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
            // light red| bags contain |1 bright white bag|, |2 muted yellow bags.
            var arr = s.split(" bags contain ");
            var left = arr[0];
            var right = List.of(arr[1].split(", "));
            return new Rule(left, right.map(Bags::parse).filter(bag -> bag != null));
        }

        public boolean containsColor(String c) {
            return inner.exists(i -> i.color.equals(c));
        }

        public List<Bags> multiply(int ntimes) {
            return inner.map(bag -> new Bags(bag.amount * ntimes, bag.color));
        }
    }
    

    private List<Rule> findContainingRules(Rule r) {
        return rules.filter(rule -> rule.containsColor(r.color));
    }

    private Rule findRule(String color) {
        return rules.find(rule -> rule.color.equals(color)).get();
    }

    private void part1() throws Exception {

        List<Rule> next = List.of(findRule("shiny gold"));
        Set<Rule> found = HashSet.empty();

        // convex hull: rule(s) --> containing rule(s)
        while (! next.isEmpty()) {
            next = next.flatMap(this::findContainingRules).removeAll(found);
            found = found.addAll(next);
        }
        
//        System.out.println(found.map(r -> r.outer));
        System.out.println(found.size());
    }


    private void part2() throws Exception {

        Bags shinyGold = new Bags(1, "shiny gold");
        List<Bags> todo = List.of(shinyGold);
        List<Bags> expanded = List.empty();

        // convex hull expansion: bag(s) --> inner bags * N
        while (!todo.isEmpty()) {
            expanded = expanded.appendAll(todo);
            todo = todo.flatMap(bag -> findRule(bag.color).multiply(bag.amount));
        }

        expanded = expanded.remove(shinyGold);

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
