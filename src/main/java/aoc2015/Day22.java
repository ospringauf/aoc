package aoc2015;

import common.AocPuzzle;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 22: Wizard Simulator 20XX ---
// https://adventofcode.com/2015/day/22

class Day22 extends AocPuzzle {

	int bossHits = 58;
	final int bossDamage = 9;

	record Spell(String name, int price, int effect, int damage, int armor, int heal, int mana) {
	}

	record State(int me, int boss, int mana, Map<Spell, Integer> active) {
	}

	Spell missile = new Spell("MagicMissile", 53, 1, 4, 0, 0, 0);
	Spell drain = new Spell("Drain", 73, 1, 2, 0, 2, 0);
	Spell shield = new Spell("Shield", 113, 6, 0, 7, 0, 0);
	Spell poison = new Spell("Poison", 173, 6, 3, 0, 0, 0);
	Spell recharge = new Spell("Recharge", 229, 5, 0, 0, 0, 101);

	List<Spell> spells = List.of(missile, drain, shield, poison, recharge);

	static final int NONE = Integer.MAX_VALUE;
	int bestCost = NONE;
	int loseHits = 0;

	private int checkBest(int cost) {
		if (cost < bestCost) {
			System.out.println("best: " + cost);
			bestCost = cost;
		}
		return cost;
	}

	int play(State s, Spell spell, int cost) {
		var damage = 0;
		var me = s.me;
		var boss = s.boss;
		var mana = s.mana;
		var active = s.active;

		me -= loseHits;
		if (me <= 0)
			return NONE;

		
		// player turn - active effects
		mana += active.keySet().map(e -> e.mana).sum().intValue();
		damage = active.keySet().map(e -> e.damage).sum().intValue();
		boss -= damage;
		active = active.filter(e -> e._2 > 0).toMap(e -> Tuple.of(e._1, e._2 - 1));

		if (active.containsKey(spell)) 
			throw new RuntimeException("wrong spell");
		final int currentCost =  cost + spell.price;
		mana -= spell.price;

//			System.out.println();
//			System.out.println("my turn");
//			System.out.println("player : " + me + " hit, " + mana + " mana");
//			System.out.println("boss: " + boss + " hit");


		if (spell.effect == 1) {
			me += spell.heal;
			boss -= spell.damage;
		} else
			active = active.put(spell, spell.effect - 1);

		if (boss <= 0) {
			return checkBest(currentCost);
		}
		
		
		me -= loseHits;
		if (me <= 0)
			return NONE;



//			System.out.println();
//			System.out.println("boss turn");
//			System.out.println("player : " + me + " hit, " + mana + " mana");
//			System.out.println("boss: " + boss + " hit");
		mana += active.keySet().map(e -> e.mana).sum().intValue();
		damage = active.keySet().map(e -> e.damage).sum().intValue();
		var armor = active.keySet().map(e -> e.armor).sum().intValue();
		active = active.filter(e -> e._2 > 0).toMap(e -> Tuple.of(e._1, e._2 - 1));
		boss -= damage;
		if (boss <= 0) {
			return checkBest(currentCost);
		}
		me -= Math.max(1, bossDamage - armor);
		if (me <= 0)
			return NONE;

		
		// select next spell
		var s2 = new State(me, boss, mana, active);
		
		var blocked = active.filter(t -> t._2 > 0).map(t -> t._1);
		var opt = spells.removeAll(blocked).filter(x -> x.price <= s2.mana && currentCost + x.price < bestCost);
		if (opt.isEmpty())
			return NONE;
		
		return opt.map(n -> play(s2, n, currentCost)).min().get();		
	}


	void part1() {	
		loseHits = 0;
		var s0 = new State(50, bossHits, 500, HashMap.empty());
		var r = spells.map(s -> play(s0, s, 0)).min();
		System.out.println(r);
	}

	void part2() {
		loseHits = 1;
		var s0 = new State(50, bossHits, 500, HashMap.empty());
		var r = spells.map(s -> play(s0, s, 0)).min();
		System.out.println(r);

	}

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 1269
		new Day22().part1();

		System.out.println("=== part 2"); // 1309
		new Day22().part2();
	}
}
