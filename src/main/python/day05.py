
from intcomp import IntComputer

with open("input05.txt") as f:
    prog = [int(x) for x in f.readlines()[0].split(",")] 

c = IntComputer(prog)
c.input = lambda : 1
c.run()

# part 2

c = IntComputer(prog)
c.input = lambda : 5
c.run()
