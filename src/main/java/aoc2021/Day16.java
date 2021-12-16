package aoc2021;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 16: Packet Decoder ---
// https://adventofcode.com/2021/day/16

class Day16 extends AocPuzzle {

    String myInput = file2string("input16.txt");

    class Bitstream {
        private List<Integer> bits;

        Bitstream(String hexMessage) {
            bits = List.ofAll(hexMessage.toCharArray()).flatMap(c -> bits(c));
        }

        Bitstream(List<Integer> subBits) {
            bits = subBits;
        }

        static Long readLong(List<Integer> bits) {
            return bits.foldLeft(0L, (r, i) -> 2 * r + i);
        }

        long readLong(int n) {
            return readLong(readBits(n));
        }

        int readInt(int n) {
            return (int) readLong(n);
        }

        List<Integer> readBits(int n) {
            var r = bits.take(n);
            bits = bits.drop(n);
            return r;
        }

        public boolean exhausted() {
            return bits.isEmpty();
        }
    }

    class Packet {
        List<Packet> packets = List.empty();
        int version;
        int typeid;
        long literal;

        Packet(String hex) {
            this(new Bitstream(hex));
        }

        Packet(Bitstream b) {
            version = b.readInt(3);
            typeid = b.readInt(3);

            if (typeid == 4) {
                readLiteral(b);
            } else {
                readOperator(b);
            }
        }

        int sumVersion() {
            return version + packets.map(p -> p.sumVersion()).sum().intValue();
        }

        long value() {
            var values = packets.map(p -> p.value()).toList();

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

            // System.out.println(r + " <- " + vals + " | " + typeid);
            return r;
        }

        void readOperator(Bitstream b) {
            int lenghtType = b.readInt(1);

            if (lenghtType == 0) {
                
                int size = b.readInt(15);
                var subBits = new Bitstream(b.readBits(size));
                while (!subBits.exhausted()) {
                    var p = new Packet(subBits);
                    packets = packets.append(p);
                }
                
            } else {
                
                int numPackets = b.readInt(11);
                for (int i = 0; i < numPackets; ++i) {
                    var p = new Packet(b);
                    packets = packets.append(p);
                }
            }
        }

        void readLiteral(Bitstream b) {
            List<Integer> bits = List.empty();
            int prefix;
            do {
                prefix = b.readInt(1);
                bits = bits.appendAll(b.readBits(4));
            } while (prefix == 1);
            literal = Bitstream.readLong(bits);
        }
    }

    static List<Integer> bits(char hex) {
        var v = (hex >= 'A') ? (hex - 'A' + 10) : (hex - '0');
        return List.range(0, 4).map(i -> (v >> (3 - i)) & 1);
    }

    void solve(String msg) {
        var p = new Packet(msg);
        System.out.println("version: " + p.sumVersion());
        System.out.println("value  : " + p.value());
    }

    void test() {
        assertThat(bits('1').mkString(), is("0001"));
        assertThat(bits('B').mkString(), is("1011"));
        assertThat(new Bitstream("D2FE28").bits.mkString(), is("110100101111111000101000"));

        assertThat(new Packet("D2FE28").version, is(6));
        assertThat(new Packet("D2FE28").typeid, is(4));
        assertThat(new Packet("D2FE28").value(), is(2021L));

        assertThat(new Packet("8A004A801A8002F478").sumVersion(), is(16));
        assertThat(new Packet("620080001611562C8802118E34").sumVersion(), is(12));
        assertThat(new Packet("C0015000016115A2E0802F182340").sumVersion(), is(23));
        assertThat(new Packet("A0016C880162017C3686B18A3D4780").sumVersion(), is(31));

        assertThat(new Packet(myInput).sumVersion(), is(971));

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

        System.out.println("=== solve");
        new Day16().solve(new Day16().myInput);
    }
}
