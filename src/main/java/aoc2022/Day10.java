package aoc2022;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;

// --- Day 10: Cathode-Ray Tube ---
// https://adventofcode.com/2022/day/10

class Day10 extends AocPuzzle {

    public static void main(String[] args) {
        timed(() -> new Day10().solve());
    }

//	List<String> data = file2lines("input10_example.txt");
    List<String> data = file2lines("input10.txt");

    List<Integer> generateSignal() {
        int x = 1;
        List<Integer> signal = List.of(x);

        for (var instruction : data) {
            if ("noop".equals(instruction)) {
                signal = signal.append(x);
            } else {
                signal = signal.append(x);
                signal = signal.append(x);
                x += split(instruction, " ").i(1);
            }
        }
        return signal;
    }

    void solve() {
        List<Integer> signal = generateSignal();
//		System.out.println(signal);

        System.out.println("=== part 1"); // 12840
        
        var strengths = List.of(20, 60, 100, 140, 180, 220).map(t -> t * signal.get(t));
        System.out.println(strengths.sum());

        
        System.out.println("=== part 2");

        var crt = new PointMap<Character>();
        for (int i = 0; i < 6 * 40; ++i) {
            var x = signal.get(i + 1);
            var sprite = List.of(x - 1, x, x + 1);
            if (sprite.contains(i % 40))
                crt.put(Point.of(i % 40, i / 40), '#');
        }
        crt.print();
    }
}
