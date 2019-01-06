package aoc2018;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Day23 {

	static class Bot {
		int x, y, z, r;
		
		@Override
		public String toString() {
			return String.format("(%d,%d,%d).%d", x, y, z, r);
		}
		
		int dist(Bot b) {
			return Math.abs(x-b.x)+Math.abs(y-b.y)+Math.abs(z-b.z);
		}

		int dist(int x0) {
			return Math.abs(x-x0);
		}
		
		int dist(int x0, int y0) {
			return Math.abs(x-x0) + Math.abs(y-y0);
		}
		
		int dist(int x0, int y0, int z0) {
			return Math.abs(x-x0) + Math.abs(y-y0) + Math.abs(z-z0);
		}
		
		boolean inRange(int x0, int y0, int z0) {
			return dist(x0, y0, z0) <= r;
		}
		
		boolean intersect(Bot b) {
			return dist(b) <= (r + b.r);
		}

		static Bot parse(String s) {
			String[] f = s.split("[^\\-0-9]+");
			Bot b = new Bot();
			b.x = Integer.parseInt(f[1]);
			b.y = Integer.parseInt(f[2]);
			b.z = Integer.parseInt(f[3]);
			b.r = Integer.parseInt(f[4]);
			return b;
		}
	}

	private static List<Bot> bots;
	
	public static void main(String[] args) throws Exception {
		bots = seq(Util.lines("aoc2018/day23.txt")).map(s -> Bot.parse(s)).toList();
		
//		part1();
		
		part2();
		System.out.println("end");
	}
	
	static int botsInRange(int x) {
		int c = 0;
		for (Bot bot : bots) {
			if (bot.dist(x) <= bot.r) c++;
		}
		return c;
	}

//	private static void part2() {
//		int xmin = seq(bots).min(b -> b.x).get();
//		int xmax = seq(bots).max(b -> b.x).get();
//		
////		rangeClosed(xmin, xmax).forEach(x -> {
////			long ir = seq(bots).count(b -> b.r>=b.dist(x));
////			System.out.println("x=" + x + " / " + ir);
////		});
////		
//		int hix =0;
//		int hi = 0;
//		for (int x0=13873165; x0<=xmax; ++x0) {
//			int ir = botsInRange(x0);
//			if (ir >= hi) {
//				hi = ir;
//				hix = x0;
//				System.out.println(".. x=" + x0 + " ir=" + ir);
//			}
//		}
//		System.out.println("hi x "  + hix);
//		// .. x=13873165 ir=985
//		
//	}
	
	static void part2() {
		List<Bot> todo = new ArrayList<Bot>(bots);
		List<Bot> clique = new ArrayList<Bot>();
		
		while (!todo.isEmpty()) {
			//Bot bmax = seq(todo).maxBy(b -> b.r).get();
			//Bot bmax = todo.get(0);
			Bot bmax = seq(todo).minBy(b -> b.r).get();
			if (clique.isEmpty() || seq(clique).allMatch(c -> c.intersect(bmax))) {
				clique.add(bmax);
			}
			todo.remove(bmax);
		}

//		todo = new ArrayList<Bot>(bots);
//		while (!todo.isEmpty()) {
//			//Bot bmax = seq(todo).maxBy(b -> b.r).get();
//			
//			if (clique.contains(bmax)) {
//				todo.remove(bmax);
//				continue;
//			}
//
//			if (clique.isEmpty() || seq(clique).allMatch(c -> c.intersect(bmax))) {
//				clique.add(bmax);
//			}
//			todo.remove(bmax);
//		}

		for (int i=0; i<clique.size(); ++i) {
			for (int j=0; j<clique.size(); ++j) {
				if (!clique.get(i).intersect(clique.get(j))) {
					System.out.println("no overlap " + i + "/" + j);
				}
			}
		}
		
//		clique.forEach(c -> System.out.println(c));
		System.out.println("clique size: " + clique.size());
		
		int x1 = seq(clique).max(c -> c.x - c.r).get();
		int x2 = seq(clique).min(c -> c.x + c.r).get();
		System.out.println("x-range " + x1 + " ... " + x2);
		int y1 = seq(clique).max(c -> c.y - c.r).get();
		int y2 = seq(clique).min(c -> c.y + c.r).get();
		System.out.println("y-range " + y1 + " ... " + y2);
		int z1 = seq(clique).max(c -> c.z - c.r).get();
		int z2 = seq(clique).min(c -> c.z + c.r).get();
		System.out.println("z-range " + z1 + " ... " + z2);
		
//		clique size: 975
//		x-range 13873165 ... 19676839
//		y-range 22217614 ... 34152347
//		z-range 48378141 ... 59774048
		
//		int x = x1 + (x2-x1)/2;
//		int y = y1 + (y2-y1)/2;
//		int z = z1 + (z2-z1)/2;

//		Optional<Integer> q = rangeClosed(z1, z2).findFirst(z -> seq(clique).allMatch(c -> c.inRange(x, y, z)));
//		Optional<Integer> q = rangeClosed(y1, y2).findFirst(y -> seq(clique).allMatch(c -> c.inRange(x, y, z)));
//		Optional<Integer> q = rangeClosed(x1, x2).findFirst(x -> seq(clique).allMatch(c -> c.inRange(x, y, z)));
//		System.out.println(q);
		
//		Optional<Bot> b0 = seq(bots).findFirst(b -> seq(clique).allMatch(c -> c.inRange(b.x, b.y, b.z)));
//		System.out.println(b0);
		
//		System.out.println(seq(clique).minBy(c -> c.r));
		
		for (int i=0; i<100; ++i) {
			int x = (int) (x1 + (Math.random() * (double)(1+x2-x1)));
			int y = (int) (y1 + (Math.random() * (double)(1+y2-y1)));
			int z = (int) (z1 + (Math.random() * (double)(1+z2-z1)));
			
			if (seq(clique).allMatch(c -> c.inRange(x, y, z))) {
				System.out.println("found: " + x + ", " + y + ", " + z); 
			}
		}
		
		
		
		
		int d0 = seq(clique).max(b -> b.dist(0,0,0) - b.r).get();
		List<Bot> c0 = seq(clique).filter(b -> b.dist(0,0,0)-b.r == d0).toList();
		System.out.println(c0);
	}

	private static void part1() {
		Bot bmax = seq(bots).maxBy(b -> b.r).get();
		System.out.println("strongest: " + bmax);
		long inrange = seq(bots).filter(b -> b.dist(bmax) <= bmax.r).count();
		System.out.println("in range: " + inrange);
//		bots.forEach(b-> System.out.println(b + " dist " + b.dist(bmax) + " in range: " + (bmax.r >= b.dist(bmax))));
	}
}
