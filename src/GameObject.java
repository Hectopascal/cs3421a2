
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class GameObject {
    public ArrayList<GameObject> all_object = new ArrayList<GameObject>();

    public static final GameObject ROOT = new GameObject(null);
    protected GameObject parent;
    private ArrayList<GameObject> children = new ArrayList<GameObject>();
    protected boolean enabled;

    public Coord myTranslation;
    public Coord myRotation;
    public Coord myScale;
    
    // constructor
    public GameObject (GameObject parent){
        this.parent = parent;
        if (parent != null){
            this.parent.children.add(this);
        }
        this.children = new ArrayList<GameObject>();
        all_object.add(this);
        this.myTranslation = new Coord(0.0,0.0,0.0);
        this.myRotation = new Coord(0.0,0.0,0.0);
        this.myScale = new Coord(1.0,1.0,1.0);
        this.enabled = true;
    }
    
    public void drawSelf(GL2 gl) {
    	//to be overridden
    }
    public void draw(GL2 gl){
        gl.glPushMatrix();
	        gl.glTranslated(myTranslation.x,myTranslation.y,myTranslation.z);
	        gl.glRotated(myRotation.x,1.0,0.0,0.0);
	        gl.glRotated(myRotation.y,0.0,1.0,0.0);
	        gl.glRotated(myRotation.z,0.0,0.0,1.0);
	        gl.glScaled(myScale.x,myScale.y,myScale.z);
	        drawSelf(gl);
	        ArrayList<GameObject> cList = new ArrayList<GameObject>(children);
	        for (GameObject o: cList){
	            o.draw(gl);
	        }
        gl.glPopMatrix();
    }

    // some helper?
    public Coord getGlobalRotation(){
    	Coord rotation = myRotation;
    	GameObject p = this.parent;
    	while(p!=null) {
	    	rotation.x += p.myRotation.x;
	    	rotation.y += p.myRotation.y;
	    	rotation.x += p.myRotation.x;
	        p=p.parent;
    	}
    	return rotation;
    }
    
    //return global transform matrix
    public double[][] getGlobalTransform() {

    	GameObject p = this.parent;
    	double[][] s,r,t;
    	double[][] matrix = MathUtil.identityMatrix();
    	
    	while(p!=null) {
    		s = MathUtil.scaleMatrix4(p.myScale);
        	r = MathUtil.rotationMatrixXYZ(p.myRotation);
        	t = MathUtil.translationMatrix4(p.myTranslation);
    		matrix = MathUtil.multiply(MathUtil.multiply(MathUtil.multiply4(t,r),s),matrix);
        	
            p=p.parent;
        }
    	
    	return matrix;
    }
    
    public Coord getGlobalPosition(){
    	double[] myPos = {myTranslation.x,myTranslation.y,myTranslation.z};

        myPos=MathUtil.multiply(getGlobalTransform(),myPos);
    	Coord globPosition = new Coord(myPos[0],myPos[1],myPos[2]);
    	
    	return globPosition;
    	/*
        // a vector was returned
        Coord parentPosition;
        Coord parentRotation;
        Coord parentScale;
        if (this == GameObject.ROOT){
            parentPosition = new Coord();
            parentRotation = new Coord();
            parentScale = new Coord(1.0,1.0,1.0);
        }else{
            parentPosition = parent.getPosition();
            parentRotation = parent.getRotation();
            parentScale = parent.getScale();
        }
        Coord copy = myTranslation;
        double [][] localMatrix = MathUtil.translationMatrix4(copy);
        double [][] parentRotateMatrix = MathUtil.rotationMatrixXYZ(parentRotation);
        double [][] RTmatrix = MathUtil.multiply4(parentRotateMatrix,localMatrix);

        Coord intermidateV = MathUtil.translationVector(RTmatrix);
        // intermidateV need a power
        intermidateV.multiplySelfAndScale(parentScale);

        Coord result = intermidateV.add(parentPosition);
        return result; */
    }

    public Coord getGlobalScale(){
    	Coord scale = myScale;
    	GameObject p = this.parent;
    	while(p!=null) {
	    	scale.x = scale.x*p.myScale.x;
	    	scale.y = scale.y*p.myScale.y;
	    	scale.z = scale.z*p.myScale.z;
	        p=p.parent;
    	}
    	return scale;
    }
   
    public void setScale(Coord coord) { this.myScale =coord;}
    public void setRotation(Coord coord) { this.myRotation =coord;}
    public void setPosition(Coord coord) { this.myTranslation =coord;}
}
