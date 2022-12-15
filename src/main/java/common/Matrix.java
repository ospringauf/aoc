package common;

import io.vavr.Function1;
import io.vavr.collection.List;

public class Matrix extends PointMap<Integer> {
    
    private static final long serialVersionUID = 1L;
    
    
    int dim;
    
    public Matrix(int dim) {
        super();
        this.dim = dim;
    }
    
    public int get(int x, int y) {
        return getOrDefault(Point.of(x,y), 0);
    }
    
    public void put(int x, int y, int v) {
        put(Point.of(x,y), v);
    }
    
    public void print() {
        super.printS(i -> String.format(" %2d", (i==null)?0:i));
    }
    
    public int det() {
        if (dim==2) {
            return get(0,0)*get(1,1) - get(0,1)*get(1,0);
        } else {
            return   get(0,0)*get(1,1)*get(2,2)
                    +get(1,0)*get(2,1)*get(0,2)
                    +get(2,0)*get(0,1)*get(1,2)
                    -get(0,2)*get(1,1)*get(2,0)
                    -get(1,2)*get(2,1)*get(0,0)
                    -get(2,2)*get(0,1)*get(1,0);
        }
    }
    
    public List<Integer> apply(List<Integer> v) {
        var l = v.size();
        return List.range(0, l).map(i -> List.range(0, l).map(j -> get(j, i)*v.get(j)).sum().intValue());
    }
    
    public Point apply(Point p) {
        var r = apply(List.of(p.x(), p.y()));
        return Point.of(r.get(0), r.get(1));
    }

    public Pos3 apply(Pos3 p) {
        var r = apply(List.of(p.x(), p.y(), p.z()));
        return Pos3.of(r.get(0), r.get(1), r.get(2));
    }
    
    Function1<Point, Point> toFunc2d() {
        return p -> this.apply(p);
    }

    Function1<Pos3, Pos3> toFunc3d() {
        return p -> this.apply(p);
    }

    // det(M)=1 are rotations, det(M)=-1 are mirrors 
    static List<Matrix> transformations3d() {
        List<Matrix> result = List.empty();
        for (var p : List.of(0,1,2).permutations().toList()) {
            for (var s : List.of(-1, 1).crossProduct(3).toList()) {
                var m = new Matrix(3);
                m.put(0, p.get(0), s.get(0));
                m.put(1, p.get(1), s.get(1));
                m.put(2, p.get(2), s.get(2));
                result = result.append(m);
            }
        }
        
        return result;
    }
    
    static List<Matrix> transformations2d() {
        List<Matrix> result = List.empty();
        for (var p : List.of(0,1).permutations().toList()) {
            for (var s : List.of(-1, 1).crossProduct(2).toList()) {
                var m = new Matrix(2);
                m.put(0, p.get(0), s.get(0));
                m.put(1, p.get(1), s.get(1));
                result = result.append(m);
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        var t = transformations3d();
        System.out.println(t.size());
        t = t.filter(m -> m.det()==1);
        System.out.println(t.size());
        
        t.head().print();
        var v = t.last().apply(List.of(1,0,0));
        System.out.println(v);
    }

}
