import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    //reference https://www.youtube.com/watch?v=YKFYtekgnP8&index=10&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP
    private float[] vertice = null;
    private float[] texture = null;
    private float[] normal = null;
    private short[] index = null;

    public void loadObj(String name){
        FileReader obj = null;
        try{
            obj = new FileReader(new File(name+".obj"));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        BufferedReader buffreader = new BufferedReader(obj);
        String line = null;
        List<vector3> vetexlist = new ArrayList<vector3>();
        List<vector2f> texturelist = new ArrayList<vector2f>();
        List<vector3> normallist = new ArrayList<vector3>();
        List<Short> indexlist = new ArrayList<Short>();

        try{
            while(true) {
                line = buffreader.readLine();
                String[] currLine = line.split(" ");
                if (line.startsWith("v ")) {
                    vector3 vertex = new vector3(Float.parseFloat(currLine[1]), Float.parseFloat(currLine[2]), Float.parseFloat(currLine[3]));
                    vetexlist.add(vertex);
                } else if (line.startsWith("vt ")) {
                    vector2f texture = new vector2f(Float.parseFloat(currLine[1]), Float.parseFloat(currLine[2]));
                    texturelist.add(texture);
                } else if (line.startsWith("vn ")) {
                    vector3 normal = new vector3(Float.parseFloat(currLine[1]), Float.parseFloat(currLine[2]), Float.parseFloat(currLine[3]));
                    normallist.add(normal);
                } else if (line.startsWith("f ")) {
                    texture = new float[vetexlist.size() * 2];
                    normal = new float[vetexlist.size() * 3];
                    break;
                }

            }
            while (line != null){
                if (!line.startsWith("f ")){
                    line = buffreader.readLine();
                    continue;
                }
                String[] curr = line.split(" ");
                String[] index1 = curr[1].split("/");
                String[] index2 = curr[2].split("/");
                String[] index3 = curr[3].split("/");
                //System.out.println(index1[0]);
                processVertex(index1,indexlist,texturelist,normallist,texture,normal);
                processVertex(index2,indexlist,texturelist,normallist,texture,normal);
                processVertex(index3,indexlist,texturelist,normallist,texture,normal);
                line =buffreader.readLine();
            }
            buffreader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        vertice = new float[vetexlist.size()*3];
        index = new short[indexlist.size()];
        int count = 0;
        for(vector3 v: vetexlist){
            vertice[count++] = v.getX();
            vertice[count++] = v.getY();
            vertice[count++] = v.getZ();
        }
        for(int i =0; i < indexlist.size(); ++i){
            index[i] = indexlist.get(i);
        }
    }

    private static void processVertex(String[] vertex, List<Short> ind, List<vector2f> textures,
                                      List<vector3> normals, float[] textureA, float[] normalA){
            int currentVertexPointer = Integer.parseInt(vertex[0])-1;
            ind.add((short)currentVertexPointer);
            vector2f currenTex = textures.get(Integer.parseInt(vertex[1])-1);
            textureA[currentVertexPointer*2] = currenTex.getX();
            textureA[currentVertexPointer*2+1] = currenTex.getY();
            vector3 currentNorm = normals.get(Integer.parseInt(vertex[2])-1);
            normalA[currentVertexPointer*3] = currentNorm.getX();
            normalA[currentVertexPointer*3+1] = currentNorm.getY();
            normalA[currentVertexPointer*3+2] = currentNorm.getZ();
    }

    public float[] getVertice(){
        return vertice;
    }

    public float[] getNormal(){
        return normal;
    }

    public short[] getindex(){
        return index;
    }

    public float[] getTexture(){
        return texture;
    }

}

