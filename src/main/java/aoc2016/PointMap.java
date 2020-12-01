package aoc2016;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import io.vavr.collection.List;

public class PointMap<T> extends HashMap<Point, T> {

	private static final long serialVersionUID = 1L;
	
	//Function<Point, Stream<Point>> neigbors = p -> p.neighbors();
	Function<Point, List<Point>> neigbors = p -> p.neighbors();

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
		return findPoints(value).getOrElse((Point)null);
	}

	public List<Point> findPoints(T value) {
		return List.ofAll(keySet()).filter(p -> get(p).equals(value));
	}

	public List<Point> findPoints(Predicate<T> filter) {
		return List.ofAll(keySet()).filter(p -> filter.test(get(p)));
	}

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed) {
		return minDistances(start, allowed, x -> true);
	}
	

	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed, Predicate<T> via) {
		return calcPaths(start, allowed, via).distance;
	}

	// calc paths and distances in one go
	public PathResult calcPaths(Point start, Predicate<T> allowed, Predicate<T> via) {
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
				var bestNeighbor =
						neigbors.apply(p)
						.filter(x -> via.test(get(x)))
						.minBy(n -> dist.getOrDefault(n, INF));

				if (bestNeighbor.isEmpty()) continue;
				
				int dn = dist.getOrDefault(bestNeighbor.get(), INF);
				int dp = dist.getOrDefault(p, INF);
				if (dn + 1 < dp) {
					better = true;
					dist.put(p, dn + 1);
					pred.put(p, bestNeighbor.get());
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
