
import com.jogamp.opengl.GL2;


import java.util.ArrayList;
public class GameObject {
    public ArrayList<GameObject> all_object = new ArrayList<GameObject>();

    public static final GameObject ROOT = new GameObject(null);
    protected GameObject parent;
    private ArrayList<GameObject> children = new ArrayList<GameObject>();
    protected boolean enabled;

    public Coord GlobalPosition;
    public Coord GlobalRotation;
    public Coord GlobalScale;
    // constructor
    public GameObject (GameObject parent){
        this.parent = parent;
        if (parent != null){
            this.parent.children.add(this);
        }
        this.children = new ArrayList<GameObject>();
        all_object.add(this);
        this.GlobalPosition = new Coord(0.0,0.0,0.0);
        this.GlobalRotation = new Coord(0.0,0.0,0.0);
        this.GlobalScale = new Coord(1.0,1.0,1.0);
        this.enabled = true;
    }

    public void draw(GL2 gl){
        gl.glPushMatrix();
        gl.glTranslated(GlobalPosition.x,GlobalPosition.y,GlobalPosition.z);
        gl.glRotated(GlobalRotation.x,1.0,0.0,0.0);
        gl.glRotated(GlobalRotation.y,0.0,1.0,0.0);
        gl.glRotated(GlobalRotation.z,0.0,0.0,1.0);
        gl.glScaled(GlobalScale.x,GlobalScale.y,GlobalScale.z);
        ArrayList<GameObject> cList = new ArrayList<GameObject>(children);
        for (GameObject o: cList){
            o.draw(gl);
        }
        gl.glPopMatrix();
    }

    // some helper?
    public Coord getRotation(){
        // need type and implementation
        return null;
    }

    public Coord getPosition(){
        // a vector was returned
        Coord parentPosition;
        Coord parentRotation;
        Coord parentScale;
        if (this == GameObject.ROOT){
            parentPosition = new Coord();
            parentRotation = new Coord();
            parentScale = new Coord(1.0,1.0,1.0);
        }else{
            parentPosition = parent.getPosition();
            parentRotation = parent.getRotation();
            parentScale = parent.getScale();
        }
        Coord copy = GlobalPosition;
        double [][] localMatrix = MathUtil.translationMatrix4(copy);
        double [][] parentRotateMatrix = MathUtil.rotationMatrixXYZ(parentRotation);
        double [][] RTmatrix = MathUtil.multiply4(parentRotateMatrix,localMatrix);

        Coord intermidateV = MathUtil.translationVector(RTmatrix);
        // intermidateV need a power
        intermidateV.multiplySelfAndScale(parentScale);

        Coord result = intermidateV.add(parentPosition);
        return result;
    }

    public Coord getScale(){
        // need type and implementation
        return null;
    }
}
