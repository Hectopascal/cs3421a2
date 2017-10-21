
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
	private int myDirection;
	
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
	public void setAltitude(double height) {
		this.myPosition[1] = height;
	}
	
	public void rotateLeft() {
		this.myRotation = (this.myRotation + 1) % 360;
		this.myDirection = (int)(this.myRotation/90);
		System.out.println(this.myRotation);
	}
	public void rotateRight() {
		this.myRotation = (this.myRotation - 1) % 360;
		if(this.myRotation < 0.0) {
			this.myRotation = 360 + this.myRotation;
		}
		this.myDirection = (int)(this.myRotation/90);
		System.out.println(this.myRotation);
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
		/*               |       Use trig to find x and z lengths (Q1)
		             |\  |--/    sin(angle) * 0.5 = x
		             | \ | /     cos(angle) * 0.5 = z
		             |  \|/      
		          -------------- Where b is X movement in quadrant 1/3, and Z movement in quad 2/4  
		                /|\    | c is X movement in quad 2/4, and Z movement in quad 1/3
		               / | \   | Q1: +X +Z
		              /  |  \  | Q2: +X -Z
		             /   |   \ | Q3: -X -Z
		            /----|    \| Q4: -X +Z
		*/
		final double movement = 0.05;
		double angleFromQuadrant = this.myRotation % 90;
		double xMove;
		double zMove;
		switch (myDirection) {

			case 0: 
				xMove = Math.sin(Math.toRadians(angleFromQuadrant)) * movement;
				zMove = Math.cos(Math.toRadians(angleFromQuadrant)) * movement;
				break;
			case 1: 
				//Positive x, negative z
				xMove = Math.cos(Math.toRadians(angleFromQuadrant)) * movement;
				zMove = Math.sin(Math.toRadians(angleFromQuadrant)) * movement;
				zMove = -zMove;
				break;
			case 2: 
				//Negative x, negative z
				xMove = Math.sin(Math.toRadians(angleFromQuadrant)) * movement;
				zMove = Math.cos(Math.toRadians(angleFromQuadrant)) * movement;
				xMove = -xMove;
				zMove = -zMove;
				break;
			case 3: 
				//Negative x, positive z
				xMove = Math.cos(Math.toRadians(angleFromQuadrant)) * movement;
				zMove = Math.sin(Math.toRadians(angleFromQuadrant)) * movement;
				xMove = -xMove;
				break;
			default:
				xMove = 0;
				zMove = 0;
		}
		if(this.myPosition[0] + xMove >= 0 && this.myPosition[0] + xMove <= size.width - 1){
			if(this.myPosition[2]+zMove >= 0 && this.myPosition[2] + zMove <= size.height - 1) {
				this.myPosition[0] += xMove;
				this.myPosition[2] += zMove;
			}
		}
	}
	public void goBackwards(Dimension size) {
		/*               |       sin rule: a/sin(A) == b/sin(B) == c/sin(C)
		        |\  |--/    We move 0.05 on the diagonal so
		        | \ | /     b = 0.05/sin(90) * sin(B)
		        |  \|/      c = 0.05/sin(90) * sin(90-B)
		     -------------- Where b is X movement in quadrant 1/3, and Z movement in quad 2/4  
		           /|\    | c is X movement in quad 2/4, and Z movement in quad 1/3
		          / | \   | Q1: +X +Z
		         /  |  \  | Q2: +X -Z
		        /   |   \ | Q3: -X -Z
		       /----|    \| Q4: -X +Z
		*/
		final double movement = -0.05;
		double angleFromQuadrant = this.myRotation % 90;
		double xMove = (movement/Math.sin(Math.toRadians(angleFromQuadrant))) * (Math.sin(Math.toRadians(90-angleFromQuadrant)));
		double zMove = (movement/Math.sin(Math.toRadians(90-angleFromQuadrant))) * (Math.sin(Math.toRadians(angleFromQuadrant)));
		double temp;
		switch (myDirection) {
		
		case 0: 
			//Do nothing, both positive
			break;
		case 1: 
			//Positive x, negative z
			temp = xMove;
			xMove = zMove;
			zMove = temp;
			zMove = -zMove;
			break;
		case 2: 
			//Negative x, negative z
			xMove = -xMove;
			zMove = -zMove;
			break;
		case 3: 
			//Negative x, positive z
			temp = xMove;
			xMove = zMove;
			zMove = temp;
			xMove = -xMove;
			break;
		}
		if(this.myPosition[0] + xMove >= 0 && this.myPosition[0] + xMove <= size.width - 1){
			if(this.myPosition[2]+zMove >= 0 && this.myPosition[2] + zMove <= size.height - 1) {
				this.myPosition[0] += xMove;
				this.myPosition[2] += zMove;
			}
		}
	}
}
