package common;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record BoundingBox(int xMin, int xMax, int yMin, int yMax) {

    static <T extends Point> BoundingBox of(Collection<T> points) {
        return of(points.toArray(new Point[0]));
    }

    static BoundingBox of(Point... points) {
        return new BoundingBox(

        Stream.of(points).mapToInt(p -> p.x()).min().orElse(0),
        Stream.of(points).mapToInt(p -> p.x()).max().orElse(0),
        Stream.of(points).mapToInt(p -> p.y()).min().orElse(0),
        Stream.of(points).mapToInt(p -> p.y()).max().orElse(0));
    }

    Iterable<Integer> xRange() {
        return IntStream.rangeClosed(xMin, xMax).boxed()::iterator;
    }

    Iterable<Integer> yRange() {
        return IntStream.rangeClosed(yMin, yMax).boxed()::iterator;
    }

    void print(Function<Point, Character> point2String) {
        for (int y : yRange()) {
            for (int x : xRange()) {
                System.out.print(point2String.apply(new Point(x, y)));
            }
            System.out.println();
        }
    }
    
    void printS(Function<Point, String> point2String) {
        for (int y : yRange()) {
            for (int x : xRange()) {
                System.out.print(point2String.apply(new Point(x, y)));
            }
            System.out.println();
        }
    }

	Point center() {
		return new Point((xMin+xMax)/2, (yMin+yMax)/2);
	}

	public int height() {		
		return yMax-yMin+1;
	}
	
	public int width() {		
		return xMax-xMin+1;
	}
	
	public Point wrapX(Point p) {
		return new Point(((p.x()-xMin) % width()) + xMin, p.y());
	}

	public Point wrapY(Point p) {
		return new Point(p.x(), ((p.y()-yMin) % height()) + yMin);
	}

	public boolean contains(Point p) {		
		return p.x() >= xMin && p.x() <= xMax && p.y() >= yMin && p.y() <= yMax;
	}
}
