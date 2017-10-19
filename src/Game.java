import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;




/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    private Camera myCamera;
    
    private float globAmb[] = {0.1f, 0.1f, 0.1f, 1.0f};
	private float xAngle = 0.0f, yAngle = 0.0f; // Rotation angles of white light.
	private int p = 1; // Positional light 1, directional 0
	
    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          
          myCamera = new Camera(GameObject.ROOT);

          myCamera.setPosition(new Coord(1,2,1));
          myCamera.setRotation(new Coord(10,20,10));
          myCamera.setScale(new Coord(2,2,2));
          panel.addGLEventListener(myCamera);
          panel.addKeyListener(myCamera);
          panel.setFocusable(true);   
          
          panel.addGLEventListener(this);
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        //setLighting(gl);
        // set the view matrix based on the camera position
        myCamera.setView(gl); 
        GLU glu = new GLU();
        //First 3 arguments are location of the view
        //Second 3 arguments are what point to look at
        //Last 3 arguments are the UP Vector (which way is up I guess?)
        //glu.gluLookAt(0.0, myTerrain.getGridAltitude(0, 0), 0.0, 1.5,myTerrain.getGridAltitude(0, 0), 1.0, 0.0, 1.0, 0.0);
        // draw the scene tree
        GameObject.ROOT.draw(gl);  
        
    	//this.myTerrain.drawGame(gl);
    	gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	//Probably incomplete
	public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
    	gl.glEnable(GL2.GL_TEXTURE_2D); 
    	
    	myTerrain.initAll(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	public void setLighting(GL2 gl) {
		
		gl.glEnable(GL2.GL_LIGHT0);
        // Light property vectors.
    	float lightAmb[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };

		globAmb[0] = 0.1f;
		globAmb[1] = 0.1f;
		globAmb[2] = 0.1f;
		//Light0 properties
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, (new float[] {1,2,10}),0);
    	//Global light properties
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.
    	
    	drawAndPositionLights(gl);
	}
	private void drawAndPositionLights(GL2 gl){
    	GLUT glut = new GLUT();
    	//p will be 1 for positional and 0 for directional
    	float lightPos0[] = { 1.0f, 2.0f, 0.0f, p };
    	//float lightPos1[] = { 1.0f, 2.0f, 0.0f, 1.0f };
        
    	// Light0 positioned 
    	// sphere (positional light)
        // or arrow (directional light).
        gl.glPushMatrix();{
        	//Transformations to move lights
        	gl.glRotated(xAngle, 1.0, 0.0, 0.0); // Rotation about x-axis.
        	gl.glRotated(yAngle, 0.0, 1.0, 0.0); // Rotation about z-axis.        	
            //The light pos will be subject to current transformation
        	//matrix, so will be rotated
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);
        	
        	//Also translate to draw to representation of the light
        	//Usually you would not do this if you did not
        	//want to actually draw the light.
        	gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
        	
        	// Properties of the white sphere/line
        	// representing the light
        	float emmL[] = {1.0f, 1.0f, 1.0f, 1.0f};
        	float matAmbAndDifL[] = {0.0f, 0.0f, 0.0f, 1.0f};
        	float matSpecL[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        	float matShineL[] = { 50.0f };

        	// Material properties of sphere/line.
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifL,0);
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecL,0);
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShineL,0);
        	
        	//Since the actual sphere/line representing the light will not necessarily be hit
        	//by any light and we want to, see it, we give it an emissive property.
        	//The other alternative would be to temporarily turn off lighting.
        	//We do this for the other light to give an example of both ways
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
        	if (p == 1) {
        		glut.glutSolidSphere(0.05, 8, 8); // Sphere at positional light source.
        	}
        	else //line representing directional light
        	{
        		gl.glLineWidth(3.0f);
        		gl.glBegin(GL2.GL_LINES);
        		gl.glVertex3d(0.0, 0.0, 0.25);
        		gl.glVertex3d(0.0, 0.0, -0.25);
        		gl.glEnd();
        		gl.glLineWidth(1.0f);
        	}
        }gl.glPopMatrix();
        gl.glEnable(GL2.GL_LIGHTING);
	}
}
