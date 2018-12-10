package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.awt.Rectangle;
import java.util.List;
import java.util.stream.IntStream;

/**
 * lights in the sky
 * https://adventofcode.com/2018/day/10
 *
 */
public class Day10 {

	static class Dot {
        int px, py, vx, vy;

        public Dot(String s) {
			px = Integer.parseInt(s.substring(s.indexOf('<')+1, s.indexOf(',')).trim());
			py = Integer.parseInt(s.substring(s.indexOf(',')+1, s.indexOf('>')).trim());
			vx = Integer.parseInt(s.substring(s.lastIndexOf('<')+1, s.lastIndexOf(',')).trim());
			vy = Integer.parseInt(s.substring(s.lastIndexOf(',')+1, s.lastIndexOf('>')).trim());
		}
		
		public Dot(int i, int j) {
			px = i;
			py = j;
		}

		public Dot pos(int t) {
			return new Dot(px+t*vx, py+t*vy);
		}
	}

	static List<Dot> dots;
	static long start = System.currentTimeMillis();
	
	static Rectangle bbox(int t) {
		List<Dot> img = seq(dots).map(d -> d.pos(t)).toList();
		Rectangle r = new Rectangle(img.get(0).px, img.get(0).py, 1, 1);
		seq(img).forEach(d -> r.add(d.px, d.py));
		return r;
	}
	
	static long area(Rectangle r) {
		return (long)r.width * (long)r.height;
	}
	
	static void printSky(int t) {
		List<Dot> img = seq(dots).map(d -> d.pos(t)).toList();		
		Rectangle r = bbox(t);
		
		IntStream.rangeClosed(r.y, r.y + r.height)
		.forEach(y -> {
			IntStream.rangeClosed(r.x, r.x + r.width)
			.forEach(x -> {
				boolean set = seq(img).anyMatch(d -> d.px==x && d.py==y);				
				System.out.print(set ? "#" : ".");
			});	
			System.out.println();
		});
	}
	
	public static void main(String[] args) throws Exception {
	    
		dots = seq(Util.lines("aoc2018/day10.txt")).map(Dot::new).toList();

		Integer t0 = seq(IntStream.range(0, 1000000)).limitWhile(i -> area(bbox(i)) <= area(bbox(i-1))).findLast().orElse(0);
//		Integer t0 = seq(IntStream.range(0, 100000)).minBy(i -> area(bbox(i))).orElse(0);
		System.out.println(t0);
		System.out.println(bbox(t0));
		printSky(t0);
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}
}
