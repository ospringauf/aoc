package aoc2018;

import static org.jooq.lambda.Seq.of;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;


/**
 * https://adventofcode.com/2018/day/7
 * uses https://github.com/jOOQ/jOOL
 *
 */
public class Day7b {
	
	static class Edge {
		String src;
		String tgt;

		public Edge(String s) {
			src = s.substring(5, 6);
			tgt = s.substring(36, 37);
		}
		
		@Override
		public String toString() {			
			return src + " --> " + tgt;
		}
	}
	
	private static List<Edge> edges;
	private static Set<String> nodes;

	public static void main(String[] args) throws Exception {
	    edges = seq(Util.lines("aoc2018/day7.txt")).map(Edge::new).toList();
	    nodes = seq(edges).flatMap(e -> of(e.src, e.tgt)).distinct().toSet();
	   
	    System.out.println("=== part 1 ===");
		part1();
		
		System.out.println("=== part 2 ===");
		part2();
	}

	private static void part1() throws Exception {
		Set<String> todo = new HashSet<>(nodes);	
		List<String> done = new ArrayList<>();
		
		// all preconditions (sources) for target n are done 
		Predicate<String> ready = n -> seq(edges).filter(e -> e.tgt.equals(n)).allMatch(e -> done.contains(e.src));
		
		while (!todo.isEmpty()) {
		    
		    String next = seq(todo).filter(ready).sorted().findFirst().get();
			
			todo.remove(next);
			done.add(next);
		}

		System.out.println(seq(done));
	}

	static int duration(String s) {
		return (s.charAt(0) - 'A') + 1 + 60;
	}
	
	private static void part2() throws Exception {
		Set<String> todo = new HashSet<>(nodes);		
		List<String> assigned = new ArrayList<>();
		
		// end times
		Map<String, Integer> end = seq(todo).toMap(n -> n, n -> 0);
		
		int t = 0;
		int workers = 5;
		
		while (!todo.isEmpty()) {
			final int now = t;
			
			// finished work
			Set<String> done = seq(assigned).filter(n -> end.get(n) <= now).toSet();
			int idle = workers - (assigned.size() - done.size());
			
			System.out.println("t = " + t + " idle: " + idle);
			
		     // all preconditions (sources) for target n are done 
	        Predicate<String> ready = n -> seq(edges).filter(e -> e.tgt.equals(n)).allMatch(e -> done.contains(e.src));

			seq(todo)
				.filter(ready)
				.sorted()
				.limit(idle)
				.peek(n -> System.out.println("assign " + n))
				.forEach(n -> { 
				    // start work at node n, mark end time
					end.put(n, now + duration(n));
					todo.remove(n);
					assigned.add(n);
				});
			
			t++;
		}
		
		// last finished at
		System.out.println(seq(end.values()).max());
	}
}
