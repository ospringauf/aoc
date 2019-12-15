package aoc2019;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class PointMap<T> extends HashMap<Point, T> {

	private static final long serialVersionUID = 1L;

	// f: translate characters to value type T
	void read(String[] s, Function<Character, T> f) {
		for (int y = 0; y < s.length; ++y)
			for (int x = 0; x < s[y].length(); ++x) {
				put(new Point(x, y), f.apply(s[y].charAt(x)));
			}

	}

	public Point findPoint(T value) {
		return keySet().stream().filter(p -> get(p) == value).findFirst().get();
	}
	
	
	public PointMap<Integer> minDistances(Point start, Predicate<T> allowed) {
		final int INF = Integer.MAX_VALUE -1;
		PointMap<Integer> dist = new PointMap<>();
		dist.put(start, 0);
		
		boolean better = true;
		while (better) {
			better = false;
			for (var p : keySet()) {
				if ( ! allowed.test(get(p))) continue;
				// best neighbor distance?
				int dx = p.neighbors()
						.mapToInt(x -> dist.getOrDefault(x, INF))
						.min()
						.getAsInt();
				int dp = dist.getOrDefault(p, INF);
				if (dx + 1 < dp) {
					better = true;
					dist.put(p, dx+1);
				}
			}
		}
		
		return dist;
	}

	public BoundingBox boundingBox() {
		return BoundingBox.of(keySet());
	}
}
