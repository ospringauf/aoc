include("point.jl")

in1, in2 = map(s->split(s, ","), readlines("input03.txt"))[1:2]

function next(p::Point, d::Char)
    d == 'U' ? north(p) :
    d == 'D' ? south(p) :
    d == 'L' ? west(p) :
    east(p)
end

function wire(input)::Array{Point}
    p = Point(0, 0)
    points = []
    for leg = input
        dir = leg[1]
        len = parse(Int, leg[2:end])
        for _ in 1:len
            p = next(p, dir)
            push!(points, p)
        end        
        
    end
    return points
end

# part 1

w1, w2 = wire(in1), wire(in2)

i = collect(intersect(Set(w1), Set(w2)))

min(manhattan.(i)...)


# part 2

idx(p, a) = findfirst(x->x == p, a)

runlen = map(x->idx(x, w1) + idx(x, w2), i)

min(runlen...)

