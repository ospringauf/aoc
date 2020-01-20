include("intcomp.jl")
include("point.jl")

prog = map(x->parse(Int64, x), split(readlines("input13.txt")[1], ","))

screen = Dict{Point,Int}()

paint(x, y, v) = screen[Point(x, y)] = v

c = IntComputer(prog)
c.output = bufferAndSplat(3, paint)
run!(c)

print(screen, x->" :#-*"[x + 1])

part1 = count(x==2 for x=values(screen))

# part 2

findonscreen(n)::Point = findfirst(v->v==n, screen)
ball() = findonscreen(4)
paddle() = findonscreen(3)
joystick() = sign(ball().x - paddle().x)


function paint(x, y, v) 
    p = Point(x, y)
    if p == Point(-1, 0)
        global score = v
        # print(screen, x->" :#-o"[x+1])
    else
        screen[p] = v
    end
end

empty!(screen)

score = 0

c = IntComputer(prog)
c.mem[1] = 2
c.output = bufferAndSplat(3, paint)
c.input = joystick
@time run!(c)

part2 = score


