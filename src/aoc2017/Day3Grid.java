package aoc2017;

public class Day3Grid {

	public static class Pos {
		public int x;
		public int y;
		public Pos(int x, int y) {
			this.x=x;
			this.y=y;
		}
		
		public Pos left() { return new Pos(x-1,y); }
		public Pos right() { return new Pos(x+1,y); }
		public Pos up() { return new Pos(x,y-1); }
		public Pos down() { return new Pos(x,y+1); }
	}
	
	Integer[] a;
	int w, h;
	
	public Day3Grid(int w, int h) {
		this.w = w;
		this.h= h;
		a = new Integer[w*h];
	}
	
	private int pos(Pos p) {
		return (p.y+h/2)*w + (p.x+w/2);
	}

	public Integer get(Pos p) {
		return a[pos(p)];
	}

	public Integer getOrZero(Pos p) {
		Integer v = get(p);
		return (v == null) ? 0 : v;
	}

	public boolean has(Pos p) {
		return get(p) != null;
	}

	public void set(Pos p, Integer i) {
		a[pos(p)] = i;
	}

	
	//		
	//		x				R
	//		
	//----------------------------	
	//		x				
	//	*	x	.			R
	//						
	//----------------------------	
	//		*				
	//		x	x			D
	//		.				
	//----------------------------	
	//						
	//	.	x	*			L
	//		x				
	//----------------------------	
	//		.				
	//	x	x				U
	//		*				
	

	public Pos next(Pos p) {
		// R
		if (has(p.up()) && !has(p.right())) {
			return p.right();
		}
		// D
		if (has(p.right()) && !has(p.down())) {
			return p.down();
		}
		// L
		if (has(p.down()) && !has(p.left())) {
			return p.left();
		}
		// U
		if (has(p.left()) && !has(p.up())) {
			return p.up();
		}
		return p.right();
	}


	public Integer sumAdjacent(Pos p) {
		int v = 
				  getOrZero(p.right())
				+ getOrZero(p.left())
				+ getOrZero(p.up())
				+ getOrZero(p.down())
				+ getOrZero(p.up().left())
				+ getOrZero(p.up().right())
				+ getOrZero(p.down().left())
				+ getOrZero(p.down().right());
		return v;
	}

	public void print() {
		for (int y=-h/2; y<h/2; ++y) {
			for(int x=-w/2; x<w/2; ++x) {
				System.out.print(String.format("%7d", get(new Pos(x,y))));
			}
			System.out.println();
		}
	}
	
}
