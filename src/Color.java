
public class Color {
    public ColorObject ambient;
    public ColorObject diffuse;
    public ColorObject specular;
    public ColorObject phong;

    public Color(){
        ambient = new ColorObject();
        diffuse = new ColorObject();
        specular = new ColorObject();
        phong = new ColorObject();
    }

}
