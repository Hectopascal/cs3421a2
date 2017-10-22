import com.jogamp.opengl.GL2;
import jdk.nashorn.internal.codegen.CompilationException;

import java.io.*;

public class Shader {

    private String[] mySource;
    private int myType;
    private int myID;
    public Shader(int type, File sourceFile) throws IOException{
        myType = type;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(sourceFile)));
            StringWriter writer = new StringWriter();
            mySource = new String[1];
            String line = reader.readLine();
            while(line != null){
                writer.write(line);
                writer.write("\n");
                line = reader.readLine();
            }
            reader.close();
            mySource[0] = writer.getBuffer().toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void compile(GL2 gl){
        for(int i=0; i < mySource.length;i++){
            System.out.println(mySource[i]);
        }
        myID = gl.glCreateShader(myType);
        gl.glShaderSource(myID,1, mySource, new int[] { mySource[0].length() }, 0);
        gl.glCompileShader(myID);

        int[] compiled = new int[1];
        gl.glGetShaderiv(myID,GL2.GL_COMPILE_STATUS, compiled, 0);
        if(compiled[0] == 0){
            int[] logLength = new int[1];
            gl.glGetShaderiv(myID,GL2.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(myID,logLength[0], (int[]) null, 0, log, 0);
        }

    }


    public static int initShaders(GL2 gl, String vs, String fs) throws Exception{
        Shader vertexShader = new Shader(GL2.GL_VERTEX_SHADER,new File(vs));
        vertexShader.compile(gl);
        Shader fragmentShader = new Shader(GL2.GL_FRAGMENT_SHADER,new File(fs));
        fragmentShader.compile(gl);
        int shaderprogram = gl.glCreateProgram();
        gl.glAttachShader(shaderprogram,vertexShader.myID);
        gl.glAttachShader(shaderprogram,fragmentShader.myID);
        gl.glLinkProgram(shaderprogram);

        int[] error = new int[0];
        gl.glGetProgramiv(shaderprogram,GL2.GL_LINK_STATUS,error,9);
        if(error[0] != GL2.GL_TRUE){
            int[] logLength = new int[1];
            gl.glGetProgramiv(shaderprogram,GL2.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(shaderprogram,logLength[0], (int[]) null, 0, log, 0);
            //throw new CompilationException("Error" + new String(log));
        }
        gl.glValidateProgram(shaderprogram);
        gl.glGetProgramiv(shaderprogram,GL2.GL_VALIDATE_STATUS, error, 0);
        if(error[0] != GL2.GL_TRUE){
            throw new Exception("fail to validate");
        }
        return shaderprogram;
    }

}
