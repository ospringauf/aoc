package aoc2021;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

//--- Day 8: Seven Segment Search ---
// https://adventofcode.com/2021/day/8

class Day08 extends AocPuzzle {

    static final String[] SEGMENTS = "abcefg cf acdeg acdfg bcdf abdfg abdefg acf abcdefg abcdfg".split(" "); // 0 .. 9 patterns
	static final Array<Set<Character>> DIGITS = Array.of(SEGMENTS).map(s -> HashSet.ofAll(s.toCharArray()));

	// "dcfgeab" means abcdefg->dcfgeab (i.e.  a->d, b->c, c->f, d->g ...) 
	record Permutation(Array<Character> perm) {
	    private Set<Character> apply(String s) {
	        return HashSet.ofAll(s.toCharArray()).map(c -> perm.get(c - 'a'));
	    }
	    
	    boolean isDigit(String pattern) {
	        return DIGITS.contains(apply(pattern));
	    }

	    int toDigit(String pattern) {
	        return DIGITS.indexOf(apply(pattern));
	    }

        boolean validForAll(List<String> patterns) {
            return patterns.forAll(this::isDigit);
        }
	}
	
//	List<String> input = Util.splitLines(example1);
//	List<String> input = Util.splitLines(example2); // 26 61229
	List<String> input = file2lines("input08.txt"); // 272 1007675
	
	void solve() {
	    var generator = Generator.permutation('a', 'b', 'c', 'd', 'e', 'f', 'g').simple();
        var allPermutations = List.ofAll(generator).map(l -> new Permutation(Array.ofAll(l)));

		int result1 = 0;
		int result2 = 0;

		for (var entry : input) {
			
			var f = entry.split(" \\| ");
			var patterns = Util.splitFields(f[0]);
			var output = Util.splitFields(f[1]);
			
			// find the one permutation that produces valid digits for all seen patterns 
			var permutation = allPermutations.filter(perm -> perm.validForAll(patterns)).single();

			var outputDigits = output.map(permutation::toDigit);

			result1 += outputDigits.count(List.of(1, 4, 7, 8)::contains);
			result2 += Integer.parseInt(outputDigits.mkString()); 
		}
		System.out.println(result1);
		System.out.println(result2);
	}

	public static void main(String[] args) {
		timed(() -> new Day08().solve()); 
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
