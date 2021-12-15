package common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import io.vavr.Tuple2;
import io.vavr.collection.List;

public class PointMap<T> extends HashMap<Point, T> {

	private static final long serialVersionUID = 1L;

	public Function<Point, List<Point>> neigbors = p -> p.neighbors();
	public BiFunction<Point, Point, Integer> cost = (a,b) -> 1;

	public static class PathResult {
		public PointMap<Integer> distance;
		public PointMap<Point> predecessor;

		public List<Point> path(Point start, Point target) {
			List<Point> r = List.of(target);
			while (!r.head().equals(start)) {
				r = r.prepend(predecessor.get(r.head()));
			}
			return r;
		}
	}

	public PointMap() {
		super();
	}

	public PointMap(int icap) {
		super(icap);
	}

	// f: translate characters to value type T
	public void read(String[] s, Function<Character, T> f) {
		for (int y = 0; y < s.length; ++y)
			for (int x = 0; x < s[y].length(); ++x) {
				put(new Point(x, y), f.apply(s[y].charAt(x)));
			}
	}

	public void read(List<String> lines, Function<Character, T> f) {
		read(lines.toJavaArray(String[]::new), f);
	}

	@SuppressWarnings("unchecked")
	public void read(List<String> lines) {
		read(lines, c -> (T) c);
	}

	public long countValues(Predicate<T> p) {
		return values().stream().filter(p).count();
	}

	public long countValues(T value) {
		return values().stream().filter(v -> v.equals(value)).count();
	}

	public Point findPoint(T value) {
		for (var e : entrySet()) {
			if (e.getValue().equals(value))
				return e.getKey();
		}
		return null;
	}

	public List<Point> findPoints(T value) {
		return findPoints(x -> value.equals(x));
	}

	public List<Point> findPoints(Predicate<T> filter) {
		List<Point> r = List.empty();
		for (var e : entrySet()) {
			if (filter.test(e.getValue()))
				r = r.append(e.getKey());
		}
		return r;
	}

	public BoundingBox boundingBox() {
		return BoundingBox.of(keySet());
	}

	public void print() {
		boundingBox().print(p -> containsKey(p) ? String.valueOf(get(p)).charAt(0) : '·');
	}

	public void print(Function<T, Character> val2Text) {
		// boundingBox().print(p -> val2Text.apply(get(p)));
		boundingBox().print(p -> containsKey(p) ? val2Text.apply(get(p)) : '·');
	}

	public void printS(Function<T, String> val2Text) {
		boundingBox().printS(p -> val2Text.apply(getOrDefault(p, null)));
	}

	public void shiftToOrigin() {
		var bb = boundingBox();
		Function<Point, Point> shift = p -> new Point(p.x() - bb.xMin(), p.y() - bb.yMin());
		transformPoints(shift);
	}

	public void transformPoints(Function<Point, Point> f) {
		var newEntries = List.ofAll(entrySet()).map(e -> new Tuple2<Point, T>(f.apply(e.getKey()), e.getValue()));
		clear();
		newEntries.forEach(t -> put(t._1, t._2));
	}

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed) {
		return minDistances(start, allowed, x -> true);
	}

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed, Predicate<T> via) {
		return dijkstraAll(start, allowed, via).distance;
	}

	// calc paths and distances in one go
	public PathResult dijkstraAll(Point start, Predicate<T> allowed, Predicate<T> via) {
		final int INF = Integer.MAX_VALUE / 2;
		var points = this.keySet();

		var result = new PathResult();
		var dist = result.distance = new PointMap<>(size());
		var pred = result.predecessor = new PointMap<>(size());

		dist.put(start, 0);
		pred.put(start, null);

		boolean better = true;
		while (better) {
			better = false;
			for (var current : points) {
				if (!allowed.test(get(current)))
					continue;

				// best neighbor distance?
				var bestNeighbor = neigbors.apply(current)
						.filter(x -> via.test(get(x)))
						.minBy(n -> dist.getOrDefault(n, INF));

				if (bestNeighbor.isEmpty())
					continue;

				var successor = bestNeighbor.get();
				int dn = dist.getOrDefault(successor, INF);
				int dp = dist.getOrDefault(current, INF);
				int c = cost.apply(current, successor);
				if (dn + c < dp) {
					better = true;
					dist.put(current, dn + c);
					pred.put(current, successor);
				}
			}
		}

		return result;
	}


	public PathResult astar(Point start, Point target, Predicate<T> allowed) {
		final int INF = Integer.MAX_VALUE / 2;
		var closed = new HashSet<Point>();

		var result = new PathResult();
		var dist = result.distance = new PointMap<>(size());
		var pred = result.predecessor = new PointMap<>(size());
		dist.put(start, 0);
		pred.put(start, null);

		Function<Point, Integer> h = target::manhattan;
		Map<Point, Integer> priority = new HashMap<>();
		Comparator<Point> prioComp = Comparator.comparingInt(p -> priority.getOrDefault(p, INF));

		var openlist = new PriorityQueue<Point>(prioComp);
		BiConsumer<Point, Integer> enqueue = (p, v) -> {
			priority.put(p, v);
			if (!openlist.contains(p))
				openlist.add(p);
		};

		enqueue.accept(start, 0);

		while (!openlist.isEmpty()) {
			// Knoten mit dem geringsten f-Wert aus der Open List entfernen
			var currentNode = openlist.poll();

			// Wurde das Ziel gefunden?
			if (currentNode.equals(target))
				return result;

			// Der aktuelle Knoten soll durch nachfolgende Funktionen
			// nicht weiter untersucht werden, damit keine Zyklen entstehen
			closed.add(currentNode);

			// expandNode(currentNode)
			for (var successor : neigbors.apply(currentNode)) {
				if (!allowed.test(get(successor)))
					continue;

				if (closed.contains(successor))
					continue;

				// g-Wert für den neuen Weg berechnen: g-Wert des Vorgängers plus
				// die Kosten der gerade benutzten Kante
				// tentative_g = g(currentNode) + c(currentNode, successor)
				var tg = dist.getOrDefault(currentNode, INF) + cost.apply(currentNode, successor);

				// wenn der Nachfolgeknoten bereits auf der Open List ist,
				// aber der neue Weg nicht besser ist als der alte – tue nichts
				if (openlist.contains(successor) && tg >= dist.getOrDefault(successor, INF))
					continue;

				// Vorgängerzeiger setzen und g Wert merken oder anpassen
				pred.put(successor, currentNode);
				dist.put(successor, tg);

				// f-Wert des Knotens in der Open List aktualisieren
				// bzw. Knoten mit f-Wert in die Open List einfügen
				var f = tg + h.apply(successor);
				enqueue.accept(successor, f);
			}
		}

		// die Open List ist leer, es existiert kein Pfad zum Ziel
		return null;
	}

}
