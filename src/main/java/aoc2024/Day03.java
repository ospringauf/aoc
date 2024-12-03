package aoc2024;

import java.util.regex.Pattern;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 3: Mull It Over ---
// https://adventofcode.com/2024/day/3

class Day03 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== test"); 
        timed(() -> new Day03().test());
        System.out.println("=== part 1"); // 162813399
        timed(() -> new Day03().part1());
        System.out.println("=== part 2"); // 53783319
        timed(() -> new Day03().part2());
    }

    List<String> data = file2lines("input03.txt");
//	List<String> data = Util.splitLines(example);

    void test() {
        var s = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
        System.out.println(scan(s, false));
        
        s = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";
        System.out.println(scan(s, true));
    }
    
    void part1() {
        var r = scan(data.mkString(), false);
        System.out.println(r);
    }

    void part2() {
        var r = scan(data.mkString(), true);
        System.out.println(r);
    }

    long scan(String s, boolean withEnable) {
        long r = 0;
        boolean enabled = true;
        Pattern p = Pattern.compile("mul\\(\\d{1,3}\\,\\d{1,3}\\).*");
        
        while (s.length() > 0) {

            if (withEnable) {
                if (s.startsWith("do()"))
                    enabled = true;
                if (s.startsWith("don\'t()"))
                    enabled = false;
            }

            var m = p.matcher(s);
            if (enabled && m.matches()) {
                var i = s.indexOf(')');
                var f = s.substring(0, i).split("[\\)\\(\\,]");
                r += Long.valueOf(f[1]) * Long.valueOf(f[2]);
                s = s.substring(i);

            } else {
                s = s.substring(1);
            }
        }
        return r;
    }

    static String example = """                    
            """;
}
