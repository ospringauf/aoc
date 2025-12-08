package aoc2025;

import java.util.HashMap;

import common.AocPuzzle;
import common.Util;
import common.Vec3;
import io.vavr.Tuple2;
import io.vavr.collection.*;

//--- Day 8: Playground  ---
// https://adventofcode.com/2025/day/8

class Day08 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 123420
		timed(() -> new Day08().part1());
		System.out.println("=== part 2"); // 673096646
		timed(() -> new Day08().part2());
	}

//    List<String> input = file2lines("input08.txt");
	List<String> input = Util.splitLines(example);
	List<Vec3> data = input.map(s -> Vec3.parse(s));
	
	// map: assign circuit id to each box
	HashMap<Vec3, Integer> circuit = new HashMap<>();
	
	// initial circuit assignment: each box in its own circuit
	{
		int nextCircuit = 1;
		for (var p : data) {
			circuit.put(p, nextCircuit++);
		}		
	}

	void connect(Vec3 box1, Vec3 box2) {
		var c1 = circuit.get(box1);
		var c2 = circuit.get(box2);
		circuit.forEach((box, c) -> {
			if (c == c2)
				circuit.put(box, c1);
		});
	}
	
	void part1() {
		var pairs = data.combinations(2).map(l -> new Tuple2<Vec3, Vec3>(l.get(0), l.get(1)));
		pairs = pairs.sortBy(l -> l._1.euclidean(l._2));

		pairs = pairs.take(1000);
		for (var p : pairs) {
//			System.out.println(pair);
			connect(p._1, p._2);
		}
		
		var c = List.ofAll(circuit.values());		
		List<Integer> sizes = c.groupBy(x -> x).map(t -> t._2.size()).toList();
		System.out.println(sizes);
		System.out.println(sizes.sorted().reverse().take(3).product());
	}


	void part2() {
		var pairs = data.combinations(2).map(l -> new Tuple2<Vec3, Vec3>(l.get(0), l.get(1)));
		pairs = pairs.sortBy(l -> l._1.euclidean(l._2));

		for (var p : pairs) {
//			System.out.println(pair);
			connect(p._1, p._2);

			// stop when all boxes are in 1 circuit
			var c = List.ofAll(circuit.values());	
			if (c.distinct().size() == 1) {
				System.out.println(p._1.x() * p._2.x());
				break;
			}
		}
	}

	static String example = """
			162,817,812
			57,618,57
			906,360,560
			592,479,940
			352,342,300
			466,668,158
			542,29,236
			431,825,988
			739,650,466
			52,470,668
			216,146,977
			819,987,18
			117,168,530
			805,96,715
			346,949,466
			970,615,88
			941,993,340
			862,61,35
			984,92,344
			425,690,689""";
}
