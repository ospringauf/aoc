using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using aoc;

namespace aoc2019
{
    class Day02 : AocDay
    {

        long[] Program = ReadIntcode("input02.txt");

        public void main()
        {
            Console.WriteLine("=== AOC 2019 day 02");
            var t0 = DateTime.Now;

            Console.WriteLine("--- test");
            TestIntcode();

            Console.WriteLine("--- part 1");
            Part1();

            Console.WriteLine("--- part 2");
            Part2();

            Console.WriteLine("=== end ({0} ms)", (DateTime.Now - t0).Milliseconds);
        }

        private void Part2()
        {
            var x = 19690720;
            var comp = new IntComputer(Program);

            for (int verb = 0; verb <= 99; ++verb)
                for (int noun = 0; noun <= 99; ++noun)
                {
                    comp.Reset();
                    comp.Mem[1] = noun;
                    comp.Mem[2] = verb;

                    comp.Run();
                    if (comp.Mem[0] == x)
                    {
                        Console.WriteLine(100 * noun + verb);
                        return;
                    }
                }
        }

        private static void TestIntcode()
        {
            var c = new IntComputer(new long[] { 1, 1, 1, 4, 99, 5, 6, 0, 99 });
            c.Run();
            Console.WriteLine(c.Mem[0]);
        }

        private void Part1()
        {
            var comp = new IntComputer(Program);
            comp.Mem[1] = 12;
            comp.Mem[2] = 2;

            comp.Run();
            Console.WriteLine(comp.Mem[0]);
        }
    }
}
