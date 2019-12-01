package aoc2019;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.lambda.Seq;

public class Day00 {
	   public static void main(String[] args) throws Exception {
	    	var hello = "now on Java " + System.getProperty("java.version");
	    	var list = List.of("A", "B", "C");
	    	
	    	System.out.println(hello); 
	    	System.out.println(seq(list).toString(", "));
	    	list.stream().forEach(System.out::println);
	    	
	    }
}
