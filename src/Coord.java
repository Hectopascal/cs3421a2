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


}
