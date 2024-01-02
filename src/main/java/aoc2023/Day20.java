package aoc2023;

import java.util.Map;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 20: Pulse Propagation ---
// https://adventofcode.com/2023/day/20

class Day20 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 730797576
        timed(() -> new Day20().part1());
        System.out.println("=== part 2"); // 226732077152351
        timed(() -> new Day20().part2());
    }

    List<String> data = file2lines("input20.txt");
//    List<String> data = Util.splitLines(example2);

    List<Pulse> pulses = List.empty();
    Map<String, Module> modules;

    class Module {
        String name;
        String nameWithPrefix;
        List<String> output = List.empty();

        Module(String s) {
            var a = s.split(" -> ");
            nameWithPrefix = a[0];
            name = nameWithPrefix;
            if (name.startsWith("%") || name.startsWith("&"))
                name = name.substring(1);
            if (a.length == 2)
                output = List.of(a[1].split(", "));
        }

        @Override
        public String toString() {
            return nameWithPrefix + " -> " + output.mkString(",");
        }

        void send(boolean high) {
            pulses = pulses.appendAll(output.map(x -> new Pulse(name, x, high)));
        }

        void receive(Pulse p) {
            send(p.high);
        }
    }

    class Flipflop extends Module {
        boolean on = false;

        Flipflop(String s) {
            super(s);
        }

        @Override
        void receive(Pulse p) {
            if (!p.high) {
                on = !on;
                send(on);
            }
        }
    }

    class Conjunction extends Module {
        Map<String, Boolean> input;

        Conjunction(String s) {
            super(s);
        }

        void setInputs(List<String> inputNames) {
            input = inputNames.toMap(x -> x, x -> false).toJavaMap();
        }

        @Override
        void receive(Pulse p) {
            input.put(p.from, p.high);
            var allHigh = input.values().stream().allMatch(s -> s);
            send(!allHigh);
        }
    }

    record Pulse(String from, String to, boolean high) {
    }

    Module parse(String s) {
        if (s.startsWith("%"))
            return new Flipflop(s);
        if (s.startsWith("&"))
            return new Conjunction(s);
        return new Module(s);
    }

    Day20() {
        var m = data.map(this::parse);
        modules = m.toMap(x -> x.name, x -> x).toJavaMap();

        // connect inputs
        var cn = m.filter(x -> x instanceof Conjunction).map(x -> x.name);
        for (var c : cn) {
            var in = m.filter(x -> x.output.contains(c)).map(x -> x.name);
            ((Conjunction) (modules.get(c))).setInputs(in);
        }
    }

    void part1() {
        int low = 0;
        int high = 0;

        for (int button = 0; button < 1000; ++button) {
            pulses = pulses.append(new Pulse("button", "broadcaster", false));

            while (!pulses.isEmpty()) {
                var p = pulses.head();
                if (p.high)
                    high++;
                else
                    low++;
                pulses = pulses.tail();
                var dst = modules.get(p.to);
                if (dst != null)
                    dst.receive(p);
            }
        }

        System.out.println("low: " + low);
        System.out.println("high: " + high);
        System.err.println(low * high);
    }

    void part2() {
        // rx is fed from a conjunction with 4 inputs
        // assuming that these 4 inputs deliver periodic "high" pulses,
        // let's find the cycle lengths and their least common multiple (LCM)
        // btw, the cycle lengths turn out to be prime numbers ;-)

        // &gh -> rx
        var driver = List.ofAll(modules.values()).find(m -> m.output.contains("rx")).get();
        System.out.println(driver);

        // &qx -> gh, &zf -> gh, &rk -> gh, &cd -> gh
        var inputs = List.ofAll(modules.values()).filter(m -> m.output.contains(driver.name));
        System.out.println(inputs);

        var cycles = inputs.map(m -> new Day20().firstHighPulse(m.name));
        System.out.println(cycles); // 4057, 3947, 3733, 3793

        var v = cycles.toJavaList().stream().mapToInt(x -> x).toArray();
        System.err.println(Util.lcm(v));
    }

    // how many times to push the button until first high pulse from the given
    // module?
    int firstHighPulse(String from) {
        int button = 0;
        boolean found = false;
        while (!found) {
            button++;
            pulses = pulses.append(new Pulse("button", "broadcaster", false));

            while (!pulses.isEmpty()) {
                var p = pulses.head();
                found |= p.high && p.from.equals(from);
                pulses = pulses.tail();
                var destination = modules.get(p.to);
                if (destination != null)
                    destination.receive(p);
            }
        }
        return button;
    }

    static String example = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """;

    static String example2 = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
                        """;
}
