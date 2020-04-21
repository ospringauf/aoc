
from intcomp import IntComputer

with open("input05.txt") as f:
    prog = [int(x) for x in f.readlines()[0].split(",")] 

c = IntComputer([3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0])
c.input = lambda : 1
c.run()

# part 2

c = IntComputer(prog)
c.input = lambda : 5
c.run()
