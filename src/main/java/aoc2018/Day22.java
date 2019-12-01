package aoc2018;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.range;

/**
 * rock/wet/narrow labyrinth & shortest paths 
 * https://adventofcode.com/2018/day/22
 */

public class Day22 {

	static char[][] cave;
	static int[][] geo;
	
	// shortest paths for each gear
	static int[][] torch;
	static int[][] climb;
	static int[][] none;
	
//	static int tx=10, ty=10, depth=510, mx=tx+10, my=ty+10;
	static int tx=14, ty=778, depth=11541, mx=30, my=1000;
	
	static final int MAX = 1000000; // "infinite" distance
	static final char rock='.', wet='=', narrow='|';

	static long start = System.currentTimeMillis();

	
	public static void main(String[] args) {
		System.out.println("=== part 1 ===");
		geo = new int[mx+1][my+1];
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> geo[x][y] = geoIndex(x,y)));

		cave = new char[mx+1][my+1];
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> cave[x][y] = type(x,y)));
		
		printCave();

		Integer level = rangeClosed(0, ty).sum(y -> rangeClosed(0, tx).sumInt(x -> risk(x,y))).get();
		System.out.println(level);
		
		
		System.out.println("=== part 2 ===");
		torch = new int[mx+1][my+1];
		climb = new int[mx+1][my+1];
		none = new int[mx+1][my+1];
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> torch[x][y] = climb[x][y] = none[x][y] = MAX));
		torch[0][0] = 0; // start with torch at 0,0

		// M and T are "rock"
		cave[0][0] = '.';
		cave[tx][ty] = '.';
		
		calcPaths();
		printDist(torch);
		System.out.println(torch[tx][ty]); // min dist to reach T with the torch
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}

	static void calcPaths() {
		int best = MAX;
		int lastBest = MAX;
		while (best == MAX || best < lastBest) {
			lastBest = best;
			System.out.println("iterate");
			updateDistances();
			best = torch[tx][ty];
		}
	}
	
	static int min(int a, int b, int c, int d) {
		return Math.min(Math.min(a, b), Math.min(c, d)); 
	}
	
	static boolean update;
	
	static boolean updateDistances() {
		update = true;
		boolean improve = false;
		while (update) {
			update = false;
			range(0, my).forEach(y -> range(0, mx).forEach(x -> update |= updateCell(x, y)));
			improve |= update;
		}
		return improve;
	}
	
	// try to improve the distance at x,y by looking at the adjacent cells 
	// or consider changing gear here
	private static boolean updateCell(int x, int y) {
		char t = cave[x][y];
		boolean improve = false;
		switch (t) {
			case rock: // rock (.) - torch / climb
				// keep torch
				improve |= walkFromAdjacent(torch, x, y);
				// keep climb
				improve |= walkFromAdjacent(climb, x, y);
				// change torch->climb
				improve |= changeGear(torch, climb, x, y);
				// change climb->torch
				improve |= changeGear(climb, torch, x, y);
				break;
			case wet: // wet (=) - climb / none
				// keep none or climb
				improve |= walkFromAdjacent(none, x, y);
				improve |= walkFromAdjacent(climb, x, y);
				// change none<->climb
				improve |= changeGear(none, climb, x, y);
				improve |= changeGear(climb, none, x, y);
				break;
			case narrow: // narrow (|) - torch / none
				// keep none or torch
				improve |= walkFromAdjacent(none, x, y);
				improve |= walkFromAdjacent(torch, x, y);
				// change none<->torch
				improve |= changeGear(none, torch, x, y);
				improve |= changeGear(torch, none, x, y);
				break;
		}
		return improve;
	}

	// walk into cell from "cheapest" adjacent cell without changing gear
	// return true if cell value has improved
	private static boolean walkFromAdjacent(int[][] dist, int x, int y) {
		int u = (y == 0) ? MAX : dist[x][y-1];
		int l = (x == 0) ? MAX : dist[x-1][y];
		int d = dist[x][y+1];
		int r = dist[x+1][y];
		
		int best = min(u,l,r,d) + 1;
		if (best < dist[x][y]) {
			dist[x][y] = best;
			return true;
		}
		return false;
	}

	// change gear at this cell (only if allowed by terrain)
	// return true if cell value for new gear has improved
	private static boolean changeGear(int[][] oldGear, int[][] newGear, int x, int y) {
		if (newGear[x][y] > oldGear[x][y] + 7) {
			newGear[x][y] = oldGear[x][y] + 7;
			return true;
		}
		return false;
	}

	
	static void printDist(int[][] len) {
		rangeClosed(0, my).forEach(y -> System.out.println(rangeClosed(0, mx).map(x ->  {
			int l = len[x][y];
			return (l == MAX ? "    -" : String.format("%5d", l));
		})));
		System.out.println();
	}

	static char type(int x, int y) {
		if (x==0 && y==0) return 'M';
		if (x==tx && y==ty) return 'T';
		switch (erosion(x, y) % 3) {
			case 0: return rock; // rocky
			case 1: return wet; // wet
			case 2: return narrow; // narrow
		}
		return '?';
	}
	
	static int risk(int x, int y) {
		if (x==0 && y==0) return 0;
		if (x==tx && y==ty) return 0;
		return erosion(x, y) % 3;
	}
	
	static void printCave() {
		rangeClosed(0, my).forEach(y -> System.out.println(rangeClosed(0, mx).map(x -> cave[x][y])));
	}
	
	static int erosion(int x, int y) {
		return (geo[x][y] + depth) % 20183;
	}
	
	static int geoIndex(int x, int y) {
		if (x==0 && y==0) return 0;
		if (x==tx && y==ty) return 0;
		if (x==0) return y*48271;
		if (y==0) return x*16807;
		return erosion(x-1, y) * erosion(x, y-1);		
	}
}
