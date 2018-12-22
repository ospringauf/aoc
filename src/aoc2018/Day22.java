package aoc2018;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.range;

public class Day22 {

	static char[][] cave;
	static int[][] geo;
	
	// shortest paths
	static int[][] torch;
	static int[][] climb;
	static int[][] none;
	
//	static int tx=10, ty=10, depth = 510, mx=20, my=20;
	static int tx=14, ty=778, depth = 11541, mx=30, my=1000;
	
	static final int MAX = 1000000;
	

	public static void main(String[] args) {
		geo = new int[mx+1][my+1];
		torch = new int[mx+1][my+1];
		climb = new int[mx+1][my+1];
		none = new int[mx+1][my+1];
		
		cave = new char[mx+1][my+1];
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> geo[x][y] = geoIndex(x,y)));
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> cave[x][y] = type(x,y)));
				
		rangeClosed(0, my).forEach(y -> rangeClosed(0, mx).forEach(x -> torch[x][y] = climb[x][y] = none[x][y] = MAX));
		torch[0][0] = 0;
		
		printCave();
		cave[0][0] = '.';
		cave[tx][ty] = '.';
		
		
		Integer level = rangeClosed(0, ty).sum(y -> rangeClosed(0, tx).sumInt(x -> risk(x,y))).get();
		System.out.println(level);

		calcPaths();
		print(torch);
		System.out.println(torch[tx][ty]);
	}

	static void calcPaths() {
		int best = MAX;
		int lastBest = MAX;
		while (best == MAX || best < lastBest) {
			lastBest = best;
			System.out.println("better");
			calcTorch();
			calcClimb();
			calcNone();
			calcTorch();
			best = torch[tx][ty];
		}
		
	}

	static int min(int a, int b) {
		return Math.min(a, b); 
	}

	static int min(int a, int b, int c) {
		return Math.min(a, Math.min(b, c)); 
	}
	
	static int min(int a, int b, int c, int d) {
		return Math.min(Math.min(a, b), Math.min(c, d)); 
	}
	
	static boolean update;
	
	// . torch/climb
	// = climb/none
	// | torch/none
	
	static boolean calcTorch() {
		// torch can be used in rocky (.) or narrow (|)
		// not in wet (=)
		update = true;
		boolean better = false;
		while (update) {
			update = false;
			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
				char t = cave[x][y];
				if (t != '=') {
					int u = (y == 0) ? MAX : torch[x][y-1];
					int l = (x == 0) ? MAX : torch[x-1][y];
					int d = torch[x][y+1];
					int r = torch[x+1][y];
					
					int best = min(u,l,r,d)+1;
					if (best < torch[x][y]) {
						torch[x][y] = best;
						update = true;
					}
					if (t == '.' && climb[x][y]>torch[x][y]+7) {
						climb[x][y] = torch[x][y] + 7;
						update = true;
					}
					if (t == '|' && none[x][y]>torch[x][y]+7) {
						none[x][y] = torch[x][y] + 7;
						update = true;
					}
				}
			}));
			better |= update;
		}
		return better;
	}
	
	static boolean calcClimb() {
		// climb can be used in rocky (.) or wet (=)
		// not in narrow (|)
		update = true;
		boolean better = false;
		while (update) {
			update = false;
			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
				char t = cave[x][y];
				if (t != '|') {
					int u = (y == 0) ? MAX : climb[x][y-1];
					int l = (x == 0) ? MAX : climb[x-1][y];
					int d = climb[x][y+1];
					int r = climb[x+1][y];
					
					int best = min(u,l,r,d)+1;
					if (best < climb[x][y]) {
						climb[x][y] = best;
						update = true;
					}
					if (t == '.' && torch[x][y]>climb[x][y]+7) {
						torch[x][y] = climb[x][y] + 7;
						update = true;
					}
					if (t == '=' && none[x][y]>climb[x][y]+7) {
						none[x][y] = climb[x][y] + 7;
						update = true;
					}
				}
			}));
			better |= update;
		}
		return better;
	}

	static boolean calcNone() {
		// none can be used in narrow (|) or wet (=)
		// not in rocky (.)
		update = true;
		boolean better = false;
		while (update) {
			update = false;
			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
				char t = cave[x][y];
				if (t != '.') {
					int u = (y == 0) ? MAX : none[x][y-1];
					int l = (x == 0) ? MAX : none[x-1][y];
					int d = none[x][y+1];
					int r = none[x+1][y];
					
					int best = min(u,l,r,d)+1;
					if (best < none[x][y]) {
						none[x][y] = best;
						update = true;
					}
					if (t == '=' && climb[x][y]>none[x][y]+7) {
						climb[x][y] = none[x][y] + 7;
						update = true;
					}
					if (t == '|' && torch[x][y]>none[x][y]+7) {
						torch[x][y] = none[x][y] + 7;
						update = true;
					}
				}
			}));
			better |= update;
		}
		return better;
	}
	
	
