package aoc2021;

import java.util.function.Function;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import io.vavr.collection.Array;
import io.vavr.collection.List;

// --- Day 20: Trench Map ---
// https://adventofcode.com/2021/day/20

class Day20 extends AocPuzzle {

//   String input = example;
     String input = file2string("input20.txt");

    Image img = new Image();
    Array<Integer> algorithm;

    @SuppressWarnings("serial")
    static class Image extends PointMap<Integer> {
        int background = 0;
        static boolean invert = false;

        int pixelValue(Point p) {
            var n = List.of( //
                    p.translate(-1, -1), p.translate(0, -1), p.translate(1, -1), //
                    p.translate(-1, 0), p, p.translate(1, 0), //
                    p.translate(-1, 1), p.translate(0, 1), p.translate(1, 1));
            return n.map(np -> getOrDefault(np, background)).foldLeft(0, (r, i) -> 2 * r + i);
        }

        Image enhance(Array<Integer> alg) {
            var next = new Image();

            boundingBox()
                .expand(1, 1)
                .generatePoints()
                .forEach(p -> next.put(p, alg.get(pixelValue(p))));
            
            if (invert)
                next.background = (background == 0) ? 1 : 0;
            return next;
        }

        public void print() {
            super.print(i -> (i == 1) ? '#' : '.');
        }
    }

    void solve() {
        String[] blocks = input.split("\n\n");

        Function<Character, Integer> convertInput = c -> (c == '#') ? 1 : 0;
        algorithm = Array.ofAll(blocks[0].toCharArray()).map(convertInput);
        Image.invert = algorithm.get(0) == 1;

        img.read(blocks[1].split("\n"), convertInput);
//      img.print();

        // 2 enhancement rounds
        int i = 1;
        for (; i <= 2; ++i) {
            img = img.enhance(algorithm);
        }

        System.out.println("=== part 1");
        System.out.println(img.countValues(1)); // 5619
        // img.print();

        // 48 more enhancement rounds
        for (; i <= 50; ++i) {
            img = img.enhance(algorithm);
        }

        System.out.println("=== part 2");
        System.out.println(img.countValues(1)); // 20122
    }

    public static void main(String[] args) {
        timed(() -> new Day20().solve());
    }

    static String example =
            """
                    ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

                    #..#.
                    #....
                    ##..#
                    ..#..
                    ..###						""";

}
