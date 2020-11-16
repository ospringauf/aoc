package aoc2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {

    static class Instruction {
        int xmin;
        int ymin;
        int xmax;
        int ymax;

        IntFunction<Integer> op1;
        IntFunction<Integer> op2;

        Instruction(String s) {
            String[] f = s.replace("turn ", "turn_").split(" ");
            xmin = Integer.parseInt(f[1].split(",")[0]);
            ymin = Integer.parseInt(f[1].split(",")[1]);
            xmax = Integer.parseInt(f[3].split(",")[0]);
            ymax = Integer.parseInt(f[3].split(",")[1]);

            switch (f[0]) {
            case "toggle":
                op1 = i -> (i + 1) % 2;
                op2 = i -> i + 2;
                break;
            case "turn_on":
                op1 = i -> 1;
                op2 = i -> i + 1;
                break;
            case "turn_off":
                op1 = i -> 0;
                op2 = i -> Math.max(0, i - 1);
            default:
                break;
            }
        }

        public void applyOp1(int[][] lights) {
            for (int x = xmin; x <= xmax; ++x)
                for (int y = ymin; y <= ymax; ++y)
                    lights[x][y] = op1.apply(lights[x][y]);
        }

        public void applyOp2(int[][] lights) {
            for (int x = xmin; x <= xmax; ++x)
                for (int y = ymin; y <= ymax; ++y)
                    lights[x][y] = op2.apply(lights[x][y]);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Instruction> instr = Files.readAllLines(Paths.get("src/main/java/aoc2015/day06.txt"))
                .stream()
                .map(Instruction::new)
                .collect(Collectors.toList());

        
        System.out.println("=== part 1");

        int[][] lights1 = new int[1000][1000];
        instr.forEach(i -> i.applyOp1(lights1));
        System.out.println(countLights(lights1));
        
        System.out.println("=== part 2");

        int[][] lights2 = new int[1000][1000];
        instr.forEach(i -> i.applyOp2(lights2));
        System.out.println(countLights(lights2));
    }

    private static int countLights(int[][] lights) {
        return IntStream.range(0, 1000000).map(i -> lights[i / 1000][i % 1000]).sum();
    }
}
