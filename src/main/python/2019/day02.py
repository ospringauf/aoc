
from intcomp import IntComputer

with open("input02.txt") as f:
    data = [int(x) for x in f.readlines()[0].split(",")] 

# c = IntComputer([1,1,1,4,99,5,6,0,99])

c = IntComputer(data)
c.mem[1:3] = 12, 2
c.run()
print(c.mem[0:10])

# === part 2

for noun in range(100):
    for verb in range(100):
        c = IntComputer(data)
        c.mem[1:3] = noun, verb
        c.run()
        if c.mem[0] == 19690720: print(c.mem[1:3])
