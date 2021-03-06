package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

/*
  * Day 9: Sensor Boost
  * https://adventofcode.com/2019/day/9
  */
public class Day09 {

    public static void main(String[] args) throws Exception {
        System.out.println("=== diag 1 ===");
        new IntComp(example1a, List.of(0)).run(null);
        System.out.println("=== diag 2 ===");
        new IntComp(example1b, List.of(0)).run(null);
        System.out.println("=== diag 3 ===");
        new IntComp(example1c, List.of(0)).run(null);

        System.out.println("=== part1 ==="); // 3409270027
        new IntComp(my_input, List.of(1)).run(null);

        System.out.println("=== part2 ==="); // 82760
        new IntComp(my_input, List.of(2)).run(null);
    }

    static long[] example1a = { 109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99 };
    static long[] example1b = { 1102, 34915192, 34915192, 7, 4, 7, 99, 0 };
    static long[] example1c = { 104, 1125899906842624L, 99 };

    static long[] my_input = { 1102, 34463338, 34463338, 63, 1007, 63, 34463338, 63, 1005, 63, 53, 1101, 0, 3, 1000,
            109, 988, 209, 12, 9, 1000, 209, 6, 209, 3, 203, 0, 1008, 1000, 1, 63, 1005, 63, 65, 1008, 1000, 2, 63,
            1005, 63, 904, 1008, 1000, 0, 63, 1005, 63, 58, 4, 25, 104, 0, 99, 4, 0, 104, 0, 99, 4, 17, 104, 0, 99, 0,
            0, 1101, 0, 252, 1023, 1101, 0, 0, 1020, 1102, 1, 39, 1013, 1102, 1, 234, 1029, 1102, 26, 1, 1016, 1101, 37,
            0, 1005, 1101, 0, 27, 1011, 1101, 21, 0, 1000, 1101, 0, 29, 1019, 1101, 35, 0, 1003, 1102, 22, 1, 1007,
            1102, 1, 32, 1001, 1101, 1, 0, 1021, 1102, 1, 216, 1027, 1102, 30, 1, 1012, 1102, 1, 24, 1009, 1101, 36, 0,
            1002, 1101, 0, 31, 1010, 1101, 0, 243, 1028, 1102, 787, 1, 1024, 1102, 255, 1, 1022, 1102, 33, 1, 1017,
            1102, 1, 23, 1004, 1102, 778, 1, 1025, 1102, 1, 28, 1008, 1101, 0, 223, 1026, 1102, 1, 25, 1015, 1101, 0,
            20, 1006, 1102, 34, 1, 1014, 1101, 38, 0, 1018, 109, -4, 1202, 5, 1, 63, 1008, 63, 32, 63, 1005, 63, 203, 4,
            187, 1106, 0, 207, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 37, 2106, 0, -6, 1001, 64, 1, 64, 1106, 0, 225, 4,
            213, 1002, 64, 2, 64, 109, 3, 2106, 0, -8, 4, 231, 1001, 64, 1, 64, 1105, 1, 243, 1002, 64, 2, 64, 109, -12,
            2105, 1, -1, 1105, 1, 261, 4, 249, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, -13, 2102, 1, -3, 63, 1008, 63,
            31, 63, 1005, 63, 285, 1001, 64, 1, 64, 1106, 0, 287, 4, 267, 1002, 64, 2, 64, 109, 6, 21102, 40, 1, 0,
            1008, 1017, 40, 63, 1005, 63, 313, 4, 293, 1001, 64, 1, 64, 1105, 1, 313, 1002, 64, 2, 64, 109, -10, 2107,
            31, -6, 63, 1005, 63, 331, 4, 319, 1105, 1, 335, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, -6, 2102, 1, 7, 63,
            1008, 63, 28, 63, 1005, 63, 357, 4, 341, 1105, 1, 361, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 2, 21107, 41,
            40, 8, 1005, 1011, 377, 1106, 0, 383, 4, 367, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, -1, 1201, 2, 0, 63,
            1008, 63, 26, 63, 1005, 63, 403, 1106, 0, 409, 4, 389, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 22, 1205, -4,
            425, 1001, 64, 1, 64, 1105, 1, 427, 4, 415, 1002, 64, 2, 64, 109, -9, 21101, 42, 0, 3, 1008, 1018, 39, 63,
            1005, 63, 451, 1001, 64, 1, 64, 1105, 1, 453, 4, 433, 1002, 64, 2, 64, 109, 3, 21107, 43, 44, 0, 1005, 1018,
            475, 4, 459, 1001, 64, 1, 64, 1105, 1, 475, 1002, 64, 2, 64, 109, -7, 21101, 44, 0, 0, 1008, 1011, 44, 63,
            1005, 63, 497, 4, 481, 1105, 1, 501, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 17, 1206, -7, 513, 1105, 1, 519,
            4, 507, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, -24, 1207, 5, 25, 63, 1005, 63, 537, 4, 525, 1105, 1, 541,
            1001, 64, 1, 64, 1002, 64, 2, 64, 109, 7, 21108, 45, 43, 2, 1005, 1013, 557, 1106, 0, 563, 4, 547, 1001, 64,
            1, 64, 1002, 64, 2, 64, 109, -5, 1207, -3, 34, 63, 1005, 63, 583, 1001, 64, 1, 64, 1106, 0, 585, 4, 569,
            1002, 64, 2, 64, 109, 5, 21108, 46, 46, 5, 1005, 1016, 607, 4, 591, 1001, 64, 1, 64, 1105, 1, 607, 1002, 64,
            2, 64, 109, -12, 2108, 20, 8, 63, 1005, 63, 627, 1001, 64, 1, 64, 1105, 1, 629, 4, 613, 1002, 64, 2, 64,
            109, 24, 1206, -3, 647, 4, 635, 1001, 64, 1, 64, 1105, 1, 647, 1002, 64, 2, 64, 109, -30, 2108, 32, 8, 63,
            1005, 63, 665, 4, 653, 1106, 0, 669, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 22, 1208, -9, 20, 63, 1005, 63,
            691, 4, 675, 1001, 64, 1, 64, 1106, 0, 691, 1002, 64, 2, 64, 109, -4, 21102, 47, 1, 3, 1008, 1014, 49, 63,
            1005, 63, 715, 1001, 64, 1, 64, 1105, 1, 717, 4, 697, 1002, 64, 2, 64, 109, -10, 2101, 0, 1, 63, 1008, 63,
            36, 63, 1005, 63, 743, 4, 723, 1001, 64, 1, 64, 1105, 1, 743, 1002, 64, 2, 64, 109, 16, 1201, -9, 0, 63,
            1008, 63, 28, 63, 1005, 63, 769, 4, 749, 1001, 64, 1, 64, 1105, 1, 769, 1002, 64, 2, 64, 109, 2, 2105, 1, 5,
            4, 775, 1001, 64, 1, 64, 1106, 0, 787, 1002, 64, 2, 64, 109, -5, 1202, -6, 1, 63, 1008, 63, 26, 63, 1005,
            63, 807, 1106, 0, 813, 4, 793, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, -16, 2107, 37, 4, 63, 1005, 63, 833,
            1001, 64, 1, 64, 1105, 1, 835, 4, 819, 1002, 64, 2, 64, 109, 2, 2101, 0, 1, 63, 1008, 63, 34, 63, 1005, 63,
            855, 1105, 1, 861, 4, 841, 1001, 64, 1, 64, 1002, 64, 2, 64, 109, 19, 1205, 2, 875, 4, 867, 1105, 1, 879,
            1001, 64, 1, 64, 1002, 64, 2, 64, 109, -2, 1208, -8, 23, 63, 1005, 63, 899, 1001, 64, 1, 64, 1106, 0, 901,
            4, 885, 4, 64, 99, 21101, 0, 27, 1, 21102, 915, 1, 0, 1106, 0, 922, 21201, 1, 61455, 1, 204, 1, 99, 109, 3,
            1207, -2, 3, 63, 1005, 63, 964, 21201, -2, -1, 1, 21102, 942, 1, 0, 1105, 1, 922, 22102, 1, 1, -1, 21201,
            -2, -3, 1, 21102, 1, 957, 0, 1105, 1, 922, 22201, 1, -1, -2, 1106, 0, 968, 22101, 0, -2, -2, 109, -3, 2105,
            1, 0 };

