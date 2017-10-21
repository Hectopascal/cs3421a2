public class ColorObject {
    public float x;
    public float y;
    public float z;
    public float o;

    public ColorObject(){
        this(0.0f,0.0f,0.0f,0.0f);
    }

    public ColorObject(float x,float y,float z, float o){
        this.x = x;
        this.y = y;
        this.z = z;
        this.o = o;
    }
}