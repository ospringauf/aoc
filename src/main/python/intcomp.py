

class IntComputer:
    ADD = 1
    MULT = 2
    HALT = 99

    mem = []
    iptr = 0
    
    def add(self, p, acc):
        self.mem[p(3)] = acc(1) + acc(2)
        self.iptr += 4

    def mult(self, p, acc):
        self.mem[p(3)] = acc(1) * acc(2)
        self.iptr += 4

    def unknown(self, p, acc):
        raise ValueError("unknown opcode " + str(p(0)) + " at " + str(self.iptr))


    def __init__(self, prog):
        self.mem = prog[:]

    def run1(self):
        # print("iptr=", self.iptr)
        ops = {
            1 : self.add,
            2 : self.mult
        }
        
        p = lambda n: self.mem[self.iptr + n]
        pos = lambda n: self.mem[p(n)]

        instr = p(0)
        op = instr
        
        ops.get(op)(p, pos)

    def run(self):
        while self.mem[self.iptr] != 99:
            self.run1()