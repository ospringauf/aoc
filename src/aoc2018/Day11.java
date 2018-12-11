package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.rangeClosed;

import java.util.Optional;
import java.util.function.Function;

import org.jooq.lambda.Seq;

/**
 * max sub-matrix sum
 * https://adventofcode.com/2018/day/11
 *
 */
public class Day11 {

	static class Pos {
		
		public Pos(Integer x2, Integer y2) {
			x = x2;
			y = y2;
		}
		
		public Pos(Integer x2, Integer y2, Integer n2) {
			x = x2;
			y = y2;
			n = n2;
		}

		int x, y, n;
		
		@Override
		public String toString() {
			return "" + x + "," + y + "," + n;
		}
	}
	
	static long start = System.currentTimeMillis();

	// single cell value
	static int power(int x, int y, int serial) {
		return ((((((x+10) * y) + serial) * (x+10)) / 100) % 10) - 5; 
	}
	
	// NxN cell sum
	static int powerN(int x, int y, int s, int n) { 
		return range(x, x+n).sum(px -> range(y, y+n).sum(py -> power(px, py, s)).get()).get();
				
	}
	
	public static void main(String[] args) throws Exception {
		part1(1723);		
		part2(1723);
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}

	protected static void part1(int serial) {
//		System.out.println(power(122,79, 57));
//		System.out.println(power(217,196, 39));
//		System.out.println(power(101,153, 71));
		
		Optional<Pos> r = range(1, 300-2)
			.flatMap(x -> range(1, 300-2).map(y -> new Pos(x,y)))
			.maxBy(p -> powerN(p.x, p.y, serial, 3));
		
		System.out.println("=== part 1 ===");
		System.out.println(r);
	}


	protected static void part2naive(int serial) {
		Function<Pos, Integer> powN = (p) -> powerN(p.x, p.y, serial, p.n);

		Optional<Pos> r = Seq.rangeClosed(1, 300)
				.peek(n -> System.out.println(n))
				.flatMap(n -> rangeClosed(1, 300-n)
				.flatMap(x -> rangeClosed(1, 300-n).map(y -> new Pos(x, y, n))))
				.maxBy(powN);
			
		System.out.println(r);
	}
	

	static Pos maxp = new Pos(0,0);
	static int max = 0;

	protected static void part2(int serial) {
		// init: 1x1 grid values
		final int[][] grid = new int[301][301];
		rangeClosed(1, 300).forEach(x -> rangeClosed(1, 300).forEach(y -> grid[x][y] = power(x, y, serial)));
		
		// larger sub-grids n=2..300
		rangeClosed(2, 300).forEach(n -> { // n
			System.out.println(n);
			rangeClosed(1, 300-n+1).forEach(x -> // x  
				rangeClosed(1, 300-n+1).forEach(y -> // y
				{
					// expand sub-grid in x and y direction
					grid[x][y] +=  
							range(x, x+n-1).sum(dx -> power(dx, y+n-1, serial)).get() +
							range(y, y+n-1).sum(dy -> power(x+n-1, dy, serial)).get() +
							power(x+n-1, y+n-1, serial);
					
					if (grid[x][y] > max) {
						maxp = new Pos(x, y, n);
						max = grid[x][y];
//						System.out.println(maxp);
					}
				}));
		});
		System.out.println("=== part 2 ===");
		System.out.println(maxp);
		
	}

}
