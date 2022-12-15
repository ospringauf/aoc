package aoc2019;

class ZPoint extends Point {

	int z;

	public ZPoint(int x, int y, int z) {
		super(x, y);
		this.z = z;
	}
	
	public ZPoint(Point p, int z) {
		super(p.x, p.y);
		this.z = z;
	}

	static ZPoint of(int x, int y, int z) {
        return new ZPoint(x,y,z);
    }
	
	@Override
	public ZPoint north() {
    	return ZPoint.of(x,  y-1, z);
    }
	
	@Override
    public ZPoint south() {
    	return ZPoint.of(x,  y+1, z);
    }
	
	@Override
    public ZPoint east() {
    	return ZPoint.of(x+1,  y, z);
    }

	@Override
    public ZPoint west() {
    	return ZPoint.of(x-1,  y, z);
    }
    
	@Override
    public String toString() {
        return String.format("P[%d,%d,%d]", x, y,z);
    }
    
    @Override
    public boolean equals(Object obj) {
        ZPoint a = (ZPoint)obj;
        return x==a.x && y==a.y && z==a.z;
    }
    
    @Override
    public int hashCode() {
        return x + 1000*y + 100000*z;
    }
	
}