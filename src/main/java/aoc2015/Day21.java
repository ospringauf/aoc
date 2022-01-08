package aoc2015;

import org.paukov.combinatorics3.Generator;

import common.AocPuzzle;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

// --- Day 21: RPG Simulator 20XX ---
// https://adventofcode.com/2015/day/21

class Day21 extends AocPuzzle {

	record Player(int hit, int damage, int armor) {
	}

	boolean win(Player me, Player boss) {
		while (me.hit > 0 && boss.hit > 0) {
			boss = new Player(boss.hit - Math.max(1, me.damage - boss.armor), boss.damage, boss.armor);
//			System.out.println("boss: " + boss);
			if (boss.hit <= 0) break;
			me = new Player(me.hit - Math.max(1, boss.damage - me.armor), me.damage, me.armor);
//			System.out.println("me: " + me);
		}
		return me.hit > 0;

	}

	interface Item extends Function1<Player, Player> {}
	
	void solve() {
		Item None = p -> new Player(p.hit, p.damage, p.armor);
		
		// weapons
		Item Dagger = p -> new Player(p.hit, p.damage+4, p.armor);
		Item Shortsword = p -> new Player(p.hit, p.damage+5, p.armor);
		Item Warhammer = p -> new Player(p.hit, p.damage+6, p.armor);
		Item Longsword = p -> new Player(p.hit, p.damage+7, p.armor);
		Item Greataxe = p -> new Player(p.hit, p.damage+8, p.armor);
		
		// armor
		Item Leather = p -> new Player(p.hit, p.damage, p.armor+1);
		Item Chainmail = p -> new Player(p.hit, p.damage, p.armor+2);
		Item Splintmail = p -> new Player(p.hit, p.damage, p.armor+3);
		Item Bandedmail = p -> new Player(p.hit, p.damage, p.armor+4);
		Item Platemail = p -> new Player(p.hit, p.damage, p.armor+5);
		
		// rings
		Item Damage1 = p -> new Player(p.hit, p.damage+1, p.armor);
		Item Damage2 = p -> new Player(p.hit, p.damage+2, p.armor);
		Item Damage3 = p -> new Player(p.hit, p.damage+3, p.armor);
		Item Defense1 = p -> new Player(p.hit, p.damage, p.armor+1);
		Item Defense2 = p -> new Player(p.hit, p.damage, p.armor+2);
		Item Defense3 = p -> new Player(p.hit, p.damage, p.armor+3);
		
		Map<Item, Integer> price = HashMap.of(None, 0)
		.put(Dagger, 8)
		.put(Shortsword, 10)
		.put(Warhammer, 25)
		.put(Longsword, 40)
		.put(Greataxe, 74)
		.put(Leather, 13)
		.put(Chainmail, 31)
		.put(Splintmail, 53)
		.put(Bandedmail, 75)
		.put(Platemail, 102)
		.put(Damage1, 25)
		.put(Damage2, 50)
		.put(Damage3, 100)
		.put(Defense1, 20)
		.put(Defense2, 40)
		.put(Defense3, 80);
		
		
		var w = List.of(Dagger, Shortsword, Warhammer, Longsword, Greataxe).asJava();
		var a = List.of(None, Leather, Chainmail, Splintmail, Bandedmail, Platemail).asJava();
		var r = List.of (None, Damage1, Damage2, Damage3, Defense1, Defense2, Defense3).asJava();
		
		Function1<List<Item>, Integer> cost = l -> l.map(x -> price.getOrElse(x,0)).sum().intValue();
		Function1<List<Item>, Player> equip = l -> l.foldLeft(new Player(100, 0, 0), (p,f) -> f.apply(p));
		
		var options = Stream.ofAll(Generator.cartesianProduct(w, a, r, r).stream()).map(List::ofAll);
		options = options.filter(l -> (l.get(2) == None) || (l.get(2) != l.get(3))); // no two  identical rings allowed
				
		var boss = new Player(109, 8, 2);
		

		System.out.println("=== part 1");
		var best = options.filter(x -> win(equip.apply(x), boss)).minBy(cost);
		System.out.println(cost.apply(best.get()));
		
		System.out.println("=== part 2");
		var worst = options.filter(x -> ! win(equip.apply(x), boss)).maxBy(cost);
		System.out.println(cost.apply(worst.get()));
	}

	public static void main(String[] args) {
		new Day21().solve();
	}
}
