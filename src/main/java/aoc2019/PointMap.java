package aoc2019;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PointMap<T> extends HashMap<Point, T> {

	private static final long serialVersionUID = 1L;

	public static class PathResult {
		public PointMap<Integer> distance;
		public PointMap<Point> predecessor;
	}

	public PointMap() {
		super();
	}

	public PointMap(int icap) {
		super(icap);
	}

	// f: translate characters to value type T
	void read(String[] s, Function<Character, T> f) {
		for (int y = 0; y < s.length; ++y)
			for (int x = 0; x < s[y].length(); ++x) {
				put(new Point(x, y), f.apply(s[y].charAt(x)));
			}

	}

	public Point findPoint(T value) {
		return findPoints(value).findFirst().orElse(null);
	}

	public Stream<Point> findPoints(T value) {
		return keySet().stream().filter(p -> get(p) == value);
	}

	public Stream<Point> findPoints(Predicate<T> filter) {
		return keySet().stream().filter(p -> filter.test(get(p)));
	}

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed) {
		return minDistances(start, allowed, x -> true);
	}
	

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed, Predicate<T> via) {
		return calPaths(start, allowed, via).distance;
	}

	// calc paths and distances in one go
	public PathResult calPaths(Point start, Predicate<T> allowed, Predicate<T> via) {
		final int INF = Integer.MAX_VALUE - 1;
		var points = this.keySet();

		var result = new PathResult();
		var dist = result.distance = new PointMap<>(points.size());
		var pred = result.predecessor = new PointMap<>(points.size());

		dist.put(start, 0);
		pred.put(start, null);

		boolean better = true;
		while (better) {
			better = false;
			for (var p : points) {
				if (!allowed.test(get(p)))
					continue;

				// best neighbor distance?
				Point bestNeighbor = p.neighbors()
						.filter(x -> via.test(get(x)))
						.min(Comparator.comparing(n -> dist.getOrDefault(n, INF)))
						.get();

				int dn = dist.getOrDefault(bestNeighbor, INF);
				int dp = dist.getOrDefault(p, INF);
				if (dn + 1 < dp) {
					better = true;
					dist.put(p, dn + 1);
					pred.put(p, bestNeighbor);
				}
			}
		}

		return result;
	}


	public BoundingBox boundingBox() {
		return BoundingBox.of(keySet());
	}

	void print() {
		boundingBox().print(p -> String.valueOf(get(p)).charAt(0));
	}

	void print(Function<T, Character> val2Text) {
		boundingBox().print(p -> val2Text.apply(get(p)));
	}

}
