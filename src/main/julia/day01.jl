using Printf

masses = map(x->parse(Int, x), readlines("input01.txt"))

fuel(x) = floor(x / 3 - 2)
fuel(1969)

function fuel2(x) 
    f = fuel(x)
    f < 0 ? 0 : f + fuel2(f)
end    

fuel2(1969)

@printf "part 1: %10.0f\n" sum(map(x->fuel(x), masses))
@printf "part 2: %10.0f\n" sum(map(x->fuel2(x), masses))
# exit()

