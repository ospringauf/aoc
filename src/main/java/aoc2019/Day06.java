package aoc2019;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Day 6: Universal Orbit Map
 * https://adventofcode.com/2019/day/6
 * 
 * b2cc: streams + recursion (part1) 
 */
public class Day06 {

    static class Orbit {
        String inner;
        String outer;

        public Orbit(String a, String b) {
            this.inner = a;
            this.outer = b;
        }

        public Orbit(String input) {
            this(input.split("\\)")[0], input.split("\\)")[1]);
        }
    }
    

    String[] test1 = { "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L" };
    String[] test2 = { "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN" };

    private List<Orbit> map;

    public static void main(String[] args) throws Exception {
        System.out.println("=== part1 ==="); // 145250
        new Day06().part1();
        System.out.println("=== part2 ==="); // 274
        new Day06().part2();
    }

    int countOrbits(String x, int innerOrbits) {
        return map
                .stream()
                .filter(orb -> orb.inner.equals(x)) // orbiting x directly
                .mapToInt(orb -> countOrbits(orb.outer, innerOrbits + 1)) // recursion: orbiting x indirectly
                .map(outerOrbits -> innerOrbits + 1 + outerOrbits)
                .sum();
    }

    void shortestPaths(Map<String, Integer> dist) {
        boolean better = true;
        while (better) {
            better = false;

            for (Orbit orbit : map) {
                Integer dinner = dist.get(orbit.inner);
                Integer douter = dist.get(orbit.outer);
                                      
                if (douter > 1 + dinner) {
                    better = true;
                    dist.put(orbit.outer, 1 + dinner);
                }
                if (dinner > 1 + douter) {
                    better = true;
                    dist.put(orbit.inner, 1 + douter);
                }
            }
        }
    }

    void part1() throws Exception {
//      map = Arrays.stream(test).map(s -> new Orbit(s)).collect(Collectors.toList());
        map = Util.stringStreamOf("input06.txt").map(Orbit::new).collect(Collectors.toList());

        int result = countOrbits("COM", 0);
        System.out.println(result);

    }

    void part2() throws Exception {
        // map = Arrays.stream(test2).map(s -> new Orbit(s)).collect(Collectors.toList());
        map = Util.stringStreamOf("input06.txt").map(Orbit::new).collect(Collectors.toList());

        var objects = map.stream().flatMap(x -> Stream.of(x.inner, x.outer)).collect(Collectors.toSet());
        
        // init distance map with "infinite" except dist(YOU)=0
        var dist = objects.stream().collect(Collectors.toMap(x -> x, x -> Integer.MAX_VALUE-1));
        dist.put("YOU", 0);
        shortestPaths(dist);

        System.out.println(dist.get("SAN") - 2);
    }
}
