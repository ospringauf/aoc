

class IntComputer:
    mem = []
    iptr = 0

    input = lambda: 0
    output = lambda self,n: print("==> ", n)
    
    def add(self, p, param):
        self.mem[p(3)] = param(1) + param(2)
        self.iptr += 4

    def mult(self, p, param):
        self.mem[p(3)] = param(1) * param(2)
        self.iptr += 4

    def inp(self, p, param): 
        self.mem[p(1)] = self.input()
        self.iptr += 2

    def outp(self, p, param): 
        self.output(param(1))
        self.iptr += 2

    def jumpiftrue(self, p, param): 
        self.iptr = param(2) if param(1) != 0 else self.iptr+3

    def jumpiffalse(self, p, param): 
        self.iptr = param(2) if param(1) == 0 else self.iptr+3

    def lessthan(self, p, param): 
        self.mem[p(3)] = 1 if param(1) < param(2) else 0
        self.iptr += 4

    def equals(self, p, param): 
        self.mem[p(3)] = 1 if param(1) == param(2) else 0
        self.iptr += 4

    def unknown(self, p, param):
        raise ValueError("unknown opcode " + str(p(0)) + " at " + str(self.iptr))


    def __init__(self, prog):
        self.mem = prog[:]

    def run1(self):
        # print("iptr=", self.iptr)
        ops = {
            1 : self.add,
            2 : self.mult,
            3 : self.inp,
            4 : self.outp,
            5 : self.jumpiftrue,
            6 : self.jumpiffalse,
            7 : self.lessthan,
            8 : self.equals
        }
        
        # param value
        p = lambda n: self.mem[self.iptr + n]
        # position mode (0)
        pos = lambda n: self.mem[p(n)]
        # immediate mode (1)
        imm = lambda n: p(n)

        instr = p(0)
        mode = [0, instr//100%10, instr//1000%10, instr//10000%10]
        param = lambda n: [pos,imm][mode[n]](n)

        op = instr%100
        
        # execute
        ops.get(op)(p, param)


    def run(self):
        while self.mem[self.iptr] != 99:
            self.run1()