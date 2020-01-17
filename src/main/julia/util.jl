function minby(f, A) 
    _,index = findmin(f.(A))
    return A[index]
end

function maxby(f, A) 
    _,index = findmax(f.(A))
    return A[index]
end
