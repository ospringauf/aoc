Chem = AbstractString

struct Prod
    qty::Int
    name::Chem
end

struct Reac
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
    Reac(output, input)
end

# parseprod("2 GPBXP")
# parsereact("3 SMLPV, 11 NZJV, 1 HTSXK => 2 GPBXP")

R = Dict(r.output.name=>r for r=(parsereact(x) for x=readlines("input14.txt"))) # reaction map, eg. R["FUEL"]
N = Dict{Reac,Int}() # reaction factor for planned reactions

done(r::Reac) = r in keys(N)
todo(r::Reac) = !done(r)
done() = filter(done, collect(values(R)))
todo() = filter(todo, collect(values(R)))

# calc iterations of reaction to produce needed quantity
nreact(r::Reac, qty) = ceil(qty / r.output.qty) 

needed(p::Prod, chem::Chem) = p.name==chem ? p.qty : 0
needed(r::Reac, chem::Chem) = sum(needed(pin, chem) for pin=r.input)
# total amount of chem needed by all reactions
needed(c::Chem) = sum(N[r] * needed(r,c) for r=done())

needs(r::Reac, chem::Chem) = any(pin.name==chem for pin=r.input)
needs(r::Reac, pin::Prod) = needs(r, pin.name)

# no "to do" reaction depends on this reaction (needs the output)
ready(r::Reac) = any(needs(x, r.output) for x=done()) && !any(needs(x, r.output) for x=todo())
ready(c::Chem) = ready(R[c])

function produce(qty, result)
    global N = Dict(R[result]=>qty)

    # schedule reactions: as soon as we know the total required output
    while !isempty(todo())
        for r = filter(ready, todo())
            # how much of r's output do we need?
            need = needed(r.output.name)
            # how many times do we have to repeat the reaction?
            N[r] = nreact(r, need)
            # println("produce (", need, " ", r.output.name, ") <- ", N[r], " x ", r)
        end
    end

    return needed("ORE")
end

@time part1 = produce(1, "FUEL")


# part 2


# binary search for maximum parameter for predicate f
function binse(f::Function, (l,u)) 
    if (u <= l+1) return l; end
    m = (u+l) ÷ 2
    return f(m) ? binse(f, (m,u)) : binse(f, (l,m))
end
# binse(x->x<398, (0,1e9))

qore = 1000000000000
enoughOre(x) = produce(x, "FUEL") <= qore

@time part2 = binse(enoughOre, (0, qore))