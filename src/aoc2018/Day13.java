package aoc2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

public class Day13 {

	private static char[][] tracks;
	private static int width;
	private static int height;
	static List<Cart> carts = new ArrayList<>();

	static char map(char c, String s1, String s2) {
		return s2.charAt(s1.indexOf(c));
	}
	
	static class Cart {
		public Cart(Integer x2, Integer y2, char c) {
			x = x2;
			y = y2;
			dir = c;
		}
		
		int x, y;
		char dir;
		int turn = 0; // 0=left, 1=straight, 2=right repeat
		
		
		char crossing() {		
			dir = 
					(turn == 0)? map(dir, "><^v", "^v<>") : // left
					(turn == 2)? map(dir, "><^v", "v^><") : // right
					dir;
			turn = (turn + 1) % 3;
			return dir;
		}
		
		void move() {
			if (dir=='>') {
				x++;
				switch(tracks[x][y]) {
				case '-':
					break;
				case '\\':
					dir='v'; break;
				case '/':
					dir='^'; break;
				case '+':
					crossing(); break;
				case '|': throw new IllegalStateException();
				}
			}
			else if (dir=='<') {
				x--;
				switch(tracks[x][y]) {
				case '-':
					break;
				case '\\':
					dir='^'; break;
				case '/':
					dir='v'; break;
				case '+':
					crossing(); break;
				case '|': throw new IllegalStateException();
				}
			}
			else if (dir=='^') {
				y--;
				switch(tracks[x][y]) {
				case '|':
					break;
				case '\\':
					dir='<'; break;
				case '/':
					dir='>'; break;
				case '+':
					crossing(); break;
				case '-': throw new IllegalStateException();
				}
			}
			else if (dir=='v') {
				y++;
				switch(tracks[x][y]) {
				case '|':
					break;
				case '\\':
					dir='>'; break;
				case '/':
					dir='<'; break;
				case '+':
					crossing(); break;
				case '-': throw new IllegalStateException();
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> l = Util.lines("aoc2018/day13.txt");
		
		width = l.get(0).length();
		height = l.size();
		tracks = new char[width][height];
		
		// extract carts
		range(0, width).forEach(x -> range(0, height).forEach(y -> {
			char c = l.get(y).charAt(x);
			if (seq("^v><".chars()).contains((int) c)) {
				carts.add(new Cart(x, y, c));
				c = (c=='^'||c=='v') ? '|' : '-';
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
		if (seq(carts).count(z -> z.x==c.x && z.y==c.y) > 1) { 
			//throw new IllegalStateException("crash at " + c.x + "," + c.y);
			System.err.println("crash at " + c.x + "," + c.y);
			carts.removeIf(k -> k.x==c.x && k.y==c.y);
		}
	}

	private static void printTrack() {
		range(0, height).forEach(y -> {
			System.out.println(
			range(0, width).map(x -> {
				Optional<Cart> c = seq(carts).findFirst(z -> z.x==x && z.y==y);
				return c.isPresent() ? c.get().dir : tracks[x][y];				
			}).toString());
		});
		System.out.println();
	}
	
}
