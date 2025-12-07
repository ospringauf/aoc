package aoc2025;

import common.AocPuzzle;
import common.BoundingBox;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.Function1;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

//--- Day 7: Laboratories ---
// https://adventofcode.com/2025/day/7

class Day07 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1587
        timed(() -> new Day07().part1());
        System.out.println("=== part 2"); // 5748679033029
        timed(() -> new Day07().part2());
    }

    List<String> data = file2lines("input07.txt");
//	List<String> data = Util.splitLines(example);

    private PointMap<Character> m;
    private BoundingBox bb;

    Day07() {
        m = new PointMap<Character>();
        m.read(data);
        bb = m.boundingBox();
    }

    
    void part1() {
        var s = m.findPoint('S');
        var tachyons = HashSet.of(s); // no duplicates!
        var y = s.y();

        var r = 0;
        while (y <= bb.yMax()) {
            var split = tachyons.filter(this::isSplitter);
            r += split.size();

            tachyons = tachyons
                    .addAll(split.map(p -> p.west()))
                    .addAll(split.map(p -> p.east()))
                    .removeAll(split)
                    .map(p -> p.south());
            y++;
        }
        System.out.println(r);
    }

    private boolean isSplitter(Point p) {
        return m.getOrDefault(p, '.') == '^';
    }

    long timelines(Point p) {
        if (p.y() == bb.yMax())
            return 1;
        if (isSplitter(p))
            return m_timelines.apply(p.west().south()) + m_timelines.apply(p.east().south());
        else
            return m_timelines.apply(p.south());
    }

    Function1<Point, Long> m_timelines = Function1.of(this::timelines).memoized();

    void part2() {
        var s = m.findPoint('S');
        var r = timelines(s);
        System.out.println(r);
    }

    static String example = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............""";
}
