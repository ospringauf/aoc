package aoc2020;

import java.util.function.UnaryOperator;

import io.vavr.collection.Stream;

// https://adventofcode.com/2020/day/3
// --- Day 3: Toboggan Trajectory ---

@SuppressWarnings({ "deprecation", "preview" })
public class Day03 {

	private static final char TREE = '#';


	public static void main(String[] args) throws Exception {
		new Day03().solve();
	}

	private void solve() throws Exception {
		var data = example.split("\\n");
//		var data = Util.lines("input03.txt");
		
		var m = new PointMap<Character>();
		m.read(data, c -> c);

		UnaryOperator<Point> nextPoint = p -> p.east(3).south(1);		
		long t1 = countTreesOnSlope(m, nextPoint);
		
		System.out.println("=== part 1");
		System.out.println(t1);

		System.out.println("=== part 2");
		long t2 = countTreesOnSlope(m, p -> p.east(1).south(1));
		long t3 = countTreesOnSlope(m, p -> p.east(5).south(1));
		long t4 = countTreesOnSlope(m, p -> p.east(7).south(1));
		long t5 = countTreesOnSlope(m, p -> p.east(1).south(2));
		System.out.println(t1 * t2 * t3 * t4 * t5);
	}

	private long countTreesOnSlope(PointMap<Character> m, UnaryOperator<Point> nextPoint) {
		BoundingBox bb = m.boundingBox();
		
		var path = Stream
				.iterate(new Point(0,0), nextPoint)
				.map(bb::wrapX)
				.takeWhile(bb::contains)
				;
		
		long trees = path.count(p -> m.get(p) == TREE);
		
		return trees;
	}


	static String example = """
			..##.......
			#...#...#..
			.#....#..#.
			..#.#...#.#
			.#...##..#.
			..#.##.....
			.#.#.#....#
			.#........#
			#.##...#...
			#...##....#
			.#..#...#.#
					""";
}
