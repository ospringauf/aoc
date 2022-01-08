package aoc2016;

import java.util.Comparator;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

// --- Day 4:  ---
// https://adventofcode.com/2016/day/4

class Day04 extends AocPuzzle {

	record Room(String name, int sector, String checksum) {
		static Room parse(String s) {
			var f = List.of(s.split("[\\-\\[\\]]")).reverse();
			var checksum = f.head();
			var sector = Integer.parseInt(f.tail().head());
			var name = f.tail().tail().reverse().mkString();
			return new Room(name, sector, checksum);
		}
		
		boolean isReal() {
			var letters = List.ofAll(name.toCharArray());
			
			Comparator<Character> cmp = Comparator.comparing(c -> letters.count(x -> x==c));
			cmp = cmp.reversed().thenComparingInt(c -> (int)c);
			var cs = letters.distinct().sorted(cmp).take(5).mkString();
			
//			System.out.println(f + " -> " + cs);
			return checksum.equals(cs);
		}
		
		String decrypt() {
			var alphabet = List.rangeClosed('a', 'z');
			var letters = List.ofAll(name.toCharArray());
			return letters.map(c -> alphabet.get(((c-'a')+sector) % alphabet.size())).mkString();
		}
	}
	
	void part1() {
//		var l = Util.splitLines(example).map(Room::parse);
		var l = file2lines("day04.txt").map(Room::parse);
		var x = l.filter(r -> r.isReal()).map(r -> r.sector).sum();
		System.out.println(x);
	}

	void part2() {
		file2lines("day04.txt")
		.map(Room::parse)
		.filter(r -> r.decrypt().contains("north"))
		.stdout();
		
		System.out.println(Room.parse("kloqemlib-lygbzq-pqloxdb-991[lbqod]").decrypt());
	}

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day04().part1());

		System.out.println("=== part 2");
		timed(() -> new Day04().part2());
}

	static String example = """
aaaaa-bbb-z-y-x-123[abxyz]
a-b-c-d-e-f-g-h-987[abcde]
not-a-real-room-404[oarel]
totally-real-room-200[decoy]		
			""";
}
