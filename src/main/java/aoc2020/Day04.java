package aoc2020;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

// -- Day 4: Passport Processing ---
// https://adventofcode.com/2020/day/4

@SuppressWarnings({ "deprecation", "preview" })
class Day04 extends AocPuzzle {

	private static final String BLANK_LINE = "\\n\\n";
	private static final List<String> REQUIRED_FIELDS = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
	private static final List<String> ALL_FIELDS = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
	private static final List<String> EYE_COLORS = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

	static record Passport(Map<String, String> field) {
		
		static Passport parse(String s) {
			var elements = s.split("[: \\n]+"); // --> [cid, 296, hgt, 98, ecl, grt, iyr, 2028, hcl, #a97842, byr, 2022, pid, 69736889, eyr, 1935]
			var fields = List.of(elements).sliding(2, 2).toMap(l -> l.get(0), l -> l.get(1));
			return new Passport(fields);
		}

		boolean matchRule1() {
			return field.keySet().containsAll(REQUIRED_FIELDS);
		}

		boolean matchRule2() {
			Set<String> validFields = field.filter(this::validEntry).keySet();
			return validFields.containsAll(REQUIRED_FIELDS);
		}

		private boolean validEntry(String key, String value) {
			return switch (key) {
			case "byr" -> numberInRange(value, 1920, 2002);
			case "iyr" -> numberInRange(value, 2010, 2020);
			case "eyr" -> numberInRange(value, 2020, 2030);
			case "hgt" -> validHeight(value);
			case "hcl" -> value.matches("#[0-9a-f]{6}");
			case "ecl" -> EYE_COLORS.contains(value);
			case "pid" -> value.matches("[0-9]{9}");
			case "cid" -> true;
			default -> false;
			};
		}

		private boolean numberInRange(String v, int min, int max) {
			int n = Integer.valueOf(v);
			return n >= min && n <= max;
		}

		private boolean validHeight(String value) {
			String digits = value.substring(0, value.length() - 2);
			if (value.endsWith("cm"))
				return numberInRange(digits, 150, 193);
			if (value.endsWith("in"))
				return numberInRange(digits, 59, 76);
			return false;
		}
	}

	public static void main(String[] args) throws Exception {

//		var data = example;
		var data = new Day04().readString("input04.txt");
		var passports = List.of(data.split(BLANK_LINE)).map(Passport::parse);

		System.out.println("=== part 1");
		var n1 = passports.count(Passport::matchRule1);
		System.out.println(n1);

		System.out.println("=== part 2");
		var n2 = passports.count(Passport::matchRule2);
		System.out.println(n2);
	}

	static String example = """
			ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
			byr:1937 iyr:2017 cid:147 hgt:183cm

			iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
			hcl:#cfa07d byr:1929

			hcl:#ae17e1 iyr:2013
			eyr:2024
			ecl:brn pid:760753108 byr:1931
			hgt:179cm

			hcl:#cfa07d eyr:2025 pid:166559648
			iyr:2011 ecl:brn hgt:59in
					""";

	static String invalid = """
			eyr:1972 cid:100
			hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

			iyr:2019
			hcl:#602927 eyr:1967 hgt:170cm
			ecl:grn pid:012533040 byr:1946

			hcl:dab227 iyr:2012
			ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

			hgt:59cm ecl:zzz
			eyr:2038 hcl:74454a iyr:2023
			pid:3556412378 byr:2007
					""";

	static String valid = """
			pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
			hcl:#623a2f

			eyr:2029 ecl:blu cid:129 byr:1989
			iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

			hcl:#888785
			hgt:164cm byr:2001 iyr:2015 cid:88
			pid:545766238 ecl:hzl
			eyr:2022

			iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
					""";
}
