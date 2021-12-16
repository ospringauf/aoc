package aoc2021;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;

// --- Day 16: Packet Decoder ---
// https://adventofcode.com/2021/day/16

class Day16 extends AocPuzzle {

	class Packet {
		List<Integer> bits;
		List<Packet> subpackets = List.empty();
		int version;
		int typeid;
		long literal;

		Packet(String hex) {
			this(bits(hex));
		}

		Packet(List<Integer> bits) {
			this.bits = bits;

			version = (int) valb(consume(3).mkString());
			typeid = (int) valb(consume(3).mkString());

			if (typeid == 4) {
				literal = readLiteral();
			} else {
				readOperator();
			}
		}

		int sumVersion() {
			return version + subpackets.map(p -> p.sumVersion()).sum().intValue();
		}

		long value() {
			var values = subpackets.map(p -> p.value()).toList();
//			System.out.println(vals);

			var r = switch (typeid) {
			case 0 -> values.sum().longValue();
			case 1 -> values.product().longValue();
			case 2 -> values.min().get();
			case 3 -> values.max().get();
			case 4 -> literal;
			case 5 -> (values.head() > values.tail().head()) ? 1 : 0;
			case 6 -> (values.head() < values.tail().head()) ? 1 : 0;
			case 7 -> (values.head().equals(values.tail().head())) ? 1 : 0;
			default -> throw new IllegalArgumentException("Unexpected value: " + typeid);
			};

//			System.out.println(r + " <- " + vals + " | " + typeid);
			return r;
		}

		List<Integer> consume(int l) {
			var r = bits.take(l);
			bits = bits.drop(l);
			return r;
		}

		void readOperator() {
			int lenghtType = consume(1).head();

			if (lenghtType == 0) {
				int size = (int) valb(consume(15).mkString());
				var subbits = consume(size);
				while (subbits.nonEmpty()) {
					var p = new Packet(subbits);
					subpackets = subpackets.append(p);
					subbits = p.bits;
				}

			} else {
				int nump = (int) valb(consume(11).mkString());
				for (int i = 0; i < nump; ++i) {
					var p = new Packet(bits);
					subpackets = subpackets.append(p);
					bits = p.bits;
				}
			}
		}

		long readLiteral() {
			List<Integer> l = List.empty();
			int prefix;
			do {
				prefix = consume(1).head();
				l = l.appendAll(consume(4));
			} while (prefix == 1);
			return valb(l.mkString());
		}
	}

	void part1(String msg) {
		var p = new Packet(msg);
		System.out.println(p.sumVersion());
	}

	void part2(String msg) {
		var p = new Packet(msg);
		System.out.println(p.value());
	}

	long valb(String s) {
		return Long.parseLong(s, 2);
	}

	int valh(char c) {
		return (c >= 'A') ? (c - 'A' + 10) : (c - '0');
	}
	
	String bits(char c) {
		var s = "000" + Integer.toString(valh(c), 2);
		return s.substring(s.length() - 4);
	}


	List<Integer> bits(String hex) {
		var b = List.ofAll(hex.toCharArray()).map(this::bits).mkString();
		return List.ofAll(b.toCharArray()).map(c -> c - '0');
	}

	void test() {
		// System.out.println(bits('1'));
		// System.out.println(bits('A'));
		// System.out.println(bits("D2FE28"));
		// System.out.println(bitsl("D2FE28"));

		Packet m;
		// m = new Packet("D2FE28");
		// System.out.println(m.version + " / " + m.typeid + " / " + m.literal);
		// System.out.println(m.bits);

		// m = new Packet("38006F45291200");
		// System.out.println(m.version + " / " + m.typeid + " / " + m.literal + " / " + m.subpackets.size());
		// System.out.println(m.bits);

		assertThat(new Packet("8A004A801A8002F478").sumVersion(), is(16));
		assertThat(new Packet("620080001611562C8802118E34").sumVersion(), is(12));
		assertThat(new Packet("C0015000016115A2E0802F182340").sumVersion(), is(23));
		assertThat(new Packet("A0016C880162017C3686B18A3D4780").sumVersion(), is(31));

		assertThat(new Packet(myInput).sumVersion(), is(971));

		assertThat(new Packet("D2FE28").value(), is(2021L));

		assertThat(new Packet("C200B40A82").value(), is(3L));
		assertThat(new Packet("04005AC33890").value(), is(54L));
		assertThat(new Packet("880086C3E88112").value(), is(7L));
		assertThat(new Packet("CE00C43D881120").value(), is(9L));
		assertThat(new Packet("D8005AC2A8F0").value(), is(1L));
		assertThat(new Packet("F600BC2D8F").value(), is(0L));
		assertThat(new Packet("9C005AC2F8F0").value(), is(0L));
		assertThat(new Packet("9C0141080250320F1802104A08").value(), is(1L));
		assertThat(new Packet(myInput).value(), is(831996589851L));

		System.out.println("passed");
	}

	public static void main(String[] args) {

		System.out.println("=== test");
		new Day16().test();

		System.out.println("=== part 1");
		new Day16().part1(myInput);

		System.out.println("=== part 2");
		new Day16().part2(myInput);
	}

	static String example = "";
	static String myInput = "805311100469800804A3E488ACC0B10055D8009548874F65665AD42F60073E7338E7E5C538D820114AEA1A19927797976F8F43CD7354D66747B3005B401397C6CBA2FCEEE7AACDECC017938B3F802E000854488F70FC401F8BD09E199005B3600BCBFEEE12FFBB84FC8466B515E92B79B1003C797AEBAF53917E99FF2E953D0D284359CA0CB80193D12B3005B4017968D77EB224B46BBF591E7BEBD2FA00100622B4ED64773D0CF7816600B68020000874718E715C0010D8AF1E61CC946FB99FC2C20098275EBC0109FA14CAEDC20EB8033389531AAB14C72162492DE33AE0118012C05EEB801C0054F880102007A01192C040E100ED20035DA8018402BE20099A0020CB801AE0049801E800DD10021E4002DC7D30046C0160004323E42C8EA200DC5A87D06250C50015097FB2CFC93A101006F532EB600849634912799EF7BF609270D0802B59876F004246941091A5040402C9BD4DF654967BFDE4A6432769CED4EC3C4F04C000A895B8E98013246A6016CB3CCC94C9144A03CFAB9002033E7B24A24016DD802933AFAE48EAA3335A632013BC401D8850863A8803D1C61447A00042E3647B83F313674009E6533E158C3351F94C9902803D35C869865D564690103004E74CB001F39BEFFAAD37DFF558C012D005A5A9E851D25F76DD88A5F4BC600ACB6E1322B004E5FE1F2FF0E3005EC017969EB7AE4D1A53D07B918C0B1802F088B2C810326215CCBB6BC140C0149EE87780233E0D298C33B008C52763C9C94BF8DC886504E1ECD4E75C7E4EA00284180371362C44320043E2EC258F24008747785D10C001039F80644F201217401500043A2244B8D200085C3F8690BA78F08018394079A7A996D200806647A49E249C675C0802609D66B004658BA7F1562500366279CCBEB2600ACCA6D802C00085C658BD1DC401A8EB136100";
}
