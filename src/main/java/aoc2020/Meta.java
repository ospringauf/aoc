package aoc2020;

import io.vavr.collection.List;

class Meta {

	static List<Integer> order = List.of(1,2,3,4,5,6,7,8,17,9,16,18,15,19,10,14,20,11,13,21,12,22,23,24,25);
	
	static String puzzles = """
--- Day 1: Report Repair ---
--- Day 2: Password Philosophy ---
--- Day 3: Toboggan Trajectory ---
--- Day 4: Passport Processing ---
--- Day 5: Binary Boarding ---
--- Day 6: Custom Customs ---
--- Day 7: Handy Haversacks ---
--- Day 8: Handheld Halting ---
--- Day 9: Encoding Error ---
--- Day 10: Adapter Array ---
--- Day 11: Seating System ---
--- Day 12: Rain Risk ---		
--- Day 13: Shuttle Search ---	
--- Day 14: Docking Data ---
--- Day 15: Rambunctious Recitation ---
--- Day 16: Ticket Translation ---
--- Day 17: Conway Cubes ---
--- Day 18: Operation Order ---
--- Day 19: Monster Messages ---
--- Day 20: Jurassic Jigsaw ---
--- Day 21: Allergen Assessment ---
--- Day 22: Crab Combat ---
--- Day 23: Crab Cups ---
--- Day 24: Lobby Layout ---
--- Day 25: x
			""";
	
	public static void main(String[] args) {
		var pz = List.of(puzzles.split("\n"));
		var pz1 = pz.map(s -> s.split(": ")[1].charAt(0)).mkString();
		var x = order.map(i -> pz1.charAt(i-1)).mkString();
		System.out.println(x);
	}
}
