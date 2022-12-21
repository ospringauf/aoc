package aoc2022;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 20: Grove Positioning System ---
// https://adventofcode.com/2022/day/20

class Day20 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 11037
        timed(() -> new Day20().part1());
        System.out.println("=== part 2"); // 3033720253914
        timed(() -> new Day20().part2());
    }

//    List<Long> data = Util.string2longs(example);
    List<Long> data = file2longs("input20.txt");
    static int SIZE = 0;

    class Element {
        
        long val;
        Element prev;
        Element next;
        
        Element(Long v) {
            this.val = v;
        }

        Element walk(long d) {
            Element n = this;
            while (d > 0) {
                n = n.next;
                d--;
            }
            while (d < 0) {
                n = n.prev;
                d++;
            }
            return n;
        }

        void mix() {
            // important - never move more than SIZE-1!
            var dist = val % (SIZE - 1);
            if (dist == 0) 
                return;
            
            Element target = walk(dist);
            Element after = (val < 0) ? target.prev : target;
            var before = after.next;

            // remove this element 
            prev.next = this.next;
            next.prev = this.prev;

            // re-insert
            this.prev = after;
            this.next = before;
            after.next = this;
            before.prev = this;
        }

        List<Long> toList(int l) {
            List<Long> r = List.empty();
            Element n = this;
            for (int i=0; i<l; ++i) {
                r = r.append(n.val);
                n = n.next;
            }
            return r;
        }
    }

    Map<Integer, Element> elements = new HashMap<>();

    void buildCycle() {
        data.forEachWithIndex((v, i) -> elements.put(i, new Element(v)));
        elements.forEach((i, n) -> {
            n.prev = elements.get((i - 1 + SIZE) % SIZE);
            n.next = elements.get((i + 1) % SIZE);
        });
    }
    
    void part1() {
        SIZE = data.size();

        buildCycle();

        for (int i = 0; i < SIZE; ++i) {
            elements.get(i).mix();
        }

        printCoordinates();
    }


    void part2() {
        data = data.map(x -> x * 811589153L);

        buildCycle();
        
        for (int j = 0; j < 10; ++j) {
            System.out.print(".");
            for (int i = 0; i < SIZE; ++i) {
                elements.get(i).mix();
            }
        }
        System.out.println();

        printCoordinates();
    }


    void printCoordinates() {
        Element n0 = elements.values().stream().filter(n -> n.val == 0).findFirst().get();
        var v1000 = n0.walk(1000).val;
        var v2000 = n0.walk(2000).val;
        var v3000 = n0.walk(3000).val;
        var r = v1000 + v2000 + v3000;
        System.out.println("-> " + v1000 + " + " + v2000 + " + " + v3000 + "\n = " + r);
    }

    static String example = """
            1
            2
            -3
            3
            -2
            0
            4
                        """;

}
