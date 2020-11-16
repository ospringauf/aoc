

with open("input06.txt") as f:
    lines = f.readlines()
    data = [(a[0], a[1]) for a in [l.strip().split(")") for l in lines]]


def orbits(inner, n=0):
    outer = [x[1] for x in data if x[0] == inner]
    return n + sum([orbits(x,n+1) for x in outer])

# === part 1

print(orbits("COM"))

# === part 2

