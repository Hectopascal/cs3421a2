
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
		gl.glTranslated(this.myPosition[0], this.myPosition[1], this.myPosition[2]);
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId()); 
		glut.glutSolidTeapot(0.4);
		gl.glPopMatrix();
		gl.glPopMatrix();
	}
	public void goForwards(Dimension size) {
		System.out.println("Moving");
		switch(myDirection) {
			case 0:
				if((this.myPosition[2]+0.05) <= size.height-1) {
					this.myPosition[2] += 0.05;
				}
				break;
			case 1:
				if((this.myPosition[0]+0.05) <= size.width-1) {
					this.myPosition[0] += 0.05;
				}
				break;
			case 2:
				if((this.myPosition[2]-0.05) >= 0) {
					this.myPosition[2] -= 0.05;
				}
				break;
			case 3:
				if((this.myPosition[0] - 0.05) >= 0) {
					this.myPosition[0] -= 0.05;
				}
				break;
		}
	}
	public void goBackwards(Dimension size) {
    	switch (this.myDirection) {
    	case 0: 
    		if ((this.myPosition[2]-0.05) >= 0)
    			this.myPosition[2] -= 0.05;
    		break;
    	case 1: 
    		if ((this.myPosition[0]-0.05) >= 0)
    			this.myPosition[0] -= 0.05;
    		break;
    	case 2: 
    		if ((this.myPosition[2]+0.05) <= size.height-1)
    			this.myPosition[2] += 0.05;
    		break;
    	case 3: 
    		if ((this.myPosition[0]+0.05) <= size.width-1)
    			this.myPosition[0] += 0.05;
    		break;
    	}
	}
}
