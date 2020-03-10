

class IntComputer:
    ADD = 1
    MULT = 2
    HALT = 99

    mem = []
    iptr = 0

    def __init__(self, prog):
        self.mem = prog[:]

    def say(self):
        print("my program is ", len(self.mem))
    
    def run1(self):
        # print("iptr=", self.iptr)
        
        p = lambda n: self.mem[self.iptr + n]
        pos = lambda n: self.mem[p(n)]

        instr = p(0)
        op = instr
        
        # ADD
        if op==self.ADD: 
            self.mem[p(3)] = pos(1) + pos(2)
            self.iptr += 4
        # MULT
        elif op==2: 
            self.mem[p(3)] = pos(1) * pos(2)
            self.iptr += 4

        else:
            raise ValueError("unknown opcode " + str(op) + " at " + str(self.iptr))

    def run(self):
        while self.mem[self.iptr] != 99:
            self.run1()