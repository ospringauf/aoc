package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.Tuple2;
import io.vavr.collection.*;

//--- Day 22: Monkey Market ---
// https://adventofcode.com/2024/day/22

class Day22 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 18694566361
        timed(() -> new Day22().part1());
        System.out.println("=== part 2"); // 2100
        timed(() -> new Day22().part2());
    }

    List<Long> data = file2longs("input22.txt");
//	List<Long> data = Util.splitLines(example2).map(Long::valueOf);

	long next(long n0) {
	    long n=n0;
	    n = prune(mix(n*64, n));
	    n = prune(mix(n/32, n));
	    n = prune(mix(n*2048, n));
	    
	    return n;
	}
	
    long mix(long l, long n) {
        return l ^ n;
    }
    
    long prune(long n) {
        return n % 16777216;
    }
    
    Map<List<Long>,Long> numbers(long n) {
        int num = 2000;
        
        var l0 = Stream.iterate(n, this::next).take(num).toList();
        var l1 = l0.map(x -> x%10);
        var l2 = l1.sliding(2).map(t -> t.get(1) - t.get(0)).toList();

//        System.out.println(l0);
//        System.out.println(l1);
//        System.out.println(l2);
//        
        var t0 = new Tuple2<Long, List<Long>>(l1.head(), null);
        var l3 = l1.drop(4).zip(l2.sliding(4));
        
        var m = l3.reverse().toMap(t-> t._2, t-> t._1);
        
//        System.out.println(l3.toList());
//        System.out.println(m.get(List.of(-1L,-1L,0L,2L)));
        return m;
    }
    
    void part1() {
        long r = 0;
        for (long n: data) {
            long n0=n;
            for (int i=0; i<2000; ++i) n = next(n);
//            System.out.println(n0 + " -> " + n);
            r += n;
        }
        System.out.println(r);
    }

    void part2() {
    	var maps = data.map(this::numbers);

    	var signals = maps.flatMap(m -> m.keySet()).toSet();
    	System.out.println(signals.size());
    	
    	var banana = signals.map(s -> maps.map(m-> m.getOrElse(s,0L)).sum().longValue());
    	System.out.println(banana.max());
    	
    }

    static String example1 = """
1
10
100
2024
""";
    
    static String example2 = """
1
2
3
2024
""";

}
 