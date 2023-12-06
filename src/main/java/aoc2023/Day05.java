package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

//--- Day 5: If You Give A Seed A Fertilizer ---
// https://adventofcode.com/2023/day/5

class Day05 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 173706076
        timed(() -> new Day05().part1());
        System.out.println("=== part 2"); // 11611182
        timed(() -> new Day05().part2());
//        System.out.println("=== part 2 naive");
//        timed(() -> new Day05().part2naive());
    }

    String data = file2string("input05.txt");
//	String data = example;

    List<Amap> chain;
    List<Long> seeds;

    record SeedRange(long start, long len) {

        boolean contains(long p) {
            return p >= start && p < (start + len);
        }
    }

    record AmapRange(long dst, long src, long len) {

        static AmapRange parse(String s) {
            var i = Util.string2longs(s);
            return new AmapRange(i.get(0), i.get(1), i.get(2));
        }

        boolean contains(long n) {
            return (n >= src && n < src + len);
        }

        boolean containsOut(long n) {
            return (n >= dst && n < dst + len);
        }

        long mapFwd(long n) {
            return dst + (n - src);
        }

        long mapBack(long n) {
            return src + (n - dst);
        }
    }

    // "almanac map"
    record Amap(String from, String to, List<AmapRange> ranges) {

        static Amap parse(String s) {
            var lines = List.of(s.split("\n"));
            var name = lines.head().split(" ")[0].split("-to-");
            var ranges = lines.tail().map(AmapRange::parse);
            return new Amap(name[0], name[1], ranges);
        }

        long mapFwd(long n) {
            var r = ranges.find(x -> x.contains(n));
            return r.isDefined() ? r.get().mapFwd(n) : n;
        }

        long mapBack(long n) {
            var r = ranges.find(x -> x.containsOut(n));
            return r.isDefined() ? r.get().mapBack(n) : n;
        }

        // pivot points: relevant inputs where the output is tipping
        List<Long> pivotPoints(List<Long> l) {
            return l.map(x -> mapBack(x)).appendAll(ranges.map(x -> x.src))
//					.appendAll(ranges.map(x -> x.src + x.len-1))
            ;
        }

        static long chainMap(List<Amap> chain, long num) {
            return chain.foldLeft(num, (val, m) -> m.mapFwd(val));
        }
    }

    public Day05() {
        var blocks = List.of(data.split("\n\n"));
        seeds = Util.string2longs(blocks.head().split(": ")[1]);

        // build the chain of mappings from "seed" to "location"
        // maps are already given in the right order, but let's order them anyway
        var maps = blocks.tail().map(Amap::parse);
        chain = List.of(maps.find(m -> m.from.equals("seed")).get());
        while (!chain.last().to.equals("location")) {
            var last = chain.last().to;
            chain = chain.append(maps.find(m -> m.from.equals(last)).get());
        }
    }

    void part1() {
        var locs = seeds.map(s -> Amap.chainMap(chain, s));
        System.out.println(locs.min());
    }

    // works as well, but takes 10 minutes. why didn't i try this the first time?
    void part2naive() {
        long min = Integer.MAX_VALUE;
        var seedRanges = this.seeds.sliding(2, 2).map(x -> new SeedRange(x.get(0), x.get(1))).toList();

        for (var sr : seedRanges) {
            System.out.println(sr);
            for (long d = 0; d < sr.len; ++d) {
                var l = sr.start + d;
                for (var m : chain)
                    l = m.mapFwd(l);
                if (l < min)
                    min = l;
            }
        }
        System.out.println(min);
    }

    void part2() {
        var seedRanges = this.seeds.sliding(2, 2).map(x -> new SeedRange(x.get(0), x.get(1))).toList();

        // calculate pivot points *backwards* along the map chain
        var pivots = chain.reverse().foldLeft(List.of(0L), (l, m) -> m.pivotPoints(l));
        pivots = pivots.distinct().sorted();

        System.out.println("checking " + pivots.length() + " pivot points");

        List<Long> location = List.empty();
        for (var s : seedRanges) {
            // only pivot points in the seed range have to be considered
            var seeds = pivots.filter(s::contains).append(s.start)
//					.append(s.start + s.len - 1)
                    .distinct();
            List<Long> locs = seeds.map(x -> Amap.chainMap(chain, x));
            location = location.appendAll(locs);
        }
        System.out.println(location.min());
    }

    static String example = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
            """;
}
