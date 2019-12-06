package aoc2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    public static List<String> lines(String fname) throws IOException {
        return Files.readAllLines(Paths.get("src/main/java", fname));
    }

    public static List<Integer> ints(String fname) throws IOException {
        return lines(fname).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
    }
    
    public static IntStream intStreamOf(String fname) throws IOException {
        return lines(fname).stream().mapToInt(s -> Integer.parseInt(s));
    }

    public static List<String> splitLine(String s) {
        return Arrays.asList(s.split("\\s+"));
    }
    
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
    
    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
}
