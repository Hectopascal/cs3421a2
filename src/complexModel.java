import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class complexModel {

    //  reference from https://www.youtube.com/watch?v=YKFYtekgnP8&index=10&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP
    private Loader test = new Loader();
    private String texturefileName = "stallTexture.png";
    private String textureext1 = "png";
    private int i = 0;
    private MyTexture myTexture[];
    private int[] bufferIds = new int[2];
    private int texUnitLoc;

    private static final String VERTEX_SHADER = "GaroudVertex.glsl";
    private static final String FRAGMENT_SHADER = "GaroudFragment.glsl";

    private int shaderprogram;
    private int texUnit;

    public complexModel(GL2 gl){
        test.loadObj("stall");
        this.generateBuffers(gl);
    }

    public void draw(GL2 gl, Terrain t){
        gl.glPushMatrix();{
            gl.glTranslated(2,t.altitude(2,2),2);
            gl.glScaled(0.2,0.2,0.2);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
            gl.glBindTexture(GL2.GL_TEXTURE_2D,myTexture[i].getTextureId());
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);


            gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
            gl.glNormalPointer(GL.GL_FLOAT, 0, test.getVertice().length*Float.SIZE/8);
            gl.glTexCoordPointer(2, GL.GL_FLOAT,0, test.getVertice().length*Float.SIZE/8+test.getNormal().length*Float.SIZE/8);

            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
            gl.glDrawElements(GL2.GL_TRIANGLES, test.getindex().length,GL2.GL_UNSIGNED_SHORT, 0);
            //Set wrap mode for texture in S direction
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            //Set wrap mode for texture in T direction
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        }gl.glPopMatrix();
    }

    public void generateBuffers(GL2 gl){
        FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(test.getVertice());
        FloatBuffer normalBuffer = Buffers.newDirectFloatBuffer(test.getNormal());
        ShortBuffer indexBuffer = Buffers.newDirectShortBuffer(test.getindex());
        FloatBuffer textBuffer = Buffers.newDirectFloatBuffer(test.getTexture());
        gl.glEnable(GL2.GL_TEXTURE_2D);
        myTexture = new MyTexture[1];
        myTexture[0] =  new MyTexture(gl,this.texturefileName,this.textureext1,false);

        //gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        gl.glGenBuffers(2,this.bufferIds,0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,this.bufferIds[0]);
        long len = test.getVertice().length*Float.SIZE/8 + test.getTexture().length*Float.SIZE/8+ test.getNormal().length*Float.SIZE/8;;
        gl.glBufferData(GL.GL_ARRAY_BUFFER,len, null, GL2.GL_STATIC_DRAW);


        gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, test.getVertice().length*Float.SIZE/8, verticesBuffer);
        gl.glBufferSubData(GL.GL_ARRAY_BUFFER, test.getVertice().length*Float.SIZE/8, test.getNormal().length*Float.SIZE/8, normalBuffer);
        gl.glBufferSubData(GL.GL_ARRAY_BUFFER, test.getVertice().length*Float.SIZE/8+test.getNormal().length*Float.SIZE/8, test.getTexture().length*Float.SIZE/8, textBuffer);

        //Now for the element array
        //Element arrays hold indexes to an array buffer
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
                test.getindex().length *Short.SIZE/8,
                indexBuffer, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        try {
            shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        texUnitLoc = gl.glGetUniformLocation(shaderprogram,"texUnit");
    }
}

