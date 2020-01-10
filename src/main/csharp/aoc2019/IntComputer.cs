using System;
using System.Collections;
using System.Collections.Generic;

namespace aoc2019
{
    enum OpCode { ADD = 1, MULT = 2, INPUT = 3, OUTPUT = 4, JUMP_IF_TRUE = 5, JUMP_IF_FALSE = 6, LESS_THAN = 7, EQUALS = 8, HALT = 99 }


    class IntComputer
    {
        public long[] Mem = new long[100000];
        int iptr = 0;
        bool Halted => Mem[iptr] == (int)OpCode.HALT;
        long instructionCount = 0;
        private readonly long[] program;

        public Queue<long> InputData {get;} = new Queue<long>();

        public IntComputer(long[] program)
        {
            this.program = program;
            Reset();
        }

        public void Run()
        {
            while (!Halted)
            {
                Run1();
            }
        }

        public void Run1()
        {
            instructionCount++;
            int instruction = (int)Mem[iptr];
            var op = (OpCode)(instruction % 100);

            (long p1, long p2, long p3) = (Mem[iptr + 1], Mem[iptr + 2], Mem[iptr + 3]);
            var mode = new int[] { 0, instruction / 100 % 10, instruction / 1000 % 10, instruction / 10000 % 10 };

            Func<int, long> param = i => mode[i] == 0 ? /*pos*/ Mem[Mem[iptr + i]] : /*imm*/ Mem[iptr + i];

            switch (op)
            {
                case OpCode.ADD:
                    Mem[p3] = param(1) + param(2);
                    iptr += 4;
                    break;

                case OpCode.MULT:
                    Mem[p3] = param(1) * param(2);
                    iptr += 4;
                    break;

                case OpCode.INPUT:
                    Mem[p1] = Input();
                    iptr += 2;
                    break;

                case OpCode.OUTPUT:
                    Output(param(1));
                    iptr += 2;
                    break;

                case OpCode.JUMP_IF_TRUE:
                    iptr = param(1) != 0 ? (int)param(2) : iptr + 3;
                    break;

                case OpCode.JUMP_IF_FALSE:
                    iptr = param(1) == 0 ? (int)param(2) : iptr + 3;
                    break;

                case OpCode.LESS_THAN:
                    Mem[p3] = (param(1) < param(2)) ? 1 : 0;
                    iptr += 4;
                    break;

                case OpCode.EQUALS:
                    Mem[p3] = (param(1) == param(2)) ? 1 : 0;
                    iptr += 4;
                    break;

                case OpCode.HALT:
                    break;

                default:
                    throw new NotImplementedException($"opcode {op} is unknown");
            }

        }

        public virtual void Output(long v)
        {
            Console.WriteLine("output: {0}", v);
        }

        public virtual long Input()
        {
            return InputData.Dequeue();
        }

        internal void Reset()
        {
            Array.Fill(Mem, 0);
            Array.Copy(program, Mem, program.Length);
            iptr = 0;
        }
    }
}