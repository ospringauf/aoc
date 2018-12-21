package aoc2018;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.seq;

import java.util.List;

/**
 * falling/spreading water in sand or clay
 * https://adventofcode.com/2018/day/17
 */

public class Day17 {

	static class Clay {
		int xfrom, xto, yfrom, yto;
		
		static Clay parse(String s) {
			String[] f = s.split(", ");
			int v0 = Integer.parseInt(f[0].substring(2));
			String[] g = f[1].substring(2).split("\\.\\.");
			int v1 = Integer.parseInt(g[0]);
			int v2 = Integer.parseInt(g[1]);
			
			Clay c = new Clay();
			if (s.startsWith("x")) {
				c.xfrom = v0; 
				c.xto = v0;
				c.yfrom = v1;
				c.yto = v2;
			} else {
				c.yfrom = v0; 
				c.yto = v0;
				c.xfrom = v1;
				c.xto = v2;
			}
			return c;
		}

		public void insertToGrid() {
			rangeClosed(xfrom,xto).forEach(x -> rangeClosed(yfrom, yto).forEach(y -> grid[x][y] = '#'));
		}
	}

	private static char[][] grid;
	private static Integer xmin;
	private static Integer xmax;
	private static Integer ymin;
	private static Integer ymax;
	
	static void printGrid() {
		rangeClosed(ymin, ymax).forEach(y -> {
			System.out.println(rangeClosed(xmin, xmax).map(x -> grid[x][y]));	
		});
		System.out.println();
	}
	
	public static void main(String[] args) throws Exception {
		List<Clay> clays = seq(Util.lines("aoc2018/day17-test.txt")).map(x -> Clay.parse(x)).toList();
		
		xmin = seq(clays).min(c -> c.xfrom).get()-1;
		xmax = seq(clays).max(c -> c.xto).get()+1; 
		ymin = seq(clays).min(c -> c.yfrom).get();
		ymax = seq(clays).max(c -> c.yto).get();
		
		grid = new char[xmax+1][ymax+1];
		rangeClosed(ymin, ymax).forEach(y -> rangeClosed(xmin, xmax).forEach(x -> grid[x][y] = '.'));
		
		seq(clays).forEach(c -> c.insertToGrid());
		
		printGrid();
		
		// drop from spring
		drop(500, 0);
		
		printGrid();
		
		Long result1 = rangeClosed(ymin, ymax).sum(y -> rangeClosed(xmin, xmax).count(x -> grid[x][y]=='|' || grid[x][y]=='~')).get();
		System.out.println(result1);

		Long result2 = rangeClosed(ymin, ymax).sum(y -> rangeClosed(xmin, xmax).count(x -> grid[x][y]=='~')).get();
		System.out.println(result2);
	}

	// drop vertically
	// false = water blocked, true = water keeps flowing
	private static boolean drop(int x, int y) {
		if (y > ymax) return true;
		if (grid[x][y] == '#' || grid[x][y] == '~') return false;
		
		grid[x][y] = '|';
		printGrid();
		if (drop(x, y+1)) {
			return true;
		} else {
			boolean left = spread(x-1, y, -1);
			boolean right = spread(x+1, y, +1);
			
			if (!left && !right) {
				turnToWater(x,y);
			}
			
			return left || right;
		}
	}

	// spread horizontally in given direction (left/right)
	// false = water blocked, true = water keeps flowing
	private static boolean spread(int x, int y, int dir) {
		if (grid[x][y] == '#') 
			// stopped by a wall
			return false;
		else {
			// drop down or continue to spread 
			grid[x][y] = '|';
			printGrid();
			return drop(x,y+1) || spread(x+dir, y, dir);
		}
	}

	private static void turnToWater(int x0, int y) {
		grid[x0][y] = '~';
		int x = x0-1;
		while (grid[x][y]=='|') {
			grid[x--][y]='~';
		}
		x = x0+1;
		while (grid[x][y]=='|') {
			grid[x++][y]='~';
		}
		printGrid();		
	}

}
