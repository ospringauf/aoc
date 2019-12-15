package aoc2019;

import java.util.stream.Stream;

public class Point {
    int x;
    int y;
    
    static final Point ZERO = of(0,0);

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static Point of(int x, int y) {
        return new Point(x,y);
    }
    
    int manhattan(Point a) {
        return Math.abs(a.x - x) + Math.abs(a.y - y);
    }
    
    public Point north() {
    	return Point.of(x,  y-1);
    }
    public Point south() {
    	return Point.of(x,  y+1);
    }
    public Point east() {
    	return Point.of(x+1,  y);
    }
    public Point west() {
    	return Point.of(x-1,  y);
    }
    
    public Stream<Point> neighbors() {
		return Stream.of(north(), south(), east(), west());
	}
    
    @Override
    public String toString() {
        return String.format("P[%d,%d]", x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        Point a = (Point)obj;
        return x==a.x && y==a.y;
    }
    
    @Override
    public int hashCode() {
        return x+3*y;
    }
}
