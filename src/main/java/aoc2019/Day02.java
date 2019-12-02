package aoc2019;

import java.util.Arrays;

/*
 * https://adventofcode.com/2019/day/2
 */
public class Day02 {

    private static final int HCF = 99;
    private static final int ADD = 1;
    private static final int MUL = 2;

    public static void main(String[] args) throws Exception {
        new Day02().part1();
        new Day02().part2();
    }

    // int[] input = { 1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50 };
    
    int[] input = { 1, 0, 0, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 9, 1, 19, 1, 5, 19, 23, 2, 9, 23, 27, 1, 27, 5,
            31, 2, 31, 13, 35, 1, 35, 9, 39, 1, 39, 10, 43, 2, 43, 9, 47, 1, 47, 5, 51, 2, 13, 51, 55, 1, 9, 55, 59, 1,
            5, 59, 63, 2, 6, 63, 67, 1, 5, 67, 71, 1, 6, 71, 75, 2, 9, 75, 79, 1, 79, 13, 83, 1, 83, 13, 87, 1, 87, 5,
            91, 1, 6, 91, 95, 2, 95, 13, 99, 2, 13, 99, 103, 1, 5, 103, 107, 1, 107, 10, 111, 1, 111, 13, 115, 1, 10,
            115, 119, 1, 9, 119, 123, 2, 6, 123, 127, 1, 5, 127, 131, 2, 6, 131, 135, 1, 135, 2, 139, 1, 139, 9, 0, 99,
            2, 14, 0, 0 };

    int run(int[] program, int noun, int verb) {
        int[] mem = Arrays.copyOf(program, program.length);
        int adr = 0;
        mem[1] = noun;
        mem[2] = verb;

        while (adr < mem.length && mem[adr] != HCF) {
            int opcode = mem[adr];
            int p1 = mem[adr + 1];
            int p2 = mem[adr + 2];
            int p3 = mem[adr + 3];

            switch (opcode) {
            case ADD:
                mem[p3] = mem[p1] + mem[p2];
                break;
                
            case MUL:
                mem[p3] = mem[p1] * mem[p2];
                break;

            case HCF:
            default:
                break;
            }

            adr += 4;
        }
        return mem[0];
    }

    void part1() throws Exception {
        var result = run(input, 12, 2);
        System.out.println(result);
    }

    void part2() throws Exception {
        for (int noun = 0; noun <= 99; ++noun)
            for (int verb = 0; verb <= 99; ++verb)
                if (run(input, noun, verb) == 19690720) {
                    System.out.println(100 * noun + verb);
                    break;
                }
    }

}
