import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject {
    private static final int SLICES = 32;
    private static final double CYLINDER_RADIUS = 0.1;
    private static final double HEIGHT = 1;
    private Coord myPos;
    public Tree(double x, double y, double z, GameObject parent) {
    	super(parent);
        myPos = new Coord(x,y,z);
    }
    public Tree(Coord coords, GameObject parent) {
    	super(parent);
    	myPos = coords;
    }
    public Coord getPosition() {
        return myPos;
    }
    
    public void draw(GL2 gl) {
    	GLUT glut = new GLUT();
    	//Trunk
    	gl.glPushMatrix();
    		gl.glRotated(90, -90, 0, 0);
    		glut.glutSolidCylinder(0.1, 1.5, 40, 40);
    	gl.glPopMatrix();
    	//Leaves
    	gl.glPushMatrix();
    		gl.glTranslated(0, 1.5, 0);
    		glut.glutSolidSphere(0.55, 40, 40);
    	gl.glPopMatrix();
    }
}
