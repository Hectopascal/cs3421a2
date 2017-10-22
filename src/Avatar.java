
import java.awt.Dimension;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar{
	private double[] myPosition;
	
	private float matAmbAndDif1[] = {1f, 1f, 1f, 1.0f};
	private float matAmbAndDif2[] = {0.7f, 1.0f, 0.8f, 1.0f};
	private float matSpec[] = { 0.8f, 0.8f, 0.8f, 1.0f };
	private float matSpec2[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float matShine[] = { 25.0f };
	private float matShine2[] = { 100.0f };
	
	private double myRotation;
	private double myTilt;
	private int myDirection;
	
	private Coord portalEntrance;
	private Coord portalExit;
	private String textureFileName1 = "src/textures/water.bmp";
	private String bmpExt = "bmp";
	
	private MyTexture texture;
	
	public Avatar(double x, double y, double z) {
		this.myPosition = new double[3];
		this.myPosition[0] = x;
		this.myPosition[1] = y;
		this.myPosition[2] = z;
		
		this.myRotation = 0;
		this.myDirection = 0;
	}
	public double getRotation() {
		return this.myRotation;
	}
	
	public void setPortals(Coord in, Coord out) {
		portalEntrance = in;
		portalExit = out;
	}
	public void setAltitude(double height) {
		this.myPosition[1] = height;
	}
	
	public void rotateLeft() {
		myRotation = myRotation+1;
	}
	public void rotateRight() {
		myRotation = myRotation-1;
	}
	public void lookUp() {
		if(myTilt-1< 45) myTilt +=1;
	}
	public void lookDown() {
		if(myTilt-1> -45) myTilt -=1;
	}
	public double[] getPosition() {
		return this.myPosition;
	}
    public void init(GL2 gl){
    	this.texture = new MyTexture(gl,textureFileName1,bmpExt,true);
    }
	public void draw(GL2 gl) {

		gl.glPushMatrix();
			/*System.out.println("X" + this.myPosition[0]);
			System.out.println("Y" + this.myPosition[1]);
			System.out.println("Z" + this.myPosition[2]);*/
			//Move to position
			gl.glTranslated(this.myPosition[0], this.myPosition[1]+0.1, this.myPosition[2]);
			gl.glRotated(getRotation(), 0, 1, 0);
			GLUT glut = new GLUT();
			gl.glPushMatrix();
				gl.glTranslated(0, 0.3, 0);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId()); 
				gl.glRotated(-90, 0, 1, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
				glut.glutSolidTeapot(0.2);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	public void goForwards(Dimension size) {
		final double movement = 0.05;
		double xMove=myPosition[0];
		double zMove=myPosition[2];
		xMove += Math.sin(Math.toRadians(myRotation%360)) * movement;
		zMove += Math.cos(Math.toRadians(myRotation%360)) * movement;
			
		if( xMove >= 0 && xMove <= size.width - 1
		&& zMove >= 0 && zMove <= size.height - 1) {
				myPosition[0] = xMove;
				myPosition[2] = zMove;
		}

		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	
	private boolean inPortalRange() {
		double range = 0.005;
		return (myPosition[0] > portalEntrance.x -range && myPosition[0] < portalEntrance.x +range
				&& myPosition[2] > portalEntrance.z -range &&myPosition[2] > portalEntrance.z +range);				
	}
	public void goBackwards(Dimension size) {
		
		final double movement = -0.05;
		//double angleFromQuadrant = this.myRotation % 90;
		double xMove=myPosition[0];
		double zMove=myPosition[2];
		System.out.println("lol");
		xMove += Math.sin(Math.toRadians(myRotation%360)) * movement;
		zMove += Math.cos(Math.toRadians(myRotation%360)) * movement;
		
		if( xMove >= 0 && xMove <= size.width - 1){
			if(zMove >= 0 && zMove <= size.height - 1) {

				myPosition[0] = xMove;
				myPosition[2] = zMove;
				
			}
		}

		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	public void goLeft(Dimension size) {
		final double movement = 0.05;
		double xMove=myPosition[0];
		double zMove=myPosition[2];

		xMove += Math.sin(Math.toRadians((myRotation+90)%360)) * movement;
		zMove += Math.cos(Math.toRadians((myRotation+90)%360)) * movement;
			
		if( xMove >= 0 && xMove <= size.width - 1
		&& zMove >= 0 && zMove <= size.height - 1) {
				myPosition[0] = xMove;
				myPosition[2] = zMove;
		}

		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	public void goRight(Dimension size) {
		final double movement = 0.05;
		double xMove=myPosition[0];
		double zMove=myPosition[2];
		xMove -= Math.sin(Math.toRadians((myRotation+90)%360)) * movement;
		zMove -= Math.cos(Math.toRadians((myRotation+90)%360)) * movement;
			
		if( xMove >= 0 && xMove <= size.width - 1
		&& zMove >= 0 && zMove <= size.height - 1) {
				myPosition[0] = xMove;
				myPosition[2] = zMove;
		}
		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	public double getTilt() {
		return myTilt;
	}
}
