package kata;

import io.vavr.collection.List;

/**
 * TDD
 * Recursion
 * 
 * expand a "masked" number (eg X01110X0X) to all possible numbers
 *
 * from Day 2020/14: Docking Data 
 * https://adventofcode.com/2020/day/14
 */
public class MaskedNumbers {

	List<Long> resolveAdr(String mask) {
		if (mask.contains("X")) {
			var a0 = resolveAdr(mask.replaceFirst("X", "0"));
			var a1 = resolveAdr(mask.replaceFirst("X", "1"));
			return a0.appendAll(a1);
		} else {
			return List.of(Long.parseLong(mask, 2));
		}
	}

}
