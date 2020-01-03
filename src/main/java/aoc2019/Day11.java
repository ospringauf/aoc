package aoc2019;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*
 * Day 11: Space Police
 * https://adventofcode.com/2019/day/11
 *
 */
public class Day11 {
    private static final int BLACK = 0;
    private static final int WHITE = 1;

    private static final int TURN_LEFT = 0;
    private static final int TURN_RIGHT = 1;

    static long[] my_input = { 3, 8, 1005, 8, 290, 1106, 0, 11, 0, 0, 0, 104, 1, 104, 0, 3, 8, 1002, 8, -1, 10, 1001,
            10, 1, 10, 4, 10, 108, 1, 8, 10, 4, 10, 1002, 8, 1, 28, 1006, 0, 59, 3, 8, 1002, 8, -1, 10, 101, 1, 10, 10,
            4, 10, 108, 0, 8, 10, 4, 10, 101, 0, 8, 53, 3, 8, 1002, 8, -1, 10, 101, 1, 10, 10, 4, 10, 1008, 8, 0, 10, 4,
            10, 101, 0, 8, 76, 1006, 0, 81, 1, 1005, 2, 10, 3, 8, 102, -1, 8, 10, 1001, 10, 1, 10, 4, 10, 1008, 8, 1,
            10, 4, 10, 1002, 8, 1, 105, 3, 8, 102, -1, 8, 10, 1001, 10, 1, 10, 4, 10, 108, 1, 8, 10, 4, 10, 1001, 8, 0,
            126, 3, 8, 1002, 8, -1, 10, 1001, 10, 1, 10, 4, 10, 108, 1, 8, 10, 4, 10, 1002, 8, 1, 148, 3, 8, 102, -1, 8,
            10, 101, 1, 10, 10, 4, 10, 1008, 8, 1, 10, 4, 10, 1001, 8, 0, 171, 3, 8, 1002, 8, -1, 10, 1001, 10, 1, 10,
            4, 10, 1008, 8, 0, 10, 4, 10, 101, 0, 8, 193, 1, 1008, 8, 10, 1, 106, 3, 10, 1006, 0, 18, 3, 8, 1002, 8, -1,
            10, 1001, 10, 1, 10, 4, 10, 108, 0, 8, 10, 4, 10, 1001, 8, 0, 225, 1, 1009, 9, 10, 1006, 0, 92, 3, 8, 1002,
            8, -1, 10, 1001, 10, 1, 10, 4, 10, 108, 0, 8, 10, 4, 10, 1001, 8, 0, 254, 2, 1001, 8, 10, 1, 106, 11, 10, 2,
            102, 13, 10, 1006, 0, 78, 101, 1, 9, 9, 1007, 9, 987, 10, 1005, 10, 15, 99, 109, 612, 104, 0, 104, 1, 21102,
            1, 825594852136L, 1, 21101, 0, 307, 0, 1106, 0, 411, 21101, 0, 825326580628L, 1, 21101, 0, 318, 0, 1105, 1,
            411, 3, 10, 104, 0, 104, 1, 3, 10, 104, 0, 104, 0, 3, 10, 104, 0, 104, 1, 3, 10, 104, 0, 104, 1, 3, 10, 104,
            0, 104, 0, 3, 10, 104, 0, 104, 1, 21102, 179557207043L, 1, 1, 21101, 0, 365, 0, 1106, 0, 411, 21101, 0,
            46213012483L, 1, 21102, 376, 1, 0, 1106, 0, 411, 3, 10, 104, 0, 104, 0, 3, 10, 104, 0, 104, 0, 21101,
            988648727316L, 0, 1, 21102, 399, 1, 0, 1105, 1, 411, 21102, 988224959252L, 1, 1, 21101, 0, 410, 0, 1106, 0,
            411, 99, 109, 2, 21201, -1, 0, 1, 21101, 0, 40, 2, 21102, 1, 442, 3, 21101, 432, 0, 0, 1105, 1, 475, 109,
            -2, 2105, 1, 0, 0, 1, 0, 0, 1, 109, 2, 3, 10, 204, -1, 1001, 437, 438, 453, 4, 0, 1001, 437, 1, 437, 108, 4,
            437, 10, 1006, 10, 469, 1102, 0, 1, 437, 109, -2, 2105, 1, 0, 0, 109, 4, 2102, 1, -1, 474, 1207, -3, 0, 10,
            1006, 10, 492, 21101, 0, 0, -3, 21202, -3, 1, 1, 22102, 1, -2, 2, 21101, 0, 1, 3, 21102, 511, 1, 0, 1105, 1,
            516, 109, -4, 2105, 1, 0, 109, 5, 1207, -3, 1, 10, 1006, 10, 539, 2207, -4, -2, 10, 1006, 10, 539, 21201,
            -4, 0, -4, 1106, 0, 607, 21202, -4, 1, 1, 21201, -3, -1, 2, 21202, -2, 2, 3, 21101, 558, 0, 0, 1106, 0, 516,
            22101, 0, 1, -4, 21101, 1, 0, -1, 2207, -4, -2, 10, 1006, 10, 577, 21102, 1, 0, -1, 22202, -2, -1, -2, 2107,
            0, -3, 10, 1006, 10, 599, 21201, -1, 0, 1, 21101, 0, 599, 0, 105, 1, 474, 21202, -2, -1, -2, 22201, -4, -2,
            -4, 109, -5, 2106, 0, 0 };

    // Emergency Hull Painting Robot
    static class EHPR extends IntComputer {

        boolean paintMode = true;

        Pose pos = new Pose(0, 0, Direction.NORTH);
        Function<Point, Integer> camera;
        BiConsumer<Point, Integer> paint;

        EHPR(long[] program) {
            super(program);
            input = () -> camera.apply(pos); 
        }

        void move(int turn) {
            switch (turn) {
            case TURN_RIGHT:
                pos.turnRight();
                break;
            case TURN_LEFT:
                pos.turnLeft();
            }
            pos = pos.ahead();
        }

        @Override
        void output(long value) {
            int i = (int)value; 
            if (paintMode)
                paint.accept(pos, i);
            else
                move(i);
            paintMode = !paintMode;
            
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== part1 ===");
        new Day11().part1();
        System.out.println("=== part2 ===");
        new Day11().part2();

    }

    void part1() {
        var hull = new HashMap<Point, Integer>();
        var robo = new EHPR(my_input);
        robo.camera = p -> hull.getOrDefault(p, BLACK);
        robo.paint = hull::put;
        robo.run();
        System.out.println(hull.size());
    }

    void part2() {
        var hull = new HashMap<Point, Integer>();
        var robo = new EHPR(my_input);
        hull.put(robo.pos, WHITE);
        robo.camera = p -> hull.getOrDefault(p, BLACK);
        robo.paint = hull::put;
        robo.run();

        Function<Point, Character> pointColor = p -> hull.getOrDefault(p, BLACK) == WHITE ? '#' : ' ';
        
        var bb = BoundingBox.of(hull.keySet());
        bb.print(pointColor);
    }
}
