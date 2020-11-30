package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import io.vavr.collection.List;

class Util {

    private static final String INPUT_PATH = "src/main/java/aoc2020";

	static List<String> lines(String fname) throws IOException {
        return List.ofAll(Files.lines(Paths.get(INPUT_PATH, fname)));
    }
    
//    static String[] linesArray(String fname) throws IOException {
//        var l = Files.readAllLines(Paths.get(INPUT_PATH, fname));
//        return l.toArray(new String[0]);
//    }
    
//    static Stream<String> stringStreamOf(String fname) throws IOException {
//        return Files.lines(Paths.get(INPUT_PATH, fname));
//    }

    static List<Integer> ints(String fname) throws IOException {
        return lines(fname).map(Integer::valueOf);
    }
    
    static List<Long> longs(String fname) throws IOException {
        return lines(fname).map(Long::valueOf);
    }

    static List<String> splitLine(String s) {
        return List.of(s.split("\\s+"));
    }
    
    public static void main(String[] args) {
		System.out.println(splitLine("a b,c d"));
	}
    
    static long[] readIntProg(String filename) {
        try {
            String l = lines(filename).head();
            return Arrays.stream(l.split(",")).mapToLong(Long::parseLong).toArray();
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
