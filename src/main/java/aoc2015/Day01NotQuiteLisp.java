package aoc2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day01NotQuiteLisp {

    public static void main(String[] args) throws IOException {
        
        List<String> l = Files.readAllLines(Paths.get("src/main/java/aoc2015/day01.txt"));
        String s = l.get(0);
        
        int floors = s.chars().map(c -> (c == '(') ? 1 : -1).sum();
        
        System.out.println(floors);
        
        
    }
}
