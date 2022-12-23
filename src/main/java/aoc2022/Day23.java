package aoc2022;

import java.util.function.Predicate;

import common.AocPuzzle;
import common.BoundingBox;
import common.Direction;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 23: Unstable Diffusion ---
// https://adventofcode.com/2022/day/23

class Day23 extends AocPuzzle {

    public static void main(String[] args) {
        // part1: 3757, part2: 918
        timed(() -> new Day23().solve());
    }

//    List<String> data = Util.splitLines(example2);
    List<String> data = file2lines("input23.txt");
    
    Set<Point> elves;

    record Check(Predicate<Point> scan, Direction dir) {
    }

    List<Check> checks = List.empty();

    {
        Check checkNorth = new Check(
                p -> !List.of(p.north(), p.north().west(), p.north().east()).exists(n -> elves.contains(n)),
                Direction.NORTH);
        Check checkSouth = new Check(
                p -> !List.of(p.south(), p.south().west(), p.south().east()).exists(n -> elves.contains(n)),
                Direction.SOUTH);
        Check checkWest = new Check(
                p -> !List.of(p.west(), p.north().west(), p.south().west()).exists(n -> elves.contains(n)),
                Direction.WEST);
        Check checkEast = new Check(
                p -> !List.of(p.east(), p.north().east(), p.south().east()).exists(n -> elves.contains(n)),
                Direction.EAST);

        checks = List.of(checkNorth, checkSouth, checkWest, checkEast);
    }

    Point proposeMove(Point elf) {
        var n8 = elf.neighbors8();
        if (!n8.exists(x -> elves.contains(x)))
            return elf;

        for (var c : checks) {
            if (c.scan.test(elf))
                return elf.translate(c.dir);
        }
        return elf;
    }

    void solve() {
        PointMap<Character> map = new PointMap<Character>();
        map.read(data);
        elves = HashSet.ofAll(map.findPoints('#'));
        System.out.println("elves: " + elves.size());

        int round = 0;
        boolean moving = true;
        
        while (moving) {
            round++;
            System.out.print('.');
            if (round%100==0) System.out.println();

            var next = elves.toMap(e -> e, e -> proposeMove(e));
            
            moving = next.exists(t -> ! t._1.equals(t._2));
            
            var dest = next.values().toList();
            Predicate<Point> unique = p -> dest.existsUnique(d -> d.equals(p));
            elves = next.map(t -> unique.test(t._2)? t._2 : t._1).toSet();
            
            checks = checks.tail().append(checks.head());

            if (round == 10) {
                System.out.println("\n=== part 1"); // 3757
                var rectangle = BoundingBox.of(elves.toJavaSet());
                var emptyTiles = rectangle.width() * rectangle.height() - elves.size();
                System.out.println(emptyTiles);
            }
        }
        
        System.out.println("\n=== part 2"); // 918
        System.out.println(round);
    }

    static String example1 = """
            .....
            ..##.
            ..#..
            .....
            ..##.
            .....
                        """;

    static String example2 = """
            ....#..
            ..###.#
            #...#.#
            .#...##
            #.###..
            ##.#.##
            .#..#..
                        """;

}
