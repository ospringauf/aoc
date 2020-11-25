package aoc2020;

import java.util.stream.Stream;


record Point(int x, int y) {
	Point next() {
		return new Point(x + 1, y);
	}

	Point next(Heading h, int d) {
		return switch (h) {
		case NORTH -> new Point(x, y - d);
		case EAST -> new Point(x + d, y);
		case SOUTH -> new Point(x, y + d);
		case WEST -> new Point(x - d, y);
		};
	}
	
	int manhattan() {
		return Math.abs(x) + Math.abs(y);
	}

	public static Point of(int x, int y) {
	    return new Point(x,y);
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
}