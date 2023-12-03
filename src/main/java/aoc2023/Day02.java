package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 2: Cube Conundrum ---
// https://adventofcode.com/2023/day/2
// TODO clean up

class Day02 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 2169
        timed(() -> new Day02().part1());
        System.out.println("=== part 2"); // 60948
        timed(() -> new Day02().part2());
    }

    List<String> data = file2lines("input02.txt");
//	List<String> data = Util.splitLines(example);

    record Cubeset(Integer red, Integer green, Integer blue) {

        // 5 green, 1 red
        public static Cubeset parse(String s) {
            Integer blue = null;
            Integer red = null;
            Integer green = null;
            for (var g : s.split(", ")) {
                var n = split(g, " ").i(0);
                var c = split(g, " ").s(1);
                switch (c) {
                case "red" -> red = n;
                case "blue" -> blue = n;
                case "green" -> green = n;
                }
            }
            return new Cubeset(red, green, blue);
        }

        public boolean match(int r, int g, int b) {
            return (red == null || red <= r) && (blue == null || blue <= b) && (green == null || green <= g);
        }

        int power() {
            return red * green * blue;
        }
    }

    record Game(int id, List<Cubeset> sets) {
        static Game parse(String s) {
            var s1 = s.split(": ")[0];
            var id = split(s1, " ").i(1);
            var s2 = s.split(": ")[1].split("; ");
            var sets = List.of(s2).map(x -> Cubeset.parse(x));
            return new Game(id, sets);
        }

        Cubeset minGame() {
            var r = sets.map(s -> s.red).map(n -> (n == null) ? 0 : n).max().get();
            var g = sets.map(s -> s.green).map(n -> (n == null) ? 0 : n).max().get();
            var b = sets.map(s -> s.blue).map(n -> (n == null) ? 0 : n).max().get();
            return new Cubeset(r, g, b);
        }

        boolean match(int r, int g, int b) {
            return sets.forAll(s -> s.match(r, g, b));
        }
    }

    void part1() {
        var games = data.map(Game::parse);
        var m = games.filter(g -> g.match(12, 13, 14)).map(g -> g.id).sum();
        System.out.println(m);
    }

    void part2() {
        var games = data.map(Game::parse);
        var r = games.map(g -> g.minGame().power()).sum();
        System.out.println(r);
    }

    static String example = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """;
}
