package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.awt.Rectangle;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * lights in the sky
 * https://adventofcode.com/2018/day/10
 *
 */
public class Day10 {

	static class Dot {
		public Dot(String s) {
			px = Integer.parseInt(s.substring(s.indexOf("<")+1, s.indexOf(",")).trim());
			py = Integer.parseInt(s.substring(s.indexOf(",")+1, s.indexOf(">")).trim());
			vx = Integer.parseInt(s.substring(s.lastIndexOf("<")+1, s.lastIndexOf(",")).trim());
			vy = Integer.parseInt(s.substring(s.lastIndexOf(",")+1, s.lastIndexOf(">")).trim());
		}
		
		public Dot(int i, int j) {
			px = i;
			py = j;
		}

		public Dot pos(int t) {
			return new Dot(px+t*vx, py+t*vy);
		}

		int px, py, vx, vy;
	}

	private static List<Dot> dots;
	
	static Rectangle bb(int t) {
		List<Dot> img = seq(dots).map(d -> d.pos(t)).toList();
		int x0 = seq(img).min(d -> d.px).get();
		int y0 = seq(img).min(d -> d.py).get();
		int x1 = seq(img).max(d -> d.px).get();
		int y1 = seq(img).max(d -> d.py).get();
		return new Rectangle(x0, y0, x1-x0, y1-y0);
	}
	
	static long area(Rectangle r) {
		return (long)r.width * (long)r.height;
	}
	
	static void print(int t) {
		List<Dot> img = seq(dots).map(d -> d.pos(t)).toList();		
		Rectangle r = bb(t);
		
		IntStream.rangeClosed(r.y, r.y+r.height)
		.forEach(y -> {
			IntStream.rangeClosed(r.x, r.x+r.width)
			.forEach(x -> {
				boolean q = seq(img).anyMatch(d -> d.px==x && d.py==y);				
				System.out.print(q ? "#" : ".");
			});	
			System.out.println();
		});
		
	}
	
	public static void main(String[] args) throws Exception {
		dots = Util.lines("aoc2018/day10.txt").stream().map(s -> new Dot(s)).collect(Collectors.toList());
		
		Comparator<Integer> cbb = (i, j) -> {
			long a1 = area(bb(i));
			long a2 = area(bb(j));
			return (a1 < a2) ? -1 : 1;
		};
		
		Integer t0 = seq(IntStream.range(0, 100000)).min(cbb).get();
		System.out.println(t0);
		System.out.println(bb(t0));
//		System.out.println(area(bb(t0)));
		print(t0);
		
	}


}
