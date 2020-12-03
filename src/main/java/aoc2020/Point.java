package aoc2020;

import io.vavr.collection.List;


public record Point(int x, int y) {
	
	int manhattan() {
		return Math.abs(x) + Math.abs(y);
	}

	public static Point of(int x, int y) {
	    return new Point(x,y);
	}
	
	public Point translate(int dx, int dy) {
        return Point.of(x+dx, y+dy);
    }

	public Point translate(Heading h) {
        return translate(h, 1);
    }
	
	Point translate(Heading h, int d) {
		return Point.of(x + d*h.dx, y + d*h.dy);
	}
	
	public Point north() {
        return north(1);
    }
	
    public Point north(int steps) {
        return Point.of(x,  y-steps);
    }

    public Point south() {
    	return south(1);
    }
    
    public Point south(int steps) {
        return Point.of(x,  y+steps);
    }
    
    public Point east() {
    	return east(1);
    }
    
    public Point east(int steps) {
        return Point.of(x+steps,  y);
    }
    
    public Point west() {
    	return west(1);
    }
    
    public Point west(int steps) {
        return Point.of(x-steps,  y);
    }
    
    public Point modulo(int w, int h) {    	
    	return Point.of(x % w, y % h);   			
    }
    
//    public Stream<Point> neighbors() {
//        return Stream.of(north(), south(), east(), west());
//    }
    
    public List<Point> neighbors() {
        return List.of(north(), south(), east(), west());
    }

    public String toString() {
    	return String.format("(%d,%d)", x,y);
    }
}