package aoc2018;

import static org.jooq.lambda.Seq.range;

import java.util.List;

import org.jooq.lambda.Seq;

public class Day18 {

	static class Pos {
		int x, y;
		Pos(int x0, int y0) {
			x = x0;
			y = y0; 
		}
	}
	
	private static char[][] grid;
	private static char[][] next;
	private static int w;
	private static int h;
	
	static void printGrid() {
		range(0, h).forEach(y -> {
			System.out.println(range(0, w).map(x -> grid[x][y]));	
		});
		System.out.println();
	}
	
	static Seq<Pos> adjacent(int x, int y) {
		return Seq.of(
				new Pos(x-1, y-1), new Pos(x-1, y), new Pos(x-1,y+1),
				new Pos(x, y-1), new Pos(x,y+1),
				new Pos(x+1, y-1), new Pos(x+1, y), new Pos(x+1,y+1)
				)
				.filter(p -> p.x>=0 && p.x<w && p.y>=0 && p.y<h);
				
	}
	
	public static void main(String[] args) throws Exception {
		List<String> l = Util.lines("aoc2018/day18.txt");
		w = l.get(0).length();
		h = l.size();
		grid = new char[w][h];
		next = new char[w][h];
		
		range(0, h).forEach(y -> range(0,  w).forEach(x -> grid[x][y] = l.get(y).charAt(x)));
		
		printGrid();
		range(0, 10).forEach(i -> {
			step();
//			printGrid();
			System.out.println("" + i + "\t" + value());
			
		});
		
		System.out.println(value());
	}

	private static long value() {
		long ly = range(0,h).sum(y -> range(0,w).count(x -> grid[x][y] == '#')).get();
		long wood = range(0,h).sum(y -> range(0,w).count(x -> grid[x][y] == '|')).get();
		long result1 = ly * wood;
		return result1;
	}


	static void step() {
		range(0, h).forEach(y -> range(0,  w).forEach(x -> {
			next[x][y] = grid[x][y];
			
			// tree?
			if (grid[x][y] == '.' && adjacent(x, y).count(p -> grid[p.x][p.y] == '|') >= 3)
				next[x][y] = '|';
				
			// lumberyard?
			if (grid[x][y] == '|' && adjacent(x, y).count(p -> grid[p.x][p.y] == '#') >= 3)
				next[x][y] = '#';
				
			// open?
			if (grid[x][y] == '#') 
			{
				if (adjacent(x, y).count(p -> grid[p.x][p.y] == '#') >= 1 && 
				    adjacent(x, y).count(p -> grid[p.x][p.y] == '|') >= 1)
					next[x][y] = '#';
				else 
					next[x][y] = '.';
			}
		}));
		
		char[][] tmp = grid;
		grid = next;
		next = tmp;
	}
}
