package aoc2025;


import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Tuple;
import io.vavr.collection.*;

//--- Day 9: Movie Theater ---
// https://adventofcode.com/2025/day/9

class Day09Ugly extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 4758598740
        timed(() -> new Day09Ugly().part1());
        System.out.println("=== part 2"); // 1474699155
        timed(() -> new Day09Ugly().part2());
    }

    List<String> data = file2lines("input09.txt");
//	List<String> data = Util.splitLines(example);

    long area(Point p1, Point p2) {
        return (Math.abs(p2.x() - p1.x()) + 1L) * (Math.abs(p2.y() - p1.y()) + 1L);
    }

    void part1() {
        var tiles = data.map(Point::parse);
//		System.out.println(tiles);

        var max = tiles.combinations(2).map(l -> area(l.get(0), l.get(1))).max();
        System.out.println(max);

    }

    record Line(Point p1, Point p2) {

        List<Point> points() {
            int dx = p2.x() - p1.x();
            int dy = p2.y() - p1.y();
            int sgx = (int) Math.signum(dx);
            int sgy = (int) Math.signum(dy);
            int len = Math.abs(Math.max(dx * sgx, dy * sgy));
            return List.rangeClosed(0, len).map(i -> p1.translate((i * sgx), (i * sgy)));
        }
    }

    void part2() {
        var red = data.map(Point::parse);
        var lines = red.append(red.head()).sliding(2).map(x -> new Line(x.get(0), x.get(1)));

        var green = lines.flatMap(l -> l.points()).toList().removeAll(red);

        System.out.println("candidates: " + red.combinations(2).size());
        System.out.println("red: " + red.size());
        System.out.println("green: " + green.size());

        var m0 = new PointMap<Character>();
        red.forEach(t -> m0.put(t, '#'));
        var bb = m0.boundingBox();
        System.out.println(bb.width());
        System.out.println(bb.height());
        System.out.println(bb.width() * bb.height());

        var redx = red.map(p -> p.x()).toSet();
        int[] mx = new int[bb.xMax()+10];
        int i=1;
        boolean used = false;
        for (int x=0; x<=bb.xMax(); ++x) {
            boolean hasRed = redx.contains(x);
            if (hasRed) {
                if (used) i++;
                mx[x] = i;
                i++;
                used=false;
            } else {
                mx[x] = i;
                used=true;
            }
        }
        
        var redy = red.map(p -> p.y()).toSet();
        int[] my = new int[bb.yMax()+10];
        i=1;
        used = false;
        for (int y=0; y<=bb.yMax(); ++y) {
            boolean hasRed = redy.contains(y);
            if (hasRed) {
                if (used) i++;
                my[y] = i;
                i++;
                used=false;
            } else {
                my[y] = i;
                used=true;
            }
        }
        
        var mred= red.map(p -> Point.of(mx[p.x()], my[p.y()]));
        
        var m = new PointMap<Character>();
        for (var p : green) 
            m.put(Point.of(mx[p.x()], my[p.y()]), 'X');
        for (var p : mred)            
            m.put(p, '#');

        
        var s = Point.of(mx[bb.xMax()/2], my[bb.yMax()/3]);
//        s = Point.of(6,3);
        m.put(s, 'S');
        m.print();
//        System.out.println();
        
        
        System.out.println("filling");
        floodFill(m, s);
        System.out.println("done filling");
        
        var contained = mred.combinations(2).filter(l -> contained(m, l));
        
        System.out.println("contained: " + contained.size());
        
        var rx = List.ofAll(mx).groupBy(x -> x).map((x,l) -> Tuple.of(x, l.size()));
        var ry = List.ofAll(my).groupBy(x -> x).map((x,l) -> Tuple.of(x, l.size()));
        
        System.out.println(rx);
//        m.print();
        var ca = contained.map(c -> area2(c, rx, ry));
        
        var max = contained.maxBy(c -> area2(c, rx, ry)).get();
        var bb2 = BoundingBox.of(max.toJavaList());
        bb2.generatePoints().forEach(p -> m.put(p, 'O'));
        System.out.println();
        m.print();
        
        System.out.println(area2(max, rx, ry));
    }
    
    private long area2(List<Point> l, Map<Integer, Integer> rx, Map<Integer, Integer> ry) {
        var p0 = l.get(0);
        var p1 = l.get(1);
        var n = new Line(p0, Point.of(p1.x(), p0.y())).points();
        var w = new Line(p0, Point.of(p0.x(), p1.y())).points();
        var dx = n.map(p -> rx.getOrElse(p.x(), 0)).sum().longValue();
        var dy = w.map(p -> ry.getOrElse(p.y(), 0)).sum().longValue();
        return dx * dy;
    }

    boolean contained(PointMap<Character> m, List<Point> l) {
        var p0 = l.get(0);
        var p1 = l.get(1);
        var n = new Line(p0, Point.of(p1.x(), p0.y())).points();
        var s = new Line(p1, Point.of(p0.x(), p1.y())).points();
        var w = new Line(p0, Point.of(p0.x(), p1.y())).points();
        var e = new Line(p1, Point.of(p1.x(), p0.y())).points();
        var k = m.keySet();
        var c = n.forAll(p -> k.contains(p)) && s.forAll(p -> k.contains(p)) && e.forAll(p -> k.contains(p)) && w.forAll(p -> k.contains(p));
        return c;
    }
    

    void floodFill(PointMap<Character> m, Point start) {
        var area = io.vavr.collection.HashSet.of(start);
        var front = io.vavr.collection.HashSet.of(start);
        boolean cont = true;
        while (cont) {
            var next = front.flatMap(Point::neighbors).removeAll(m.keySet());
            next = next.removeAll(area);
            cont = next.nonEmpty();
            front = next;
            next.forEach(p -> m.put(p, 'x'));
        }
    }
    

    static String example = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""";
}
