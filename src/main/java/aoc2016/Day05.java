package aoc2016;

import java.security.MessageDigest;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.control.Try;

// --- Day 5: How About a Nice Game of Chess? ---
// https://adventofcode.com/2016/day/5

class Day05 extends AocPuzzle {

	void part1() {
		int idx = -1;
		String key = myInput; // "abc";
		MessageDigest md = Try.of(() -> MessageDigest.getInstance("MD5")).get();

		var pw = List.empty();
		String myHash;
		do {
			do {
				idx++;
				md.update((key + Integer.toString(idx)).getBytes());
				myHash = Util.encodeHexString(md.digest());
			} while (!myHash.startsWith("00000"));
//			System.out.println(idx);
//			System.out.println(myHash);
//			System.out.println(myHash.charAt(5));
			pw = pw.append(myHash.charAt(5));
		} while (pw.size() < 8);

		System.out.println(pw.mkString());
	}

	void part2() {
		int idx = -1;
		String key = myInput;
		var pw = Array.ofAll("________".toCharArray());
		MessageDigest md = Try.of(() -> MessageDigest.getInstance("MD5")).get();

		String myHash;
		do {
			do {
				idx++;
				md.update((key + Integer.toString(idx)).getBytes());
				myHash = Util.encodeHexString(md.digest());
			} while (!myHash.startsWith("00000"));
			var pos = myHash.charAt(5) - '0';
			var c = myHash.charAt(6);
			if (pos >= 0 && pos < 8 && pw.get(pos) == '_') {
				pw = pw.update(pos, c);
				System.out.println(pw.mkString());
			}
		} while (pw.contains('_'));
	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // f97c354d
		timed(() -> new Day05().part1());

		System.out.println("=== part 2"); // 863dde27
		timed(() -> new Day05().part2());
	}

	static String myInput = "reyedfim";
}
