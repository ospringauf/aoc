package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day25 {

	static class Point {
		int a,b,c,d;
		
		@Override
		public String toString() {
			return "(" + a +"," + b + "," + c + "," + d + ")";
		}
		
		int dist(Point p) {
			return Math.abs(a-p.a) + Math.abs(b-p.b) + Math.abs(c-p.c) + Math.abs(d-p.d);
		}
		
		boolean constellation(Point p) {
			return dist(p) <= 3;
		}
		
		static Point parse(String s) {
			String[] f = s.split(",");
			Point p = new Point();
			p.a = Integer.parseInt(f[0].trim());
			p.b = Integer.parseInt(f[1].trim());
			p.c = Integer.parseInt(f[2].trim());
			p.d = Integer.parseInt(f[3].trim());
			return p;
		}
	}

	private static List<Point> points;
	
	public static void main(String[] args) throws Exception {
		points = seq(Util.lines("aoc2018/day25.txt")).map(s -> Point.parse(s)).toList();
		
//		seq(points).forEach(System.out::println);
		part1();
	}

	private static void part1() {
		boolean merge = true;
		Map<Point, Set<Point>> c = new HashMap<>();
		seq(points).forEach(p -> { c.put(p, new HashSet<Point>()); c.get(p).add(p);});
		
		while (merge)
		{
			merge = false;
			for (int i=0; i<points.size(); ++i) {
				for (int j=i+1; j<points.size(); ++j) {
					if (i==j) continue;
					Point pi = points.get(i);
					Point pj = points.get(j);
					Set<Point> li = c.get(pi);

					if (pi.constellation(pj) && !li.contains(pj)) {
						li.addAll(c.get(pj));
						li.forEach(p -> c.put(p, li));
						merge = true;
					}
				}
			}
		}
		
		List<Set<Point>> d = seq(c.values()).distinct().toList();
//		seq(points).forEach(p -> System.out.println(c.get(p)));
		System.out.println(d.size());
		
	}
}
