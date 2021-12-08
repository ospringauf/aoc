package aoc2021;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

//--- Day 8: Seven Segment Search ---
// https://adventofcode.com/2021/day/8

class Day08 extends AocPuzzle {

	// 0 1 2 3 4 5 6 7 8 9
	// 6 2 5 5 4 5 6 3 7 6
	String SEGMENTS = "abcefg cf acdeg acdfg bcdf abdfg abdefg acf abcdefg abcdfg";
	List<Set<Character>> NUMBERS = List.of(SEGMENTS.split(" ")).map(s -> HashSet.ofAll(s.toCharArray()));

	HashSet<Character> applyPermutation(String s, Array<Character> perm) {
		return HashSet.ofAll(s.toCharArray()).map(c -> (char) perm.get(c - 'a'));
	}
	
	boolean isAnyNumber(String s, Array<Character> perm) {
		var p = applyPermutation(s, perm);
		return NUMBERS.exists(n -> n.eq(p));
	}

	
	int numberOf(String s, Array<Character> perm) {
		var p = applyPermutation(s, perm);
		return NUMBERS.indexWhere(n -> n.eq(p));
	}

//	List<String> data = file2lines("input08.txt");
//	List<String> data = Util.splitLines(example2); // 26 61229
	List<String> data = Util.splitLines(example1);
	
	void solve() {

		var allPermutations = Stream.ofAll(Generator.permutation('a', 'b', 'c', 'd', 'e', 'f', 'g').simple().stream().map(Array::ofAll)).toList();

		int result1 = 0;
		int result2 = 0;

		for (var entry : data) {
			var remainingPerm = List.ofAll(allPermutations);
//			var l = split(entry, " \\| ").map(split(" ").andThen(List::of));
//			System.out.println(l);
			var patterns = List.of(entry.split(" \\| ")[0].split(" "));
			var out = List.of(entry.split(" \\| ")[1].split(" "));

			for (var s : patterns) {
				remainingPerm = remainingPerm.filter(p -> isAnyNumber(s, p));
			}
			var permutation = remainingPerm.single();

			var nums = out.map(x -> numberOf(x, permutation));

			result1 += nums.count(List.of(1, 4, 7, 8)::contains);
			result2 += nums.foldLeft(0, (R,x) -> 10*R + x);
		}
		System.out.println(result1);
		System.out.println(result2);
	}

	public static void main(String[] args) {

		new Day08().solve(); // 272 1007675
	}

	static String example1 = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf";
	static String example2 = """
			be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
			edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
			fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
			fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
			aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
			fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
			dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
			bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
			egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
			gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
			""";

}
