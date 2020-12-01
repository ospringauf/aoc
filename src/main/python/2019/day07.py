
from intcomp import IntComputer
from itertools import permutations 

with open("input07.txt") as f:
    prog = [int(x) for x in f.readlines()[0].split(",")] 

# part 1

def newval(n):
    global val
    val = n

max = 0

for sett in list(permutations([0,1,2,3,4])):
    val = 0
    for i in range(0,5):
        data = [sett[i], val]
        amp = IntComputer(prog)
        amp.input = lambda: data.pop(0)
        amp.output = newval
        amp.run()
        if val > max:
            max = val
            print(sett)
            print(max)


# part 2
