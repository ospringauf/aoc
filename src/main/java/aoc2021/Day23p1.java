package aoc2021;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

// --- Day 23: Amphipod ---
// https://adventofcode.com/2021/day/23

class Day23p1 extends AocPuzzle {

	static PointMap<Character> map = new PointMap<Character>();
	static PointMap<Character> tgt = new PointMap<Character>();
	static PointMap<Character> emptyMap = new PointMap<Character>();

	static final boolean TRACE = false;
	static Set<Point> rooms;

	Day23p1() {
		map.read(Util.splitLines(input));
		tgt.read(Util.splitLines(target));
		emptyMap.read(Util.splitLines(empty));
		hallway = emptyMap.findPoints('.').filter(p -> p.y() == 1);
		doors = emptyMap.findPoints('.').filter(p -> p.y() == 2).toSet();
		rooms = emptyMap.findPoints('.').filter(p -> p.y() >= 2).toSet();
		colors = List.of('A', 'B', 'C', 'D');
		dest = colors.toMap(c -> c, c -> tgt.findPoints(c).toSet());

		points = tgt.findPoints(c -> c != '#');
		allPaths = points.toMap(t -> t,
				t -> points.map(s -> tgt.dijkstraAll(t, c -> c != '#', x -> true).path(t, s).reverse()));

	}

	static List<Point> path(Point s, Point t) {
		return allPaths.getOrElse(t, null).find(p -> p.head().equals(s)).get();
	}

	static int dist(Point s, Point t) {
		int dx = Math.abs(t.x() - s.x());
		return dx + ((dx > 0) ? ((s.y() - 1) + (t.y() - 1)) : 0);
	}

	interface FreePath extends Predicate<List<Point>> {
	};

	record Amphipod(char color, Point pos, int cost) {

		static final Map<Character, Integer> stepCost = HashMap.of('A', 1, 'B', 10, 'C', 100, 'D', 1000);

		Amphipod move(Point d) {
			if (TRACE)
				System.out.println("move " + this + " -> " + d + " for " + costTo(d));
			return new Amphipod(color, d, cost + costTo(d));
		}

		int remainingLB() {
			return myDest().map(d -> costTo(d)).min().get();
		}

		int costTo(Point d) {
			return dist(pos, d) * stepCost.getOrElse(color, 0);
		}

		int cost(List<Point> path) {
			return (path.size() - 1) * stepCost.getOrElse(color, 0);
		}

		Option<List<Point>> pathToDest(FreePath free) {
			if (finished() && pos.y() == 3)
				return Option.none();

			Set<List<Point>> paths = myDest().map(d -> path(pos, d)).filter(free);

			// return paths.maxBy(path -> path.size());
			return paths.maxBy(path -> path.last().y());
		}

		Set<Point> myDest() {
			return dest.get(color).get().remove(pos);
		}

		boolean finished() {
			return dest.get(color).get().contains(pos);
		}

		List<Point> hallwayDest(FreePath free) {
			if (hallway.contains(pos))
				return List.empty(); // locked

			if (finished() && pos.y() == 3)
				return List.empty();

			return hallway.filter(p -> p.x() != pos.x()) // cannot stop in front of door
					.filter(p -> free.test(path(pos, p)));
		}
	}

	record Game(List<Amphipod> pods, int cost, Game prev) {

		static int bestCost = Integer.MAX_VALUE;
		static Game bestGame = null;

		public Game(List<Amphipod> pods, Game prev) {
			this(pods, pods.map(a -> a.cost).sum().intValue(), prev);
		}

		boolean finished() {
			return pods.forAll(Amphipod::finished);
		}

		boolean goon() {
			return cost + pods.map(p -> p.remainingLB()).sum().intValue() < bestCost;
		}

		boolean freePath(List<Point> path) {
			return !pods.exists(a -> path.tail().contains(a.pos));
		}

		Amphipod podAt(Point p) {
			return pods.find(a -> a.pos.equals(p)).get();
		}

		void play() {
//			var next = pods.toMap(a -> a, a -> a.hallwayDest(this::freePath)).filter(x -> !x._2.isEmpty());			
//			next.flatMap((a, ds) -> ds.map(d -> this.move(a, d)));

			if (TRACE)
				print();

			if (finished()) {
				if (cost < bestCost) {
					System.out.println(" best so far: " + cost);
					bestCost = cost;
					bestGame = this;
				}
				return;
			}

			// var doorpods = pods.filter(a -> doors.contains(a.pos) && (podAt(a.pos.south()).color != a.color));
			var roompods = pods.filter(a -> rooms.contains(a.pos));
			var x = pods.filter(a -> a.finished() && a.pos.y() == 3);
			var y = pods.filter(a -> a.finished() && a.pos.y() == 2 && x.contains(podAt(a.pos.south())));
			var tryPods = roompods.removeAll(x).removeAll(y);
			
			// move any one Pod out of the way (from room to hallway)
			for (var a : tryPods) {
				for (var d : a.hallwayDest(this::freePath)) {
					// move as many Pods as possible to their destinations
					var game = move(a, d);
					game = moveToDest(game);
					if (game.goon())
						game.play();
				}
			}
		}

		void print() {
			pods.forEach(a -> emptyMap.put(a.pos, a.color));
			emptyMap.print();
			pods.forEach(a -> emptyMap.put(a.pos, '.'));
			System.out.println(cost);
			System.out.println();
		}

		static Game moveToDest(Game g) {

			boolean moved = true;
			do {
				moved = false;
				for (var a : g.pods) {
					var ptd = a.pathToDest(g::freePath);
					if (ptd.isDefined()) {
						Point dst = ptd.get().last();

						boolean canMove = (dst.y() == 3);
						canMove |= g.pods.find(x -> x.pos.equals(dst.south())).exists(x -> x.color == a.color);
						if (canMove) {
							g = g.move(a, dst);
							moved = true;
						}
					}
				}
			} while (moved);
			return g;
		}

		Game move(Amphipod a, Point p) {
			return new Game(pods.replace(a, a.move(p)), this);
		}

	}

	void part1() {

		var pods = map.findPoints(colors::contains).map(p -> new Amphipod(map.get(p), p, 0));
		var game = new Game(pods, null);

		game = Game.moveToDest(game);
//		game.print();

		game.play();
		
		System.out.println("=== part 1 result");
		var states = Stream.iterate(Game.bestGame, g -> g.prev).takeUntil(g -> g == null).reverse();
		states.forEach(s -> s.print());
//		System.out.println(allPaths.get(Point.of(1,1)));
//		System.out.println(path(Point.of(1,1), Point.of(5,3)));

	}

	void part2() {

	}

	public static void main(String[] args) {

		System.out.println("=== part 1");
		new Day23p1().part1();

		System.out.println("=== part 2");
		new Day23p1().part2();
	}

	static String example1 = """
			#############
			#...........#
			###B#C#B#D###
			###A#D#C#A###
			#############
						""";

	static String example2 = """
#############
#.....D.....#
###.#B#C#D###
  #A#B#C#A#
  #########
						""";

	static String input = """
			#############
			#...........#
			###A#D#A#B###
			###B#C#D#C###
			#############
						""";

	static String target = """
			#############
			#...........#
			###A#B#C#D###
			###A#B#C#D###
			#############
						""";

	static String empty = """
			#############
			#...........#
			###.#.#.#.###
			###.#.#.#.###
			#############
						""";

	static List<Point> hallway;
	static Map<Character, Set<Point>> dest;
	static List<Point> points;
	static Map<Point, List<List<Point>>> allPaths;
	static Set<Point> doors;
	static List<Character> colors;

}
