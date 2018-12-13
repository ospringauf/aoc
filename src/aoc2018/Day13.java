package aoc2018;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

	private static char[][] tracks;
	private static int width;
	private static int height;
	static List<Cart> carts = new ArrayList<>();

	static char map(char c, String s1, String s2) {
		return s2.charAt(s1.indexOf(c));
	}
	
	static class Cart {
		int x, y;
		char dir;
		int turn = 0; // 0=left, 1=straight, 2=right repeat

		public Cart(Integer x2, Integer y2, char c) {
			x = x2;
			y = y2;
			dir = c;
		}
		
		void turnAtCrossing() {		
			dir = 
					(turn == 0)? map(dir, "><^v", "^v<>") : // left
					(turn == 2)? map(dir, "><^v", "v^><") : // right
					dir;
			turn = (turn + 1) % 3;
		}
		
		boolean samePos(Cart c) {
			return x == c.x && y == c.y;
		}
		
		void move() {
			if (dir=='>') {
				x++;
				switch(tracks[x][y]) {
				case  '-': break;
				case '\\': dir='v'; break;
				case  '/': dir='^'; break;
				case  '+': turnAtCrossing(); break;
				case  '|': throw new IllegalStateException();
				}
			}
			else if (dir=='<') {
				x--;
				switch(tracks[x][y]) {
				case  '-': break;
				case '\\': dir='^'; break;
				case  '/': dir='v'; break;
				case  '+': turnAtCrossing(); break;
				case  '|': throw new IllegalStateException();
				}
			}
			else if (dir=='^') {
				y--;
				switch(tracks[x][y]) {
				case  '|': break;
				case '\\': dir='<'; break;
				case  '/': dir='>'; break;
				case  '+': turnAtCrossing(); break;
				case  '-': throw new IllegalStateException();
				}
			}
			else if (dir=='v') {
				y++;
				switch(tracks[x][y]) {
				case  '|': break;
				case '\\': dir='>'; break;
				case  '/': dir='<'; break;
				case  '+': turnAtCrossing(); break;
				case  '-': throw new IllegalStateException();
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> l = Util.lines("aoc2018/day13.txt");
		
		width = l.get(0).length();
		height = l.size();
		tracks = new char[width][height];
		
		// build tracks and extract carts
		range(0, height).forEach(y -> range(0, width).forEach(x -> {
			char c = l.get(y).charAt(x);
			if ("^v><".indexOf(c) >= 0) { // cart at this position
				carts.add(new Cart(x, y, c));
				c = (c=='^'||c=='v') ? '|' : '-'; // track under cart
			}
			tracks[x][y] = c;
		}));		
		
		
		printTrack();		
		
		while (carts.size() > 1) {
			tick(); 
//			printTrack();
		}
		
		Cart last = carts.get(0);
		System.out.println("last cart: " + last.x + "," +last.y); 
	}
	
	static void tick() {
		List<Cart> s = seq(carts)
			.sorted(c -> c.y*width + c.x)
			.toList();
		
		s.forEach(c -> {
				c.move();
				crashCheck(c);
			});
	}
	
	static void crashCheck(Cart c) {
		if (seq(carts).count(z -> z.samePos(c)) > 1) { 
			//throw new IllegalStateException("crash at " + c.x + "," + c.y);
			System.err.println("crash at " + c.x + "," + c.y);
			carts.removeIf(k -> k.samePos(c));
		}
	}

	private static void printTrack() {
		range(0, height).forEach(y -> {
			System.out.println(
				range(0, width).map(x ->
					// cart here? --> cart.direction else track
					seq(carts)
						.findFirst(z -> z.x==x && z.y==y)
						.map(z -> z.dir)
						.orElse(tracks[x][y])				
				).toString());
		});
		System.out.println();
	}
	
}
