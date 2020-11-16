package aoc2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

    static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
    
    static class Box {
        int l;
        int w;
        int h;

        Box(String s) {
            String[] f = s.split("x");
            l = Integer.parseInt(f[0]);
            w = Integer.parseInt(f[1]);
            h = Integer.parseInt(f[2]);
        }
        
        int area() {
            return 2*l*w + 2*w*h + 2*h*l;
        }
        
        int slack() {
            int s = min(l*w, w*h, h*l);
            return s;
        }
        
        int ribbon() {
            int s = min(l+w, w+h, h+l);
            return 2*s;
        }
        
        int bow() {
            return l*w*h;
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        
        List<String> lines = Files.readAllLines(Paths.get("src/main/java/aoc2015/day02.txt"));
        
        List<Box> boxes = lines.stream().map(Box::new).collect(Collectors.toList());

        Box testbox = new Box("2x3x4");

        
        System.out.println("=== part 1");
        int paper = boxes.stream().mapToInt(b -> b.area() + b.slack()).sum();
        System.out.println(paper);
        
        System.out.println("=== part 2");
        int ribbon = boxes.stream().mapToInt(b -> b.ribbon() + b.bow()).sum();
        System.out.println(ribbon);
        
    }
}
