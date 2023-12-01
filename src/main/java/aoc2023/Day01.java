package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;
import io.vavr.control.Option;

//--- Day 1: Trebuchet?! ---
// https://adventofcode.com/2023/day/1

class Day01 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 55130
        timed(() -> new Day01().part1());
        System.out.println("=== part 2"); // 54985
        timed(() -> new Day01().part2());
    }

    List<String> data = file2lines("input01.txt");
//	List<String> data = Util.splitLines(example1);

    List<String> TOK1 = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    List<String> TOK2 = TOK1.appendAll(List.of("_zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
    
    
    void part1() {
        var sum = data.map(s -> calibrationValue(toTokens(s, TOK1))).sum();
        System.out.println(sum);
    }

    void part2() {
        var sum = data.map(s -> calibrationValue(toTokens(s, TOK2))).sum();
        System.out.println(sum);        
    }
    
    int calibrationValue(List<Integer> l) {
        return 10 * l.head() + l.last();
    }
    
    // depending on the allowed tokens, returns:
    // eighttkbtzjz6nineeight --> List(6)
    // eighttkbtzjz6nineeight --> List(8, 6, 9, 8)
    List<Integer> toTokens(String line, List<String> tokens) {
        var f = List.range(0, line.length())
                .map(i -> line.substring(i))
                .map(s -> tokens.find(t -> s.startsWith(t)))
                .reject(Option::isEmpty)
                .map(Option::get)
                .map(tokens::indexOf)
                .map(x -> x % 10);
        return f;
    }


//    void part1() {
//        int sum = 0;
//        for (var s : data) {
//            var digits = List.ofAll(s.toCharArray()).filter(Character::isDigit);
//            sum += 10 * (digits.head() - '0') + (digits.last() - '0');
//        }
//        System.out.println(sum);
//    }
//
//    void part2() {
//        var token = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "1", "2", "3", "4",
//                "5", "6", "7", "8", "9");
//
//        int sum = 0;
//        for (var s : data) {
//            List<String> found = token.filter(s::contains);
//            var first = found.minBy(x -> s.indexOf(x)).map(token::indexOf).get();
//            var last = found.maxBy(x -> s.lastIndexOf(x)).map(token::indexOf).get();
//            sum += 10 * ((first % 9) + 1) + (last % 9) + 1;
//        }
//        System.out.println(sum);
//    }

    static String example1 = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet            
""";
    
    static String example2 = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen            
""";
            }
