package common;

import java.util.function.Function;

import io.vavr.Tuple2;
import io.vavr.collection.List;


public record Point(int x, int y) {
	
	public int manhattan() {
		return Math.abs(x) + Math.abs(y);
	}
	
	public int manhattan(Point a) {
        return Math.abs(a.x - x) + Math.abs(a.y - y);
    }

	public static Point of(Tuple2<Integer, Integer> xy) {
		return new Point(xy._1, xy._2);
	}

	public static Point of(int x, int y) {
	    return new Point(x,y);
	}
	
	public Point translate(int dx, int dy) {
        return Point.of(x+dx, y+dy);
    }

	public Point translate(Direction h) {
        return translate(h, 1);
    }
	
	Point translate(Direction h, int d) {
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
    
    public Point rotLeft() {
    	return new Point(y, -x);
	}
    
    public Point rotRight() {
    	return new Point(-y, x);
	}

    public Point flipY() {
    	return new Point(x, -y);
	}
    
    public Point flipX() {
    	return new Point(-x, y);
	}


    public Point repeat(int n, Function<Point, Point> f) {
    	var p = this;
    	for (int i=0; i<n; ++i) {
    		p = f.apply(p);
    	}
    	return p;
    }

    
//    public Stream<Point> neighbors() {
//        return Stream.of(north(), south(), east(), west());
//    }
    
    public List<Point> neighbors() {
        return List.of(north(), south(), east(), west());
    }

    public List<Point> neighbors8() {
        return List.of(north(), south(), east(), west(), translate(1,1), translate(1,-1), translate(-1,-1), translate(-1, 1));
    }

    public String toString() {
    	return String.format("(%d,%d)", x,y);
    }
}