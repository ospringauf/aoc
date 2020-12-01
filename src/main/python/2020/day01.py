with open("input01.txt") as f:
    data = f.readlines()
    data = [int(x.strip()) for x in data] 

print(data)

{ (2020-x)*x for x in data if (2020-x) in data }

def prod(sum):
    p = { (sum-x)*x for x in data if (sum-x) in data }
    return p.pop() if p else 0

prod(2020)

{ n for n in [ x*prod(2020-x) for x in data] if n>0 }

