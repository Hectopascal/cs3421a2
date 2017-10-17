
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

/**
 * The camera is a GameObject that can be moved, rotated and scaled like any other.
 * 
 * TODO: You need to implment the setView() method.
 *       The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class Camera extends GameObject implements GLEventListener, KeyListener {

    private float[] myBackground;
    public double farPlane;

    public Camera(GameObject parent) {
        super(parent);
        farPlane = 1000.0;
        myBackground = new float[4];
    }

    public Camera() {
        this(GameObject.ROOT);
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
        Coord Translated = getPosition();
        Coord Rotate = getRotation();
        Coord Scale = getScale();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        // apply the view transform
        gl.glScaled(1.0 /Scale.x,1.0/Scale.y,1/Scale.z);
        gl.glRotated(-Rotate.x, 1.0, 0.0, 0.0);
        gl.glRotated(-Rotate.y, 0.0, 1.0, 0.0);
        gl.glRotated(-Rotate.z, 0.0, 0.0, 1.0);
        gl.glTranslated(-Translated.x, -Translated.y, -Translated.z);

    }

    public void reshape(GL2 gl, int x, int y, int width, int height) {
        
        // match the projection aspect ratio to the viewport
        // to avoid stretching
        
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
        myGLU.gluOrtho2D(left, right, bottom, top);                
    }

    
    
    
    /*------------------------------------*
     * KEY LISTENER FUNCTIONS			  *
     * -----------------------------------*/
     
    
	@Override
	public void keyPressed(KeyEvent e) {
		
	    switch (e.getKeyCode()) {
		  
		    case KeyEvent.VK_UP:
		    	//move forwards
				break;
		    case KeyEvent.VK_DOWN:
			    //move backwards
			    break;	
		    case KeyEvent.VK_LEFT:
		    	//rotate camera left
		    	break;
		    	
		    case KeyEvent.VK_RIGHT:
		    	//rotate camera right
		    	break;
		    default:
			    break;
		 }
		 System.out.println(e.getKeyCode());
		
	}

	@Override
	public void display(GLAutoDrawable arg0) {
    	GL2 gl = arg0.getGL().getGL2();

    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        //DO STUFF TO CHANGE CAMERAS?????????????
        
	}

	

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2 gl2 = arg0.getGL().getGL2();
		setView(gl2);
    	gl2.glEnable(GL2.GL_DEPTH_TEST);
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		GL2 gl = arg0.getGL().getGL2();
	    
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

    	gl.glFrustum(-2,2,-2,2,1,8);
     
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