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
	
	private String textureFileName1 = "src/textures/water.bmp";
	private String bmpExt = "bmp";
	
	private MyTexture texture;
	
	public Avatar(double x, double y, double z) {
		myPosition = new double[3];
		myPosition[0] = x;
		myPosition[1] = y;
		myPosition[2] = z;
		
		myRotation = 0;
	}
	public void rotateLeft() {
		myRotation = (myRotation + 10) % 360;
	}
	public void rotateRight() {
		myRotation = (myRotation - 10) % 360;
		if(myRotation < 0.0) {
			myRotation = 360 + myRotation;
		}
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
}
