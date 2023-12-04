package aoc2023;

import common.AocPuzzle;
import common.PointMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

//--- Day 3: Gear Ratios ---
// https://adventofcode.com/2023/day/3

class Day03 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 4361 / 557705
        timed(() -> new Day03().part1());
        System.out.println("=== part 2"); // 467835 / 84266818
        timed(() -> new Day03().part2());
    }

    List<String> data = file2lines("input03.txt");
//	List<String> data = Util.splitLines(example);
    
    static class Num {
        int val; // mutable!
    }

    PointMap<Num> toNumberMap(PointMap<Character> m) {
        var t = new PointMap<Num>();

        m.boundingBox().walkThrough().forEach(p -> {
            Character c = m.get(p);
            if (Character.isDigit(c)) {
                if (t.containsKey(p.west())) {
                    // digit continues a number 
                    var num = t.get(p.west());
                    num.val = 10 * num.val + (c - '0');
                    t.put(p, num);
                } else {
                    var num = new Num();
                    num.val = (c - '0');
                    t.put(p, num);
                }
            }
        });

        return t;
    }
    
    void part1() {
        var m = new PointMap<Character>();
        m.read(data);
        
        var symbols = HashSet.ofAll(m.values()).reject(c -> Character.isDigit(c) || c =='.');
        var num = toNumberMap(m);
        var symPos = m.findPoints(symbols::contains);
        
        List<Num> numbers = symPos.flatMap(p -> p.neighbors8())
                .filter(num::containsKey)
                .map(num::get)
                .distinct();
        
        var result = numbers.map(n -> n.val).sum();
        System.out.println(result);
    }
    
    
    void part2() {
        var m = new PointMap<Character>();
        m.read(data);

        var num = toNumberMap(m);
        long result = 0;
        
        for (var gear : m.findPoints('*')) {
            var factors = gear.neighbors8()
                    .filter(num::containsKey)
                    .map(num::get)
                    .distinct();
            if (factors.length() == 2) {
                result += factors.map(n -> n.val).product().longValue();
            }
        }
        System.out.println(result);
    }

    static String example = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
""";
}
