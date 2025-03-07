import javafx.scene.Node;
import javafx.scene.image.ImageView;

/*

    Classe MapObstacles é responsável por configurar os obstáculos no mapa.

 */
public class MapObstacles extends ImageView {
    private ImageView[] map_obj;


    public MapObstacles(double x, double y, int n) {
        load_map_images();
        map_obj[n].setX(x);
        map_obj[n].setY(y);
    }

    public MapObstacles(){
        load_map_images();
    }

    private void load_map_images(){
        //Obstáculos
        map_obj = new ImageView[4];
        //Pedra
        map_obj[0] = new ImageView("res/maprock.png");
        //Árvore
        map_obj[1] = new ImageView("res/maptree.png");
        //Altar
        map_obj[2] = new ImageView("res/mapalt.png");
        //Coluna
        map_obj[3] = new ImageView("res/mapcolum.png");

    }

    public double gettX(int n){
        return map_obj[n].getX();
    }

    public double gettY(int n){
        return map_obj[n].getY();
    }

    public double gettFitWidth(int n){
        return map_obj[n].getFitWidth();
    }

    public double gettFitHeight(int n){
        return map_obj[n].getFitHeight();
    }

    public void setX(int x, int n){
        map_obj[n].setX(x);
    }

    public Node getMap_obj(int n) {
        return map_obj[n];
    }


    public void setMap_obj(ImageView[] map_obj) {
        this.map_obj = map_obj;
    }
}
