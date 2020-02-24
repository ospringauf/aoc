using Test


function arab(r)
    vals = Dict("I" => 1, "V" => 5, "X" => 10, "L" => 50)

    if (length(r) == 0)
        return 0
    end
        
    if (endswith(r, "I"))
        return 1 + arab(r[1:end-1])
    end

    if (startswith(r, "I") && !endswith(r, "I"))
        return arab(r[2:end]) - 1
    end

    if (endswith(r, "V"))
        return 5 + arab(r[1:end-1])
    end
    

    0
end



@test arab("I") == 1
@test arab("II") == 2
@test arab("V") == 5
@test arab("IV") == 4
@test arab("IX") == 9
@test arab("XLII") == 42
@test arab("XCIX") == 99
@test arab("MMXIII") == 2013




vals = Dict("I" => 1, "V" => 5, "X" => 10, "L" => 50)
for e=vals
    println(e.first)
end

