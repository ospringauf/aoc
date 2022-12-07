package aoc2016;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

// --- Day 24: Air Duct Spelunking ---
// https://adventofcode.com/2016/day/24

class Day24 extends AocPuzzle {

    
//    List<String> data = Util.splitLines(example);
    List<String> data = file2lines("day24.txt");
    
	void solve(boolean backToStart) {
	    var m = new PointMap<Character>();
	    m.read(data);
	    
	    var start = m.findPoint('0');
	    System.out.println("start: " + start);
	    var poi = List.rangeClosed('1','9').map(i -> m.findPoint(i)).filter(p -> p!=null);
//	    m.print();
	    System.out.println("POI: " + poi);
	    
	    System.out.println("calculating distances");
	    var dist = distances(m, poi.append(start));
	    
	    System.out.println("measuring all paths");
	    var gen = Generator.permutation(poi.asJava()).simple();
	    var lengths = List.empty();
	    for (var perm : gen) {
	        var path = List.of(start).appendAll(perm);
	        if (backToStart)
	            path = path.append(start);
	        var len = path.sliding(2)
	                .map(l -> Tuple.of(l.get(0), l.get(1)))
	                .map(t -> dist.getOrElse(t, 1000000))
	                .sum();
	        lengths = lengths.append(len);
	    }
	    System.out.println("shortest: " + lengths.min());
	}

	HashMap<Tuple2<Point,Point>,Integer> distances(PointMap<Character> m, List<Point> points) {
	    HashMap<Tuple2<Point, Point>, Integer> d = HashMap.empty();
        for (var from : points) {
            var res = m.minDistances(from, c -> c!='#');
            for (var to : points) {
                Integer dto = res.get(to);
                d = d.put(Tuple.of(from, to), dto);
                d = d.put(Tuple.of(to, from), dto);
            }
        }
        return d;
    }

    void part2() {
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); 
		timed(() -> new Day24().solve(false));

		System.out.println("=== part 2"); 
		timed(() -> new Day24().solve(true));
	}
	
	static String example = """
###########
#0.1.....2#
#.#######.#
#4.......3#
###########	        
	        """;
}
