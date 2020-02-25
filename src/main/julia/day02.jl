include("intcomp.jl")

prog = map(x->parse(Int64, x), split(readlines("input02.txt")[1], ","))
c = IntComputer(prog)

function part1()
    c.mem[2:3] = [12,02]
    run!(c)
    c.mem[1]
end

function part2() 
    for p in [(noun,verb) for noun=0:99 for verb=0:99]
        c.mem = copy(prog)
        c.mem[2:3] = [p...]
        c.iptr = 1

        run!(c)
        if (c.mem[1] == 19690720)
            return p
        end
    end
end

part1()
part2()