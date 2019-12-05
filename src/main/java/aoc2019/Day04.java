package aoc2019;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;

public class Day04 {
	
	// 1694, 1148
	public static void main(String[] args) throws Exception {
		System.out.println("=== part1 ===");
		new Day04().part1();
		System.out.println("=== part2 ===");
		new Day04().part2();
		
		IntStream.of(100000, 10000, 1000, 100, 10, 1).map(n -> 123456/n%10).forEach(System.out::println);
	}

	boolean isPassword1(int x) {
		int div=100000;
		boolean mono = true;
		boolean doubl = false;
		
		int d0 = x / div;
		while (div > 1) {
			div /= 10;
			int d1 = x/div % 10;
			mono &= d0 <= d1;
			doubl |= d1 == d0;
			d0 = d1;
		}
		
		return mono && doubl;
	}
	
	boolean isPassword2(int x) {
		int div=100000;
		boolean mono = true;
		int cnt[] = new int[10];
		int d0 = 0;
		while (div > 0) {
			int d1 = x/div % 10;
			mono &= d0 <= d1;
			cnt[d1]++;
			d0 = d1;
			div /= 10;
		}
		boolean doubl = Arrays.stream(cnt).anyMatch(c -> c ==2);
		
		return mono && doubl;
	}
	
	private void part1() {
		Assertions.assertTrue(isPassword1(123446));
		Assertions.assertTrue(isPassword1(111111));
		Assertions.assertFalse(isPassword1(223450));
		Assertions.assertFalse(isPassword1(123789));
		long c = IntStream.rangeClosed(156218,652527).filter(this::isPassword1).count();
		System.out.println(c);
		
	}
	
	private void part2() {
		Assertions.assertTrue(isPassword2(112233));
		Assertions.assertFalse(isPassword2(123444));
		Assertions.assertTrue(isPassword2(111122));
		long c = IntStream.rangeClosed(156218,652527).filter(this::isPassword2).count();
		System.out.println(c);
		
	}


}
