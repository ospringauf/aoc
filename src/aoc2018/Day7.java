package aoc2018;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
	
	static class Day7Edge {
		String src;
		String tgt;

		public Day7Edge(String s) {
			src = s.substring(5, 6);
			tgt = s.substring(36, 37);
		}
		
		@Override
		public String toString() {			
			return src + " --> " + tgt;
		}
	}
	
	private static List<Day7Edge> edges;
	private static Set<String> nodes;

	public static void main(String[] args) throws Exception {
	    edges = Util.lines("aoc2018/day7.txt").stream().map(s -> new Day7Edge(s)).collect(Collectors.toList());
//	    edges.stream().forEach(System.out::println);
	    
	    nodes = Stream
	    	.concat(edges.stream().map(e -> e.src), edges.stream().map(e -> e.tgt))
	    	.distinct()
	    	.collect(Collectors.toSet());
	    	    
	    System.out.println("=== part 1 ===");
		part1();
		System.out.println("=== part 2 ===");
		part2();
	}

	private static void part1() throws Exception {
		
		Set<String> todo = new HashSet<>(nodes);	
		List<String> done = new ArrayList<>();
			
		while (!todo.isEmpty()) {
			
			String next = todo.stream()
				.filter(n -> edges.stream().allMatch(e -> !n.equals(e.tgt) || done.contains(e.src)))
				.sorted()
				.findFirst().get();
			
			todo.remove(next);
			done.add(next);
		}
		
		System.out.println(done.stream().collect(Collectors.joining("")));
	}

	static int duration(String s) {
		return (s.charAt(0) - 'A') + 1 + 60;
	}
	
	private static void part2() throws Exception {
		
		Set<String> todo = new HashSet<>(nodes);		
		List<String> assigned = new ArrayList<>();
		
		// end times
		Map<String, Integer> end = todo.stream().collect(Collectors.toMap(n -> n, n -> 0));
			
		int t = 0;
		int workers = 5;
		
		while (!todo.isEmpty()) {
			final int now = t;
			
			// finished work
			Set<String> done = assigned.stream().filter(n -> end.get(n) <= now).collect(Collectors.toSet());
			int idle = workers - (assigned.size() - done.size());
			
			System.out.println("t = " + t + " idle: " + idle);
			
			todo.stream()
					.filter(n -> edges.stream().allMatch(e -> !n.equals(e.tgt) || done.contains(e.src)))
					.sorted()
					.limit(idle)
					.peek(n -> System.out.println("assign " + n))
					.forEach(n -> { 
						end.put(n, now + duration(n));
						todo.remove(n);
						assigned.add(n);
					});
			
			t++;
		}
		
		// last finished at
		System.out.println(end.values().stream().mapToInt(i -> i).max());
	}

}
