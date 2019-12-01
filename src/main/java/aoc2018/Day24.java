package aoc2018;

import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class Day24 {
	
	static class Group {
		String name;
		int units;
		int hitPoints;
		int attackDamage;
		String attackType;
		int initiative;
		String weak;
		String immune;

		int effPower() {
			return units * attackDamage;
		}
		
		Group(int u, int hp, int ad, String at, int ini, String w, String imm) {
			units = u;
			hitPoints = hp;
			attackDamage = ad;
			attackType = at;
			initiative = ini;
			weak = w;
			immune = imm;
		}
		
		int damageFactor(String att) {
			if (immune != null && immune.contains(att))
				return 0;
			if (weak != null && weak.contains(att)) 
				return 2;
			return 1;
		}
		
		int damage(Group enemy) {
			return effPower() * enemy.damageFactor(attackType);
		}
		
		void attack(Group tgt) {
			int kill = Math.min(tgt.units, damage(tgt) / tgt.hitPoints);
			tgt.units -= kill;
			System.out.println(this + " attacks " + tgt + " killing " + kill + " units");
		}
		
		@Override
		public String toString() {
			return name + "[" + units + "]";				
		}
	}

	static List<Group> immuneSystem = new ArrayList<>();
	static List<Group> infection = new ArrayList<>();
	
	static Map<Group, Group> target = new HashMap<>();
	
	
	public static void main(String[] args) {

		part2();
	}


	protected static void part1() {
//		buildTestArmies();
		buildArmies();
		seq(immuneSystem).forEach(g -> g.name = "IMU" + (immuneSystem.indexOf(g)+1));
		seq(infection).forEach(g -> g.name = "INF" + (infection.indexOf(g)+1));
		
		do {
			round();
			System.out.println("-- next round");
		} while (!immuneSystem.isEmpty() && !infection.isEmpty());
		
		System.out.println("immune: " + seq(immuneSystem).sum(g -> g.units));
		System.out.println("infection: " + seq(infection).sum(g -> g.units));
	}

	protected static void part2() {
		
		int boost = 83;
		do {
			boost++;
			System.out.println("boost: " + boost);
//			buildTestArmies();
			buildArmies();
			seq(immuneSystem).forEach(g -> g.name = "IMU" + (immuneSystem.indexOf(g)+1));
			seq(infection).forEach(g -> g.name = "INF" + (infection.indexOf(g)+1));
			
			final int b = boost;
			immuneSystem.forEach(g -> g.attackDamage += b);
			
			do {
				round();
//				System.out.println("-- next round");
			} while (!immuneSystem.isEmpty() && !infection.isEmpty());
		} while (immuneSystem.isEmpty());
		
		System.out.println("immune: " + seq(immuneSystem).sum(g -> g.units));
		System.out.println("infection: " + seq(infection).sum(g -> g.units));
	}

	protected static void round() {
		target.clear();
		selectTargets(immuneSystem, infection);
		selectTargets(infection, immuneSystem);
		attack();
		
		immuneSystem.removeIf(g -> g.units <= 0);
		infection.removeIf(g -> g.units <= 0);
	}
	
	
	static void selectTargets(List<Group> attackers, List<Group> targets) {
		Set<Group> rem = new HashSet<Group>(targets);
		Comparator<Group> c = Comparator.comparing(Group::effPower).thenComparing(g -> g.initiative);
		seq(attackers).sorted(c.reversed()).forEach(a -> {
			
			Comparator<Group> c2 = Comparator.comparingInt(g -> a.damage(g));
			c2 = c2.thenComparingInt(g -> g.effPower());
			c2 = c2.thenComparingInt(g -> g.initiative);
			
			Optional<Group> tgt = seq(rem).filter(g -> a.damage(g) > 0).sorted(c2.reversed()).findFirst();
			if (tgt.isPresent()) {
				target.put(a, tgt.get());
				rem.remove(tgt.get());			
				System.out.println(a + " chooses target " + tgt.get() + " for " + a.damage(tgt.get()) + " damage");
			} else {
				System.out.println(a + " chooses no target");
			}
		});
	}
	
	static void attack() {
		Comparator<Group> c = Comparator.comparingInt(g -> g.initiative);
		seq(target.keySet()).sorted(c.reversed()).forEach(g -> g.attack(target.get(g)));
	}
	
	static void buildArmies() {
		immuneSystem.clear();
		infection.clear();
//		Immune System:
//		1514 units each with 8968 hit points (weak to cold) with an attack that does 57 bludgeoning damage at initiative 9
		immuneSystem.add(new Group(1514, 8968, 57, "bludgeoning", 9, "cold", null));
//		2721 units each with 6691 hit points (weak to cold) with an attack that does 22 slashing damage at initiative 15
		immuneSystem.add(new Group(2721, 6691, 22, "slashing", 15, "cold", null));
//		1214 units each with 10379 hit points (immune to bludgeoning) with an attack that does 69 fire damage at initiative 16
		immuneSystem.add(new Group(1214, 10379, 69, "fire", 16, null, "bludgeoning"));
//		2870 units each with 4212 hit points with an attack that does 11 radiation damage at initiative 12
		immuneSystem.add(new Group(2870, 4212, 11, "radiation", 12, null, null));
//		1239 units each with 5405 hit points (weak to cold) with an attack that does 37 cold damage at initiative 18
		immuneSystem.add(new Group(1239, 5405, 37, "cold", 18, "cold", null));
//		4509 units each with 4004 hit points (weak to cold; immune to radiation) with an attack that does 8 slashing damage at initiative 20
		immuneSystem.add(new Group(4509, 4004, 8, "slashing", 20, "cold", "radiation"));
//		3369 units each with 10672 hit points (weak to slashing) with an attack that does 29 cold damage at initiative 11
		immuneSystem.add(new Group(3369, 10672, 29, "cold", 11, "slashing", null));
//		2890 units each with 11418 hit points (weak to fire; immune to bludgeoning) with an attack that does 30 cold damage at initiative 8
		immuneSystem.add(new Group(2890, 11418, 30, "cold", 8, "fire", "bludgeoning"));
//		149 units each with 7022 hit points (weak to slashing) with an attack that does 393 radiation damage at initiative 13
		immuneSystem.add(new Group(149, 7022, 393, "radiation", 13, "slashing", null));
//		2080 units each with 5786 hit points (weak to fire; immune to slashing, bludgeoning) with an attack that does 20 fire damage at initiative 7
		immuneSystem.add(new Group(2080, 5786, 20, "fire", 7, "fire", "slashing, bludgeoning"));
		
		
//
//		Infection:
//		817 units each with 47082 hit points (immune to slashing, radiation, bludgeoning) with an attack that does 115 cold damage at initiative 3
		infection.add(new Group(817, 47082, 115, "cold", 3, null, "slashing, radiation, bludgeoning"));
//		4183 units each with 35892 hit points with an attack that does 16 bludgeoning damage at initiative 1
		infection.add(new Group(4183, 35892, 16, "bludgeoning", 1, null, null));
//		7006 units each with 11084 hit points with an attack that does 2 fire damage at initiative 2
		infection.add(new Group(7006, 11084, 2, "fire", 2, null, null));
//		4804 units each with 25411 hit points with an attack that does 10 cold damage at initiative 14
		infection.add(new Group(4804, 25411, 10, "cold", 14, null, null));
//		6262 units each with 28952 hit points (weak to fire) with an attack that does 7 slashing damage at initiative 10
		infection.add(new Group(6262, 28952, 7, "slashing", 10, "fire", null));
//		628 units each with 32906 hit points (weak to slashing) with an attack that does 99 radiation damage at initiative 4
		infection.add(new Group(628, 32906, 99, "radiation", 4, "slashing", null));
//		5239 units each with 46047 hit points (immune to fire) with an attack that does 14 bludgeoning damage at initiative 6
		infection.add(new Group(5239, 46047, 14, "bludgeoning", 6, null, "fire"));
//		1173 units each with 32300 hit points (weak to cold, slashing) with an attack that does 53 bludgeoning damage at initiative 19
		infection.add(new Group(1173, 32300, 53, "bludgeoning", 19, "cold, slashing", null));
//		3712 units each with 12148 hit points (immune to cold; weak to slashing) with an attack that does 5 slashing damage at initiative 17
		infection.add(new Group(3712, 12148, 5, "slashing", 17, "slashing", "cold"));
//		334 units each with 43582 hit points (weak to cold, fire) with an attack that does 260 cold damage at initiative 5
		infection.add(new Group(334, 43582, 260, "cold", 5, "cold, fire", null));
	}
	
	static void buildTestArmies() {
		immuneSystem.clear();
		infection.clear();

//		Immune System:
//		17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
		immuneSystem.add(new Group(17, 5390, 4507, "fire", 2, "radiation, bludgeoning", null));
//		989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
		immuneSystem.add(new Group(989, 1274, 25, "slashing", 3, "bludgeoning, slashing", "fire"));
//
//		Infection:
//		801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
		infection.add(new Group(801, 4706, 116, "bludgeoning", 1, "radiation", null));
//		4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
		infection.add(new Group(4485, 2961, 12, "slashing", 4, "fire, cold", "radiation"));
	}
}
