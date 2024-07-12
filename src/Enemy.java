import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe inimigo, usada para instânciar inimigos no mapa
    esta classe possui como atributo uma imagem que será usada para representar os inimigo no mapa

 */
public class Enemy extends ImageView {

    private Image skIdle;
    private Image[] skrunRight;
    private Image[] skrunLeft;
    private Image[] skatk;
    private int indexRun = 0;
    private int indexAtk = 0;
    private Timeline runR;
    private Timeline runL;
    private Timeline skatkT;
    private MapObstacles obstacles = new MapObstacles();

    public Enemy(double x, double y) {
        skIdle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/enemySkeleton.png")));
        setImage(skIdle);
        setX(x);
        setY(y);
        loadImages();
        enemy_timelines();
        obstacles.toFront();
    }

    private void loadImages() {
        skrunRight = new Image[8];
        skrunLeft = new Image[8];
        skatk = new Image[4];
        skatk[0] = new Image("/res/skatk0.png");
        skatk[1] = new Image("/res/skatk1.png");
        skatk[2] = new Image("/res/skatk2.png");
        skatk[3] = new Image("/res/skatk3.png");

        for (int i = 0; i < skrunRight.length; i++) {
            skrunRight[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/skrunRight" + i + ".png")));
            skrunLeft[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/skrunLeft" + i + ".png")));
        }
    }

    private void enemy_timelines() {
        runR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % skrunRight.length;
            setImage(skrunRight[indexRun]);
        }));
        runR.setCycleCount(Timeline.INDEFINITE);

        runL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % skrunLeft.length;
            setImage(skrunLeft[indexRun]);
        }));
        runL.setCycleCount(Timeline.INDEFINITE);

        skatkT = new Timeline(new KeyFrame(Duration.millis(500), e-> {
            indexAtk = (indexAtk + 1) % skatk.length;
            setImage(skatk[indexAtk]);
        }));
        skatkT.setCycleCount(skatk.length);

    }

    public void followPlayer(Player player) {

        // Posição do player
        double playerX = player.getImageView().getX() + player.getWidth(); //este é o p1
//        double playerX_new = player.getImageView().getX(); //este é o p0
        double playerY = player.getImageView().getY();

        // Distância entre o inimigo e o player
        double deltaX = playerX - getX();
        double deltaY = playerY - getY();

        // Verificaar a maior distância entre o Player e o inimigo
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                setX(getX() + 1); // movimenta o inimigo
                runR.play();
                runL.stop();
            } else {
                setX(getX() - 1);
                runL.play();
                runR.stop();
            }
        } else {
            if (deltaY > 0) {
                setY(getY() + 1);
                runR.play();
                runL.stop();
            } else {
                setY(getY() - 1);
                runL.play();
                runR.stop();
            }
        }
    }

    // Colisão Player | Enemy
    public boolean enemy_collision_hit(Player player){
        if(getX() == (player.getImageView().getX() + player.getWidth())){
            runR.stop();
            runL.stop();
            return true;
        }
        return false;
    }

    public Timeline getskatkT() {
        return skatkT;
    }

    public void setskatkT(Timeline skatkT) {
        this.skatkT = skatkT;
    }

}
