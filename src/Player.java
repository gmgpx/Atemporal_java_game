import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe Player, esta classe é responsável por configurar os atributor e métodos do jogador
    altura, largura, velocidade, imagem, movimentação e colisão com os limites do mapa.

*/
public class Player extends ImageView {
    private final double PLAYER_HEIGHT = 74.0;
    private final double PLAYER_WIDTH = 55.0;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;
    private double speed = 5;
    private MapObstacles[] obstacles;
    private ImageView imageView;
    private Image[] runRight;
    private Image[] runLeft;
    private Image[] knatk;
    private Image [] kndead;
    private Image idle;
    private int indexRun = 0;
    private int indexAtk = 0;
    private int indexDead = 0;
    private Timeline runR;
    private Timeline runL;
    private Timeline atk;
    private Timeline kndead_animation;

    public Player(double startX, double startY) {
        idle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/idle.png")));
        imageView = new ImageView(idle);
        imageView.setX(startX);
        imageView.setY(startY);

        loadImages();
        setupAnimations();
        setupObstacles();
    }

    //Método para inicializar as imagens
    private void loadImages() {
        runRight = new Image[7];
        runLeft = new Image[7];
        knatk = new Image[5];
        kndead = new Image[6];
        for (int i = 0; i < 6; i++) {
            kndead[i] = new Image("res/kndead" + i + ".png");
        }
        for (int i = 0; i < 5; i++) {
            knatk[i] = new Image("res/knatk" + i + ".png");
        }
        for (int i = 0; i < runRight.length; i++) {
            runRight[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/runRight" + i + ".png")));
            runLeft[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/runLeft" + i + ".png")));
        }

    }

    private void setupAnimations() {
        runR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % runRight.length;
            imageView.setImage(runRight[indexRun]);
        }));
        runR.setCycleCount(Timeline.INDEFINITE);

