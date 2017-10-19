import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject {

    private Color material;
    private static final int SLICES = 32;
    private static final double CYLINDER_RADIUS = 0.1;
    private static final double HEIGHT = 1;
    
    //Material Properties
	private float matAmbAndDif1[] = {0.0f, 0.9f, 0.0f, 1.0f};
	private float matAmbAndDif2[] = {1.0f, 1.0f, 1.0f, 1.0f};
	private float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float matShine[] = { 0.0f };
	private float matShine2[] = { 20.0f };

    private Coord myPos;
    
    private MyTexture treeTextures[];
    private String textureFileName1 = "src/textures/treeTrunk.bmp";
    private String textureFileName2 = "src/textures/bricks.bmp";
    private String bmpExt = "bmp";
    
    public Tree(double x, double y, double z, GameObject parent) {
    	super(parent);
        myPos = new Coord(x,y,z);
        this.GlobalPosition = new Coord(x,y,z);
        material = new Color();
        material.diffuse = new ColorObject(0.9f,0.6f,0.2f,0.0f);
    }
    public Tree(Coord coords, GameObject parent) {
    	super(parent);
    	this.GlobalPosition = coords;
    }

    public Coord getPosition() {
        return myPos;
    }
    
    public void init(GL2 gl) {
    	treeTextures = new MyTexture[2];
    	treeTextures[0] = new MyTexture(gl,textureFileName1,bmpExt,true);
    	treeTextures[1] = new MyTexture(gl,textureFileName2,bmpExt,true);
    }
    
    public void draw(GL2 gl) {

        gl.glColor3d(0.0, 0.0, 0.0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);

    	GLUT glut = new GLUT();
    	
    	gl.glEnable(gl.GL_TEXTURE_GEN_S); //enable texture coordinate generation
	    gl.glEnable(gl.GL_TEXTURE_GEN_T);
    	//Trunk
    	gl.glPushMatrix();
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
	    	gl.glBindTexture(GL2.GL_TEXTURE_2D, treeTextures[0].getTextureId()); 
    		gl.glRotated(90, -90, 0, 0);
    		glut.glutSolidCylinder(0.1, 1.5, 40, 40);
    	gl.glPopMatrix();
    	//Leaves
    	gl.glPushMatrix();
    		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine2,0);
    		gl.glBindTexture(GL2.GL_TEXTURE_2D, treeTextures[1].getTextureId()); 
    		gl.glTranslated(0, 1.5, 0);
    		glut.glutSolidTeapot(0.5);
    		//glut.glutSolidSphere(0.55, 40, 40);
    	gl.glPopMatrix();
    	
    	gl.glDisable(gl.GL_TEXTURE_GEN_S); //enable texture coordinate generation
	    gl.glDisable(gl.GL_TEXTURE_GEN_T);
    }
}
