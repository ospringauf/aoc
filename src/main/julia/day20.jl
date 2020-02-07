include("point.jl")
include("util.jl")

M = pmscan(identity, readlines("input20_test3.txt"))
print(M)

function collectLabels()
    L = Dict()

    for p = filter(p->M[p]=='.', points(M))
        if isuppercase(M[north(p)])
            L[p] = join([M[north(north(p))], M[north(p)]])
        elseif isuppercase(M[east(p)])
            L[p] = join([M[east(p)], M[east(east(p))]])
        elseif isuppercase(M[south(p)])
            L[p] = join([M[south(p)], M[south(south(p))]])
        elseif isuppercase(M[west(p)])
            L[p] = join([M[west(west(p))], M[west(p)]])
        end
    end    
    return L
end


function outside(p) 
    bb = pmbox(M)
    p.x == bb.x.start+2 || p.x==bb.x.stop-2 || p.y==bb.y.start+2 || p.y==bb.y.stop-2
    #p.x == bb.x.start || p.x==bb.x.stop || p.y==bb.y.start || p.y==bb.y.stop
end

partner(p) = getfirst(q->q!=p && L[q]==L[p], points(L)) 


L = collectLabels()
outer = Dict(reverse(e) for e=L if outside(e.first))
inner = Dict(reverse(e) for e=L if !outside(e.first))
warp = Dict(p=>partner(p) for p=points(L) if partner(p)!==nothing)


# pre-calc neighbor relation
N = Dict(p => [neighbors(p)..., get(warp, p, nothing)] for p=points(M))

# reduce maze: remove walls and labels
M0 = Dict(e for e=M if e.second=='.')
print(M0)
    
@time D = minDistance(M0, outer["AA"], x->true, p->N[p])
@show part1 = D[outer["ZZ"]]


# part 2 - TODO

# X = Dict("AA" => Dict(i.first=>D[i.second] for i=inner))
# X = Dict(("AA", i.first)=>D[i.second] for i=inner)

lo(s)=s[1:2]*"o"
li(s)=s[1:2]*"i"
l(p) = outside(p) ? lo(L[p]) : li(L[p])
#L2 = Dict(l(p)=>p for p=keys(L))

function adj(M)
    A = Dict()
    for p = keys(L)
        # Neigh = Dict(p => [neighbors(p)..., get(warp, p, nothing)] for p=points(M))
        Dp = minDistance(M, p, x->true, neighbors)
        for q = keys(L)
            A[(l(p), l(q))] = get(Dp, q, Inf)
        end
    end
    return A
end


@show A=adj(M0)

function addLevel(A)
    iso(s)=s[3]=='o'
    isoo(p)=iso(p[1])&&iso(p[2])
    isi(s)=s[3]=='i'
    isii(p)=isi(p[1])&&isi(p[2])
    isoi(p)=iso(p[1])&&isi(p[2])
    isio(p)=isi(p[1])&&iso(p[2])

    B = Dict(A)
    A=B
    #better = true
    #while better
        #better = false
        for x=filter(isoo, keys(A))
            # println(x)
            for y=filter(isoo, keys(A))
                s1 = (x[1], li(y[1]))
                s2 = y
                s3 = (li(y[2]), x[2])

                d0 = get(A, x, Inf)
                d1 = get(A, s1, Inf) + get(A, s2, Inf) + get(A, s3, Inf) #+ 1
                d1 += haskey(A, (li(y[1]), li(y[2]))) ? 1 : 0
                
                if (d1 < d0)
                    #better = true
                    B[x] = B[reverse(x)] = d1
                end
            end        
        end
    #end
    return B
end

Y = Dict(A)

for i=1:25
    global Y = addLevel(Y)
    @show i, Y["AAo", "ZZo"]
end

