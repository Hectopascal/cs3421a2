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

    private static final double SCALE = 0.2;
    
    private List<Coord> myPoints;
    private double myWidth;
    private Color Material;
    private ArrayList<double[]> bezierPoints;
    private ArrayList<double[]> vertexLevel;
    private int numSegment = 10;
    private Terrain myTerrain;
    private MyTexture[] textures;
    private Polygon myCrossSection;
    private ArrayList<Polygon> myMesh;
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0,Terrain terrain) {
        myWidth = width;
        myPoints = new ArrayList<Coord>();
        bezierPoints = new ArrayList<double[]>();
        vertexLevel = new ArrayList<double[]>();
        // generate bezierPoints
        double[] currPoint;
        for (int i = 0; i < this.numSegment;++i){
            double tmp = (double)i/(double)this.numSegment;
            currPoint = point(tmp);
            bezierPoints.add(currPoint);
        }

        myPoints.add(new Coord(x0,0,y0));
        
        
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
    public Road(double width, double[] spine, Terrain terrain) {
    	myTerrain = terrain;
        myWidth = width;
        myPoints = new ArrayList<Coord>();
        System.out.println(myTerrain.altitude(spine[0], spine[1]));
        for (int i = 0; i < spine.length; i+=2) {
        	//Coord toAdd = new Coord(spine[i],myTerrain.altitude(spine[i], spine[i+1]),spine[i+1]);
        	Coord toAdd = new Coord(spine[i],0,spine[i+1]);
        	
        	System.out.println(toAdd.toString());
            myPoints.add(toAdd);
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
        myPoints.add(new Coord(x1,0,y1));
        myPoints.add(new Coord(x2,0,y2)); 
        myPoints.add(new Coord(x3,0,y3));
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
        p[0] = myPoints.get(i).x;
        p[1] = myPoints.get(i).z;
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
        
        //i *= 6;
        
        double x0 = myPoints.get(i).x;
        double y0 = myPoints.get(i).z;
        double x1 = myPoints.get(i+1).x;
        double y1 = myPoints.get(i+1).z;
        double x2 = myPoints.get(i+2).x;
        double y2 = myPoints.get(i+2).z;
        double x3 = myPoints.get(i+3).x;
        System.out.println(x3);
        double y3 = myPoints.get(i+3).z;
        
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
    	initCrossSection();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textures = new MyTexture[2];
    	textures[0] = new MyTexture(gl,"src/textures/bricks.bmp","bmp",true);
    	textures[1] = new MyTexture(gl,"src/textures/bricks.bmp","bmp",true);
        Material = new Color();
        Material.diffuse = new ColorObject(0.5f,0.5f,0.5f,1.0f);
    }
  
    public void draw(GL2 gl) {
    	
    	List<Polygon> mesh = getMesh();
        if (mesh != null) {
            gl.glColor4d(0, 0, 0, 1);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureId());
            
            for (Polygon p : mesh) {
                p.draw(gl);                
            }
        }
    }
    /**
     * Get the extruded mesh
     * 
     * @return
     */
    public List<Polygon> getMesh() {
        // compute the mesh if necessary
        if (myMesh == null) {
            computeMesh();
        }
        return myMesh;
    }
    /**
     * The extrusion code.
     * 
     * This method extrudes the cross section along the spine
     *
     */
    private void computeMesh() {
               
        Polygon cs = myCrossSection;
        if (cs == null) {
            return;
        }
        
        List<Coord> crossSection = cs.getPoints();
        List<Coord> spine = myPoints;
        if (spine == null) {
            return;
        }
        
        List<Coord> vertices = new ArrayList<Coord>();

        Coord pPrev;
        Coord pCurr = new Coord(point(0)[0],
        		myTerrain.altitude(point(0)[0], point(0)[1]),
        		point(0)[1]);
        Coord pNext = new Coord(point(1/(double)numSegment)[0],
        		myTerrain.altitude(point(1/(double)numSegment)[0], point(1/(double)numSegment)[1]),
        		point(1/(double)numSegment)[1]);
        
        // first point is a special case
        addPoints(crossSection, vertices, pCurr, pCurr, pNext);
        
        // mid points
        for (int i = 0; (double)i/(double)numSegment < (double)myPoints.size()-4; i++) {
        	double t=(double)(i+1)/(double)numSegment;
            pPrev = pCurr;
            pCurr = pNext;
            pNext = new Coord(point(t)[0],
	            		myTerrain.altitude(point(t)[0], point(t)[1]),
	            		point(t)[1]);
            addPoints(crossSection, vertices, pPrev, pCurr, pNext);            
        }
        
        // last point is a special case
        pPrev = pCurr;
        pCurr = pNext;
        addPoints(crossSection, vertices, pPrev, pCurr, pCurr);
        
        myMesh = new ArrayList<Polygon>();

        int n = crossSection.size();
        
        // for each point along the 
        for (int i = 0; i<vertices.size() ; i++) {

            // for each point in the cross section
            for (int j = 0; j < n; j++) {
                
                Polygon quad = new Polygon();                
                quad.addPoint(vertices.get(i * n + j));
                quad.addPoint(vertices.get(i * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + j));
                myMesh.add(quad);
            }
            
        }
        
    }

    private void initCrossSection() {
        //myCrossSection = new LinkedHashMap<String, Polygon>();
    	//"square"
        Polygon square = new Polygon();
        square.addPoint(SCALE, myWidth*SCALE, 0);
        square.addPoint(-SCALE, myWidth*SCALE, 0);
        square.addPoint(-SCALE, -myWidth*SCALE, 0);
        square.addPoint(SCALE, -myWidth*SCALE, 0);
        myCrossSection = square;


        
    }
    /**
     * Transform the points in the cross-section using the Frenet frame
     * and add them to the vertex list.
     * 
     * @param crossSection The cross section
     * @param vertices The vertex list
     * @param pPrev The previous point on the spine
     * @param pCurr The current point on the spine
     * @param pNext The next point on the spine
     */
    private void addPoints(List<Coord> crossSection, List<Coord> vertices,
            Coord pPrev, Coord pCurr, Coord pNext) {

        // compute the Frenet frame as an affine matrix
        double[][] m = new double[4][4];
        
        // phi = pCurr        
        m[0][3] = pCurr.x;
        m[1][3] = pCurr.y;
        m[2][3] = pCurr.z;
        m[3][3] = 1;
        
        // k = pNext - pPrev (approximates the tangent)
       
        m[0][2] = pNext.x - pPrev.x;
        m[1][2] = pNext.y - pPrev.y;
        m[2][2] = pNext.z - pPrev.z;
        m[3][2] = 0;
      
        
        // normalise k
        double d = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);  
        m[0][2] /= d;
        m[1][2] /= d;
        m[2][2] /= d;
        
        // i = simple perpendicular to k
        m[0][0] = -m[1][2];
        m[1][0] =  m[0][2];
        m[2][0] =  0;
        m[3][0] =  0;
        
        // j = k x i
        m[0][1] = m[1][2] * m[2][0] - m[2][2] * m[1][0];
        m[1][1] = m[2][2] * m[0][0] - m[0][2] * m[2][0];
        m[2][1] = m[0][2] * m[1][0] - m[1][2] * m[0][0];
        m[3][1] =  0;
        
        // transform the points
       
        for (Coord cp : crossSection) {
        
            Coord q = cp.transform(m);
           
            vertices.add(q);
        }
    }
/*
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
*/
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
