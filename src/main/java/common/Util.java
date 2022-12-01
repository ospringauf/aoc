package common;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Predicate;

import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class Util {

	public static List<String> splitFields(String s) {
		return List.of(s.split("\\s+"));
	}

	public static List<Integer> string2ints(String input) {
		return splitFields(input).map(Integer::valueOf);
	}
	
	public static List<Integer> strings2ints(String[] strings) {
		return List.of(strings).map(Integer::valueOf);
	}

	public static List<String> splitLines(String s)  {
		return List.of(s.split("\n"));
	}

	public static int[] string2intArray(String input) {
		return Arrays.stream(input.split("\\s+")).mapToInt(Integer::valueOf).toArray();
	}

	public static long[] string2longArray(String input) {
		return Arrays.stream(input.split("\\s+")).mapToLong(Long::valueOf).toArray();
	}

	public static String byteToHex(byte num) {
		char[] hexDigits = new char[2];
		hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
		hexDigits[1] = Character.forDigit((num & 0xF), 16);
		return new String(hexDigits);
	}

	public static String encodeHexString(byte[] byteArray) {
		StringBuffer hexStringBuffer = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			hexStringBuffer.append(byteToHex(byteArray[i]));
		}
		return hexStringBuffer.toString();
	}

	public static <T> Seq<Seq<T>> split(Seq<T> l, Predicate<T> sep) {
		List<Seq<T>> coll = List.empty();
		while (!l.isEmpty()) {
			var p = l.splitAt(sep);
			coll = coll.append(p._1);
			l = (p._2.isEmpty()) ? p._2 : p._2.tail();
		}
		return coll;
	}
	
	// least common multiple
	// https://www.geeksforgeeks.org/lcm-of-given-array-elements/
	public static long lcm(int... numbers) {
		long result = 1;
		int divisor = 2;

		while (true) {
			int counter = 0;
			boolean divisible = false;

			for (int i = 0; i < numbers.length; i++) {

				// lcm_of_array_elements (n1, n2, ... 0) = 0.
				// For negative number we convert into
				// positive and calculate lcm_of_array_elements.

				if (numbers[i] == 0) {
					return 0;
				} else if (numbers[i] < 0) {
					numbers[i] = numbers[i] * (-1);
				}
				if (numbers[i] == 1) {
					counter++;
				}

				// Divide element_array by devisor if complete
				// division i.e. without remainder then replace
				// number with quotient; used for find next factor
				if (numbers[i] % divisor == 0) {
					divisible = true;
					numbers[i] = numbers[i] / divisor;
				}
			}

			// If divisor able to completely divide any number
			// from array multiply with lcm_of_array_elements
			// and store into lcm_of_array_elements and continue
			// to same divisor for next factor finding.
			// else increment divisor
			if (divisible) {
				result = result * divisor;
			} else {
				divisor++;
			}

			// Check if all element_array is 1 indicate
			// we found all factors and terminate while loop.
			if (counter == numbers.length) {
				return result;
			}
		}
	}

	public static long gcd(long a, long b) {
		return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
	}

	public static long modInv(long a, long n) {
		return BigInteger.valueOf(a).modInverse(BigInteger.valueOf(n)).longValue();
	}

	public static String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
		
	}
}
