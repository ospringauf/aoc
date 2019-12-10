package aoc2019;


public class Point {
    int x;
    int y;

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
