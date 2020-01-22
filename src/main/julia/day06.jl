data = [("COM", "B"),("B", "C"),("C", "D"),("D", "E"),("E", "F"),("B", "G"),("G", "H"),("D", "I"),("E", "J"),("J", "K"),("K", "L")] # ,("K", "YOU"),("I", "SAN")]

data = map(x->(x[1], x[2]), map(x->split(x,')'), readlines("input06.txt")))
# data = readlines("input06.txt") |> x->split.(x,')') |> x->tuple.(x[1], x[2])

outer(p) = map(x->x[2], filter(x->x[1] == p, data))
inner(p) = map(x->x[1], filter(x->x[2] == p, data))

function norbits(i, n)::Int
    o = outer(i)
    map(x -> n + 1 + norbits(x, n + 1), o) |> sum
end

@time norbits("COM", 0)

# part 2 --------------------------------

data = [("COM", "B"),("B", "C"),("C", "D"),("D", "E"),("E", "F"),("B", "G"),("G", "H"),("D", "I"),("E", "J"),("J", "K"),("K", "L") ,("K", "YOU"),("I", "SAN")]

function path(from, to, way)::Number
    if from==to 
        println(way)
        return 0
    end
    via = [inner(from)..., outer(from)...]
    via = filter(x-> x ∉ way, via)
    if length(via)==0
        return Inf
    end
    pathx(x) = 1 + path(x, to, [way..., x])
    paths = map(pathx, via)
    return minimum(paths)
end

function path(from, to) 
    from = inner(from)[1]
    to = inner(to)[1]
    path(from, to, [from])
end

path("B", "B")
path("G", "C")
path("C", "K")
path("X", "COM")
@time path("YOU", "SAN")

