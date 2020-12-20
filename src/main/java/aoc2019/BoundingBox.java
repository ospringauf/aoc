package aoc2019;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.IntStream;

class BoundingBox {

    int xMin, xMax, yMin, yMax;

    static <T extends Point> BoundingBox of(Collection<T> points) {
        return of(points.toArray(new Point[0]));
    }

    static BoundingBox of(Point... points) {
        var bb = new BoundingBox();

        bb.xMin = Arrays.stream(points).mapToInt(p -> p.x).min().orElse(0);
        bb.xMax = Arrays.stream(points).mapToInt(p -> p.x).max().orElse(0);
        bb.yMin = Arrays.stream(points).mapToInt(p -> p.y).min().orElse(0);
        bb.yMax = Arrays.stream(points).mapToInt(p -> p.y).max().orElse(0);

        return bb;
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

	Point center() {
		return Point.of((xMin+xMax)/2, (yMin+yMax)/2);
	}
}
