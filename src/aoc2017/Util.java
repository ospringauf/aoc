package aoc2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Util {

    static List<String> lines(String fname) throws IOException {
        return Files.readAllLines(Paths.get("bin", fname));
    }
    
    static List<String> splitLine(String s) {
        return Arrays.asList(s.split("\\s+"));
    }
}
