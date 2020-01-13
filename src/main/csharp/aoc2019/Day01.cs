using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using aoc;

namespace aoc2019
{
    class Day01 : AocDay
    {

        int[] Data = ReadInts("input01.txt");

        public void main()
        {
            Console.WriteLine("=== AOC 2019 day 01");
            var t0 = DateTime.Now;

            Console.WriteLine("--- part 1");
            Part1();

            Console.WriteLine("--- part 2");
            Part2();

            Console.WriteLine("=== end ({0} ms)", (DateTime.Now - t0).Milliseconds);
        }

        private void Part2()
        {
            int value = Data.SelectMany(RecFuel).Sum();
            Console.WriteLine(value);
        }

        IEnumerable<int> RecFuel(int mass)
        {
            int f = 0;
            while ((f = fuel(mass)) > 0)
            {
                yield return f;
                mass = f;
            }
        }

        void Part1()
        {
            int value = Data.Select(fuel).Sum();
            Console.WriteLine(value);
        }

        int fuel(int mass)
        {
            return (mass / 3) - 2;
        }
    }
}
