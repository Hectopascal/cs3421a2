import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject {
    private Color material;
    private Coord myPos;
    
    public Tree(double x, double y, double z, GameObject parent) {
    	super(parent);
        myPos = new Coord(x,y,z);
        this.GlobalPosition = new Coord(x,y,z);
        material = new Color();
        material.diffuse = new ColorObject(0.9f,0.6f,0.2f,0.0f);
    }
    public Tree(Coord coords, GameObject parent) {
    	super(parent);
    	this.GlobalPosition = coords;
    }

    public Coord getPosition() {
        return myPos;
    }
    
    public void draw(GL2 gl) {
        gl.glColor3d(0.0, 0.0, 0.0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3d(0.0, 0.0, -1.0);
        gl.glVertex3d(-0.1, 0.0, 0.0);
        gl.glVertex3d(0.1, 0.0, 0.0);
        gl.glVertex3d(0.0, 1.0, 0.0);
        gl.glNormal3d(1.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, -0.1);
        gl.glVertex3d(0.0, 0.0, 0.1);
        gl.glVertex3d(0.0, 1.0, 0.0);
        gl.glEnd();

    }

}
