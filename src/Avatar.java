
import java.awt.Dimension;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar{
	private double[] myPosition;
	
	private float matAmbAndDif[] = {1f, 1f, 1f, 1.0f};
	private float matSpec[] = { 0.8f, 0.8f, 0.8f, 1.0f };
	private float matSpec2[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float matShine[] = { 25.0f };
	private float matShine2[] = { 100.0f };
	
	private double myRotation;
	private double myTilt;
	private int myDirection;
	
	private double independentRotation;
	
	private Coord portalEntrance;
	private Coord portalExit;
	private String textureFileName1 = "src/textures/orange.bmp";
	private String textureFileName2 = "src/textures/skin.bmp";
	private String textureFileName3 = "src/textures/black.bmp";
	private String bmpExt = "bmp";
	
	private MyTexture[] texture;
	
	public Avatar(double x, double y, double z) {
		this.myPosition = new double[3];
		this.myPosition[0] = x;
		this.myPosition[1] = y;
		this.myPosition[2] = z;
		
		this.myRotation = 0;
		this.myDirection = 0;
		
		this.independentRotation = 0;
	}
	public double getRotation() {
		return this.myRotation;
	}
	public void rotateIndepLeft() {
		independentRotation = (independentRotation+1) % 360;
	}
	public void rotateIndepRight() {
		independentRotation = independentRotation-1;
		if(independentRotation < 0) {
			independentRotation += 360;
		}
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
    	this.texture = new MyTexture[3];
    	this.texture[0] = new MyTexture(gl,textureFileName1,bmpExt,true);
    	this.texture[1] = new MyTexture(gl,textureFileName2,bmpExt,true);
    	this.texture[2] = new MyTexture(gl,textureFileName3,bmpExt,true);
    }
	public void draw(GL2 gl) {
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
		gl.glPushMatrix();
			//Move to position
			gl.glTranslated(this.myPosition[0], this.myPosition[1]+0.1, this.myPosition[2]);
			gl.glRotated(getRotation(), 0, 1, 0);
			gl.glRotated(this.independentRotation, 0, 1, 0);
			GLUT glut = new GLUT();
			gl.glPushMatrix();
				gl.glTranslated(0, 0.3, 0);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0].getTextureId()); 
				gl.glRotated(-90, 0, 1, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
				//glut.glutSolidTeapot(0.2);
		    	gl.glRotated(90, -90, 0, 0);
		    	glut.glutSolidCone(0.1, 0.3, 32, 16);
		    	gl.glTranslated(0, 0, 0.3);
		    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[1].getTextureId()); 
		    	glut.glutSolidSphere(0.1, 32, 32);
		    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0].getTextureId()); 
		    	gl.glPushMatrix();
		    	gl.glTranslated(0, 0, 0.1);
		    	gl.glRotated(30, 0, 30, 0);
		    	gl.glTranslated(0.04, 0, 0);
		    	glut.glutSolidCone(0.05, 0.25, 32, 16);
		    	gl.glPopMatrix();
		    	gl.glPushMatrix();
		    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[2].getTextureId()); 
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec2,0);
		    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine2,0);
		    	gl.glTranslated(-0.06, 0.04, 0.02);
		    	gl.glScaled(1, 0.75, 3);
		    	glut.glutSolidSphere(0.02, 32, 16);
		    	gl.glPopMatrix();
		    	gl.glTranslated(-0.06, -0.04, 0.02);
		    	gl.glScaled(1, 0.75, 3);
		    	glut.glutSolidSphere(0.02, 32, 16);
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

		System.out.println("x is "+ myPosition[0]+" z is "+ myPosition[2] + " angle is "+myRotation);
		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	
	private boolean inPortalRange() {
		double range = 0.05;
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

		System.out.println("x is "+ myPosition[0]+" z is "+ myPosition[2] + " angle is "+myRotation);
		
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

		System.out.println("x is "+ myPosition[0]+" z is "+ myPosition[2] + " angle is "+myRotation);
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

		System.out.println("x is "+ myPosition[0]+" z is "+ myPosition[2] + " angle is "+myRotation);
		if(inPortalRange()) {
			myPosition[0]=portalExit.x;
			myPosition[2]=portalExit.z;
		}
	}
	public double getTilt() {
		return myTilt;
	}

}
