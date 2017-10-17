
import java.util.ArrayList;
public class GameObject {
    public  ArrayList<GameObject> all_object = new ArrayList<GameObject>();
    public static final GameObject ROOT = new GameObject(null);
    private GameObject parent;
    private ArrayList<GameObject> children = new ArrayList<GameObject>();
    // constructor
    public GameObject (GameObject parent){
        this.parent = parent;
        if (parent != null){
            this.parent.children.add(this);
        }
        this.children = new ArrayList<GameObject>();
        all_object.add(this);

    }
    // some helper?
    public Coord getRotation(){
        // need type and implementation
        return null;
    }

    public Coord getPosition(){
        // need type and implementation
        return null;
    }

    public Coord getScale(){
        // need type and implementation
        return null;
    }
}
