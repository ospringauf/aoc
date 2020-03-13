package aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7simple {

	public static void main(String[] args) throws Exception {
	
		List<String> lines = Files.readAllLines(Paths.get("src/main/java/aoc2018/day7.txt"));

		// Step I must be finished before step G can begin.
		Function<String, Character[]> parseLine = l -> new Character[]{ l.charAt(5), l.charAt(36) }; 

		List<Character[]> before = lines.stream().map(parseLine).collect(Collectors.toList());
		
		Set<Character> todo = before.stream().flatMap(e -> Stream.of(e[0], e[1])).collect(Collectors.toSet());
		
		List<Character> done = new ArrayList<Character>();
		
		while (! todo.isEmpty()) {
						
			Predicate<Character> cando = c -> before.stream().filter(b -> b[1]==c).map(b -> b[0]).allMatch(done::contains);
			
			Character next = todo.stream().filter(cando).sorted().findFirst().get();
			
			done.add(next);
			todo.remove(next);			
		}
		
		System.out.println(done);
		
	}

}
