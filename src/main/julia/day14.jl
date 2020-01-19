Chem = AbstractString

struct Prod
    qty::Int
    name::Chem
end

struct React
    output::Prod
    input::Array{Prod}
end

function parseprod(x) 
    s = split(x, " ")
    Prod(parse(Int, s[1]), s[2])
end

function parsereact(x) 
    s = split(x, " => ")
    input = collect(parseprod(x) for x=split(s[1], ", "))
    output = parseprod(s[2])
    React(output, input)
end

# parseprod("2 GPBXP")
# parsereact("3 SMLPV, 11 NZJV, 1 HTSXK => 2 GPBXP")

R = Dict(r.output.name=>r for r=(parsereact(x) for x=readlines("input14.txt")))
# R["FUEL"]
N = Dict{Chem,Int}() # reaction factor

# iterations of reaction to produce needed quantity
nreact(r::React, qty) = ceil(qty / r.output.qty) # calculate
nreact(r::React) = get(N, r.output.name, 0) # lookup

needed(p::Prod, chem) = p.name==chem ? p.qty : 0
needed(r::React, chem) = sum(needed(i, chem) for i=r.input)
# total amount of chem needed by all reactions
needed(c::Chem) = sum(needed(r,c) * nreact(r) for r=values(R))

needs(r::React, chem) = any(i.name==chem for i=r.input)
needs(cout::Chem,cin::Chem) = needs(R[cout], cin)



function produce!(qty, result)
    todo = Set(keys(R))
    global N = Dict(result=>qty)
    delete!(todo, result)

    # no "to do" reaction depends on this reaction (needs the output)
    # cando(r::React) = !any(needs(x, r.output.name) for x=(R[chem] for chem=todo))
    cando(r::React) = !any(needs(c, r.output.name) for c=todo)
    cando(c::Chem) = cando(R[c])

    while !isempty(todo)
        for chem = filter(c->cando(c), todo)
            need = needed(chem)
            N[chem] = nreact(R[chem], need)
            println("produce (", need, " ", chem, ") <- ", N[chem], " x ", R[chem])
            delete!(todo, chem)
        end
    end

    return needed("ORE")
end

@time part1 = produce!(1, "FUEL")



# part 2

qore = 1000000000000


# binary search for maximum parameter for predicate f
function binse(f::Function, (l,u)) 
    if (u <= l+1) return l; end
    m = (u+l) ÷ 2
    return f(m) ? binse(f, (m,u)) : binse(f, (l,m))
end
# binse(x->x<983, (0,1000))

enoughOre(x) = produce!(x, "FUEL") <= qore

part2 = binse(enoughOre, (0, qore))