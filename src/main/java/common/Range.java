package common;

import io.vavr.Tuple2;
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
    
    public Tuple2<Range, Range> splitAt(int c) {
        if (c < min || c > max) 
            throw new RuntimeException("invalid split of " + this + " at " + c); 
        Range r1 = new Range(min, c - 1);
        Range r2 = new Range(c, max);
        return new Tuple2<Range, Range>(r1, r2);
    }
    
    public String toString() {
        return String.format("[%d:%d]", min, max);
    }
}
