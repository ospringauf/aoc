data = map( c -> c-'0', collect(readlines("input08.txt")[1]))

LSIZE = 25*6
layers = length(data)/LSIZE

layer(i) = data[(i-1)*LSIZE+1 : (i)*LSIZE]

ncount(n, A) = count(x->x==n, A)

function minby(f, A) 
    _,index = findmin(f.(A))
    return A[index]
end

l0 = minby(i -> ncount(0, layer(i)), [1:100;])
ncount(1, layer(l0)) * ncount(2, layer(l0))


# part 2

overlay(lower,upper) = (upper == 2) ? lower : upper

function overlay(img)
    L = layer(100)
    for i = 99:-1:1
        L = overlay.(L, layer(i))
    end
    return L
end

function part2()
    img = overlay(data)

    pixel(x,y)::Char = (" #")[img[ 25(y-1) + x ] + 1]

    for y=1:6
      for x=1:25
        print(pixel(x,y))
      end
      println()  
    end
end

part2()
