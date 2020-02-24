using Test

function arab(r)
    val = Dict('I' => 1, 'V' => 5, 'X' => 10, 'L' => 50, 'C' => 100, 'D' => 500, 'M' => 1000)
    v(i) = val[r[i]]

    if (length(r) == 0)
        return 0
    end

    if (length(r) > 1 && v(1) < v(2))
        return arab(r[2:end]) - v(1)
    end

    return v(1) + arab(r[2:end])
end



@testset "roman->arab" begin
    cases = Dict("I" => 1, "II" => 2, "IV" => 4, "V" => 5, "IX" => 9, "XLII" => 42, "XCIX" => 99, "MMXIII" => 2013, "MMMDCCXXIV" => 3724)
    for c = cases
        @test arab(c.first) == c.second
    end
end

# -----------------------------------------------------------------------------


function roman(a, e)
    sym = Dict(1 => "I", 5 => "V", 10 => "X", 50 => "L", 100 => "C", 500 => "D", 1000 => "M")

    s(n) = get(sym, n*e, "?")

    digit = Dict(
        0 => "",
        1 => s(1),
        2 => s(1) * s(1),
        3 => s(1) * s(1) * s(1),
        4 => s(1) * s(5),
        5 => s(5),
        6 => s(5) * s(1),
        7 => s(5) * s(1) * s(1),
        8 => s(5) * s(1) * s(1) * s(1),
        9 => s(1) * s(10))

    if (a < 10)
        digit[a]
    else
        roman(a ÷ 10, e * 10) * digit[a % 10]
    end
end

roman(a) = roman(a, 1)
# roman(a) = begin
#     if (a >= 4000)
#         "[" * roman(a ÷ 1000, 1) * "]" * roman(a % 1000, 1)
#     else
#         roman(a,1)
#     end
# end


@testset "arab->roman" begin
    cases = Dict("I" => 1, "II" => 2, "IV" => 4, "V" => 5, "IX" => 9, "XLII" => 42, "XCIX" => 99, "MMXIII" => 2013, "MMMDCCXXIV" => 3724)
    for c = cases
        @test roman(c.second) == c.first
    end
end
