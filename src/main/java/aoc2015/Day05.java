package aoc2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day05 {

    public static void main(String[] args) throws IOException {
        List<String> strings = Files.readAllLines(Paths.get("src/main/java/aoc2015/day05.txt"));

        System.out.println("=== part 1");
        System.out.println(strings.stream().filter(Day05::nice).count());

        System.out.println("=== part 2");
        System.out.println(strings.stream().filter(Day05::nice2).count());

    }

    static boolean nice(String s) {
        long vowels = s.chars().filter(c -> "aeiou".indexOf(c) >= 0).count();
        boolean twice = IntStream.range(0, s.length() - 1).anyMatch(i -> s.charAt(i) == s.charAt(i + 1));
        boolean ugly = s.contains("ab") || s.contains("cd") || s.contains("pq") || s.contains("xy");
        
        return vowels >= 3 && twice && ! ugly;
    }

    static boolean nice2(final String s) {
        // 2-substring appears again after current position?
        IntPredicate repeats = i -> s.indexOf(s.substring(i,i+2), i+2) > 0;
        
        boolean cond1 = IntStream.range(0, s.length()-2).anyMatch(repeats);
        
        boolean cond2 = IntStream.range(0, s.length()-2).anyMatch(i -> s.charAt(i) == s.charAt(i+2));
        
        return cond1 && cond2;
        
    }
    
    @Test
    void testPart1() {
        Assertions.assertTrue(nice("ugknbfddgicrmopn"));
        Assertions.assertTrue(nice("aaa"));
        Assertions.assertFalse(nice("jchzalrnumimnmhp"));
        Assertions.assertFalse(nice("haegwjzuvuyypxyu"));
        Assertions.assertFalse(nice("dvszwmarrgswjxmb"));
    }
    
    @Test
    void testPart2() {
        Assertions.assertTrue(nice2("qjhvhtzxzqqjkmpb"));
        Assertions.assertTrue(nice2("xxyxx"));
        Assertions.assertFalse(nice2("uurcxstgmygtbstg"));
        Assertions.assertFalse(nice2("ieodomkazucvgmuy"));
    }
}
