package aoc2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day08Matchsticks {

    public static void main(String[] args) throws IOException {
        
        List<String> testdata = Files.readAllLines(Paths.get("src/main/java/aoc2015/day08-test.txt"));
        List<String> realdata = Files.readAllLines(Paths.get("src/main/java/aoc2015/day08.txt"));
        List<String> data = realdata;
        
        System.out.println("=== part 1");
        int l = data.stream().mapToInt(s -> s.length()).sum();
        int c = data.stream().mapToInt(s -> chars(s)).sum();
        System.out.println(l-c);
        
        
        System.out.println("=== part 2  ");
        c = data.stream().mapToInt(s -> expand(s).length()).sum();
        System.out.println(c-l);
    }

    private static String expand(String s) {
        
        s = s.replaceAll("\\\\", "\\\\\\\\"); // \ => \\
        s = s.replaceAll("\\\"", "\\\\\""); // " => \"
        
        return "\"" + s + "\"";
    }

    private static int chars(String s) {
        int c = 0;
        for (int i=1; i<s.length()-1; ++i) {
            c++;
            if (s.charAt(i) == '\\')
                switch (s.charAt(i+1)) {
                case '\\':
                case '\"':
                    i++;                
                    break;
                case 'x':
                    i+=3;
                    break;
                }
        }
        return c;
    }
}
