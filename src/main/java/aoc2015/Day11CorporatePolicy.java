package aoc2015;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day11CorporatePolicy {

    public static void main(String[] args) {
        
        String p = "hxbxwxba";
        
        System.out.println("=== part 1");
        p = nextPassword(p);
        System.out.println(p);

        System.out.println("=== part 2");
        p = nextPassword(p);
        System.out.println(p);

    }
    
    @Test
    public void part1PolicyTest() {
        Assertions.assertTrue(rule1("hijklmmn"));
        Assertions.assertFalse(rule2("hijklmmn"));
        
        Assertions.assertFalse(rule1("abbceffg"));
        Assertions.assertTrue(rule3("abbceffg"));
        
        Assertions.assertFalse(rule1("abbcegjk"));
        
        Assertions.assertEquals("abcdffaa", nextPassword("abcdefgh"));
        Assertions.assertEquals("ghjaabcc", nextPassword("ghijklmn"));
    }
    
    private static String next(String p) {
        char c = p.charAt(p.length()-1);
        String prefix = p.substring(0, p.length()-1);
        if (c == 'z')
            return next(prefix) + "a";
        else
            return prefix + Character.toString(c+1);        
    }
    
    private static String nextPassword(String p) {
        do {
            p = next(p);
        } while (! complies(p));
        return p;
    }

    private static boolean complies(String p) {
        return rule1(p) && rule2(p) && rule3(p);
    }
    
    private static boolean rule1(String p) {
        return IntStream
                .range(0, p.length()-3)
                .anyMatch(i -> p.charAt(i+1) == p.charAt(i)+1 && p.charAt(i+2)==p.charAt(i)+2);
    }
    
    private static boolean rule2(String p) {
        return ! p.contains("i") && ! p.contains("o") && ! p.contains("l");
    }

    private static boolean rule3(String p) {        
        return IntStream
                .rangeClosed('a', 'z')
                .filter(i -> p.contains(Character.toString(i)+Character.toString(i)))
                .count() >= 2;
    }
}
