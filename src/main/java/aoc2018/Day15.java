package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.jooq.lambda.Seq;

/**
 * elves vs gnomes strategy game 
 * https://adventofcode.com/2018/day/15
 *
 */
public class Day15 {

	static long start = System.currentTimeMillis();

	int width;
	int height;
	char[][] board;
	int[][] distMap;
	List<Unit> units;
	
	boolean update = true;
	

	class Pos {
		int x, y;
		int dist = -1;
		Pos(int x0, int y0) {
			x = x0;
			y = y0;
		}
		Pos(int x0, int y0, int d0) {
			x = x0;
			y = y0;
			dist = d0;
		}
		
		Pos setDist(int d) {
			dist=d;
			return this;
		}
		
		int manh(Pos p) {
			return (Math.abs(x-p.x) + Math.abs(y-p.y));
		}
		
		@Override
		public boolean equals(Object obj) {
			Pos p = (Pos)obj;
			return x == p.x && y == p.y;
		}
		
		@Override
		public int hashCode() {
			return width*y + x;
		}
		
		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
		
		// adjacent (or same) pos with valid path to target (0 < dist < INF)
		Seq<Pos> nextByDist() {
			return Seq.of(new Pos(x, y-1), new Pos(x, y+1), new Pos(x-1, y), new Pos(x+1, y), this)
					.map(p -> p.setDist(distMap[p.x][p.y]))
					.filter(p -> p.dist > 0 && p.dist < Integer.MAX_VALUE);
		}
	}
	
	class Unit {
		Pos pos;
		int power = 3;
		int hits = 200;
		char type;
		
		Unit(Pos p, char t) {
			pos = p;
			type = t;
		}
		
		boolean alive() {
			return hits > 0;
		}

		boolean at(int x0, int y0) {
			return pos.x==x0 && pos.y==y0;
		}
		
		@Override
		public String toString() {
			return ""+type + " "+ pos;
		}

		public void turn() {
			if (!alive()) 
				return;
			
//			System.out.println("turn for "+this);
			calcDist(this);
//			printBoard();
//			printDistMap();
			
			// attack? --> my turn ends
			if (attack()) return;
			
			// move
			Optional<Integer> min = pos.nextByDist().min(p -> p.dist);
			if (min.isPresent()) {
				Pos next = pos
						.nextByDist()
						.filter(p -> p.dist == min.get()) // min dist to target
						.sorted(p -> p.hashCode()) // ambiguous? take 1st by rank
						.findFirst()
						.get();
				pos = next;
//				System.out.println("  goto: " + pos);
			}
					
			// attack at end of move
			attack();
		}
		
		private boolean attack() {
			// targets in range
			List<Unit> inrange = seq(units)
					.filter(u -> u.type != type) // opponents=targets
					.filter(t -> pos.manh(t.pos) == 1) // in range
					.toList();
			if (inrange.size() == 0)
				return false;
			
			// target with fewest hits
			int minh = seq(inrange).min(t -> t.hits).get();
			Unit tgt = seq(inrange).filter(t -> t.hits==minh).sorted(t -> t.pos.hashCode()).findFirst().get();
			tgt.hits -= power;
			if (tgt.hits <= 0) {
				units.remove(tgt);
			}
//			System.out.println(this + " attacks " + tgt + " HP=" + tgt.hits);
			
			return true;
		}
	}
	