        runL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % runLeft.length;
            imageView.setImage(runLeft[indexRun]);
        }));
        runL.setCycleCount(Timeline.INDEFINITE);

        atk = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            indexAtk = (indexAtk + 1) % knatk.length;
            imageView.setImage(knatk[indexAtk]);
        }));
        // cada animação executada incrementa em uma unidade a váriavel 'indexAtk',
        // se usarmos como parâmetro o comprimento do vetor de imagens para animação
        // teremos percorrido o array inteiro a cada chamada do método na Classe GameLoop
        atk.setCycleCount(knatk.length);
        kndead_animation = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            indexDead = (indexDead + 1) % kndead.length;
            imageView.setImage(kndead[indexDead]);
        }));
        kndead_animation.setCycleCount(kndead.length);
    }

    public ImageView getImageView() {return imageView;}

    public void setupObstacles(){
        // Posicionamento dos obstáculos do mapa em uma camada acima a do player
        obstacles = new MapObstacles[4];
        // Imagem da pedra em uma camada acima do player
        obstacles[0] = new MapObstacles(216,416,0);
        // Árvore
        obstacles[1] = new MapObstacles(349,361,1);
        // Altar
        obstacles[2] = new MapObstacles(105,246,2);
        // Coluna
        obstacles[3] = new MapObstacles(483,520,3);
    }

    // Verificações para q0,q1,q2:

    // Verificação para q0 - pedra
    public boolean player_rock_collision_q0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 216 &&
                imageView.getX() + PLAYER_WIDTH/2 < 216+55 &&
                imageView.getY() + PLAYER_HEIGHT < 510 &&
                imageView.getY() + PLAYER_HEIGHT > 500;
    }

    // Verificar para q1 - pedra
    public boolean player_rock_collision_q1(){
        return imageView.getX() + PLAYER_WIDTH > 216 &&
                    imageView.getX() + PLAYER_WIDTH < 216+55 &&
                    imageView.getY() + PLAYER_HEIGHT < 510 &&
                    imageView.getY() + PLAYER_HEIGHT > 500;

    }

    // Verificar para q2 - pedra
    public boolean player_rock_collision_q2(){
        return imageView.getX()  > 216 &&
                imageView.getX()  < 216+55 &&
                imageView.getY() + PLAYER_HEIGHT < 510 &&
                imageView.getY() + PLAYER_HEIGHT > 500;
    }

    // Verificar para q0 - altar
    public boolean player_alt_collision_q0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 105 &&
                imageView.getX() + PLAYER_WIDTH/2 < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT < 385 &&
                imageView.getY() + PLAYER_HEIGHT > 375;
    }

    // Verificar para q1 - altar
    public boolean player_alt_collision_q1(){
        return imageView.getX() + PLAYER_WIDTH > 105 &&
                imageView.getX() + PLAYER_WIDTH < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT < 385 &&
                imageView.getY() + PLAYER_HEIGHT > 375;

    }

    // Verificar para q2 - altar
    public boolean player_alt_collision_q2(){
        return imageView.getX() > 105 &&
                imageView.getX() < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT > 385 &&
                imageView.getY() + PLAYER_HEIGHT < 375;
    }




    // Verificações para p0,p1,p2:

    // Verificação para p0 - pedra
    public boolean player_rock_collision_p0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 216 &&
                imageView.getX() + PLAYER_WIDTH/2 < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p1 - pedra
    public boolean player_rock_collision_p1(){
        return imageView.getX() + PLAYER_WIDTH > 216 &&
                imageView.getX() + PLAYER_WIDTH < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p2 - pedra
    public boolean player_rock_collision_p2(){
        return imageView.getX() > 216 &&
                imageView.getX() < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p0 - altar
    public boolean player_alt_collision_p0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 105 &&
                imageView.getX() + PLAYER_WIDTH/2 < 105+62 &&
                imageView.getY() < 385 &&
                imageView.getY() > 375;
    }

    // Verificar para p1 - altar
    public boolean player_alt_collision_p1(){
        return imageView.getX() + PLAYER_WIDTH > 105 &&
                imageView.getX() + PLAYER_WIDTH < 105+62 &&
                imageView.getY() < 385 &&
                imageView.getY() > 375;
    }

    // Verificar para p2 - altar
    public boolean player_alt_collision_p2(){
        return imageView.getX() > 105 &&
                imageView.getX() < 105+62 &&
                imageView.getY() > 385 &&
                imageView.getY() < 375;
    }


    // Colisão com limites do mapa

    public boolean player_collisionXleft(){
        return imageView.getX() <= 0;
    }
    public boolean player_collisionXright(){
        return imageView.getX() + PLAYER_WIDTH >= WIDTH;
    }
    public boolean player_collisionYup(){
        return imageView.getY() <= 0;
    }
    public boolean player_collisionYdown(){
        return imageView.getY() + PLAYER_HEIGHT >= HEIGHT;
    }

    public boolean player_enemy_collision(Enemy enemy){
        return imageView.getX() + PLAYER_WIDTH == enemy.getX();
    }

    // Métodos para movimentação do player baseado na atualização da posição atual + velocidade
    public void moveLeft() {
        imageView.setX(imageView.getX() - speed);
        System.out.println("P0(x) position: " + imageView.getX());
        runL.play();
    }

    public void moveRight() {
        imageView.setX(imageView.getX() + speed);
        System.out.println("P0(x) position: " + imageView.getX());
        runR.play();
    }

    public void moveUp() {
        imageView.setY(imageView.getY() - speed);
        System.out.println("P0(y) position: " + imageView.getY());
        runR.play();
    }

    public void moveDown() {
        imageView.setY(imageView.getY() + speed);
        System.out.println("P0(y) position: " + imageView.getY());
        runR.play();
    }

    public void stopAnimation() {
        runR.stop();
        runL.stop();
        imageView.setImage(idle);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWidth() {
        return PLAYER_WIDTH;
    }

    public double getHeight() {
        return PLAYER_HEIGHT;
    }

    public Timeline getAtk() {
        return atk;
    }
    public Timeline getDead(){return kndead_animation;}

    public void setX(int x){
        imageView.setX(x);
    }
    public void setY(int y){
        imageView.setY(y);
    }

}
