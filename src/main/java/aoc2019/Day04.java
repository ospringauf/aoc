package aoc2019;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;

/*
 * Day 4: Secure Container
 * https://adventofcode.com/2019/day/3
 *
 */
public class Day04 {

    // 1694, 1148
    public static void main(String[] args) throws Exception {
        System.out.println("=== part1 ===");
        new Day04().part1();
        System.out.println("=== part2 ===");
        new Day04().part2();

    }

//    Seq<Integer> digits(final int number) {
//        return Seq.of(100000, 10000, 1000, 100, 10, 1).map(n -> number / n % 10);
//    }
//
//    boolean pw1(final int number) {
//        boolean mono = digits(number).reduce(0, (a, b) -> (a <= b) ? b : 100) < 10;
//        boolean pair = Seq.range(0, 10).anyMatch(n -> digits(number).count(d -> d == n) >= 2);
//        return mono && pair;
//    }

    boolean isPassword1(int number) {
        int div = 100000;
        boolean mono = true;
        boolean pair = false;

        int last = number / div;
        while (div > 1) {
            div /= 10;
            int current = number / div % 10;
            mono &= last <= current;
            pair |= current == last;
            last = current;
        }

        return mono && pair;
    }

    boolean isPassword2(int number) {
        int div = 100000;
        boolean mono = true;
        int cnt[] = new int[10];
        int last = 0;
        while (div > 0) {
            int current = number / div % 10;
            mono &= last <= current;
            cnt[current]++;
            last = current;
            div /= 10;
        }
        boolean pair = Arrays.stream(cnt).anyMatch(c -> c == 2);

        return mono && pair;
    }

    private void part1() {
        Assertions.assertTrue(isPassword1(123446));
        Assertions.assertTrue(isPassword1(111111));
        Assertions.assertFalse(isPassword1(223450));
        Assertions.assertFalse(isPassword1(123789));
        long c = IntStream.rangeClosed(156218, 652527).filter(this::isPassword1).count();
        System.out.println(c);
    }


    private void part2() {
        Assertions.assertTrue(isPassword2(112233));
        Assertions.assertFalse(isPassword2(123444));
        Assertions.assertTrue(isPassword2(111122));
        long c = IntStream.rangeClosed(156218, 652527).filter(this::isPassword2).count();
        System.out.println(c);

    }

}
