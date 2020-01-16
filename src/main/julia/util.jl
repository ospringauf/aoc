function minby(f, A) 
    _,index = findmin(f.(A))
    return A[index]
end
