import com.jogamp.opengl.GL2;

public class Coord {
    public double x;
    public double y;
    public double z;

    // constructor

    // default constructor
    public Coord(){
        this(0.0,0.0,0.0);
    }

    // custom constructor
    public Coord(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // should be need x,y,0/0,y,z/x,0,z? it depends;

    public Coord multiply(double right){
        return multiply(this,right);
    }

    public Coord multiplySelfAndScale(double right){
        Coord result = multiply(this,right);
        x = result.x;
        y = result.y;
        z = result.z;
        return this;
    }

    public Coord multiplySelfAndScale(Coord right){
        Coord result = multiply(this,right);
        x = result.x;
        y = result.y;
        z = result.z;
        return this;
    }
    public void draw(GL2 gl) {
        gl.glVertex3d(x,y,z);
    }
    
    public Coord transform(double[][] m) {
        double[] p = new double[4];
        
        p[0] = x;
        p[1] = y;
        p[2] = z;
        p[3] = 1;
        
        double[] q = MathUtil.multiply(m, p);
        return new Coord(q[0],q[1],q[2]);        
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append(")");
        
        return sb.toString();
    }
    public static Coord multiply(Coord left, double right){
        return new Coord(left.x*right,left.y*right,left.z*right);
    }

    public static Coord multiply(Coord left, Coord right){
        return new Coord(left.x*right.x,left.y*right.y,left.z*right.z);
    }

    public static Coord add(Coord left,Coord right){
        return new Coord(left.x+right.x,left.y+right.y,left.z+right.z);
    }

    public Coord add(Coord right){
        return add(this,right);
    }
}
