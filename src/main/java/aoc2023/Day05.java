package aoc2023;

import java.util.HashMap;

import javax.management.modelmbean.XMLParseException;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;

//--- Day 5:  ---
// https://adventofcode.com/2023/day/5

class Day05 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 173706076
        timed(() -> new Day05().part1());
        System.out.println("=== part 2");
        timed(() -> new Day05().part2());
    }

//    String data = file2string("input05.txt");
	String data = example; 

    record Range2(long start, long len) {
        
        List<Range2> split(List<Long> p) {
            List<Range2> result = List.empty();
            var s = start;
            while (s < start+len) {
                var s0 = s;
                var e = p.filter(x -> x > s0).min().getOrElse(start+len);
                result = result.append(new Range2(s, e-s));
                s = e;
            }
            
            return result;
        }
        
    }
    
	record Xrange(long dst, long src, long len) {
	    static Xrange parse(String s) {
	        var i = Util.string2longs(s);
	        return new Xrange(i.get(0), i.get(1), i.get(2));
	    }
	    
	    boolean contains(long n) {
	        return (n >= src && n < src+len);
	    }
	    
	    long map(long n) {
	        return dst + (n-src);
	    }
	    
	    boolean intersect(Range2 r) {
	        return !(src+len < r.start || src > r.start+r.len); // grenzen??
	    }
	    
//	    Range2 map(Range2 r) {
//	        
//	    }
	}
	
	record Xmap(String from, String to, List<Xrange> ranges) {
	    static Xmap parse(String s) {
	        var l = List.of(s.split("\n"));
	        var f = l.head().split(" ")[0].split("-to-");
	        var r = l.tail().map(Xrange::parse);
	        return new Xmap(f[0], f[1], r);
	    }
	    
	    long map(long n) {
	        var r = ranges.find(x -> x.contains(n));
	        return r.isDefined() ? r.get().map(n) : n;
	    }
	    
	    List<Long> splitpoints() {
	        return ranges.flatMap(r -> List.of(r.src, r.src+r.len)).distinct().sorted();
	    }
	    
	    Range2 map(Range2 r) {
	        var m = ranges.find(x -> x.src == r.start);
	        return m.isDefined()? new Range2(m.get().dst, r.len) : r;
	    }
	    
	    List<Range2> mapAll(Range2 r) {
	        var rs = r.split(splitpoints());
	        return rs.map(x -> map(x));
	    }
	    
	    List<Range2> mapAll(List<Range2> r) {
	        return r.flatMap(x -> mapAll(x));
        }
	    
//	    List<Range2> map(Range2 r) {
//	        List<Range2> result = List.empty();
//	        var r0 = ranges.filter(x -> x.intersect(r)).minBy(x -> x.src);
//	        if (! r0.isDefined()) {
//	            result = result.append(r);
//	            return result;
//	        }
//	        var r00 = r0.get();
//	        if (r00.src > r.start) {
//	            var d = r00.src-r.start;
//	            result = result.append(new Range2(r.start, r.len-(d)));
//	            r = new Range2(, 0)
//	        }
//	    }
	    
	}
	
    void part1() {
        var l = List.of(data.split("\n\n"));
        var seeds = Util.string2longs(l.head().split(": ")[1]);
        var maps = l.tail().map(Xmap::parse);
        
        System.out.println(seeds);
        System.out.println(maps.get(0));
        var locs = List.empty();
        for (var s : seeds) {
            var m = new HashMap<String, Long>();
            m.put("seed", s);
            while (!m.containsKey("location")) {
                var y = maps.find(x -> m.containsKey(x.from) && ! m.containsKey(x.to)).get();
                m.put(y.to, y.map(m.get(y.from)));
            }
            System.out.println(s + " --> " + m.get("location"));
            locs = locs.append(m.get("location"));
        }
        
        System.out.println(locs.min());
    }

    void part2() {
        var l = List.of(data.split("\n\n"));
        var seeds = Util.string2longs(l.head().split(": ")[1]).sliding(2, 2).map(x -> new Range2(x.get(0), x.get(1))).toList();
        var maps = l.tail().map(Xmap::parse);
        
        System.out.println(seeds);
        var m0 = maps.get(0);
        var s0 = seeds.get(0);
        System.out.println(s0);
        System.out.println(m0.splitpoints());
        System.out.println(s0.split(m0.splitpoints()));
        
        List<Range2> locs = List.empty();
        for (var s : seeds) {
            var m = new HashMap<String, List<Range2>>();
            m.put("seed", List.of(s));
            while (!m.containsKey("location")) {
                var y = maps.find(x -> m.containsKey(x.from) && ! m.containsKey(x.to)).get();
                System.out.println(y.to + ", " + m.get(y.from).length());
                
                m.put(y.to, y.mapAll(m.get(y.from)));
            }
            System.out.println(s + " --> " + m.get("location"));
            locs = locs.appendAll(m.get("location"));
        }
        
        System.out.println(locs.map(x -> x.start).min());
        
        
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
