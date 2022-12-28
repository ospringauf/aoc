package common;

import io.vavr.Function1;
import io.vavr.collection.List;

public record Vec3(int x, int y, int z) {

    public static final Function1<Vec3, Vec3> ROTX = b -> new Vec3(b.x, -b.z, b.y);
    public static final Function1<Vec3, Vec3> ROTY = b -> new Vec3(b.z, b.y, -b.x);
    public static final Function1<Vec3, Vec3> ROTZ = b -> new Vec3(b.y, -b.x, b.z);

    int manhattan(Vec3 b) {
        return Math.abs(b.x - x) + Math.abs(b.y - y) + Math.abs(b.z - z);
    }

    public static Vec3 of(int x, int y, int z) {
        return new Vec3(x, y, z);
    }

    public static Vec3 of(Point p) {
        return new Vec3(p.x(), p.y(), 0);
    }
    public Vec3 rotx() {
        return new Vec3(x, -z, y);
    }

    public Vec3 rotxr() {
        return new Vec3(x, z, -y);
    }

    
    public Vec3 roty() {
        return new Vec3(z, y, -x);
    }
    
    public Vec3 rotyr() {
        return new Vec3(-z, y, x);
    }

    public Vec3 rotz() {
        return new Vec3(y, -x, z);
    }

    public Vec3 plus(Vec3 p) {
        return new Vec3(x + p.x, y + p.y, z + p.z);
    }
    
    public Vec3 plus(int dx, int dy, int dz) {
        return new Vec3(x + dx, y + dy, z + dz);
    }

    public Vec3 minus(Vec3 p) {
        return new Vec3(x - p.x, y - p.y, z - p.z);
    }
    
    public String toString() {
        return String.format("(%d,%d,%d)", x,y,z);
    }
    
    public List<Vec3> neighbors6() {
        return List.of(plus(1,0,0), plus(-1,0,0), plus(0,1,0), plus(0,-1,0), plus(0,0,1), plus(0,0,-1));
    }
    
    public static Vec3 parse(String s) {
        var f = s.split("\\W+"); // non-word characters
        return new Vec3(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]));
    }

}