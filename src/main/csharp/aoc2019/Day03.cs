using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using aoc;

namespace aoc2019
{
    class Day03 : AocDay
    {

        string[] Input1 = ReadLines("input03.txt")[0].Split(',');
        string[] Input2 = ReadLines("input03.txt")[1].Split(',');

        public void main()
        {
            Console.WriteLine("=== AOC 2019 day 03");
            var t0 = DateTime.Now;

            Console.WriteLine("--- part 1");
            Part1();

            Console.WriteLine("--- part 2");
            Part2();

            Console.WriteLine("=== end ({0} ms)", (DateTime.Now - t0).Milliseconds);
        }

        private void Part1()
        {
            var l1 = WirePoints(Input1).ToArray();
            var l2 = WirePoints(Input2).ToArray();
            var crossing = l1.ToHashSet().Intersect(l2.ToHashSet()).Min(p => p.Manhattan());
            Console.WriteLine(crossing);
        }

        private void Part2()
        {
            var l1 = WirePoints(Input1).ToArray();
            var l2 = WirePoints(Input2).ToArray();
            var crossings = l1.ToHashSet().Intersect(l2.ToHashSet());
            var p = crossings.Select(p => 2 + Array.IndexOf(l1, p) + Array.IndexOf(l2, p)).Min();
            Console.WriteLine(p);
        }

        private IEnumerable<Point> WirePoints(string[] input)
        {
            var p = Point.Of(0, 0);
            foreach (string s in input)
            {
                var dir = s.ElementAt(0);
                var len = int.Parse(s.Substring(1));
                while (len > 0)
                {
                    switch (dir)
                    {
                        case 'U': yield return (p = Point.Of(p.X, p.Y - 1)); break;
                        case 'D': yield return (p = Point.Of(p.X, p.Y + 1)); break;
                        case 'L': yield return (p = Point.Of(p.X - 1, p.Y)); break;
                        case 'R': yield return (p = Point.Of(p.X + 1, p.Y)); break;

                        default: throw new InvalidDataException($"dir {dir} unknown");
                    }
                    len--;
                }

            }
        }
    }
}
