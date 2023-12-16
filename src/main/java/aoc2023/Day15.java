package aoc2023;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.List;

//--- Day 15: Lens Library ---
// https://adventofcode.com/2023/day/15

class Day15 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 511416
        timed(() -> new Day15().part1());
        System.out.println("=== part 2"); // 290779
        timed(() -> new Day15().part2());
    }

    String data = file2string("input15.txt").trim();
//    String data = example.trim();

    int hash(String s) {
        int h = 0;
        for (int i = 0; i < s.length(); ++i) {
            h = ((h + (int) s.charAt(i)) * 17) % 256;
        }
        return h;
    }

    void part1() {
        var steps = List.of(data.split(","));
        var result = steps.map(this::hash).sum();
        System.err.println(result);
    }

    record Lens(String label, int fl) {
        public String toString() {
            return "[" + label + " " + fl + "]";
        }
    }

    void part2() {
        List<Lens> l0 = List.empty();
        Array<List<Lens>> boxes = List.range(0, 256).map(x -> l0).toArray();
        
        var steps = List.of(data.split(","));
        for (var step : steps) {
            if (step.indexOf('=') >= 0) {
                var label = step.substring(0, step.length() - 2);
                var fl = step.charAt(step.length() - 1) - '0';
                int ib = hash(label);
                var box = boxes.get(ib);
                var il = box.indexWhere(l -> l.label.equals(label), 0);
                if (il >= 0)
                    box = box.update(il, new Lens(label, fl));
                else
                    box = box.append(new Lens(label, fl));
                boxes = boxes.update(ib, box);
            } else {
                var label = step.substring(0, step.length() - 1);
                int ib = hash(label);
                var box = boxes.get(ib);
                box = box.reject(l -> l.label.equals(label));
                boxes = boxes.update(ib, box);
            }
        }

//        for (int i = 0; i < 10; ++i)
//            System.out.println(i + " --> " + boxes.get(i));

        long fp = 0;
        for (int b = 0; b < 256; ++b) {
            var box = boxes.get(b);
            for (int i = 0; i < box.size(); ++i) {
                fp += (b + 1) * (i + 1) * box.get(i).fl;
            }
        }
        System.err.println(fp);
    }

    static String example = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
}