    static final int AMPLIFIERS = 5;
    static final int LAST_AMP = AMPLIFIERS - 1;

    static class IntComp {

        private static final int HCF = 99;
        private static final int ADD = 1;
        private static final int MUL = 2;
        private static final int INP = 3;
        private static final int OUT = 4;
        private static final int JIT = 5;
        private static final int JIF = 6;
        private static final int LES = 7;
        private static final int EQU = 8;
        private static final int RBO = 9;

        int adr = 0;
        int relBase = 0;
        long[] mem;
        List<Integer> input = new ArrayList<>();
        long output = -1;

        IntComp(long[] program, List<Integer> data) {
            mem = Arrays.copyOf(program, 1000000);
            input.addAll(data);
        }

        boolean halted() {
            return mem[adr] == HCF;
        }

        long run(List<Integer> data) {
             if (halted())
                 throw new RuntimeException("already halted - cannot run");

             if (data != null) input.addAll(data);

             execution: while (!halted()) {

                 long instr = mem[adr];
                 int opcode = (int) (instr % 100);

                 boolean imm[] =  { false, (instr / 100 % 10) == 1, (instr / 1000 % 10) == 1, (instr / 10000 % 10) == 1 };
                 boolean rel[] =  { false, (instr / 100 % 10) == 2, (instr / 1000 % 10) == 2, (instr / 10000 % 10) == 2 };
                 long[] p = { 0, mem[adr+1], mem[adr+2], mem[adr+3] };

                 IntFunction<Long> arg = n -> imm[n] ? p[n] : rel[n] ? mem[(int) (p[n]+relBase)] : mem[(int) p[n]];
                 IntFunction<Integer> tgt = n -> (int) (rel[n] ? p[n] + relBase : p[n]);

                 switch (opcode) {
                 case ADD:
                     mem[tgt.apply(3)] = arg.apply(1) + arg.apply(2);
                     adr += 4;
                     break;

                 case MUL:
                     mem[tgt.apply(3)] = arg.apply(1) * arg.apply(2);
                     adr += 4;
                     break;

                 case INP:
                     mem[tgt.apply(1)] = input.remove(0);
                     adr += 2;
                     break;

                 case OUT:
                     output = arg.apply(1);
                     System.out.println(output);
                     adr += 2;
                     break; // execution;

                 case JIT:
                     if (arg.apply(1) != 0)
                         adr = arg.apply(2).intValue();
                     else
                         adr += 3;
                     break;

                 case JIF:
                     if (arg.apply(1) == 0)
                         adr = arg.apply(2).intValue();
                     else
                         adr += 3;
                     break;

                 case LES:
                     mem[tgt.apply(3)] = (arg.apply(1) < arg.apply(2)) ? 1 : 0;
                     adr += 4;
                     break;

                 case EQU:
                     mem[tgt.apply(3)] = (arg.apply(1) == arg.apply(2)) ? 1 : 0;
                     adr += 4;
                     break;

                 case RBO:
                     relBase += arg.apply(1);
                     adr += 2;
                     break;

                 case HCF:
                     System.out.println("halt");
                     break;
                 default:
                     throw new RuntimeException("invalid opcode " + opcode + " at adr " + adr);
                 }

             }
             return output;
         }
    }

}
