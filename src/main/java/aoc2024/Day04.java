package aoc2024;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.*;

//--- Day 4: Ceres Search ---
// https://adventofcode.com/2024/day/4

class Day04 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 2496
        timed(() -> new Day04().part1());
        System.out.println("=== part 2"); // 1967
        timed(() -> new Day04().part2());
    }

    List<String> data = file2lines("input04.txt");
//	List<String> data = Util.splitLines(example);
	PointMap<Character> m = new PointMap<>();

	record Dir(int x, int y) {
	    Dir mult(int f) {
	        return new Dir(f*x, f*y);
	    }
	    Point translate(Point p) {
	        return p.translate(x, y);
	    }
	}
	
    void part1() {
        m.read(data);        
        var x = m.findPoints('X');
        var r = x.map(p -> xmas(p)).sum();
                
        System.out.println(r);
    }
    
    // count the XMAS starting at p
    int xmas(Point p) {
        var dirs = List.of(-1, 0, 1).crossProduct().map(t -> new Dir(t._1, t._2));        
        var options = dirs.map(d -> List.of(0,1,2,3).map(f -> d.mult(f).translate(p)));
        return options.count(l -> "XMAS".equals(word(l)));
    }

    String word(List<Point> l) {
        return l.map(p -> m.get(p)).mkString();
    }

    
    void part2() {
        m.read(data);        
        var a = m.findPoints('A');
        var r = a.count(p -> mas(p));
        
        System.out.println(r);
    }

    // check if p is the center of two diagonal MAS
    boolean mas(Point p) {
        var dirs = List.of(-1, 1).crossProduct().map(t -> new Dir(t._1, t._2));
        var options = dirs.map(d -> List.of(-1,0,1).map(f -> d.mult(f).translate(p)));
        var c = options.count(l -> "MAS".equals(word(l)));
        return c == 2; // or c > 1? makes no difference.
    }
    
    static String example = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX            
""";
}
