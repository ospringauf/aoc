package common;

import io.vavr.Function1;
import io.vavr.collection.List;

public record Pos3(int x, int y, int z) {

    public static final Function1<Pos3, Pos3> ROTX = b -> new Pos3(b.x, -b.z, b.y);
    public static final Function1<Pos3, Pos3> ROTY = b -> new Pos3(b.z, b.y, -b.x);
    public static final Function1<Pos3, Pos3> ROTZ = b -> new Pos3(b.y, -b.x, b.z);

    int manhattan(Pos3 b) {
        return Math.abs(b.x - x) + Math.abs(b.y - y) + Math.abs(b.z - z);
    }

    public static Pos3 of(int x, int y, int z) {
        return new Pos3(x, y, z);
    }

    public Pos3 rotx() {
        return new Pos3(x, -z, y);
    }

    public Pos3 roty() {
        return new Pos3(z, y, -x);
    }

    public Pos3 rotz() {
        return new Pos3(y, -x, z);
    }

    public Pos3 plus(Pos3 p) {
        return new Pos3(x + p.x, y + p.y, z + p.z);
    }
    
    public Pos3 plus(int dx, int dy, int dz) {
        return new Pos3(x + dx, y + dy, z + dz);
    }

    public Pos3 minus(Pos3 p) {
        return new Pos3(x - p.x, y - p.y, z - p.z);
    }
    
    public String toString() {
        return String.format("(%d,%d,%d)", x,y,z);
    }
    
    public List<Pos3> neighbors6() {
        return List.of(plus(1,0,0), plus(-1,0,0), plus(0,1,0), plus(0,-1,0), plus(0,0,1), plus(0,0,-1));
    }
    
    public static Pos3 parse(String s) {
        var f = s.split("\\W+"); // non-word characters
        return new Pos3(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]));
    }

}