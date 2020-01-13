mutable struct IntComputer
    iptr::Int
    mem::Array{Int64}

    function IntComputer(prog::Array{Int64})
        new(1, copy(prog))
    end
end

ADD=1
MULT=2

function run1(c::IntComputer)
    instr = c.mem[c.iptr]
    opc = instr % 100
    p = c.mem[c.iptr + 1:c.iptr + 3]

    pos(i) = c.mem[p[i]+1]
    tgt(i) = p[i]+1

    if opc == ADD
        c.mem[tgt(3)] = pos(1) + pos(2)
        c.iptr += 4
    elseif opc == MULT
        c.mem[tgt(3)] = pos(1) * pos(2)
        c.iptr += 4
    else
        throw(DomainError(opc, "invalid opcode"))
    end
end

function run(c::IntComputer)
    while c.mem[c.iptr] != 99
        run1(c)
    end
end
