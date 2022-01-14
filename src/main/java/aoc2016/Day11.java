package aoc2016;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 11: Radioisotope Thermoelectric Generators ---
// https://adventofcode.com/2016/day/11

class Day11 extends AocPuzzle {

    static final int FAIL = Integer.MAX_VALUE / 2;

    enum Item {
        LM,
        HM,
        SM,
        PM,
        TM,
        RM,
        CM,
        EM,
        DM,
        LG,
        HG,
        SG,
        PG,
        TG,
        RG,
        CG,
        EG,
        DG;

        boolean isGen() {
            return this.toString().endsWith("G");
        }

        boolean isChip() {
            return this.toString().endsWith("M");
        }

        Item gen() {
            return switch (this) {
            case LM -> LG;
            case HM -> HG;
            case SM -> SG;
            case PM -> PG;
            case TM -> TG;
            case CM -> CG;
            case RM -> RG;
            case EM -> EG;
            case DM -> DG;
            default -> throw new IllegalArgumentException("Unexpected value: " + this);
            };
        }

        static String toString(Set<Item> s) {
            return s.map(i -> i.toString()).toSortedSet().mkString(",");
        }

        static String toString2(Set<Item> s) {
            return s.toList().map(i -> i.toString().charAt(1)).sorted().mkString();
        }
    };

    State stateExample = new State(0, Array.of(
            HashSet.of(Item.HM, Item.LM), 
            HashSet.of(Item.HG), 
            HashSet.of(Item.LG), 
            HashSet.empty()));
    State stateInput = new State(0, Array.of(
            HashSet.of(Item.SG, Item.SM, Item.PG, Item.PM),
            HashSet.of(Item.TG, Item.RG, Item.RM, Item.CG, Item.CM), 
            HashSet.of(Item.TM), 
            HashSet.empty()));
    State stateInput2 = new State(0, Array.of(
            HashSet.of(Item.SG, Item.SM, Item.PG, Item.PM, Item.EG, Item.EM, Item.DG, Item.DM),
            HashSet.of(Item.TG, Item.RG, Item.RM, Item.CG, Item.CM), 
            HashSet.of(Item.TM), 
            HashSet.empty()));

    record State(int floor, Array<Set<Item>> items) {

        boolean done() {
            return items.get(0).isEmpty() && items.get(1).isEmpty() && items.get(2).isEmpty();
        }

        State finalState() {
            return new State(3,
                    Array.of(HashSet.empty(), HashSet.empty(), HashSet.empty(), items.flatMap(a -> a).toSet()));
        }

        String key() {
            return "@" + floor + "  " + items.map(s -> Item.toString(s)).mkString(" | ");
        }

        String key2() {
            // only types M/G - elements are interchangeable
            return "@" + floor + "  " + items.map(s -> Item.toString2(s)).mkString(" | ");
        }

        boolean allowed() {
            var here = items.get(floor);
            return !here.exists(g -> g.isGen()) || here.filter(c -> c.isChip()).forAll(c -> here.contains(c.gen()));
        }
        
        State move(Set<Item> s, int f) {
            var here = items.get(floor);
            var i = items.update(floor, here.removeAll(s)).update(f, items.get(f).addAll(s));
            return new State(f, i);
        }

        Iterator<State> next() {
            var here = items.get(floor);

            // options: take any 1 or 2 items to next floor (up or down)
            var opt1 = here.map(i -> HashSet.of(i));
            var opt2 = here.flatMap(i -> here.remove(i).map(j -> HashSet.of(i, j)));
            var opt = opt2.toList().appendAll(opt1);

            List<Integer> nextFloors = List.empty();
            if (floor < 3)
                nextFloors = nextFloors.append(floor + 1);
            if (floor > 0)
                nextFloors = nextFloors.append(floor - 1);

            return opt.crossProduct(nextFloors).map(x -> move(x._1, x._2)).filter(s -> s.allowed());
        }
    }

    
    int solve(State initial) {
        var ik = initial.key2();
        var tgt = initial.finalState();
        Map<String, Integer> best = new HashMap<>();
        best.put(tgt.key2(), 0);
        Set<State> l = HashSet.of(tgt);
        while (!best.containsKey(ik)) {
            Set<State> next = HashSet.empty();
            for (var n : l) {
                var c = best.getOrDefault(n.key2(), FAIL);
                for (var n2 : n.next()) {
                    var n2k = n2.key2();
                    var c2 = best.getOrDefault(n2k, FAIL);
                    if (c + 1 < c2) {
                        best.put(n2k, c + 1);
                        next = next.add(n2);
                    }
                }
            }
            l = next;
        }

        return best.getOrDefault(ik, FAIL);
    }

    void part1() {
        // example initial
//         var m = step(stateExample); // 11
        var m = solve(stateInput);
        System.out.println(m);
    }


    void part2() {
        var m = solve(stateInput2);
        System.out.println(m);
    }

    public static void main(String[] args) {
        System.out.println("=== part 1");
        timed(() -> new Day11().part1());

        System.out.println("=== part 2");
        timed(() -> new Day11().part2());
    }

    static String example = """
            The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
            The second floor contains a hydrogen generator.
            The third floor contains a lithium generator.
            The fourth floor contains nothing relevant.
            			""";

    static String myInput = """
            The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
            The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
            The third floor contains a thulium-compatible microchip.
            The fourth floor contains nothing relevant.
            			""";
}
