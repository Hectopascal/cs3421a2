import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.test.junit.graph.demos.MSAATool;

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
        Material = new Color();
        Material.diffuse = new ColorObject(0.5f,0.5f,0.5f,1.0f);
    }


	public void draw(GL2 gl) {
//        int numPoints = myPoints.size();
//        //gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
//
//        //gl.glColor4d(0.0, 0.0, 0.0, 1);
//        gl.glLoadIdentity();
//        gl.glPointSize(20);
//
//        /*
//        gl.glBegin(GL2.GL_LINE_STRIP);
//        gl.glColor3d(1, 1, 1);
//    	gl.glVertex3d(1,1,1);
//    	gl.glVertex3d(0.5,0.5,0.5);
//    	gl.glEnd();*/
//
//        gl.glColor3d(1, 0, 1);
//        gl.glBegin(GL2.GL_POINTS);
//        //double tIncrement = 1.0/numPoints;
//        double tIncrement = ((double)this.size())/numPoints;
//        //System.out.println("numPoints " + numPoints + " " + tIncrement);
//        for(int i = 0; i < numPoints*this.size(); i++){
//        	double t = i*tIncrement;
//        	//System.out.println("t " + t);
//        	gl.glVertex3d(this.point(t)[0],0,this.point(t)[1]);
//        }
//        gl.glVertex2dv(this.controlPoint(this.size()*3),0);
//        gl.glEnd();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_AMBIENT,new float[]{Material.ambient.x,Material.ambient.y,Material.ambient.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_DIFFUSE,new float[]{Material.diffuse.x,Material.diffuse.y,Material.diffuse.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_SPECULAR,new float[]{Material.specular.x,Material.specular.y,Material.specular.z},0);
        gl.glMaterialfv(GL.GL_FRONT,GL2.GL_SHININESS,new float[]{Material.phong.x,Material.phong.y,Material.phong.z},0);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
        gl.glTexEnvf(GL.GL_TEXTURE2,GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S,GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T,GL2.GL_REPEAT);

        gl.glBegin(GL2.GL_TRIANGLE_STRIP);

        double scale = 0.25;
        double spineDist = 0;
        double count = 0;


	}

}