	/**
	 * fix point iteration: start at targets (dist = 0), for all points calculate
	 * min adjacent distance + 1.
	 * Mark walls and friends as "locked" 
	 */
	void calcDist(Unit u) {
		range(0, height).forEach(y -> range(0, width).forEach(x -> 
			distMap[x][y] = (board[x][y] == '#')? -1 : Integer.MAX_VALUE));
		
		// enemy pos = 0, friend pos is forbidden
		seq(units).forEach(v ->
			distMap[v.pos.x][v.pos.y] = (v.type == u.type) ? -1 : 0
		);
		
		// own pos is included
		distMap[u.pos.x][u.pos.y] = Integer.MAX_VALUE;
		
		update = true;
		
		while (update) {
			update = false;
			range(1, height-1).forEach(y -> range(1, width-1).forEach(x -> {
				if (distMap[x][y] > 0) {
					int newd = Seq
						.of(distMap[x][y-1], distMap[x][y+1], distMap[x+1][y], distMap[x-1][y]) // adjacent fields
						.filter(d -> d >= 0 && d < Integer.MAX_VALUE) // valid distance
						.min().map(m -> m+1).orElse(distMap[x][y]); // dist+1
					update |= newd < distMap[x][y];
					distMap[x][y] = newd;					
				}
			}));
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Day15().solve();
	}

	void solve() throws Exception {				
		
		System.out.println("=== part 1 ===");
		sim("aoc2018/day15.txt", 3, false);
		
		// power 16 --> 52374
		System.out.println("=== part 2 ===");
		part2("aoc2018/day15.txt");
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}


	// for debugging
	void printDistMap() {
		range(0, height).forEach(y -> {
			System.out.println(
				range(0, width)
				.map(x -> distMap[x][y])
				.map(d -> (d == -1)? "  X" : (d == Integer.MAX_VALUE) ? "   " : String.format("%3d", d))
				.toString(" "));
		});
		System.out.println();
	}

	// for debugging
	void printBoard() {
		char[][] b = new char[width][height];
		range(0, height).forEach(y -> range(0, width).forEach(x -> {
			b[x][y] = seq(units).findFirst(u -> u.at(x,y)).map(u -> u.type).orElse(board[x][y]);
		}));

		range(0, height).forEach(y -> {
			System.out.println(
				range(0, width).map(x -> b[x][y]).toString());
		});
		System.out.println();
	}
	
			

	protected void readBoard(String fname) throws IOException {
		List<String> l = Util.lines(fname);
		units = new ArrayList<>();
		
		width = l.get(0).length();
		height = l.size();
		board = new char[width][height];
		distMap = new int[width][height];
		
		// build board and extract actors
		range(0, height).forEach(y -> range(0, width).forEach(x -> {
			char c = l.get(y).charAt(x);
			if ("EG".indexOf(c) >= 0) { // actor at this position				
				units.add(new Unit(new Pos(x, y), c));
				board[x][y] = '.';
			} else 
				board[x][y] = c;
		}));
	}

	
	boolean sim(String fname, int elfPower, boolean allElvesMustLive) throws Exception {
		readBoard(fname);	
		seq(units).filter(u -> u.type=='E').forEach(u -> u.power = elfPower);
		
//		printBoard();
		final long elves0 = seq(units).count(u -> u.type=='E');

		// abort conditions
		Predicate<List<Unit>> abort1 = (units) -> seq(units).map(u -> u.type).distinct().count() == 1;
		Predicate<List<Unit>> elfDied = (units) -> seq(units).count(u -> u.type=='E') < elves0;
		Predicate<List<Unit>> abort2 = (units) -> elfDied.test(units) || abort1.test(units); 

		Predicate<List<Unit>> abort = allElvesMustLive ? abort2 : abort1;
		
		int i = 0;
		do {
			i++;
//			System.out.println("*** round " + i);
			
			List<Unit> order = seq(units).sorted(u -> u.pos.hashCode()).toList();		
			seq(order).forEach(u -> u.turn());			
//			printBoard();
		} while (!abort.test(units));
		
		i--;
		System.out.println("  round: " + i);
		System.out.println("  elf lost: " + elfDied.test(units));
		int h = seq(units).sum(u -> u.hits).get();
		System.out.println("  result: " + i + ", " + h + " --> " + (i*h));
		
		return !elfDied.test(units);		
	}

	// binary search for minimum elf power 
	private void part2(String fname) throws Exception {
		int lower = 4;
		int upper = 100;
		int test = 0;
		
		boolean found = false;
		while (!found && (upper - lower > 1)) {
			test = lower + (upper-lower)/2;
			System.out.println("test elf power " + lower + " < " + test + " < " + upper);
			boolean x = sim(fname, test, true);
			if (x) {
				upper = test;
			} else {
				lower = test;
			}
		}
		
		System.out.println("min elf power: " + (test + 1));
		sim(fname, test+1, true);
	}

}
