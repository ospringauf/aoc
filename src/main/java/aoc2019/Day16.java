package aoc2019;

import java.util.stream.IntStream;

/*
 * Day 16: Flawed Frequency Transmission
 * https://adventofcode.com/2019/day/16
 *
 */
public class Day16 {

    static String my_input =
            "59793513516782374825915243993822865203688298721919339628274587775705006728427921751430533510981343323758576985437451867752936052153192753660463974146842169169504066730474876587016668826124639010922391218906707376662919204980583671961374243713362170277231101686574078221791965458164785925384486127508173239563372833776841606271237694768938831709136453354321708319835083666223956618272981294631469954624760620412170069396383335680428214399523030064601263676270903213996956414287336234682903859823675958155009987384202594409175930384736760416642456784909043049471828143167853096088824339425988907292558707480725410676823614387254696304038713756368483311";
    static int my_offset = 5979351; // = 6500000 - 520649

    static final int[] basePat = { 0, 1, 0, -1 };

    public static void main(String[] args) {

        // System.out.println("=== test 1 ===");
        // // new Day16().part1(toNum("12345678"), 4);
        // time(() -> new Day16().part1(toNum("80871224585914546619083218645595"), 100));
        // // new Day16().part1(toNum("69317163492948606335995924319873"), 100);
        // // new Day16().part1(toNum(my_input), 100);
        //
        System.out.println("=== part 1 ===");
        time(() -> part1(toNum(my_input), 100));
        //
        System.out.println("=== part 2 ===");
//        part2("03036732577212944063491565474664", 10000, 303673);        
//        part2("02935109699940807407585447034323", 10000, 293510);
        
        time(() -> part2(my_input, 10000, my_offset));
        
        System.out.println("=== end ===");
    }

        
    // idea: lower-right quadrant of the coefficient matrix (to the 100) is a symmetric triangular matrix
    // these coefficients can be easily calculated
    static void part2(String s, int repeat, int offset) {
        int n = 1000000;
        
        // calc coefficients for last 1000000 input digits
        // = M^100 for the pattern matrix
        int[] coeff = new int[n];
        for (int i=0; i<n; ++i) coeff[i] = 1;
        
        for (int j = 1; j<100; ++j)
            for (int i=1; i<n; ++i) coeff[i] = (coeff[i-1] + coeff[i])%10;  
        
        int len = s.length() * repeat;
        
        for (int off = offset; off < offset+8; ++off) {
            int t = 0;
            for (int i=len-1; i>=off; --i) {                
                t += coeff[i-off] * getDigit(i,s);
                t %= 10;
            }
            System.out.print(t);
        }
        System.out.println();
    }

    static int getDigit(int i, String s) {
        return s.charAt(i % s.length()) - '0';
    }

    static void time(Runnable r) {
        long t0 = System.currentTimeMillis();
        r.run();
        System.out.println(">>> time: " + (System.currentTimeMillis() - t0));
    }

    static void part1(int[] s, int n) {

        final int len = s.length;
        int[] next = new int[len];

        for (int i = 0; i < n; ++i) {

            for (int j = 0; j < len; ++j) {
                int[] p = pattern(j + 1, len).toArray();
                next[j] = Math.abs(IntStream.range(0, len).map(k -> s[k] * p[k]).sum()) % 10;
            }

            System.arraycopy(next, 0, s, 0, len);
        }
        print(s, 0);
    }

    static int patFac(final int phase, final int pos) {
        return basePat[((pos + 1) / phase) % 4];
    }

    static void print(int[] a, int offset) {
        for (int i = offset; i < offset + 8; ++i) {
            System.out.print(a[i]);
        }
        System.out.println();
    }

    static IntStream pattern(int n, int len) {
        return IntStream.range(0, len + 1).map(i -> basePat[(i / n) % 4]).skip(1);
    }

    static int[] toNum(String s) {
        return s.chars().map(c -> c - '0').toArray();
    }


}
