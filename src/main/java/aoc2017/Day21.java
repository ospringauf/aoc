package aoc2017;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;

// --- Day 21: Fractal Art ---
// https://adventofcode.com/2017/day/21

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day21 extends AocPuzzle {

	static class Tile extends PointMap<Character> {

		List<Tile> variants() {
			var max = gridsize();
			Function<Point, Point> id = p -> p;
			Function<Point, Point> rot1 = p -> new Point(p.y(), max - p.x());
			Function<Point, Point> flip = p -> new Point(p.x(), max - p.y());
			Function<Point, Point> rot2 = rot1.andThen(rot1);
			Function<Point, Point> rot3 = rot2.andThen(rot1);
			Function<Point, Point> r1f = rot1.andThen(flip);
			Function<Point, Point> r2f = rot2.andThen(flip);
			Function<Point, Point> r3f = rot3.andThen(flip);

			var transformations = List.of(id, rot1, rot2, rot3, flip, r1f, r2f, r3f);
			return transformations.map(t -> new Tile(this, t));
		}

		Tile() {
			super();
		}

		List<Point> square(Point p, int d) {
			return List.range(0, d).crossProduct().map(t -> p.translate(t._1, t._2)).toList();
		}

		Tile(Tile base, Function<Point, Point> f) {
			// construct a transformed variant
			base.keySet().forEach(p -> put(f.apply(p), base.get(p)));
			shiftToOrigin();
		}

		int gridsize() {
			return boundingBox().xMax() + 1;
		}

		String line() {
			var s = gridsize();
			return List.range(0, s).map(y -> List.range(0, s).map(x -> get(Point.of(x, y))).mkString()).mkString("/");
		}

		FracMap split() {
			var s = gridsize();
			var m = new FracMap();
			var d = (s % 2 == 0) ? 2 : 3;
			System.out.println("split " + s + "/" + d);

			for (int x = 0; x < s; x += d)
				for (int y = 0; y < s; y += d) {
					var t = new Tile();
					var p = Point.of(x, y);
					square(p, d).forEach(q -> t.put(q.modulo(d, d), get(q)));
					m.put(Point.of(x / d, y / d), t);
				}
			return m;
		}
	}

	static class FracMap extends PointMap<Tile> {
		int gridsize() {
			return boundingBox().xMax() + 1;
		}

		Tile flatten() {
			var s = gridsize();
			var si = get(Point.of(0, 0)).gridsize();
			System.out.println("flatten " + s + "*" + si);
			var m = new Tile();
			for (int x : List.range(0, s))
				for (int y : List.range(0, s)) {
					var p = Point.of(x, y);
					var t = get(p);
					t.keySet().forEach(q -> m.put(q.translate(si * x, si * y), t.get(q)));
				}
			return m;
		}
	}

	static record Rule(Tile from, Tile to) {
		static Rule parse(String s) {
			var a = s.split(" => ");
			var tfrom = new Tile();
			tfrom.read(a[0].split("/"), c -> c);
			var tto = new Tile();
			tto.read(a[1].split("/"), c -> c);
			return new Rule(tfrom, tto);
		}

		boolean match(Tile t) {
			return t.equals(from);
		}
	}

	List<Rule> rules = file2lines("input21.txt").map(Rule::parse);
//	List<Rule> rules = Util.splitLines(example).map(Rule::parse);

	void part1() {
		var map = new Tile();
		map.read(Util.splitLines(start));
		map.print();

		for (int i = 0; i < 5; ++i) {
			System.out.println("-- round " + i);
			map = iteration(map);
			map.print();
			System.out.println(map.countValues('#'));
		}
	}

	Tile iteration(Tile map) {
		var next = new FracMap();
		var fm = map.split();

		for (var p : fm.keySet()) {
			var tile = fm.get(p);
			var rule = findRule(tile);
//			System.out.println(p + " --> apply " + rule.from);
			next.put(p, rule.to);
		}

		return next.flatten();
	}

	Map<Tile, Rule> cache = new HashMap<>();

	private Rule findRule(Tile tile) {
		if (cache.containsKey(tile))
			return cache.get(tile);

		var vs = tile.variants();
		var rule = rules.filter(r -> vs.exists(v -> r.match(v))).single();
		vs.forEach(v -> cache.put(v, rule));

		return rule;
	}

	void part2() {
		var map = new Tile();
		map.read(Util.splitLines(start));

		for (int i = 0; i < 18; ++i) {
			System.out.println("-- round " + i);
			map = iteration(map);
			System.out.println(map.countValues('#'));
		}
	}

	void test() {

		var t = new Tile();
		t.read(Util.splitLines(test0));

		System.out.println(t.square(Point.of(0, 0), 3));

		t.print();
		var f = t.split();
		f.get(Point.of(0, 0)).print();
		f.get(Point.of(0, 1)).print();

	}

	public static void main(String[] args) {

		System.out.println("=== test");
//		new Day21().test();

		System.out.println("=== part 1"); // 133
		new Day21().part1();

		System.out.println("=== part 2"); // 2221990
		timed(() -> new Day21().part2());
	}

	static String example = """
			../.# => ##./#../...
			.#./..#/### => #..#/..../..../#..#
						""";

	static String test0 = """
			.#..#.
			#..#..
			#..#..
			.#..#.
			#..#..
			#..#..
						""";

	static String start = """
			.#.
			..#
			###
						""";

}
