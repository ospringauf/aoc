package aoc2017;

import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) throws Exception {
        part2();
    }
    
    private static void part1() throws Exception {
        int result = Util.lines("aoc2017/day2.txt").stream().mapToInt(l -> diff(l)).sum();
        System.out.println(result);
    }

    private static void part2() throws Exception {
        System.out.println("--- test");
        Util.lines("aoc2017/day2b-test.txt").stream().mapToInt(l -> divi(l)).forEach(System.out::println);
        
        System.out.println("--- real");
        int result = Util.lines("aoc2017/day2.txt").stream().mapToInt(l -> divi(l)).sum();
        System.out.println(result);
    }
    
    private static int diff(String s) {
        List<String> f = Util.splitLine(s);
        return f.stream().map(Integer::parseInt).max(Integer::compareTo).get()
                - f.stream().map(Integer::parseInt).min(Integer::compareTo).get();

    }

    private static int divi(String s) {
        List<Integer> f = Util.splitLine(s).stream().map(Integer::parseInt).collect(Collectors.toList());
        for (int n1 : f) {
            for (int n2 : f) {
                if (n1>n2 && n1 % n2 == 0)
                    return n1 / n2;
            }
        }
        return 0;
    }
}
