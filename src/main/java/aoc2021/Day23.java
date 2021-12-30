package aoc2021;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

// --- Day 23: Amphipod ---
// https://adventofcode.com/2021/day/23

// TODO make this work for part 1 *and* part 2
// TODO make faster (current: 3 min)

class Day23 extends AocPuzzle {

	static PointMap<Character> map = new PointMap<Character>();
	static PointMap<Character> goal = new PointMap<Character>();
	static PointMap<Character> empty = new PointMap<Character>();

	static final boolean TRACE = false;

	static int bottom;
	static Set<Point> hallway;
	static Map<Character, Set<Point>> rooms;
	static List<Point> points;
	static Map<Point, List<List<Point>>> allPaths;
	static Set<Point> doors;
	
	static final List<Character> colors = List.of('A', 'B', 'C', 'D');
	static final Map<Character, Integer> stepCost = HashMap.of('A', 1, 'B', 10, 'C', 100, 'D', 1000);

	Day23(boolean part2) {
		Function1<String, List<String>> reader = Util::splitLines;
		if (!part2) {
			reader = reader.andThen(l -> l.removeAt(3).removeAt(3));
		}

		map.clear();
		goal.clear();
		empty.clear();
		map.read(reader.apply(input));
		goal.read(reader.apply(goalMap));
		empty.read(reader.apply(emptyMap));

		hallway = empty.findPoints('.').filter(p -> p.y() == 1).toSet();
		doors = empty.findPoints('.').filter(p -> p.y() == 2).toSet();
		rooms = colors.toMap(c -> c, c -> goal.findPoints(c).toSet());

		points = goal.findPoints(c -> c != '#');
		allPaths = points.toMap(t -> t,
				t -> points.map(s -> goal.dijkstraAll(t, c -> c != '#', x -> true).path(t, s).reverse()));

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

	record Amphipod(char color, Point pos, int cost, boolean arrived) {
		Amphipod move(Point d, boolean arrived) {
			if (TRACE)
				System.out.println("move " + this + " -> " + d + " for " + costTo(d));
			return new Amphipod(color, d, cost + costTo(d), arrived);
		}

		int remainingCost() {
			// lower bound!
			return myDest().map(d -> costTo(d)).min().get();
		}

		int costTo(Point d) {
			return dist(pos, d) * stepCost.getOrElse(color, 0);
		}

//		int cost(List<Point> path) {
//			return (path.size() - 1) * stepCost.getOrElse(color, 0);
//		}

		Option<List<Point>> pathToDest(FreePath free) {
			if (arrived)
				return Option.none();

			var paths = myDest().map(d -> path(pos, d)).filter(free);
			return paths.maxBy(path -> path.last().y());
		}

		Set<Point> myDest() {
			return rooms.get(color).get().remove(pos);
		}

		boolean inHomeRoom() {
			return rooms.get(color).get().contains(pos);
		}

		Set<Point> hallwayDest(FreePath free) {
			return hallway //
					.filter(p -> p.x() != 3 && p.x() != 5 && p.x() != 7 && p.x() != 9) // cannot stop in front of doors
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
			// return pods.forAll(Amphipod::finished);
			return pods.forAll(Amphipod::arrived);
		}

		boolean goon() {
			return cost + pods.map(p -> p.remainingCost()).sum().intValue() < bestCost;
		}

		boolean freePath(List<Point> path) {
			return !pods.exists(a -> path.tail().contains(a.pos));
		}

		Amphipod podAt(Point p) {
			return pods.find(a -> a.pos.equals(p)).get();
		}

		void play() {
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

			// candidates for moving into the hallway
			var tryPods = pods.filter(a -> a.pos.y() != 1 && !a.arrived);

			// move any one Pod out of the way (from room to hallway)
			for (var a : tryPods) {
				for (var d : a.hallwayDest(this::freePath)) {
					// move as many Pods as possible to their destinations
					var game = move(a, d, false);
					game = movePodsHome(game);
					if (game.goon())
						game.play();
				}
			}
		}

		void print() {
			pods.forEach(a -> empty.put(a.pos, a.color));
			empty.print();
			pods.forEach(a -> empty.put(a.pos, '.'));
			System.out.println(cost);
			System.out.println();
		}

		static Game movePodsHome(Game g) {

			boolean moved = true;
			do {
				moved = false;
				for (var a : g.pods.filter(x -> !x.arrived)) {
					var ptd = a.pathToDest(g::freePath);
					if (ptd.isDefined()) {
						Point dst = ptd.get().last();

						// rule: only move into a room if the room contains no other colors
						boolean canMove = (dst.y() == bottom);
						canMove |= g.pods.exists(x -> x.pos.equals(dst.south()) && x.arrived);
						if (canMove) {
							g = g.move(a, dst, true);
							moved = true;
						}
					}
				}
			} while (moved);
			return g;
		}

		Game move(Amphipod a, Point p, boolean locked) {
			return new Game(pods.replace(a, a.move(p, locked)), this);
		}

	}

	void solve() {

		// find out which amphipods are already in their final position ...
		var pods0 = map.findPoints(colors::contains).map(p -> new Amphipod(map.get(p), p, 0, false));

		bottom = pods0.map(p -> p.pos.y()).max().get();
		var south = pods0.filter(p -> p.pos.y() >= 2 && p.pos.y() <= bottom).toMap(p -> p,
				p -> pods0.find(s -> p.pos.south().equals(s.pos)).getOrElse((Amphipod) null));

		var home = pods0.filter(a -> a.inHomeRoom()).toArray();
		var arrived5 = home.filter(a -> a.pos.y() == bottom).toSet();
		var arrived4 = arrived5.addAll(home.filter(a -> a.pos.y() == 4 && arrived5.contains(south.get(a).get())));
		var arrived3 = arrived4.addAll(home.filter(a -> a.pos.y() == 3 && arrived4.contains(south.get(a).get())));
		var arrived2 = arrived3.addAll(home.filter(a -> a.pos.y() == 2 && arrived3.contains(south.get(a).get())));

		var pods = pods0.map(a -> new Amphipod(a.color, a.pos, 0, arrived2.contains(a)));
//		var pods = pods0;

		Game.bestCost = Integer.MAX_VALUE;		
		var game = new Game(pods, null);
		game = Game.movePodsHome(game);
		game.play();

		System.out.println("=== best game");
		var states = Stream.iterate(Game.bestGame, g -> g.prev).takeUntil(g -> g == null).reverse();
		states.forEach(s -> s.print());
	}

	public static void main(String[] args) {
		// part 1: 13455
		System.out.println("=== part 1");
		timed(() -> new Day23(false).solve());

		// part 2: 43567 (takes ca 3 minutes)
		System.out.println("=== part 2");
		timed(() -> new Day23(true).solve());
	}

	static String example1 = """
			#############
			#...........#
			###B#C#B#D###
			###D#C#B#A###
			###D#B#A#C###
			###A#D#C#A###
			#############
									""";

	static String input = """
			#############
			#...........#
			###A#D#A#B###
			###D#C#B#A###
			###D#B#A#C###
			###B#C#D#C###
			#############
									""";

	static String goalMap = """
			#############
			#...........#
			###A#B#C#D###
			###A#B#C#D###
			###A#B#C#D###
			###A#B#C#D###
			#############
									""";

	static String emptyMap = """
			#############
			#...........#
			###.#.#.#.###
			###.#.#.#.###
			###.#.#.#.###
			###.#.#.#.###
			#############
									""";

}
