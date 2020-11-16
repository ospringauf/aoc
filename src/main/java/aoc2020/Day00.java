package aoc2020;

import java.util.Arrays;

import io.vavr.collection.List;

public class Day00 {
	
	String inp = """
			a
			b
			c
			d
			e
			""";
	
	static record Point(int x, int y) {
		Point next() {
			return new Point(x+1, y);
		}
	}; 
	
	public static void main(String[] args) {
		new Day00().part1();
	}

	private void part1() {
		System.out.println(System.getProperty("java.version"));
		var p = new Point(0,0);
		System.out.println(p + "/" + p.next());
		
		System.out.println(Arrays.asList(inp.split("\n")));
		
		var l = Arrays.asList(inp.split("\n"));
		
		List.of(inp.split("\n")).forEach(x -> System.out.println(x));
	}

}
