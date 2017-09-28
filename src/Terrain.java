import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * Uses Bilinear Interpolation
     * Reference: http://supercomputingblog.com/graphics/coding-bilinear-interpolation/
     * 
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        int xInt = (int) x;
        int zInt = (int) z;
        if(xInt == x && zInt == z) {
        	altitude = getGridAltitude(xInt, zInt);
        }
        else {
	        int x1 = (int) Math.floor(x);
	        int x2 = (int) Math.ceil(x);
	        int z1 = (int) Math.floor(z);
	        int z2 = (int) Math.ceil(z);
	        
	        double r1 = (((x2 - x) / (x2 - x1)) * getGridAltitude(x1, z1));
	        r1 += (((x - x1) / (x2 - x1)) * getGridAltitude(x2, z1));
	        double r2 = (((x2 - x) / (x2 - x1)) * getGridAltitude(x1, z2));
	        r2 += (((x - x1) / (x2 - x1)) * getGridAltitude(x2, z2));
	        
	        double p = (((z2 - z) / (z2 - z1)) * r1);
	        p += (((z - z1) / (z2 - z1)) * r2);
	        
	        altitude = p;
        }
        return altitude;
    }

    public void drawGame(GL2 gl) {
    	draw(gl);
    	for(Tree t : this.myTrees) {
    		t.draw(gl);
    	}
    	for(Road r : this.myRoads) {
    		r.draw(gl);
    	}
    }
    
    public void draw(GL2 gl) {
    	 gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glPushMatrix();
         float width = mySize.width;
         float height = mySize.height;
         float i,j; 
         float x1=0;
         float y1=0;
         float x2=0;
         float y2=0;
         
            for (i = 0; i < width-1; i+=1.0) {
	        	 
	        	 gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	        	 System.out.println("CURRENT I IS "+i);
	        	 gl.glPolygonOffset(0.5f, 0.5f);
	        	 gl.glBegin(GL2.GL_TRIANGLES);
	        	 	
		            for ( j = 0; j < height; j+=1.0) {
		            	System.out.println("CURRENT J IS "+j);
		            	gl.glColor3f(1-i/width, i/width,j/height);
		            	
		            	if(j==0) {
		            		x1 = (2*i)/(width-1)-1;
		            		y1 =1-(2*j)/(height-1);
		            		x2 = (2*(i+1))/(width-1)-1;
		            		y2 = 1-(2*(j))/(height-1);
			            	  
		            	} else {
		            		/* A(x1,y1)	 B (x2,y2)
		            		 * 	 o_______o
		            		 *   |\		|
		            		 *   | \	|
		            		 *   |  \	|
		            		 *   |    \	|
		            		 *  o|_____\|o
		            		 * C new(x1,y1)  D new(x2,y2)
		            		 * 
		            		 * Drawing in counterclockwise, 
		            		 * B->A->D, D->A->C
		            		 */
		            		
		            		//Draw top right triangle
			            	System.out.println("Triangle 1");

		            		gl.glVertex3f(x2,(float)myAltitude[(int)i+1][(int)j-1]/2-1,y2);
		            		gl.glVertex3f(x1,(float)myAltitude[(int)i][(int)j-1]/2-1,y1);

			            	System.out.print("("+x1+","+y1+") "+"("+x2+","+y2+") ");
			            	
		            		x2 = (2*(i+1))/(width-1)-1;
		            		y2 = 1-(2*(j))/(height-1);
		            		System.out.println("("+x2+","+y2+") ");
		            		gl.glVertex3f(x2,(float)myAltitude[(int)i+1][(int)j]/2-1,y2);
		            		
		            		
		            		//Draw bottom triangle
		            		gl.glColor3f(1-(i+0.5f)/width, (i+0.5f)/width,(j+0.5f)/height);
		            		System.out.println("Triangle 2");

		            		gl.glVertex3f(x2,(float)myAltitude[(int)i+1][(int)j]/2-1,y2);
		            		gl.glVertex3f(x1,(float)myAltitude[(int)i][(int)j-1]/2-1,y1);

			            	System.out.print("("+x1+","+y1+") "+"("+x2+","+y2+") ");
		            		x1 = (2*i)/(width-1)-1;
		            		y1 =1-(2*j)/(height-1);

		            		System.out.println("("+x1+","+y1+") ");
		            		gl.glVertex3f(x1,(float)myAltitude[(int)i][(int)j]/2-1,y1);
		            		
		            	}
		            	
		            	
		            	
		             }
		             
                 gl.glEnd();
                 
	         }
	        
         gl.glPopMatrix();
         gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
     	
    }
    
    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


}
