using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using aoc;

namespace aoc2019
{
    class Day05 : AocDay
    {

        long[] Program = ReadIntcode("input05.txt");

        public void main()
        {
            Console.WriteLine("=== adventofcode.com/2019/day/5: Sunny with a Chance of Asteroids");
            var t0 = DateTime.Now;

            Console.WriteLine("--- part 1");
            Part1();

            Console.WriteLine("--- part 2");
            Part2();

            Console.WriteLine("=== end ({0} ms)", (DateTime.Now - t0).Milliseconds);
        }

        private void Part1()
        {
            var comp = new IntComputer(Program);
            comp.InputData.Enqueue(1);
            comp.Run();
        }

          private void Part2()
        {
            var comp = new IntComputer(Program);
            comp.InputData.Enqueue(5);
            comp.Run();
        }
    }
}
