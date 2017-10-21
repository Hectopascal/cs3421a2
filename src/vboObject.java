import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;

public class vboObject {
    private double rotateOnX = 0;
    private double rotateOnY = 0;
    private int maxStacks = 20;
    private int maxSlices = 30;
    private int maxVertices = (maxSlices + 1) * 2 * maxStacks;
    FloatBuffer verticesBuffer = FloatBuffer.allocate(maxVertices*3);
    FloatBuffer normalsBuffer = FloatBuffer.allocate(maxVertices*3);
    private int bufferIds[] = new int[1];

    private static final String VERTEX_SHADER = "ConstantColorVertex.glsl";
    private static final String FRAGMENT_SHADER = "ConstantColorFragment.glsl";

    private int shaderprogram;

    public vboObject(GL2 gl){
        this.generateBuffers(gl);
    }

    public void normalize(double v[]){
        double d = Math.sqrt(Math.pow(v[0],2)+Math.pow(v[1],2)+Math.pow(v[2],2));
        if (d != 0.0){
            v[0] /= d;
            v[1] /= d;
            v[2] /= d;
        }
    }
    double r(double t){
        return Math.cos(2*Math.PI *t);
    }
    public void generateBuffers(GL2 gl){
        generateData();
        gl.glGenBuffers(1,bufferIds,0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER,maxVertices*3*Float.SIZE/4,null,GL2.GL_STATIC_DRAW);
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,0,maxVertices*3*Float.SIZE/8,verticesBuffer);
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,maxVertices*3*Float.SIZE/8,maxVertices*3*Float.SIZE/8,normalsBuffer);
        try{
            shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

    }


    public void generateData(){
        double deltaT;
        deltaT = 0.5/maxStacks;
        int ang;
        int delang = 360/maxSlices;
        double x1,x2,z1,z2,y1,y2;
        double radius = 0.5;
        for (int i =0; i < maxStacks; ++i){
            double t = -0.25 + i*deltaT;
            for(int j = 0; j <= maxSlices; ++j){
                ang = j *delang;
                x1 = radius *r(t) * Math.cos((double)ang*2.0*Math.PI/360.0);
                x2 = radius *r(t+deltaT) * Math.cos((double)ang*2.0*Math.PI/360.0);
                y1 = radius * Math.sin(2*Math.PI * t);
                z1 = radius * r(t) * Math.sin((double)ang*2.0*Math.PI/360.0);
                z2 = radius * r(t+deltaT) * Math.sin((double)ang*2.0*Math.PI/360.0);
                y2 = radius * Math.sin(2*Math.PI * (t+deltaT));
                double normal[] = {x1,y1,z1};
                normalize(normal);
                verticesBuffer.put((float)x1);
                verticesBuffer.put((float)y1);
                verticesBuffer.put((float)z1);
                normalsBuffer.put((float)normal[0]);
                normalsBuffer.put((float)normal[1]);
                normalsBuffer.put((float)normal[2]);
                normal[0] = x2;
                normal[1] = y2;
                normal[2] = z2;
                normalize(normal);
                verticesBuffer.put((float)x2);
                verticesBuffer.put((float)y2);
                verticesBuffer.put((float)z2);
                normalsBuffer.put((float)normal[0]);
                normalsBuffer.put((float)normal[1]);
                normalsBuffer.put((float)normal[2]);
            }
        }
        verticesBuffer.rewind();
        normalsBuffer.rewind();
    }

    public void draw(GL2 gl, float[] light){
        gl.glTranslated(light[0],light[1],light[2]);

        float ambient[] = {0.33f,0.22f,0.03f, 1.0f};
        float diffuse[] = {0.78f, 0.57f, 0.11f, 1.0f};
        float specular[] = {0.99f, 0.91f, 0.81f, 1.0f};
        float shininess = 27.8f;

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient,0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse,0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular,0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
        gl.glPushMatrix();{
            gl.glUseProgram(shaderprogram);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glVertexPointer(3,GL.GL_FLOAT,0,0);
            gl.glNormalPointer(GL.GL_FLOAT,0,maxVertices*3*Float.SIZE/8);
            for(int i = 0; i < maxStacks; ++i){
                gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP,i*(maxSlices+1)*2,(maxSlices+1)*2);
            }

            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
            gl.glUseProgram(0);
        }gl.glPopMatrix();
    }

}
