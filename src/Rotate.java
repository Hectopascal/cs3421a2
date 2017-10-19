
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * 
 *  Simple demo to illustrate 3D rotations.
 *  Change the angle of rotation by pressing
 *  up and down arrows.
 *  
 *  Modify the code to rotate around different axes
 *  gl.glRotated ( angle, 1,0, 0);
 * 
 */

public class Rotate implements GLEventListener, KeyListener {	
	private static int angle = 0;
		
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Rotations");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Rotate p = new Rotate();
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(p);
        // NEW: add a key listener to respond to keypresses
        panel.addKeyListener(p);
        // the panel needs to be focusable to get key events
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }
    
    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
  
		 case KeyEvent.VK_UP:		       
				  angle = (angle + 10) % 360;
				  break;
		 case KeyEvent.VK_DOWN:
			     
				  angle = (angle - 10) % 360;
				  break;
		 default:
			 break;
		 }
		 System.out.println(angle);
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        //Aim camera
        gl.glTranslated(0, 0, -3);
        //GLU glu = new GLU();
        //glu.gluLookAt(2,4,3,0,0,0,0,1,0);
        //gl.glRotated(-45, 0, 0, 1);
        //gl.glRotated(-10, 0, 1, 0);
        //gl.glRotated(15, 1, 0, 0);
        //gl.glTranslated(0, -1,-3);
        
    	//rotate around x axis
    	gl.glRotated ( angle, 1,0, 0);

    	//gl.glRotated ( angle, 0,1, 0);  //Y axis
    	//gl.glRotated ( angle, 0,0, 1);  //Z axis
    	//gl.glRotated ( angle, 1,1, 1);  //Axis  (1,1,1)

    	GLUT glut = new GLUT();
    	glut.glutSolidTeapot(1);
    	 
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	  
    	// enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        //Turn on default light
        gl.glEnable(GL2.GL_LIGHT0);
        
        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();    
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glOrtho(-3,3,-3,3,1,10);       
    }
    


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}    
}