package aoc2016;

import common.Heading;
import common.Pose;
import io.vavr.collection.List;

// https://adventofcode.com/2016/day/1
public class Aoc2016Day01 {


//	String input = "R8, R4, R4, R8";
	String input = "L3, R1, L4, L1, L2, R4, L3, L3, R2, R3, L5, R1, R3, L4, L1, L2, R2, R1, L4, L4, R2, L5, R3, R2, R1, L1, L2, R2, R2, L1, L1, R2, R1, L3, L5, R4, L3, R3, R3, L5, L190, L4, R4, R51, L4, R5, R5, R2, L1, L3, R1, R4, L3, R1, R3, L5, L4, R2, R5, R2, L1, L5, L1, L1, R78, L3, R2, L3, R5, L2, R2, R4, L1, L4, R1, R185, R3, L4, L1, L1, L3, R4, L4, L1, R5, L5, L1, R5, L1, R2, L5, L2, R4, R3, L2, R3, R1, L3, L5, L4, R3, L2, L4, L5, L4, R1, L1, R5, L2, R4, R2, R3, L1, L1, L4, L3, R4, L3, L5, R2, L5, L1, L1, R2, R3, L5, L3, L2, L1, L4, R4, R4, L2, R3, R1, L2, R1, L2, L2, R3, R3, L1, R4, L5, L3, R4, R4, R1, L2, L5, L3, R1, R4, L2, R5, R4, R2, L5, L3, R4, R1, L1, R5, L3, R1, R5, L2, R1, L5, L2, R2, L2, L3, R3, R3, R1";

	static record Cmd(boolean right, int dist) {
		Cmd(String s) {
			this(s.charAt(0)=='R', Integer.valueOf(s.substring(1)));
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println("=== part 1");
		new Aoc2016Day01().part1();
		System.out.println("=== part 2");
		new Aoc2016Day01().part2();
	}

	private void part1() {
		var cmds = List.of(input.split(", ")).map(Cmd::new);
		Pose p0 = new Pose(Heading.NORTH, 0, 0);
		
		var destination = cmds.foldLeft(p0, (p,cmd) -> p.turn(cmd.right).ahead(cmd.dist));
		
		System.out.println(destination);
		System.out.println(destination.pos().manhattan());
	}
	
	private void part2() {
		List<Cmd> cmds = List.of(input.split(", ")).map(Cmd::new);
		Pose p0 = new Pose(Heading.NORTH, 0, 0);
		
		List<Pose> start = List.of(p0);		
		var path = cmds.foldLeft(start, (l, cmd) -> l.appendAll(l.last().turn(cmd.right).aheads(cmd.dist))).map(Pose::pos);
		
		var firstRevisited = path.filter(p -> path.count(p::equals) > 1).head();
		
		System.out.println(firstRevisited.manhattan());
	}
	

}
