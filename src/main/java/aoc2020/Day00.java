package aoc2020;

import java.util.Arrays;
import java.util.stream.Stream;

import io.vavr.collection.List;

public class Day00 {

	String inp = """
			a
			b
			c
			d
			e
			""";

	String instr = "L3, R1, L4, L1, L2, R4, L3, L3, R2, R3, L5, R1, R3, L4, L1, L2, R2, R1, L4, L4, R2, L5, R3, R2, R1, L1, L2, R2, R2, L1, L1, R2, R1, L3, L5, R4, L3, R3, R3, L5, L190, L4, R4, R51, L4, R5, R5, R2, L1, L3, R1, R4, L3, R1, R3, L5, L4, R2, R5, R2, L1, L5, L1, L1, R78, L3, R2, L3, R5, L2, R2, R4, L1, L4, R1, R185, R3, L4, L1, L1, L3, R4, L4, L1, R5, L5, L1, R5, L1, R2, L5, L2, R4, R3, L2, R3, R1, L3, L5, L4, R3, L2, L4, L5, L4, R1, L1, R5, L2, R4, R2, R3, L1, L1, L4, L3, R4, L3, L5, R2, L5, L1, L1, R2, R3, L5, L3, L2, L1, L4, R4, R4, L2, R3, R1, L2, R1, L2, L2, R3, R3, L1, R4, L5, L3, R4, R4, R1, L2, L5, L3, R1, R4, L2, R5, R4, R2, L5, L3, R4, R1, L1, R5, L3, R1, R5, L2, R1, L5, L2, R2, L2, L3, R3, R3, R1";

	public static void main(String[] args) {
		new Day00().part1();
	}

	private void part1() {
		List<Heading> headings = List.of(Heading.NORTH, Heading.EAST, Heading.SOUTH, Heading.WEST);
		Heading head = Heading.NORTH;
		Point pos = new Point(0,0);
		for (String s :instr.split(", ")) {
			int dist = Integer.valueOf(s.substring(1));

			head = head.turn(s.charAt(0)=='R');
				
			pos = pos.next(head, dist);
		}
		
		System.out.println(pos);
		System.out.println(pos.manhattan());

	}

	private void part0() {
		System.out.println(System.getProperty("java.version"));
		var p = new Point(0, 0);
		System.out.println(p + "/" + p.next());

		System.out.println(Arrays.asList(inp.split("\n")));

		var l = Arrays.asList(inp.split("\n"));

		List.of(inp.split("\n")).forEach(x -> System.out.println(x));
	}

}
