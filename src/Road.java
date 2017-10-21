import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private Color Material;
    private ArrayList<double[]> bezierPoints;
    private ArrayList<double[]> vertexLevel;
    private int numSegment = 20;
    private Terrain myTerrain;
    private MyTexture[] textures;
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        bezierPoints = new ArrayList<double[]>();
        vertexLevel = new ArrayList<double[]>();
        // generate bezierPoints
        double[] currPoint;
        for (int i = 0; i < this.numSegment;++i){
            double tmp = (double)i/(double)this.numSegment;
            currPoint = point(tmp);
            bezierPoints.add(currPoint);
        }

        myPoints.add(x0);
        myPoints.add(y0);
    }

    private void generateVertexLevel(){
        double[][] corners;
        double x;
        double z;
        for(int i = 0; i < this.bezierPoints.size(); ++i){
            corners = calculateCorner(bezierPoints.get(i-1),bezierPoints.get(i));
        }

    }

    public double[][] calculateCorner(double[] p1, double[] p2){
        double x = p2[0] - p1[0];
        double z = p2[1] - p1[1];
        double mag = Math.sqrt(Math.pow(x,2)+Math.pow(z,2));
        x/=mag;
        z/=mag;
        x *= this.myWidth / 2;
        z *= this.myWidth / 2;

        double[] c1 = new double[3];
        double[] c2 = new double[3];
        c1[0] = -z;
        c1[0] += p1[0];
        c1[2] = x + p1[1];

        c2[0] = z+p1[0];
        c2[2] = -x+c1[1];
        double[][] c = new double[2][3];
        c[0] = c1;
        c[1] = c2;
        return c;
    }
    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    public void setTerrain(Terrain t) {
    	myTerrain = t;
    }
    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }


    public void init(GL2 gl) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textures = new MyTexture[2];
    	textures[0] = new MyTexture(gl,"src/textures/bricks.bmp","bmp",true);
    	textures[1] = new MyTexture(gl,"src/textures/bricks.bmp","bmp",true);
        Material = new Color();
        Material.diffuse = new ColorObject(0.5f,0.5f,0.5f,1.0f);
    }

	public void draw(GL2 gl) {

		int numPoints = myPoints.size();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_AMBIENT,new float[]{Material.ambient.x,Material.ambient.y,Material.ambient.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_DIFFUSE,new float[]{Material.diffuse.x,Material.diffuse.y,Material.diffuse.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_SPECULAR,new float[]{Material.specular.x,Material.specular.y,Material.specular.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_SHININESS,new float[]{Material.phong.x,Material.phong.y,Material.phong.z},0);

        gl.glTexEnvf(GL.GL_TEXTURE2,GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S,GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T,GL2.GL_REPEAT);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);

//        double scale = 0.25;
//        double spineDist = 0;
//        double count = 0;
        
        double[] p1 = this.point(0);	
    	double alt = myTerrain.altitude(p1[0], p1[1]) + 0.1; //added manual offset as the polygon offset works poorly
    	double width = myWidth/2;
        double tIncrement = ((double)this.size())/numPoints;
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureId());
        double inc = 1/(double)numSegment;
	        for (int i = 1; (double)i/(double)numSegment   < (double)this.size()/2; i++) {
	        	double t = i/(double)numSegment;
	        	System.out.println(t+" testing");
	        	//Corners for top left triangle
	        	double[] topLeft = {this.point(t)[0]-width, 
	        						myTerrain.altitude(this.point(t)[0]-width, this.point(t)[1])+0.1, 
	        						this.point(t)[1]};
	    	    double[] topRight = {this.point(t)[0]+width, 
	    	    					myTerrain.altitude(this.point(t)[0]+width,this.point(t)[1])+0.1, 
	    	    					this.point(t)[1]};
	            double[] botLeft = {this.point(t+inc)[0]-width, 
	            					myTerrain.altitude(this.point(t+inc)[0]-width, this.point(t+inc)[1])+0.1, 
	            					this.point(t+inc)[1]};
	            double[] botRight = {this.point(t+inc)[0]+width, 
						myTerrain.altitude(this.point(t+inc)[0]+width, this.point(t+inc)[1])+0.1, 
						this.point(t+inc)[1]};
	            double[] normals = getNormal(botLeft, topRight, topLeft);
		        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureId());
	            gl.glBegin(GL2.GL_TRIANGLES);{
	            	gl.glNormal3d(normals[0], normals[1], normals[2]);
	            	gl.glTexCoord2d(botLeft[0],botLeft[2]);
		        	gl.glVertex3d(botLeft[0],botLeft[1],botLeft[2]);
		        	
		        	gl.glNormal3d(normals[0], normals[1], normals[2]);
		        	gl.glTexCoord2d(topRight[0], topRight[2]);
		        	gl.glVertex3d(topRight[0],topRight[1],topRight[2]);
		        	
		        	gl.glNormal3d(normals[0], normals[1], normals[2]);
		        	gl.glTexCoord2d(topLeft[0], topLeft[2]);
		        	gl.glVertex3d(topLeft[0],topLeft[1],topLeft[2]);
	        	}gl.glEnd();   		
		        //Corner for bottom right triangle
		        
		        normals = getNormal(botLeft, botRight, topRight);
		        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureId());
		        gl.glBegin(GL2.GL_TRIANGLES);{
		        	gl.glNormal3d(normals[0], normals[1], normals[2]);
		        	gl.glTexCoord2d(botLeft[0], botLeft[2]);
		        	gl.glVertex3d(botLeft[0], botLeft[1], botLeft[2]);
		        	gl.glNormal3d(normals[0], normals[1], normals[2]);
		        	gl.glTexCoord2d(botRight[0], botRight[2]);
		        	gl.glVertex3d(botRight[0], botRight[1], botRight[2]);
		        	gl.glNormal3d(normals[0], normals[1], normals[2]);
		        	gl.glTexCoord2d(topRight[0], topRight[2]);
		        	gl.glVertex3d(topRight[0], topRight[1], topRight[2]);
		        }
		        gl.glEnd();
		            	
		     }
    		
	}
    
    public double[] normal(double t){
    	double[] normal = new double[2];
    	int i = (int) Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        double x4 = myPoints.get(i++);
        double y4 = myPoints.get(i++);
        
        
        normal[0] = 3 * ((1-t)*(1-t)*(y2-y1) + 2*(1-t)*t*(y3-y2) + t*t*(y4-y3));
        normal[1] = 3 * ((1-t)*(1-t)*(x2-x1) + 2*(1-t)*t*(x3-x2) + t*t*(x4-x3));

        normal[0] = (normal[0] / (Math.sqrt(normal[1] * normal[1] + normal[0] * normal[0])));
        normal[1] = (normal[1] / (Math.sqrt(normal[1] * normal[1] + normal[0] * normal[0])));
        
		return normal;
    }

	double [] getNormal(double[] p0, double[] p1, double[] p2){
    	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]}; 	
    	return cross(u,v);   	
    }
	double [] cross(double u [], double v[]){
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    	
    	return crossProduct;
    }
    
}
