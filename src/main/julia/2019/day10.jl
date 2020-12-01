# https://adventofcode.com/2019/day/10
println("--- Day 10: Monitoring Station ---")

include("point.jl")
include("util.jl")

M = pmscan(c->c == '#', readlines("input10.txt"))
# print(M, v->v ? '#' : '.')

# delete empty positions (value == false)
M = Dict(filter(p -> p.second, M))

asteroids = collect(keys(M))

function nvisible(from::Point)
    angle(p::Point) = atan(p.y - from.y, p.x - from.x)
    a = map(p->angle(p), asteroids)
    length(unique(a))
end

station = maxby(a->nvisible(a), asteroids) # 20,21

part1 = nvisible(station) # ==> 247

# part 2

compass(x,y) = (atand(-x,y)+180) % 360 # north=0, clockwise

# group asteroids by angle from station, by decreasing distance from station
function byangle(from::Point)
    angle(p::Point) = compass(p.x-from.x, p.y-from.y)
    sameangle(θ) = filter(p -> angle(p)==θ, asteroids)
    
    angles = unique(map(p -> angle(p), asteroids))
    targets(θ) = sort(sameangle(θ); by = p->manhattan(station,p), rev=true)
    
    return Dict(θ => targets(θ) for θ = angles)
end

# byangle(station)[0]

function rotateLaser()
    A = byangle(station)
    angles = sort(collect(keys(A)))

    zapped=[]
    for i=1:length(asteroids)
        θ = angles[mod1(i,length(angles))]
        targets = get(A, θ, [])
        if (! isempty(targets))
            append!(zapped, pop!(targets))
        end
    end
    return zapped
end

part2 = rotateLaser()[200] 

part1
part2
