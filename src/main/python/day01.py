
with open("input01.txt") as f:
    data = f.readlines()
    data = [int(x.strip()) for x in data] 


def fuel(m):
    return int((m/3)-2)

def fuel2(m):
    f = fuel(m)
    return f + fuel2(f) if f>0 else 0

print(sum(map(fuel, data), 0))

print(sum(map(fuel2, data), 0))
