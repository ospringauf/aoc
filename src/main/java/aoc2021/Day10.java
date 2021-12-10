package aoc2021;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

// --- Day 10: Syntax Scoring ---
// https://adventofcode.com/2021/day/10

class Day10 extends AocPuzzle {

    static final HashMap<Character, Integer> SCORE_1 = HashMap.of(')', 3, ']', 57, '}', 1197, '>', 25137);
    static final HashMap<Character, Integer> SCORE_2 = HashMap.of(')', 1, ']', 2,  '}', 3,    '>', 4);
    static final HashMap<Character, Character> BRACKETS = HashMap.of('(', ')', '[', ']', '{', '}', '<', '>');

    // List<String> input = Util.splitLines(example);
    List<String> input = file2lines("input10.txt");

    int parse1(String s) {
    	// two stacks
        var tokens = List.ofAll(s.toCharArray());
        var expect = List.empty();

        while (tokens.nonEmpty()) {
            var t = tokens.head();
            tokens = tokens.pop();

            if (BRACKETS.containsKey(t)) {
                // opening bracket: expect corresponding closing bracket
                expect = expect.push(BRACKETS.get(t).get());
            } else if (t == expect.head()) {
                // closing bracket, matches expectation
                expect = expect.pop();
            } else {
                // mismatch - corrupt chunk
                // score based on unexpected/illegal token t 
                // System.out.println(" exp " + expect.head() + " but found " + h);
                return SCORE_1.get(t).get();
            }
        }
        return 0;
    }

    void part1() {
        var r = input.map(s -> parse1(s)).sum();
        System.out.println(r);
    }

    long parse2(String s) {
    	// two stacks
        List<Character> tokens = List.ofAll(s.toCharArray());
        List<Character> expect = List.empty();

        while (tokens.nonEmpty()) {
            var t = tokens.head();
            tokens = tokens.pop();

            if (BRACKETS.containsKey(t)) {
                // opening bracket: expect corresponding closing bracket
                expect = expect.push(BRACKETS.get(t).get());
            } else if (t == expect.head()) {
            	// closing bracket, matches expectation
                expect = expect.pop();
            } else {
                // mismatch - corrupt input should be ignored in part 2
                throw new RuntimeException("corrupt");
            }
        }
        
        // score remaining "expected" (closing brackets)
        //return expect.map(c -> SCORE_2.get(c).get()).foldLeft(0L, (R, x) -> 5 * R + x);
        return expect.map(SCORE_2.asPartialFunction()).foldLeft(0L, (R, x) -> 5 * R + x);
    }

    void part2() {
        var incomplete = input.filter(s -> parse1(s) == 0);
        var scores = incomplete.map(s -> parse2(s)).sorted();
        var middle = scores.get(scores.length() / 2);
        System.out.println(middle);
    }

    void test() {
        assertThat(parse1("(>"), is(25137));
        assertThat(parse1("([>"), is(25137));
        assertThat(parse1("{([(<{}[<>[]}>{[]{[(<()>"), is(1197));
        assertThat(parse1("[[<[([]))<([[{}[[()]]]"), is(3));
        System.out.println("passed");
    }

    public static void main(String[] args) {

        System.out.println("=== test");
        new Day10().test();

        System.out.println("=== part 1"); // 296535 (example: 26397)
        new Day10().part1();

        System.out.println("=== part 2"); // 4245130838 (example: 288957)
        new Day10().part2();
    }

    static String example = """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
            			""";

}
