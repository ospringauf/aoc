mutable struct IntComputer
    iptr::Int
    relbase::Int
    mem::Array{Int64}
    input::Function
    output::Function

    function IntComputer(prog::Array{Int64})
        m = copy(prog)
        push!(m, zeros(Int64, 10000)...) # add some empty space
        new(1, 0, m)
    end
end

ADD = 1
MULT = 2
INPUT = 3
OUTPUT = 4
JUMP_IF_TRUE = 5
JUMP_IF_FALSE = 6
LESS_THAN = 7
EQUALS = 8
RELBASE = 9
HALT = 99

function run1!(c::IntComputer)
    instr = c.mem[c.iptr]
    opc = instr % 100
    p = c.mem[c.iptr + 1:c.iptr + 3]
    mode = [instr ÷ 100, instr ÷ 1000, instr ÷ 10000] .% 10

    pos(i) = c.mem[p[i] + 1]
    rel(i) = c.mem[p[i] + c.relbase + 1]
    imm(i) = p[i]
    tgt(i) = mode[i] == 2 ? (p[i] + c.relbase + 1) : (p[i] + 1)
    param(i) = mode[i] == 2 ? rel(i) : mode[i] == 1 ? imm(i) : pos(i)

    # println(opc, p)

    if opc == ADD
        c.mem[tgt(3)] = param(1) + param(2)
        c.iptr += 4
    elseif opc == MULT
        c.mem[tgt(3)] = param(1) * param(2)
        c.iptr += 4
    elseif opc == INPUT
        c.mem[tgt(1)] = c.input()
        c.iptr += 2
    elseif opc == OUTPUT
        c.output(param(1))
        c.iptr += 2
    elseif opc == JUMP_IF_TRUE
        c.iptr = param(1) != 0 ? param(2) + 1 : c.iptr + 3
    elseif opc == JUMP_IF_FALSE
        c.iptr = param(1) == 0 ? param(2) + 1 : c.iptr + 3
    elseif opc == LESS_THAN
        c.mem[tgt(3)] = (param(1) < param(2)) ? 1 : 0
        c.iptr += 4
    elseif opc == EQUALS
        c.mem[tgt(3)] = (param(1) == param(2)) ? 1 : 0
        c.iptr += 4
    elseif opc == RELBASE
        c.relbase += param(1)
        c.iptr += 2
    else
        throw(DomainError(opc, "invalid opcode"))
    end
end

nextop(c::IntComputer) = c.mem[c.iptr]
halted(c::IntComputer) = nextop(c) == HALT

function run!(c::IntComputer)
    while !halted(c)
        run1!(c)
    end
end

function runToOutput!(c::IntComputer)
    while (nextop(c) != OUTPUT) && !halted(c)
        run1!(c)
    end
    if !halted(c)
        run1!(c)
    end
end

