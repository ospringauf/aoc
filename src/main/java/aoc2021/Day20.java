package aoc2021;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

// --- Day 20: Trench Map ---
// https://adventofcode.com/2021/day/20

class Day20 extends AocPuzzle {

//	String input = example;
	String input = file2string("input20.txt");
	
	Image img = new Image();
	Array<Integer> algorithm;

	@SuppressWarnings("serial")
	static class Image extends PointMap<Integer> {
		static int background = 0;
		static boolean invert = false;

		int pixelValue(Point p) {
			var n = List.of( //
					p.translate(-1, -1), p.translate(0, -1), p.translate(1, -1), //
					p.translate(-1, 0), p, p.translate(1, 0), //
					p.translate(-1, 1), p.translate(0, 1), p.translate(1, 1));
			return n.map(np -> getOrDefault(np, background)).foldLeft(0, (r, i) -> 2 * r + i);
		}

		Image enhance(Array<Integer> alg) {
			var next = new Image();

			outerBounds().forEach(p -> next.put(p, alg.get(pixelValue(p))));
			if (invert)
				background = (background == 0) ? 1 : 0;
			return next;
		}

		Seq<Point> outerBounds() {
			var b = boundingBox();
			var d = 1;
			
			return List.rangeClosed(b.xMin() - d, b.xMax() + d)
					.flatMap(x -> List.rangeClosed(b.yMin() - d, b.yMax() + d).map(y -> Point.of(x, y)));
		}
		
		public void print() {
			super.print(i -> (i==1)? '#' : '.');
		}
	}

	void solve() {
		String[] blocks = input.split("\n\n");
		
		algorithm = Array.ofAll(blocks[0].toCharArray()).map(c -> (c=='#')?1:0);
		img.invert = algorithm.get(0) == 1;

		img.read(blocks[1].split("\n"), c -> (c == '#') ? 1 : 0);
//		img.print();

		int i = 1;
		for (; i <= 2; ++i) {
			img = img.enhance(algorithm);
		}

		System.out.println("=== part 1");
		System.out.println(img.countValues(1)); // 5619
//		img.print();

		for (; i <= 50; ++i) {
			img = img.enhance(algorithm);
		}

		System.out.println("=== part 2");
		System.out.println(img.countValues(1)); // 20122
	}

	public static void main(String[] args) {
		timed(() -> new Day20().solve());
	}

	static String example = """
			..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

			#..#.
			#....
			##..#
			..#..
			..###						""";

}
