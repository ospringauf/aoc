package common;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;

public record Range(int min, int max) {
    public int size() {
        return max - min + 1;
    }

    public boolean contains(int x) {
        return min <= x && x <= max;
    }

    public boolean isEdge(int x) {
        return x == min || x == max;
    }
    
    public List<Integer> values() {
        return List.rangeClosed(min, max);
    }

    public static Range of(Traversable<Integer> s) {
        return new Range(s.min().get(), s.max().get());
    }

    public static Range empty() {
        return new Range(0,-1);
    }
}
