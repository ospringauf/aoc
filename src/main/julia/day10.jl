include("point.jl")


m = Dict{Point,Bool}()

readlines("input10.txt")

m[Point(0,0)]=3
m[Point(1,2)]=5
values(m)
