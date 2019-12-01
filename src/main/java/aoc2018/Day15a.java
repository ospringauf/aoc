package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.lambda.Seq;

import aoc2018.Day13.Cart;
import aoc2018.Day15a.Pos;
import aoc2018.Day15a.Unit;

/**
 * elves vs gnomes strategy game - first ugly aproach 
 * https://adventofcode.com/2018/day/15
 *
 */
public class Day15a {

	static long start = System.currentTimeMillis();
	
	static int elfPower = 3;
	
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
	}
	
	class Unit {
		Pos pos;
		int power = 3;
		int hits = 200;
		char type;
		
		Unit(Pos p, char t) {
			pos = p;
			type = t;
			power = (t == 'E') ? elfPower : 3;
		}
		
		boolean alive() {
			return hits > 0;
		}

		boolean at(int x0, int y0) {
			return pos.x==x0 && pos.y==y0;
		}
		
		boolean at(Pos p) {
			return p.equals(pos);
		}
		
		Pos nearest(Unit target) {
			List<Pos> cand = candidates();
			List<Pos> dst = distances(target.pos, cand);
			List<Pos> next = seq(dst).filter(p -> p.manh(pos)<=1).toList();
			Pos c = choose(next);
//			System.out.println("  nearest to " + target+ ": " + c);
			return c;
		}
		
		List<Pos> candidates() {
			return
			range(0,height).flatMap(y -> range(0,width).map(x -> new Pos(x,y)))
			.filter(p -> board[p.x][p.y] == '.')
			.filter(p -> seq(units).noneMatch(u -> u.at(p)))
			.concat(pos)
			.toList();
		}
		
		public void turn() {
			if (!alive()) 
				return;
			
//			System.out.println("turn for "+this);
			List<Unit> targets = seq(units).filter(u -> u.type != type).toList();
//			System.out.println("  targets: " + targets);
			
			// attack
			if (attack(targets)) 
				return;
			
			
			// move
			Pos next = choose(seq(targets).map(u -> nearest(u)).filter(p -> p != null).toList());
//			System.out.println("  goto: " + next);
			if (next != null) {
				//return new Unit(next, type);
				pos = next;
//				return this;
//			} else {
//				return this;
			}
			
			// attack
			targets = seq(units).filter(u -> u.type != type).toList();
			attack(targets);
		}
		
		private boolean attack(List<Unit> targets) {
			//targets in range
			List<Unit> inrange = seq(targets).filter(t -> pos.manh(t.pos) == 1).toList();
			if (inrange.size() == 0)
				return false;
			
			// target with fewest hits
			int minh = seq(inrange).min(t -> t.hits).get();
			Unit tgt = seq(inrange).filter(t -> t.hits==minh).sorted(t -> t.pos.hashCode()).findFirst().get();
			tgt.hits -= power;
			if (tgt.hits <= 0) {
				units.remove(tgt);
			}
			System.out.println(this + " attacks " + tgt + " HP=" + tgt.hits);
			
			return true;
		}

		@Override
		public String toString() {
			return ""+type + " "+ pos;
		}
	}
	
	Pos choose(List<Pos> next) {
		if (next.size() == 0)
			return null;
		int min = seq(next).min(p -> p.dist).get();
		Optional<Pos> n = seq(next).filter(p -> p.dist == min).sorted(p -> p.hashCode()).findFirst();
		return n.orElse(null);
	}
	
	int width;
	int height;
	char[][] board;
	int[][] dist;
	List<Unit> units;
	
	boolean update = true;
	
	void calcDist(Unit u) {
		range(0, height).forEach(y -> range(0, width).forEach(x -> 
			dist[x][y] = (board[x][y] == '#')? -1 : Integer.MAX_VALUE));
		
		// enemy pos = 0, friend pos is forbidden
		seq(units).forEach(v ->
			dist[v.pos.x][v.pos.y] = (v.type == u.type) ? -1 : 0
		);
		
		// own pos is included
		dist[u.pos.x][u.pos.y] = Integer.MAX_VALUE;
		
		while (update) {
			update = false;
			range(1, height-1).forEach(y -> range(1, width-1).forEach(x -> {
				if (dist[x][y] > 0) {
					int newd =
						Seq.of(dist[x][y-1], dist[x][y+1], dist[x+1][y], dist[x-1][y])
						.filter(d -> d >= 0)
						.min().get() + 1;
					update |= newd < dist[x][y];
					dist[x][y] = newd;					
				}
			}));
			
		}
		
	}
	
	Seq<Pos> inRange(Pos p) {
		return Seq.of(new Pos(p.x,p.y-1), new Pos(p.x,p.y+1), new Pos(p.x-1,p.y), new Pos(p.x+1,p.y))
				.filter(q -> board[q.x][q.y] != '#')
				.filter(q -> seq(units).noneMatch(u -> u.at(q)))
				.map(q -> q.setDist(p.dist+1));
	}
	
	
	public static void main(String[] args) throws Exception {
		new Day15a().solve();
	}
	
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
	
	List<Pos> distances(Pos p0, List<Pos> cand) {
		List<Pos> l = new ArrayList<>();
		l.add(new Pos(p0.x, p0.y, 0));
		int s = 1;
		do {
			s = l.size();
			ArrayList<Pos> rem = new ArrayList<Pos>();
			cand.forEach(c -> {
				Optional<Pos> n = seq(l).filter(p -> p.manh(c)==1).sorted(p -> p.dist).findFirst();
				if (n.isPresent()) {
					//cand.remove(c);
					rem.add(c);
					l.add(new Pos(c.x, c.y, n.get().dist+1));
				}
			});
			cand.removeAll(rem);
		} while (l.size() > s);
		return l.subList(1, l.size());
	}
			
	void solve() throws Exception {				
		
		System.out.println("=== part 1 ===");
		part1();
//		System.out.println("=== part 2 ===");
//		part2();
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));
	}

	protected void readBoard(String fname) throws IOException {
		List<String> l = Util.lines(fname);
		units = new ArrayList<>();
		
		width = l.get(0).length();
		height = l.size();
		board = new char[width][height];
		
		// build board and extract actors
		range(0, height).forEach(y -> range(0, width).forEach(x -> {
			char c = l.get(y).charAt(x);
			if ("EG".indexOf(c) >= 0) {// actor at this position
				units.add(new Unit(new Pos(x, y), c));
				board[x][y] = '.';
			} else 
				board[x][y] = c;
		}));
	}

	void part1() throws Exception {
		readBoard("aoc2018/day15.txt");	

		printBoard();
		for (int i=0; i<100; ++i) {
			System.out.println("*** round " + i);
			List<Unit> order = seq(units).sorted(u -> u.pos.hashCode()).toList();		
			seq(order).forEach(u -> u.turn());			
//			printBoard();
						
			boolean end = seq(units).map(u -> u.type).distinct().count() == 1;
			if (end) {
				System.out.println("winner: " + units.get(0).type);
				int h = seq(units).sum(u -> u.hits).get();
				System.out.println("result: " + i + ", " + h + " --> " +i*h);
				return;
			}
		}
	}
	
	boolean part2() throws Exception {
		// power 16 --> 52374
		
		readBoard("aoc2018/day15.txt");	
//		printBoard();
		long elves0 = seq(units).count(u -> u.type=='E');
		long elves = elves0;

		int i = 0;
		do {
			System.out.println("*** round " + i);
			List<Unit> order = seq(units).sorted(u -> u.pos.hashCode()).toList();		
			seq(order).forEach(u -> u.turn());			
			
			elves = seq(units).count(u -> u.type=='E');
			i++;
//			printBoard();
		} while (elves == elves0 && (units.size() > elves));
		
		i--;
		System.out.println("elf lost: " + (elves != elves0));
		int h = seq(units).sum(u -> u.hits).get();
		System.out.println("result: " + i + ", " + h + " --> " +i*h);
		
		return (elves == elves0);
		
	}

}
