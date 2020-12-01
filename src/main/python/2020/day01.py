
with open("input01.txt") as f:
    data = f.readlines()
    data = [int(x.strip()) for x in data] 

print(data)

def prod(sum, d):
    try:
        return list(filter(lambda x: (sum-x) in d, d))[0]
    except IndexError:
        return 0

prod(20241, data)

r1 = prod(2020, data)
print (r1 * (2020-r1))

list(filter(lambda x: prod(2020-x, data) in data, data))

reduce(lambda x,y: x*y, list(filter(lambda x: prod(2020-x, data) in data, data)))