package aoc2015;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.ToIntFunction;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day12 {

    
    public static void main(String[] args) throws Exception {
        String json = Files.readAllLines(Paths.get("src/main/java/aoc2015/day12.txt")).get(0);
        
        System.out.println("=== part 1");
        System.out.println(jsonSum1(json));
        
        System.out.println("=== part 2");
        
        JsonObject j = parseJsonObj(json);        
//        System.out.println(j);
        System.out.println(jsonSum(j));
    }


    private static JsonObject parseJsonObj(String json) {
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject x = jsonReader.readObject();
        jsonReader.close();
        return x;
    }
    
    private static JsonArray parseJsonArray(String json) {
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonArray x = jsonReader.readArray();
        jsonReader.close();
        return x;
    }
    
    private static boolean isRed(JsonValue v) {
        return (v instanceof JsonString) && ((JsonString)v).getString().equals("red");
    }
    
    private static int jsonSum(JsonObject obj) {
        if (obj.values().stream().anyMatch(Day12::isRed))
            return 0;
       
        return jsonSumBase(obj.values());
    }
    
    private static int jsonSum(JsonArray obj) {
//        if (obj.stream().anyMatch(Day12::isRed))
//            return 0;
       
        return jsonSumBase(obj);        
    }

    private static int jsonSumBase(Collection<JsonValue> obj) {
        int s = 0;
        for (JsonValue v: obj) {
            
            switch (v.getValueType()) {
            case OBJECT:
                s += jsonSum((JsonObject) v);
                break;
                
            case NUMBER:
                s += ((JsonNumber) v).intValue();
                break;
                
            case ARRAY:
                s += jsonSum((JsonArray) v);
                break;
                
            case STRING:
                break;

            default:
                System.out.println("unhandled: " + v.getClass() + "/" + v.getValueType());
                break;
            }
        }
        return s;
    }
    

    
    private static int jsonSum1(String s) {
        String[] f = s.split("[\\[\\],\\{\\}\\\\\"\\:]");
        ToIntFunction<String> tryparse = x -> {
            try {
                return Integer.parseInt(x);
            } catch (Exception e) {
                return 0;
            }
        };
        return Arrays.stream(f).mapToInt(tryparse).sum();
    }
    
    @Test
    public void testPart1() {
        Assertions.assertEquals(6, jsonSum1("[1,2,3]"));
        Assertions.assertEquals(6, jsonSum1("{\"a\":2,\"b\":4}"));
        
        Assertions.assertEquals(3, jsonSum1("{\"a\":{\"b\":4},\"c\":-1}"));
        Assertions.assertEquals(0, jsonSum1("{\"a\":[-1,1]}"));
        Assertions.assertEquals(0, jsonSum1("{}"));
    }
    
    @Test
    public void testPart2() {
        Assertions.assertEquals(4, jsonSum(parseJsonArray("[1,{\"c\":\"red\",\"b\":2},3]")));
        Assertions.assertEquals(0, jsonSum(parseJsonObj("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}")));
    }

}
