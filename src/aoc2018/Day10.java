package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.seq;

import java.awt.Rectangle;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * lights in the sky
 * https://adventofcode.com/2018/day/10
 *
 */
public class Day10 {

	static class Dot {
        int px, py, vx, vy;

        static Dot parse(String s) {
        	Dot d = new Dot();
        	Matcher m = Pattern.compile("[\\-0-9]+").matcher(s);
        	m.find();
        	
        	d.px = Integer.parseInt(m.group());
        	m.find();
        	d.py = Integer.parseInt(m.group());
        	m.find();
        	d.vx = Integer.parseInt(m.group());
        	m.find();
        	d.vy = Integer.parseInt(m.group());
        	return d;
		}
		
		public Dot(int i, int j) {
			px = i;
			py = j;
		}

		private Dot() {
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
        
        // is there a dot at (x,y)?
        BiPredicate<Integer, Integer> isSet = (x,y) -> seq(img).anyMatch(d -> d.px==x && d.py==y);
        
        String s = rangeClosed(r.y, r.y + r.height)
	        .map(y -> 
	        	// build one scanline
	            rangeClosed(r.x, r.x + r.width)
	                .map(x -> isSet.test(x,y) ? "█" : "·")
	                .toString() + "\n"
	        ).toString();
        
        System.out.println(s);
    }
	
	public static void main(String[] args) throws Exception {
	    
		dots = seq(Util.lines("aoc2018/day10.txt")).map(Dot::parse).toList();

		Integer t0 = range(0, 1000000).limitWhile(i -> area(bbox(i)) <= area(bbox(i-1))).findLast().orElse(0);
//		Integer t0 = seq(IntStream.range(0, 100000)).minBy(i -> area(bbox(i))).orElse(0);
		System.out.println(t0);
		System.out.println(bbox(t0));
		printSky(t0);
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}
}
