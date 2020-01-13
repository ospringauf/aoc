using Printf
using IterTools


prog = map(x->parse(Int64, x), split(readlines("input02.txt")[1], ","))

mutable struct IntComputer
    iptr::Int
    mem::Array{Int64}

    function IntComputer(prog::Array{Int64})
        new(1, copy(prog))
    end
end

function run1(c::IntComputer)
    instr = c.mem[c.iptr]
    opc = instr % 100
    p = c.mem[c.iptr + 1:c.iptr + 3]

    pos(i) = c.mem[p[i]+1]
    tgt(i) = p[i]+1

    if opc == 1
        c.mem[tgt(3)] = pos(1) + pos(2)
        c.iptr += 4
    elseif opc == 2
        c.mem[tgt(3)] = pos(1) * pos(2)
        c.iptr += 4
    else
        println("invalid opcode ", opc)
    end
end

function run(c::IntComputer)
    while c.mem[c.iptr] != 99
        run1(c)
    end
end

c = IntComputer(prog)
c.mem[2:3]=[12,02]
run(c)
c.mem[1]


for p in Iterators.product(0:99,0:99)
    c.mem = copy(prog)
    c.mem[2:3]=[p...]
    c.iptr = 1

    run(c)
    if (c.mem[1]==19690720)
        println(p, c.mem[1])
        best=p
    end
end

best