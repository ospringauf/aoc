using System;
using System.IO;
using System.Linq;

namespace aoc
{
    class AocDay
    {
        private const string Prefix = "aoc2019/";

        static void Main(string[] args)
        {
            new aoc2019.Day05().main();
        }

        public static int[] ReadInts(string fname)
        {
            return File.ReadAllLines(Prefix + fname)
                       .Select(int.Parse)
                       .ToArray();
        }

        public static long[] ReadIntcode(string fname)
        {
            return File.ReadAllLines(Prefix + fname)[0]
                       .Split(',')
                       .Select(long.Parse)
                       .ToArray();
        }
        public static string[] ReadLines(string fname)
        {
            return File.ReadAllLines(Prefix + fname);
        }

        public static string[] ReadLineSplit(string fname)
        {
            return File.ReadAllLines(Prefix + fname)[0]
                       .Split(',')
                       .ToArray();
        }
    }
}
