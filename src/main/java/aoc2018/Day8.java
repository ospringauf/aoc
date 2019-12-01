package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * Tree de-serialization
 * https://adventofcode.com/2018/day/8
 *
 */
public class Day8 {
	
	static class Node {
		List<Node> children = new ArrayList<>();
		List<Integer> metadata = new ArrayList<>();

		public Node() {
		}
		
		static Node build(List<Integer> i) {
			Integer c = i.remove(0);
			Integer m = i.remove(0);

			Node n = new Node();
			IntStream.range(0, c).forEach(x -> n.children.add(build(i)));
			IntStream.range(0, m).forEach(x -> n.metadata.add(i.remove(0)));
			
			return n;
		}
		
		int sumMeta() {
			int s = seq(metadata).sum().orElse(0)
					 + seq(children).mapToInt(n -> n.sumMeta()).sum();
			return s;			
		}		
		
		int value() {
			if (children.size() == 0) {
				return seq(metadata).sum().orElse(0);
			} else {
				return seq(metadata)
					.filter(x -> x>0 && x<=children.size())
					.mapToInt(x -> children.get(x-1).value())
					.sum();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<Integer> i = new ArrayList<>();
		
		seq(Util.splitLine(Util.lines("aoc2018/day8.txt").get(0))).forEach(n -> i.add(Integer.parseInt(n)));		
//		seq(Util.splitLine("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")).forEach(n -> i.add(Integer.parseInt(n)));
		
		Node root = Node.build(i);
	   
	    System.out.println("=== part 1 ===");
		System.out.println(root.sumMeta());
		
		System.out.println("=== part 2 ===");
		System.out.println(root.value());
	}

}
