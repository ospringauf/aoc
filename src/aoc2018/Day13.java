/*********************************************************************
* Copyright (c) 2018 Angelika Wittek, Oliver Springauf.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
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
		
		
		void turnNext() {
			switch (turn) {
				case 0: dir = map(dir, "><^v", "^v<>"); break;
				case 2: dir = map(dir, "><^v", "v^><"); break;
			}
			turn = (turn + 1) % 3;
		}
		
		void move() {
			// >- >\ >/ >+ 			
			if (dir=='>') {
				switch(tracks[x+1][y]) {
				case '-':
					x++; break;
				case '\\':
					x++; dir='v'; break;
				case '/':
					x++; dir='^'; break;
				case '+':
					x++; turnNext(); break;
				case '|': throw new IllegalStateException();
				}
			}
			// >- >\ >/ >+ 			
			else if (dir=='<') {
				switch(tracks[x-1][y]) {
				case '-':
					x--; break;
				case '\\':
					x--; dir='^'; break;
				case '/':
					x--; dir='v'; break;
				case '+':
					x--; turnNext(); break;
				case '|': throw new IllegalStateException();
				}
			}
			// | + / \  			
			else if (dir=='^') {
				switch(tracks[x][y-1]) {
				case '|':
					y--; break;
				case '\\':
					y--; dir='<'; break;
				case '/':
					y--; dir='>'; break;
				case '+':
					y--; turnNext(); break;
				case '-': throw new IllegalStateException();
				}
			}
			// | + / \  			
			else if (dir=='v') {
				switch(tracks[x][y+1]) {
				case '|':
					y++; break;
				case '\\':
					y++; dir='>'; break;
				case '/':
					y++; dir='<'; break;
				case '+':
					y++; turnNext(); break;
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
		
		range(0, width).forEach(x -> range(0, height).forEach(y -> {
			char c = l.get(y).charAt(x);
			if (seq("^v><".chars()).contains((int) c)) {
				carts.add(new Cart(x, y, c));
				c = (c=='^'||c=='v') ? '|' : '-';
			}
			tracks[x][y] = c;
		}));		
		
		
		printTrack();		
		
		while (true) {
			tick(); 
//			printTrack();
		}
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
	if (seq(carts).count(z -> z.x==c.x && z.y==c.y) > 1) 
		throw new IllegalStateException("crash at " + c.x + "," + c.y); 
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
