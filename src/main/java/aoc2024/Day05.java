package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 5: Print Queue ---
// https://adventofcode.com/2024/day/5

class Day05 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 6260
        timed(() -> new Day05().part1());
        System.out.println("=== part 2"); // 5346
        timed(() -> new Day05().part2());
    }

    String data = file2string("input05.txt");
//	String data = example;

    List<Rule> rules;
    List<List<Integer>> updates;

    record Rule(Integer p1, Integer p2) {
        static Rule parse(String s) {
            var x = s.split("\\|");
            return new Rule(Integer.valueOf(x[0]), Integer.valueOf(x[1]));
        }

		boolean fulfilled(Integer n, List<Integer> printed) {
			return p2 != n || printed.contains(p1);
		}

		boolean fulfilled(List<Integer> u) {
			return u.indexOf(p1) < u.indexOf(p2);
		}

		boolean applies(List<Integer> u) {
			return  u.contains(p1) && u.contains(p2);
		}
    }

    Day05() {
        var block = data.split("\n\n");
        rules = Util.splitLines(block[0]).map(Rule::parse);
        updates = Util.splitLines(block[1]).map(s -> s.replace(',', ' ')).map(Util::string2ints);
    }

    void part1() {
        var correct = updates.filter(this::checkUpdate);
        System.out.println(middlePages(correct));
    }

    boolean checkUpdate(List<Integer> u) {
        var apply = rules.filter(r -> r.applies(u));
        return apply.forAll(r -> r.fulfilled(u));
    }

    Number middlePages(List<List<Integer>> correct) {
        return correct.map(u -> u.get(u.length() / 2)).sum();
    }

    
    
    void part2() {
        var wrong = updates.reject(this::checkUpdate);
        var correct = wrong.map(this::fixUpdate);
        System.out.println(middlePages(correct));
    }

    List<Integer> fixUpdate(List<Integer> u0) {
        List<Integer> fixed = List.empty();
        var relevant = rules.filter(r -> r.applies(u0));

        var u = u0;
        while (u.nonEmpty()) {
            var f0 = fixed;
            // pick the page where all relevant rules are fulfilled (fixed contains preceding pages) 
            var next = u.filter(n -> relevant.forAll(r -> r.fulfilled(n, f0))).single();
            fixed = fixed.append(next);
            u = u.remove(next);
        }

        return fixed;
    }

    static String example = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """;
}
