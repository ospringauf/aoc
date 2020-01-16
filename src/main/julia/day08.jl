data = map( c -> c-'0', collect(readlines("input08.txt")[1]))

LSIZE = 25*6
layers = length(data)/LSIZE
layer(i) = data[(i-1)*LSIZE+1 : (i)*LSIZE]

ncount(n, A) = count(x->x==n, A)

function minby(f, a) 
    _,i = findmin(f.(a))
    return a[i]
end

l0 = minby(i -> ncount(0, layer(i)), [1:100;])
ncount(1, layer(l0)) * ncount(2, layer(l0))

# part 2

function combine(L,U)
    combine1(l,u) = (u == 2) ? l : u
    return combine1.(L,U)
end

function combine(img)
    L = layer(100)
    for i=reverse(1:99)
        L = combine(L, layer(i))
    end
    return L
end

function part2()
    L = combine(data)
    for y=1:6
      for x=1:25
        i = 25(y-1) + x
        v = L[i]+1
        print(" #"[v])
      end
      println()  
    end

    # for i in ((25(y-1) + x) for y=1:6 for x=1:25)
    #     v = L[i]+1
    #     print(" #"[v])
    # end
end

part2()
