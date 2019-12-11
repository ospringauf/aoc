package aoc2019;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.IntStream;

public class BoundingBox {

    int xMin, xMax, yMin, yMax;

    static <T extends Point> BoundingBox of(Collection<T> points) {
        return of(points.toArray(new Point[0]));
    }

    static BoundingBox of(Point... points) {
        var bb = new BoundingBox();

        bb.xMin = Arrays.stream(points).mapToInt(p -> p.x).min().getAsInt();
        bb.xMax = Arrays.stream(points).mapToInt(p -> p.x).max().getAsInt();
        bb.yMin = Arrays.stream(points).mapToInt(p -> p.y).min().getAsInt();
        bb.yMax = Arrays.stream(points).mapToInt(p -> p.y).max().getAsInt();

        return bb;
    }

    Iterable<Integer> xValues() {
        return IntStream.rangeClosed(xMin, xMax).boxed()::iterator;
    }

    Iterable<Integer> yValues() {
        return IntStream.rangeClosed(yMin, yMax).boxed()::iterator;
    }

    void print(Function<Point, String> point2String) {
        for (int y : yValues()) {
            for (int x : xValues()) {
                System.out.print(point2String.apply(new Point(x, y)));
            }
            System.out.println();
        }
    }
}
