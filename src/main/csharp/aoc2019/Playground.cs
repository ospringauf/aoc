using System;
using System.Collections.Generic;
using System.Linq;
using Combinatorics.Collections;

namespace aoc2019
{
    internal class Playground
    {

        internal void main()
        {
            var value = new List<string> { "a", "b", "c", "d", "e" };

            // var c = new Combinations<string>(value, 2);//, GenerateOption.WithoutRepetition);

            var c = Enumerable.Range(0, 5).SelectMany(i => new Combinations<string>(value, i));
            foreach (var v in c)
            {
                Console.WriteLine(string.Join(",", v));
            }

            Console.WriteLine("===perm===");

            var p = new Permutations<string>(value);
             foreach (var v in p)
            {
                Console.WriteLine(string.Join(",", v));
            }
        }
    }
}