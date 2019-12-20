package aoc2019;

import java.util.stream.Collectors;

/*
 * Day 17: Set and Forget
 * https://adventofcode.com/2019/day/17
 *
 */
public class Day17manual {

    static final long[] PROGRAM = Util.readIntProg("input17.txt");

    static class AsciiCam extends IntComputer {
        AsciiCam(long[] program) {
            super(program);
        }

        @Override
        void output(long value) {
            if (value > 255)
                System.out.println(value);
            else
                System.out.print((char) value);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== part 1 ===");
        new Day17manual().part1a();
        new Day17manual().part1b();

        System.out.println("=== part 2 ===");
        new Day17manual().part2();
    }

    void part1a() {
        var robo = new AsciiCam(PROGRAM);
        robo.run();
    }

    void part1b() throws Exception {
        String PIXELS = "#.^v><";
        PointMap<Integer> map = new PointMap<>();
        map.read(Util.linesArray("output17.txt"), PIXELS::indexOf);

//        map.boundingBox().print(p -> PIXELS.charAt(map.get(p)));

        final int SCAFF = PIXELS.indexOf('#');
        var scaffolds = map.findPoints(SCAFF).collect(Collectors.toSet());
        var intersections = map.findPoints(SCAFF).filter(p -> p.neighbors().allMatch(scaffolds::contains));
        var sap = intersections.mapToInt(p -> p.x * p.y).sum();
        System.out.println(sap);
    }

    void part2() {
        var A = "R,4,L,12,R,6,L,12";
        var B = "R,10,R,6,R,4";
        var C = "R,4,R,10,R,8,R,4";
        var M = "C,B,C,B,A,B,A,C,B,A";
        var video = "n";

        var commands = M + "\n" + A + "\n" + B + "\n" + C + "\n" + video + "\n";
        var input = commands.chars().boxed().collect(Collectors.toList());

        var robo = new AsciiCam(PROGRAM);
        robo.input = () -> input.remove(0);
        robo.mem[0] = 2;
        robo.run();
    }
}

// solved by hand:
// R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,10,R,6,R,4,R,4,L,12,R,6,L,12,R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,L,12,R,6,L,12
//
//     01234567890123456789
// a = R,4,L,12,R,6,L,12
// m = R,4,R,10,R,8,R,4,R,10,R,6,R,4,R,4,R,10,R,8,R,4,R,10,R,6,R,4,a,R,10,R,6,R,4,a,R,4,R,10,R,8,R,4,R,10,R,6,R,4,a
//
// b = R,10,R,6,R,4
// m = R,4,R,10,R,8,R,4,b,R,4,R,10,R,8,R,4,b,a,b,a,R,4,R,10,R,8,R,4,b,a
//
// c = R,4,R,10,R,8,R,4
// m = C,B,C,B,A,B,A,C,B,A

