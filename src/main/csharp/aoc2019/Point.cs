using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using aoc;

namespace aoc2019
{
    class Point
    {
        public int X {get; set;}
        public int Y {get; set;}

        public int Manhattan() => Math.Abs(X) + Math.Abs(Y);

        public static Point Of(int x, int y) => new Point() {X=x, Y=y};

        public override string ToString() {
            return $"P[{X},{Y}]";
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }
            
            var p = obj as Point;
            return X == p.X && Y == p.Y;
        }
        
        // override object.GetHashCode
        public override int GetHashCode()
        {
            return X + 10000*Y;
        }
    }
}