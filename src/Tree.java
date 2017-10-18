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
    private static final int SLICES = 32;
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
        GLU glu = new GLU();
        gl.glPushMatrix();
        gl.glTranslated(myPos.x,myPos.y,myPos.z);
        
        double angleIncrement = (Math.PI * 2.0) / SLICES;
        double zFront = -1;
        double zBack = -3;
        
        gl.glBegin(GL2.GL_POLYGON);{
            
        for(int i = 0; i < SLICES; i++)
        {
            double angle0 = i*angleIncrement;
        
            gl.glNormal3d(0.0, 0.0, 1);
            gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
            gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
        }
        }gl.glEnd();
        
        gl.glBegin(GL2.GL_QUAD_STRIP);{      
	        for(int i=0; i<= SLICES; i++){
	        	double angle0 = i*angleIncrement;
	        	double angle1 = (i+1)*angleIncrement;
	        	double xPos0 = Math.cos(angle0);
	        	double yPos0 = Math.sin(angle0);
	        	double sCoord = 2.0/SLICES * i; //Or * 2 to repeat label
	        	

	        	gl.glNormal3d(xPos0, yPos0, 0);
	        	gl.glTexCoord2d(sCoord,1);
	        	gl.glVertex3d(xPos0,yPos0,zFront);
	        	gl.glTexCoord2d(sCoord,0);
	        	gl.glVertex3d(xPos0,yPos0,zBack);	        	
	        	
	        }
        }gl.glEnd();    
        
        //Draw the bottom of the cylinder also with the canTop.bmp texture :)
        //just for demonstration.
        gl.glBegin(GL2.GL_POLYGON);{
           
            for(int i = 0; i < SLICES; i++)
            {
            	double angle0 = -i*angleIncrement;
            	
         	    gl.glNormal3d(0.0, 0.0, -1);
         	 
         	    gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
         	    gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zBack);
            }
            }gl.glEnd();
    }
}
