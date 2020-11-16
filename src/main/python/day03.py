

with open("input03.txt") as f:
    lines = f.readlines()
    w1 = lines[0].split(",")
    w2 = lines[1].split(",")

# === part 1


def wirepoints(w):

    nextpoint = {
        'R': lambda p: (p[0]+1, p[1]),
        'L': lambda p: (p[0]-1, p[1]),        
        'U': lambda p: (p[0], p[1]-1),
        'D': lambda p: (p[0], p[1]+1),
    }

    p = (0,0)
    res = [p]
    for t in w:
        dir = t[0]
        run = int(t[1:])
        for _ in range(run):
            p = nextpoint[dir](p)
            res.append(p)
    return res

def manh(p):
    return abs(p[0]) + abs(p[1])

wp1 = wirepoints(w1)
wp2 = wirepoints(w2)

i1 = list(set(wp1) & set(wp2))
print(sorted(map(manh, i1))[1])


# === part 2

def steps(p):
    return wp1.index(p) + wp2.index(p)

print(sorted(map(steps, i1))[1])    