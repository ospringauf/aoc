package aoc2019;

import java.util.stream.Stream;

public class Day01Java17 {

    record Load(int mass) {
        int fuel() {
            return mass / 3 - 2;
        }

        int totalFuel() {
            int f = fuel();
            // return (f > 0) ? f + new Load(f).totalFuel() : 0;

            return switch (f) {
            case -2, -1, 0 -> 0;
            default -> f + new Load(f).totalFuel();
            };
        }
    }

    public static void main(String[] args) {

        var result = Stream.of(input.split("\n")).mapToInt(Integer::parseInt).mapToObj(Load::new).mapToInt(
                Load::totalFuel).sum();

        System.out.println(result);
    }

    static String input = """
            104489
            69854
            93424
            103763
            119636
            130562
            121744
            84851
            143661
            94519
            116576
            148771
            74038
            131735
            95594
            125198
            92217
            84471
            53518
            97787
            55422
            137807
            78806
            74665
            103930
            121642
            123008
            104598
            97383
            129359
            85372
            88930
            106944
            118404
            126095
            67230
            116697
            85950
            148291
            123171
            82736
            52753
            134746
            53238
            74952
            105933
            104613
            115283
            80320
            139152
            76455
            66729
            81209
            130176
            116843
            67292
            74262
            131694
            92817
            51540
            58957
            143342
            76896
            129631
            77148
            129784
            115307
            96214
            110538
            51492
            124376
            78161
            59821
            58184
            132524
            130714
            112653
            137988
            112269
            62214
            115989
            123073
            119711
            82258
            67695
            81023
            70012
            93438
            131749
            103652
            63557
            88224
            117414
            75579
            146422
            139852
            85116
            124902
            143167
            147781
                        """;
}
