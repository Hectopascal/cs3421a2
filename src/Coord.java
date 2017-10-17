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
