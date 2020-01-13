function pdigits(n)
    return [n÷100000, n÷10000, n÷1000, n÷100, n÷10, n] .% 10
end

mono(a) = all(i -> a[i]<=a[i+1], [1:5;])

pair(a) = any(i -> a[i]==a[i+1], [1:5;])

# pdigits(654321)
# mono(pdigits(123456))
# mono(pdigits(123465))

# pair(pdigits(123456))
# pair(pdigits(124456))
# pair(pdigits(124446))

passwd(n) = mono(pdigits(n)) && pair(pdigits(n))

length(filter(p -> passwd(p), [156218:652527;]))