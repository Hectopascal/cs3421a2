import java.io.File;
import java.io.FileNotFoundException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import java.util.Timer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;




/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private Terrain myTerrain;
    private Avatar myAvatar;
	private Camera myCamera;

    private float globAmb[] = {0.1f, 0.1f, 0.1f, 1.0f};
	private int p = 1; // Positional light 1, directional 0

    private boolean forwardPressed = false;
    private boolean backwardsPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean pPressed = false;
    private boolean oPressed = false;

    private vboObject o = null;
    private int camStage;
    private float[] lightPos = new float[3];
    private boolean firstPersonMode = false;
    private boolean nightMode = false;
    
	private int angleC;


	private Timer timer;
	private TimerTask tt;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
		angleC = -1;
		camStage = 0;
		myCamera = new Camera();
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();

          panel.addGLEventListener(this);
          panel.addKeyListener(this);
          
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
          
          myAvatar = new Avatar(0,myTerrain.altitude(0, 0),0);
          myAvatar.setPortals(myTerrain.getEntrancePortal(), myTerrain.getExitPortal());
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
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    	
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
        GLU glu = new GLU();

        if(this.firstPersonMode) {
	 		gl.glTranslated(0, -1.3, 0);
		 	gl.glRotated(-myAvatar.getRotation(), 0, 1, 0);
	 		glu.gluLookAt(myAvatar.getPosition()[0], myAvatar.getPosition()[1], myAvatar.getPosition()[2], myAvatar.getPosition()[0], 
					myAvatar.getPosition()[1], myAvatar.getPosition()[2]+1, 0.0,1, 0);
        }
        else {
	 		gl.glTranslated(0, -1.25, -1);
		 	gl.glRotated(-myAvatar.getRotation(), 0, 1, 0);
	 		glu.gluLookAt(myAvatar.getPosition()[0], myAvatar.getPosition()[1], myAvatar.getPosition()[2], myAvatar.getPosition()[0], 
					myAvatar.getPosition()[1], myAvatar.getPosition()[2]+1, 0.0,1,0);
        }
    	setLighting(gl);
		changeLight(gl);
        o.draw(gl,lightPos);
    	myTerrain.draw(gl);   	
        myAvatar.draw(gl);

    	gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
    	
    	if(forwardPressed) {
    		goForwards();
    	}
    	if (backwardsPressed) {
    		goBackwards();
    	}
    	if(leftPressed) {
    		this.myAvatar.rotateLeft();
    	}
    	if(rightPressed) {
    		this.myAvatar.rotateRight();
    	}
    	if(wPressed) {
    		goForwards();
    	}
    	if(aPressed) {
    		goLeft();
    	}
    	if(sPressed) {
    		goBackwards();
    	}
    	if(dPressed) {
    		goRight();
    	}
    	if(pPressed) {
    		this.myAvatar.rotateIndepRight();
    	}
    	if(oPressed) {
    		this.myAvatar.rotateIndepLeft();
    	}
	}

	
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	//Probably incomplete
	public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);

    	gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT,GL2.GL_NICEST);

    	gl.glEnable(GL2.GL_LIGHTING);
    	gl.glEnable(GL2.GL_LIGHT0);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV,GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
    	gl.glEnable(GL2.GL_NORMALIZE);
    	gl.glEnable(GL2.GL_TEXTURE_2D); 

    	this.myTerrain.init(gl);
    	this.myAvatar.init(gl);
    	o = new vboObject(gl);

    	timer();

	}


	public void setLighting(GL2 gl){
		float lightAmb[];
		float lightDifAndSpec[];
		if(this.nightMode) {
			lightAmb = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
			lightDifAndSpec = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
		}
		else {
	    	lightAmb = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
	    	lightDifAndSpec = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
		}
    	float lightPos[] = myTerrain.getSunlight(); 	
    	
    	
    	gl.glEnable(GL2.GL_LIGHT0); 
    	globAmb[0] = 0.1f;
    	globAmb[1] = 0.1f;
    	globAmb[2] = 0.1f;
    		
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);

    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0);
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); 
    	
    	gl.glLightf(GL2.GL_LIGHT0,GL2.GL_CONSTANT_ATTENUATION, 2);
    	gl.glLightf(GL2.GL_LIGHT0,GL2.GL_LINEAR_ATTENUATION, 1);
    	gl.glLightf(GL2.GL_LIGHT0,GL2.GL_QUADRATIC_ATTENUATION, 0.5f);

    	if(this.nightMode) {
    		lightAmb = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
	    	lightDifAndSpec = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    		gl.glEnable(GL2.GL_LIGHT1);
    		GLUT glut = new GLUT();
    		//Position the spotlight
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDifAndSpec,0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightDifAndSpec,0);
    		gl.glPushMatrix();
    			double[] pos = myAvatar.getPosition();
    			float[] floatPos = {(float)(pos[0]),(float)pos[1],(float)(pos[2]),1.0f};
    			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, floatPos, 0);
    		gl.glPopMatrix();
    		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 30);
    		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 4);
    		float[] direction = new float[] {0.0f, 0.5f, 0.0f};
    		double rotation = this.myAvatar.getRotation();
    		direction[0] = (float)Math.sin(Math.toRadians(rotation));
    		direction[2] = (float)Math.cos(Math.toRadians(rotation));
    		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, direction, 0);
    		gl.glPopMatrix();
    	}
    	
    	gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
	}
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				this.forwardPressed = true;
				break;
			case KeyEvent.VK_DOWN:
				this.backwardsPressed = true;
				break;
			case KeyEvent.VK_LEFT:
				this.leftPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				this.rightPressed = true;
				break;
			case KeyEvent.VK_W:
				this.wPressed = true;
				break;
			case KeyEvent.VK_A:
				this.aPressed = true;
				break;
			case KeyEvent.VK_S:
				this.sPressed = true;
				break;
			case KeyEvent.VK_D:
				this.dPressed = true;
				break;
			case KeyEvent.VK_V:
				this.firstPersonMode = !this.firstPersonMode;
				break;
			case KeyEvent.VK_N:
				this.nightMode = !this.nightMode;
				break;
			case KeyEvent.VK_P:
				this.pPressed = true;
				break;
			case KeyEvent.VK_O:
				this.oPressed = true;
				break;
			default:
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				this.forwardPressed = false;
				break;
			case KeyEvent.VK_DOWN:
				this.backwardsPressed = false;
				break;
			case KeyEvent.VK_LEFT:
				this.leftPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				this.rightPressed = false;
				break;
			case KeyEvent.VK_W:
				this.wPressed = false;
				break;
			case KeyEvent.VK_A:
				this.aPressed = false;
				break;
			case KeyEvent.VK_S:
				this.sPressed = false;
				break;
			case KeyEvent.VK_D:
				this.dPressed = false;
				break;
			case KeyEvent.VK_P:
				this.pPressed = false;
				break;
			case KeyEvent.VK_O:
				this.oPressed = false;
				break;
			default:
				break;
		}

	
	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // Perspective camera       
        GLU glu = new GLU();
	
	    if (this.firstPersonMode) {
	       	glu.gluPerspective(30, (width/height), 0, 10);
	    } else {
	     	glu.gluPerspective(90, (width/height), 0.1, 30);
	    }
		
        gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void goForwards() {
		myAvatar.goForwards(myTerrain.size());
		myAvatar.setAltitude(myTerrain.altitude(myAvatar.getPosition()[0], myAvatar.getPosition()[2]));
	}
	public void goBackwards() {
		myAvatar.goBackwards(myTerrain.size());
		myAvatar.setAltitude(myTerrain.altitude(myAvatar.getPosition()[0], myAvatar.getPosition()[2]));
	}
	public void goRight() {
		myAvatar.goRight(myTerrain.size());
		myAvatar.setAltitude(myTerrain.altitude(myAvatar.getPosition()[0], myAvatar.getPosition()[2]));
	}
	public void goLeft() {
		myAvatar.goLeft(myTerrain.size());
		myAvatar.setAltitude(myTerrain.altitude(myAvatar.getPosition()[0], myAvatar.getPosition()[2]));
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void changeLight(GL2 gl) {

		// Change Light Position
		float ambVal;
		double incAngle = (2 * Math.PI / 360);
		double radius = Math.hypot((myTerrain.size().getWidth()/2-myTerrain.getSunlight()[0]),
				(0-myTerrain.getSunlight()[1]));
		double initAngle = Math.atan(myTerrain.getSunlight()[0]/myTerrain.getSunlight()[0]);

		// Calculate new light position
		lightPos[0] = (float) (radius * Math.sin(incAngle*angleC + initAngle));
		lightPos[1] = (float) (radius * Math.cos(incAngle*angleC + initAngle));
		lightPos[2] = myTerrain.getSunlight()[2];

		// Set Ambient Light
		if (lightPos[1] >= 0) {
			ambVal = (float) (lightPos[1]/radius + 0.1f);
			float lightAmb[] = { ambVal, ambVal, ambVal, 1.0f };
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
		} else {
			float lightAmb[] = {0.3f, 0.3f, 0.3f, 1.0f};
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lightAmb,0); // Global ambient light.
		}


		float lightDif0[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float lightSpec0[] = { 1.0f, 1.0f, 1.0f, 1.0f };

		// Light0 properties.
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);



		// Material
		float matAmbAndDif[] = {0.5f, 0.5f, 0.5f, 0.5f};
		float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float matShine[] = { 50.0f };
		float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};

		// Material properties of teapot
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emm,0);
	}

	public void timer(){
		timer = new Timer();
		tt = new TimerTask() {
			public void run(){
				if (angleC - 1 == 359){
						angleC = -1;
				}
				angleC ++;
			}
		};
		timer.schedule(tt,40,40);
	}
}
