
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * The camera is a GameObject that can be moved, rotated and scaled like any other.
 * 
 * TODO: You need to implment the setView() method.
 *       The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class Camera implements GLEventListener, KeyListener {

    private float[] myBackground;
    public double farPlane;
    public Coord coords;
    public int angle = 0;
    
    Coord myTranslation;
    Coord myRotation;
    Coord myScale;
    
    public Camera() {
        coords = new Coord(0,0,0);
        this.myTranslation = new Coord(0.0,0.0,0.0);
        this.myRotation = new Coord(0.0,0.0,0.0);
        this.myScale = new Coord(1.0,1.0,1.0);
        farPlane = 1000.0;
        myBackground = new float[4];
        myBackground[0] = 0;
        myBackground[1] = 0.6f;
        myBackground[2] = 0.9f;
        myBackground[3] = 1;
    }
    
    public float[] getBackground() {
        return myBackground;
    }

    public void setBackground(float[] background) {
        myBackground = background;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
   
    
    public void setView(GL2 gl) {
        gl.glClearColor(myBackground[0],myBackground[1],myBackground[2],myBackground[3]);
        //  1. clear the view to the background colour
        gl.glClearStencil(0x00);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_STENCIL_TEST);
        gl.glColorMask(true,true,true,true);
        gl.glDepthMask(true);
        gl.glClearDepth(farPlane);
        gl.glStencilMask(0x00);
        gl.glStencilFunc(GL2.GL_EQUAL, 0x00, 0x00);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
        gl.glClear(GL2.GL_STENCIL_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        //  2. set the view matrix to account for the camera's position         
        gl.glLoadIdentity();
        //int size = (int)this.getScale();
        Coord Translated = this.myTranslation;
        Coord Rotate = this.myRotation;
        Coord Scale = this.myScale;
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        // apply the view transform
        gl.glScaled(1.0 /Scale.x,1.0/Scale.y,1/Scale.z);
        gl.glRotated(-Rotate.x, 1.0, 0.0, 0.0);
        gl.glRotated(-Rotate.y, 0.0, 1.0, 0.0);
        gl.glRotated(-Rotate.z, 0.0, 0.0, 1.0);
        gl.glTranslated(-Translated.x, -Translated.y, -Translated.z);

    }
    
    @Override
    public void reshape(GLAutoDrawable arg0 , int x, int y, int width, int height) {
        // match the projection aspect ratio to the viewport
        // to avoid stretching
        GL2 gl = arg0.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double top, bottom, left, right;
        
        if (width > height) {
            double aspect = (1.0 * width) / height;
            top = 1.0;
            bottom = -1.0;
            left = -aspect;
            right = aspect;            
        }
        else {
            double aspect = (1.0 * height) / width;
            top = aspect;
            bottom = -aspect;
            left = -1;
            right = 1;                        
        }        
        GLU myGLU = new GLU();
        // coordinate system (left, right, bottom, top)

        //gl.glOrtho(-3,3,-3,3,1,10);  
        gl.glFrustum(-1,1,-1,1,-1,5);
        //gl.glOrtho(-1,1,-1,1,-1,5);  
        //myGLU.gluOrtho2D(left, right, bottom, top);    

    }

    
    
    
    /*------------------------------------*
     * KEY LISTENER FUNCTIONS			  *
     * -----------------------------------*/
     
    
	@Override
	public void keyPressed(KeyEvent e) {
		//uphill rotates on x (pitch), rotate left/right is heading (y)
		
	    switch (e.getKeyCode()) {
		    case KeyEvent.VK_UP:
		    	coords.z+=1;
				break;
		    case KeyEvent.VK_DOWN:
			    coords.z-=1;
			    break;	
		    case KeyEvent.VK_LEFT:
		    	angle = (angle + 10) % 360;
		    	break;
		    case KeyEvent.VK_RIGHT:
		    	angle = (angle - 10) % 360;
		    	break;
		    default:
			    break;
		 }
		 System.out.println(angle);
		
	}

	@Override
	public void display(GLAutoDrawable arg0) {
    	GL2 gl = arg0.getGL().getGL2();

    	//gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLU glu = new GLU();
        //glu.gluLookAt(2,4,3,0,0,0,0,1,0);
        //gl.glRotated(-45, 0, 0, 1);
        //gl.glRotated(-10, 0, 1, 0);
        //gl.glRotated(15, 1, 0, 0);
        //gl.glTranslated(0, -1,-3);
        
        //Aim camera
        this.myTranslation = new Coord(coords.x,coords.y,coords.z);

        this.myRotation = new Coord(0,angle,0);
        //gl.glTranslated(coords.x,coords.y,coords.z);
    	gl.glRotated (angle, 0,1, 0);  //Y axis
    	
    	GLUT glut = new GLUT();
    	glut.glutSolidTeapot(1);
    	
	}

    public void setPosition(Coord t) {
    	this.myTranslation = t;
    }
    public void setRotation(Coord r) {
    	this.myRotation = r;
    }
    public void setScale(Coord s) {
    	this.myScale = s;
    }

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		
		setView(gl);
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	  
    	// enable lighting
        //gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable(GL2.GL_LIGHT0);
        //gl.glEnable(GL2.GL_NORMALIZE);
	}
	
	//-------------------- Functions that can be left empty (?)
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
