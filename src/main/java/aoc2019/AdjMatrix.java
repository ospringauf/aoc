package aoc2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AdjMatrix<T> {

	static final Integer INF = 99999999; // Integer.MAX_VALUE / 2;

	static class Pair<T> {
		T a;
		T b;

		public Pair(T a, T b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object obj) {
			Pair<T> p = (Pair<T>) obj;
			return p.a.equals(a) && p.b.equals(b);
		}

		@Override
		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}

		@Override
		public String toString() {
			return String.format("(%s,%s)", a.toString(), b.toString());
		}
	}

	Map<Pair<T>, Integer> mat = new HashMap<>();

	public AdjMatrix() {
	}

	public AdjMatrix(AdjMatrix<T> a) {
		mat = new HashMap<Pair<T>, Integer>(a.mat);
	}

	void put(T a, T b, Integer dist) {
		if (dist == null)
			return;
		if (dist >= INF)
			return;
		if (a.equals(b))
			return;
		mat.put(new Pair<T>(a, b), dist);
		mat.put(new Pair<T>(b, a), dist);
	}

	Integer get(T a, T b) {
		if (a.equals(b)) return 0;
		return mat.getOrDefault(new Pair<T>(a, b), INF);
	}

	@Override
	public String toString() {
		var b = new StringBuilder();
		for (var k : mat.keySet()) {
			b.append(String.format("%s = %d%n", k.toString(), mat.get(k)));
		}
		return b.toString();
	}

	public AdjMatrix<T> combine(AdjMatrix<T> inner, HashMap<T, T> warp) {
		var adj = new AdjMatrix<T>(this);
	
		boolean better = true;
		while (better) {
			better = false;
			
			var pairs = new HashSet<>(adj.mat.keySet());
			for (var p1 : pairs) {
				for (var p2 : pairs) {
					if (p1.equals(p2)) continue;
					var d1 = adj.get(p1.a, p1.b);
					var d12 = adj.get(p1.b, p2.a);
					var d2 = adj.get(p2.a, p2.b);
	
					var d = adj.get(p1.a, p2.b);
					
					if (d1 + d12 + d2 < d) {
						d = d1 + d12 + d2;
						adj.put(p1.a, p2.b, d);
						better = true;
					}
					var w1b = warp.get(p1.b);
					var w2a = warp.get(p2.a);
					if (w1b != null && w2a != null) {
						var di = inner.get(w1b, w2a) + 2;
						if (d1 + di + d2 < d) {
							adj.put(p1.a, p2.b, d1 + di + d2);
							better = true;
						}
					}
				}
			}
		}

		return adj;
	}

}
