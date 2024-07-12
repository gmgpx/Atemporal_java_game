import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

/*
    Classe Boss possui as configurações especiais do Chefe do jogo.

 */


public class Boss extends Enemy{
    private double life;
//    private Image[] bossgif_normalmode;
    private Image[] boss_atk_ragemode;
    private Image[] boss_idle_ragemode;
    private Timeline atkrage;
    private Timeline idlerage;
    private int indexAtk = 0;
    private int indexIdle = 0;
    public Boss(double x, double y) {
        super(x, y);
        loadimages();
        setupAnimations();
    }

    private void loadimages(){
//        bossgif_normalmode = new Image[3];
//        bossgif_normalmode[0] = new Image("res/bossidle.gif"); //idle
//        bossgif_normalmode[1] = new Image("res/bossatk.gif"); // atk
//        bossgif_normalmode[2] = new Image("res/bossbreath.gif"); // rage transition
        boss_idle_ragemode = new Image[6];
        for (int i = 0; i < 6; i++) {
            boss_idle_ragemode[i] = new Image("res/boss_idle" + i + ".png");
        }
        boss_atk_ragemode = new Image[11];
        for (int i = 0; i < 11; i++) {
            boss_atk_ragemode[i] = new Image("res/atk_rage" + i + ".png");
        }
    }

    private void setupAnimations(){
        atkrage = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexAtk = (indexAtk + 1) % boss_atk_ragemode.length;
            setImage(boss_atk_ragemode[indexAtk]);
        }));
        atkrage.setCycleCount(Timeline.INDEFINITE);

        idlerage = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexIdle = (indexIdle + 1) % boss_idle_ragemode.length;
            setImage(boss_idle_ragemode[indexIdle]);
        }));
        idlerage.setCycleCount(Timeline.INDEFINITE);
    }

    public boolean boss_ready_to_atk(Player player){
        return getX() < player.getImageView().getX() + player.getWidth();
    }

    public void bossatk(){
//        setImage(bossgif_normalmode[1]); //normal mode boss
        idlerage.stop();
        atkrage.play(); // rage mode boss
    }

    public void movement(Player player){
//        setImage(bossgif_normalmode[0]);
        // Posição do player
        double playerX = player.getImageView().getX() + player.getWidth(); //este é o p1
//        double playerX_new = player.getImageView().getX(); //este é o p0
        double playerY = player.getImageView().getY();

        // Distância entre o inimigo e o player
        double deltaX = playerX - getX();
        double deltaY = playerY - getY();

        // Verificar a maior distância entre o Player e o inimigo
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                setX(getX() + 1); // movimenta o inimigo
                idlerage.play();
//                atkrage.play();
            } else {
                setX(getX() - 1);
                idlerage.play();
//                atkrage.play();
            }
        } else {
            if (deltaY > 0) {
                setY(getY() + 1);
                idlerage.play();
//                atkrage.play();
            } else {
                setY(getY() - 1);
                idlerage.play();
//                atkrage.play();
            }
        }
    }

    public double getLife() {
        return life;
    }

    public Timeline getAtkrage() {
        return atkrage;
    }

    public Timeline getIdlerage() {
        return idlerage;
    }
}