//	static boolean calcTorch() {
//		// torch can be used in rocky (.) or narrow (|)
//		// not in wet (=)
//		update = true;
//		boolean better = false;
//		while (update) {
//			update = false;
//			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
//				if (x!=0 || y!=0)
//				if (cave[x][y] != '=') {
//					int u = (y == 0) ? MAX : min(torch[x][y-1], climb[x][y-1]+7, none[x][y-1]+7);
//					int l = (x == 0) ? MAX : min(torch[x-1][y], climb[x-1][y]+7, none[x-1][y]+7);
//					int d = min(torch[x][y+1], climb[x][y+1]+7, none[x][y+1]+7);
//					int r = min(torch[x+1][y], climb[x+1][y]+7, none[x+1][y]+7);
//					
//					int best = min(u,l,r,d)+1;
//					best = min(best, climb[x][y]+7, none[x][y]+7);
//					if (best < torch[x][y]) {
//						torch[x][y] = best;
//						update = true;
//					}
//				}
//			}));
//			better |= update;
//		}
//		return better;
//	}
//	
//	static boolean calcClimb() {
//		// climb can be used in rocky (.) or wet (=)
//		// not in narrow (|)
//		update = true;
//		boolean better = false;
//		while (update) {
//			update = false;
//			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
//				if (x!=0 || y!=0)
//				if (cave[x][y] != '|') {
//					int u = (y == 0) ? MAX : min(torch[x][y-1]+7, climb[x][y-1], none[x][y-1]+7);
//					int l = (x == 0) ? MAX : min(torch[x-1][y]+7, climb[x-1][y], none[x-1][y]+7);
//					int d = min(torch[x][y+1]+7, climb[x][y+1], none[x][y+1]+7);
//					int r = min(torch[x+1][y]+7, climb[x+1][y], none[x+1][y]+7);
//					
//					int best = min(u,l,r,d)+1;
//					best = min(best, torch[x][y]+7, none[x][y]+7);
//					if (best < climb[x][y]) {
//						climb[x][y] = best;
//						update = true;
//					}
//				}
//			}));
//			better |= update;
//		}
//		return better;
//	}
//
//	static boolean calcNone() {
//		// none can be used in narrow (|) or wet (=)
//		// not in rocky (.)
//		update = true;
//		boolean better = false;
//		while (update) {
//			update = false;
//			range(0, my).forEach(y -> range(0, mx).forEach(x -> {
//				if (x!=0 || y!=0)
//				if (cave[x][y] != '.') {
//					int u = (y == 0) ? MAX : min(torch[x][y-1]+7, climb[x][y-1]+7, none[x][y-1]);
//					int l = (x == 0) ? MAX : min(torch[x-1][y]+7, climb[x-1][y]+7, none[x-1][y]);
//					int d = min(torch[x][y+1]+7, climb[x][y+1]+7, none[x][y+1]);
//					int r = min(torch[x+1][y]+7, climb[x+1][y]+7, none[x+1][y]);
//					
//					int best = min(u,l,r,d)+1;
//					best = min(best, climb[x][y]+7, torch[x][y]+7);
//					if (best < none[x][y]) {
//						none[x][y] = best;
//						update = true;
//					}
//				}
//			}));
//			better |= update;
//		}
//		return better;
//	}
//	
	static void print(int[][] len) {
		rangeClosed(0, my).forEach(y -> System.out.println(rangeClosed(0, mx).map(x ->  {
			int l = len[x][y];
			return (l == MAX ? "    X" : String.format("%5d", l));
		})));
		System.out.println();
	}

	static char type(int x, int y) {
		if (x==0 && y==0) return 'M';
		if (x==tx && y==ty) return 'T';
		int e = erosion(x, y);
		switch (e%3) {
		case 0: return '.'; // rocky
		case 1: return '='; // wet
		case 2: return '|'; // narrow
		}
		return '?';
	}
	
	static int risk(int x, int y) {
		if (x==0 && y==0) return 0;
		if (x==tx && y==ty) return 0;
		return erosion(x, y) % 3;
	}
	
	static void printCave() {
		//rangeClosed(0, my).forEach(y -> System.out.println(rangeClosed(0, mx).map(x -> type(x,y))));		
		rangeClosed(0, my).forEach(y -> System.out.println(rangeClosed(0, mx).map(x -> cave[x][y])));
	}
	
	static int erosion(int x, int y) {
		//return (geoIndex(x, y) + depth) % 20183;
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
