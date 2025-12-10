package aoc2025;

import java.util.Arrays;
import java.util.HashMap;

import aoc2025.MinSumIntegerSolution.Result;
import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.List;

//--- Day 10:  ---
// https://adventofcode.com/2025/day/10

class Day10Wrong extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day10Wrong().part1()); // 385
        System.out.println("=== part 2");
        timed(() -> new Day10Wrong().part2());
        // 17138 too high
    }

	List<String> data = file2lines("input10.txt");
//    List<String> data = Util.splitLines(example);

    record Machine(int lights, List<Integer> buttons, Array<Integer> joltage, int size) {
        static Machine parse(String s) {
            var s1 = s.substring(1, s.indexOf(']'));
            int lights = 0;
            var chars = List.ofAll(s1.toCharArray()).reverse();
            while (chars.nonEmpty()) {
                lights *= 2;
                if (chars.head() == '#')
                    lights = lights + 1;
                chars = chars.tail();
            }

            var s2 = s.substring(s.indexOf(']') + 2, s.indexOf('{') - 1);
            List<Integer> buttons = List.empty();
            s2 = s2.replace("(", "").replace(")", "");
            for (var b : List.of(s2.split(" "))) {
                var bi = Util.string2ints(b.replace(',', ' '));
                int x = 0;
                for (var bii : bi)
                    x |= (1 << bii);
                buttons = buttons.append(x);
            }

            var s3 = s.substring(s.indexOf('{') + 1);
            var sj = s3.replace("}", "").replace(',', ' ');
            var joltage = Util.string2ints(sj).toArray();

            return new Machine(lights, buttons, joltage, joltage.size());
        }

        int minLights() {
            var dist = new HashMap<Integer, Integer>();
            dist.put(0, 0);
            var todo = List.of(0);
            while (todo.nonEmpty()) {
                var state = todo.head();
                for (var b : buttons) {
                    var next = state ^ b;
                    var d0 = dist.getOrDefault(next, Integer.MAX_VALUE);
                    var d1 = dist.get(state) + 1;
                    if (d1 < d0) {
                        dist.put(next, d1);
                        todo = todo.append(next);
                    }
                }
                todo = todo.tail();
            }

            return dist.get(lights);
        }

        Array<Integer> incJoltages(Array<Integer> j, int button) {
            for (int i = 0; i < size; ++i) {
                if ((button & (1 << i)) != 0) {
                    j = j.update(i, j.get(i) + 1);
                }
            }
            return j;
        }

//        int[][] buttonMatrix() {
//            var a = new int[buttons.size()][size];
//            for (var b = 0; b < buttons.size(); ++b) {
//                for (var i = 0; i < size; ++i) {
//                    a[b][i] = (buttons.get(b) & (1 << i)) > 0 ? 1 : 0;
//                }
//            }
//            return a;
//        }
        
        int[][] buttonMatrix() {
            var a = new int[size][buttons.size()];
            for (var b = 0; b < buttons.size(); ++b) {
                for (var i = 0; i < size; ++i) {
                    a[i][b] = (buttons.get(b) & (1 << i)) > 0 ? 1 : 0;
                }
            }
            return a;
        }

        boolean exceeds(Array<Integer> j) {
            return List.range(0, joltage.size()).exists(i -> j.get(i) > joltage.get(i));
        }

        Array<Integer> buttonJoltages(int button) {
            var j = joltage.map(x -> 0).toArray();
            for (int i = 0; i < size; ++i) {
                if ((button & (1 << i)) != 0) {
                    j = j.update(i, 1);
                }
            }
            return j;
        }

        int minJoltage() {
            var j0 = joltage.map(x -> 0).toArray();
            var dist = new HashMap<Array<Integer>, Integer>();
            dist.put(j0, 0);
            var todo = List.of(j0);

            while (todo.nonEmpty()) {
                var state = todo.head();
                for (var b : buttons) {
                    var next = incJoltages(state, b);
                    if (!exceeds(next)) {
                        var d0 = dist.getOrDefault(next, Integer.MAX_VALUE);
                        var d1 = dist.get(state) + 1;
                        if (d1 < d0) {
                            dist.put(next, d1);
                            if (!todo.tail().contains(next))
                                todo = todo.append(next);
                        }

                    }
                }
                if (dist.size() % 1000 == 0)
                    System.out.println(dist.size());
                todo = todo.tail();
            }

            return dist.get(joltage);
        }
    }

    void part1() {
        var machines = data.map(Machine::parse);
        var r = 0;
        for (var m : machines) {
            var p = m.minLights();
            System.out.println(m + " -> " + p);
            r += p;
        }
        System.err.println(r);
    }

    void part2() {
        var machines = data.map(Machine::parse);
//        machines = machines.take(1);
        var s = new MinSumIntegerSolution();
        var total = 0;
        for (var m : machines) {
            var A = m.buttonMatrix();
            var b = m.joltage.toJavaStream().mapToInt(Integer::intValue).toArray();
            var r = s.solve(A, b);
            if (r.feasible) {
                System.out.println("x = " + Arrays.toString(r.x));
                System.out.println("sum(x) = " + r.sum);
                total += r.sum;
            } else {
                System.out.println("No feasible solution");
            }
        }
		System.err.println(total);
    }

    static String example = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""";
}
