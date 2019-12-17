package aoc2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class Util {

    static List<String> lines(String fname) throws IOException {
        return Files.readAllLines(Paths.get("src/main/java/aoc2019", fname));
    }
    
    static String[] linesArray(String fname) throws IOException {
        var l = Files.readAllLines(Paths.get("src/main/java/aoc2019", fname));
        return l.toArray(new String[0]);
    }
    
    static Stream<String> stringStreamOf(String fname) throws IOException {
        return Files.lines(Paths.get("src/main/java/aoc2019", fname));
    }

    static List<Integer> intListOf(String fname) throws IOException {
        return lines(fname).stream().map(Integer::parseInt).collect(Collectors.toList());
    }
    
    static IntStream intStreamOf(String fname) throws IOException {
        return lines(fname).stream().mapToInt(Integer::parseInt);
    }

    static LongStream longStreamOf(String fname) throws IOException {
        return lines(fname).stream().mapToLong(Long::parseLong);
    }

    static List<String> splitLine(String s) {
        return Arrays.asList(s.split("\\s+"));
    }
    
    static long[] readIntProg(String filename) {
        try {
            var l = lines(filename);
            return Arrays.stream(l.get(0).split(",")).mapToLong(Long::parseLong).toArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
    }
    
    // least common multiple
    // https://www.geeksforgeeks.org/lcm-of-given-array-elements/
    public static long lcm(int... numbers) 
    { 
        long result = 1; 
        int divisor = 2; 
          
        while (true) { 
            int counter = 0; 
            boolean divisible = false; 
              
            for (int i = 0; i < numbers.length; i++) { 
  
                // lcm_of_array_elements (n1, n2, ... 0) = 0. 
                // For negative number we convert into 
                // positive and calculate lcm_of_array_elements. 
  
                if (numbers[i] == 0) { 
                    return 0; 
                } 
                else if (numbers[i] < 0) { 
                    numbers[i] = numbers[i] * (-1); 
                } 
                if (numbers[i] == 1) { 
                    counter++; 
                } 
  
                // Divide element_array by devisor if complete 
                // division i.e. without remainder then replace 
                // number with quotient; used for find next factor 
                if (numbers[i] % divisor == 0) { 
                    divisible = true; 
                    numbers[i] = numbers[i] / divisor; 
                } 
            } 
  
            // If divisor able to completely divide any number 
            // from array multiply with lcm_of_array_elements 
            // and store into lcm_of_array_elements and continue 
            // to same divisor for next factor finding. 
            // else increment divisor 
            if (divisible) { 
                result = result * divisor; 
            } 
            else { 
                divisor++; 
            } 
  
            // Check if all element_array is 1 indicate  
            // we found all factors and terminate while loop. 
            if (counter == numbers.length) { 
                return result; 
            } 
        } 
    } 
}
