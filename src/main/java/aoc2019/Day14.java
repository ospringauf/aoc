package aoc2019;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;


/*
 * Day 14: Space Stoichiometry
 * https://adventofcode.com/2019/day/14
 *
 */
public class Day14 {

	static final String FUEL = "FUEL";
	static final String ORE = "ORE";
	
	
	static class Product {
		int amount;
		String name;
		
		public Product(int amount, String name) {
			this.amount = amount;
			this.name = name;
		}

		static Product parse(String s) {
			var f = s.trim().split(" ");
			return new Product(Integer.parseInt(f[0]), f[1]);
		}
		
		@Override
		public String toString() {
			return String.format("%d %s", amount, name);
		}
		
		@Override
		public boolean equals(Object obj) {
			Product p = (Product)obj;
			return name.equals(p.name) && amount == p.amount;
		}
	}
	
	static class React {
		Product output;
		List<Product> input;
		long factor = 0;
		
		static React parse(String s) {
			var f = s.trim().split(" => ");
			React p = new React();
			p.input = Arrays.stream(f[0].split(",")).map(Product::parse).collect(Collectors.toList());
			p.output = Product.parse(f[1]);
			return p;
		}
				
		void setFactor(long targetAmount) {
			factor = targetAmount / output.amount;
			if (targetAmount % output.amount != 0) factor++; 
		}
		
		int amountNeeded(String chem) {
			return input.stream().filter(i -> i.name.equals(chem)).mapToInt(i -> i.amount).sum();
		}

		boolean needs(String chem) {
			return input.stream().anyMatch(i -> chem.equals(i.name));
		}

		@Override
		public String toString() {
			return String.format("%s => %s", input.stream().map(Product::toString).collect(Collectors.joining(", ")), output);
		}
	}
	
	public static void main(String[] args) throws Exception {
        System.out.println("=== tests 1 ===");
        Assertions.assertEquals(31, new Day14("input14a.txt").part1());
        Assertions.assertEquals(165, new Day14("input14b.txt").part1());
        Assertions.assertEquals(13312, new Day14("input14c.txt").part1());
        Assertions.assertEquals(180697, new Day14("input14d.txt").part1());
        Assertions.assertEquals(2210736, new Day14("input14e.txt").part1());
        
        System.out.println("=== tests 2 ===");
        Assertions.assertEquals(82892753, new Day14("input14c.txt").part2());
        Assertions.assertEquals(5586022, new Day14("input14d.txt").part2());
        Assertions.assertEquals(460664, new Day14("input14e.txt").part2());
        
        System.out.println("=== part1 ===");
        new Day14("input14.txt").part1();

        System.out.println("=== part2 ===");
        new Day14("input14.txt").part2();
	}
	

	List<React> reactions;
	Set<React> todo = new HashSet<>();

	Day14(String fname) throws Exception {
		reactions = Util.stringStreamOf(fname).map(React::parse).collect(Collectors.toList());
	}
	
	long part1() {	
		long ore = calcOre(1);
		System.out.printf("==> consumed %d ORE%n", ore);
		
		reactions.stream().filter(r -> List.of("FQPNW", "CQRJP", "GRQTX").contains(r.output.name))
		.forEach(r -> System.out.printf("%d  x  %s%n", r.factor, r.toString()));
		return ore;
	}

	
	int part2() {
		long teraOre = 1000000000000L;
		
		// binary search lower=0, upper=MAX
		int lb = 0;
		int ub = Integer.MAX_VALUE;
		int fuel = 0;
		long ore = 0;
		while (lb < ub-1) {
			fuel = lb + (ub-lb)/2;
			ore = calcOre(fuel);
			if (ore > teraOre) {
				ub = fuel;
			} else {
				lb = fuel;
			}
		}
		fuel = lb;
		System.out.printf("==> %d ORE produced %d FUEL%n", calcOre(fuel), fuel);
		return fuel;
	}

	// how much of chem is required as input by the current reactions
	long totalInputAmount(String chem) {
		return reactions.stream().mapToLong(r -> r.factor * r.amountNeeded(chem)).sum();
	}
	
	React findReact(String chem) {
		return reactions.stream().filter(r -> r.output.name.equals(chem)).findFirst().get();
	}

	long calcOre(long fuel) {
		todo.clear();		
		reactions.forEach(r -> r.factor = 0);
		
		var fuelReaction = findReact(FUEL);
		fuelReaction.factor = fuel;

		// check all other reactions
		todo.addAll(reactions);
		todo.remove(fuelReaction);
		
		// plan reactions
		while ( ! todo.isEmpty()) {
			for (React r : todo) {
				// required output of r is known - no TODO reactions require this product
				boolean cando = todo.stream().noneMatch(t -> t.needs(r.output.name));
				if (cando) {
					// how much of r output is needed for other reactions, how often must r repeat
					long need = totalInputAmount(r.output.name); 
					r.setFactor(need);
					
					todo.remove(r);
					break;
				}				
			}
		}
		
		return totalInputAmount(ORE);
	}

}
