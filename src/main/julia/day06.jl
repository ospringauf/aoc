data = [("COM", "B"),("B", "C"),("C", "D"),("D", "E"),("E", "F"),("B", "G"),("G", "H"),("D", "I"),("E", "J"),("J", "K"),("K", "L")] # ,("K", "YOU"),("I", "SAN")]

outer(i) = map(x->x[2], filter(x->x[1] == i, data))

# filter(x->x[1] == "B", data)|>(x->x[2])
# outer("B")
# outer("K")
# length(outer("K"))

function norbits(i, n)::Int
    o = outer(i)
    map(x -> n + 1 + norbits(x, n + 1), o) |> sum
end


# norbits("K", 0)
norbits("COM", 0)

# part 2

data = [("COM", "B"),("B", "C"),("C", "D"),("D", "E"),("E", "F"),("B", "G"),("G", "H"),("D", "I"),("E", "J"),("J", "K"),("K", "L") ,("K", "YOU"),("I", "SAN")]

inner(p) = map(x->x[1], filter(x->x[2] == p, data))
inner("YOU")
inner("COM")

a =  [1,2]
[a...,3]

function path(from, to, way)::Int
    if from==to 
        println(way)
        return 0
    end
    via = [inner(from)..., outer(from)...]
    via = filter(x->!(x ∈ way), via)
    # println(via)
    if length(via)==0
        return 0
    end
    min(map(x -> 1 + path(x, to, [way..., x]), via)...)
end

path("B", "B", [])
path("B", "D", [])
path("G", "C", [])
path("YOU", "SAN", [])

min([1,2,3]...)

[inner("B")..., outer("B")...] - ["C"]

[inner("B")..., outer("B")...] .!= "C"

filter(x->x!="C", [inner("B")..., outer("B")...])
!(1∈[2])

