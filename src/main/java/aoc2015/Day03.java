package aoc2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Day03 {

    static class House {

        private int x;
        private int y;

        public House(int x, int y) {
            this.x = x;
            this.y = y;
        }

        House next(String c) {
            switch (c) {
            case "^":
                return new House(x, y + 1);
            case "v":
                return new House(x, y - 1);
            case "<":
                return new House(x - 1, y);
            case ">":
                return new House(x + 1, y);
            }
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            House h = (House) obj;
            return h.x == x && h.y == y;
        }

        @Override
        public int hashCode() {
            return x + y;
        }
    }

    public static void main(String[] args) throws Exception {
        String walk = Files.readAllLines(Paths.get("src/main/java/aoc2015/day03.txt")).get(0);
        // String walk = "^v^v^v^v^v";

        System.out.println("=== part 1");
        part1(walk);
        
        System.out.println("=== part 2");
        part2(walk);
    }

    private static void part1(String walk) {
        Set<House> visited = new HashSet<>();
        House santa = new House(0, 0);
        visited.add(santa);

        for (String s : walk.split("")) {
            santa = santa.next(s);
            visited.add(santa);
        }

        System.out.println(visited.size());
    }

    private static void part2(String walk) {
        Set<House> visited = new HashSet<>();
        House santa = new House(0, 0);
        House robo = new House(0, 0);
        visited.add(santa);

        int i = 0;
        for (String s : walk.split("")) {
            if (i % 2 == 0) {
                santa = santa.next(s);
                visited.add(santa);
            } else {
                robo = robo.next(s);
                visited.add(robo);

            }
            i++;
        }

        System.out.println(visited.size());
    }
}
