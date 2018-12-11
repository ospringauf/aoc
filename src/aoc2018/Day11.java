package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.rangeClosed;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.jooq.lambda.Seq;

/**
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
	

	static int power(int x, int y, int serial) {
		return ((((((x+10) * y) + serial) * (x+10)) / 100) % 10) - 5; 
	}
	
	static int powerN(int x, int y, int s, int n) { 
		return range(x, x+n).sum(px -> range(y, y+n).sum(py -> power(px, py, s)).get()).get();
				
	}
	
	public static void main(String[] args) throws Exception {
		part1();
		
		part2();
	}

	protected static void part1() {
		int ser = 1723;
		
//		System.out.println(power(122,79, 57));
//		System.out.println(power(217,196, 39));
//		System.out.println(power(101,153, 71));
		
		Optional<Pos> r = range(1, 300-2)
			.flatMap(x -> range(1, 300-2).map(y -> new Pos(x,y)))
			.maxBy(p -> powerN(p.x, p.y, ser, 3));
		
		System.out.println(r);
	}


	protected static void part2() {
		final int ser = 1723;
		
		int[][] grid = new int[301][301];
		rangeClosed(1, 300).forEach(x -> rangeClosed(1, 300).forEach(y -> grid[x][y] = power(x,y,ser)));
		
		Function<Pos, Integer> powN =
			(p) -> range(p.x, p.x+p.n).sum(px -> range(p.y, p.y+p.n).sum(py -> grid[px][py]).get()).get();

		Optional<Pos> r = Seq.rangeClosed(1, 300)
				.peek(n -> System.out.println(n))
				.flatMap(n -> rangeClosed(1, 300-n)
				.flatMap(x -> rangeClosed(1, 300-n).map(y -> new Pos(x,y, n))))
				.maxBy(powN);
			
		System.out.println(r);
	}
	

//	protected static void part2b() {
//		final int ser = 1723;
//		
//		int[][] grid = new int[301][301];
//		rangeClosed(1, 300).forEach(x -> rangeClosed(1, 300).forEach(y -> grid[x][y] = power(x,y,ser)));
//		
//		Map<Integer, Pos> maxp = new HashMap<>();
//		
//		rangeClosed(2, 300).forEach(n -> {
//			final int[][] last = grid;
//			rangeClosed(1, 300-n).forEach(x -> rangeClosed(1, 300-n).forEach(y -> 
//				{
//					grid[x][y] = last[x][y] + 
//							range(x,x+n).sum(dx -> power(dx,y,ser)).get() +
//							range(y,y+n).sum(dy -> power(x,dy,ser)).get() +
//							power(x+n,y+n,ser);
//				}));
//			Pos p0 = rangeClosed(1, 300-n).flatMap(x -> rangeClosed(1, 300-n).map(y -> new Pos(x,y,n)))
//			.maxBy(p -> grid[p.x][p.y])
//			.get();
//			System.out.println(p0 + " - " + grid[p0.x][p0.y]);
//		});
//		
//	}

}
