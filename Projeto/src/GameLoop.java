import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import java.util.Random;

/*

    Classe GameLoop é responsável por gerenciar os eventos do jogo
    além de possuir os algoritmos de combate, verificações de colisão e movimentação do player.

 */
public class GameLoop {

    private Player player;
    private Pane pane;
    private Enemy[] enemies = new Enemy[3];
    private Boss boss;
    private GameEnvironment gameE = new GameEnvironment();
    private SoundsFX sound = new SoundsFX();
    private AnimationTimer animationTimer;
    private MapObstacles[] obstacles;
    private Random Xr = new Random();
    private Random Yr  = new Random();
    private Random rr = new Random();
    private boolean flag_enemy = true;
    private int enemylife = 5;
    private int playerlife = 250;
    private int flag_cycle = 0;
    private int kill = 0;

    public GameLoop(Scene scene, Player player, Pane pane, Stage primaryStage) {
        this.player = player;
        this.pane = pane;
        setupKeyHandlers(scene, primaryStage);
        setupAnimationTimer(primaryStage);
        setObstacles();
    }

    // Método responsável por atualizar os eventos do jogo continuamente
    private void setupAnimationTimer(Stage primaryStage) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (flag_enemy && kill < 3) {
                    for (Enemy enemy : enemies) {
                        if (enemy != null && enemy.isVisible()) {
                            enemy.followPlayer(player);
                            if (enemy.enemy_collision_hit(player)) {
                                enemy.getskatkT().play();
                                playerlife--;
                                System.out.println("Player hp: " + playerlife);
                            } else {
                                enemy.getskatkT().stop();
                            }

                            if (playerlife == 0){
                                enemy.getskatkT().stop();
                                player.getDead().play();
                                enemy.getskatkT().stop();
//                                pane.getChildren().remove(player);
//                                player.getImageView().setVisible(false);
                                System.out.println("Game Over");
                                kill = 0;
                                GameOverScreen gameover = new GameOverScreen();
                                gameover.start(primaryStage);
                                animationTimer.stop();
                            }
                        }
                    }
                }
            }
        };
    }

    // Método usado para instânciar inimigos na tela
    private void spawnEnemy() {
        if (kill < 3) {
            // inimigos podem aparecer em locais aleatórios do mapa
            // Xr.nextInt(gameE.getSceneWidth()) = [0,1000[
            // Yr.nextInt(gameE.getSceneHeight()) = [0,600[
            enemies[kill] = new Enemy(Xr.nextInt(gameE.getSceneWidth()) + 50, Yr.nextInt(gameE.getSceneHeight()) + 50);
            pane.getChildren().add(enemies[kill]);
        }
    }

    private void spawnBoss(){
        boss = new Boss(500,270);
        pane.getChildren().add(boss);
    }

    private void setObstacles(){
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

        for (int i = 0; i < 4; i++) {
            pane.getChildren().add(obstacles[i].getMap_obj(i));
            obstacles[i].toFront();
        }
    }

    private void setupKeyHandlers(Scene scene, Stage primaryStage) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                handleMovement(event.getCode(), primaryStage);
                if (event.getCode() == KeyCode.B) {
                    flag_enemy = true;
                    spawnEnemy();
                    animationTimer.start();
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                player.stopAnimation();
            }
        });
    }

    private void handleMovement(KeyCode code, Stage primaryStage) {
        switch (code) {
            case A:
                player.moveLeft();
                break;
            case W:
                player.moveUp();
                break;
            case S:
                player.moveDown();
                break;
            case D:
                player.moveRight();
                break;
            default:
                break;
        }

        if(code == KeyCode.P){
            PauseScreen pause = new PauseScreen();
            pause.start(primaryStage);
        }else{
            animationTimer.start();
        }
        if (code == KeyCode.SPACE) {
            player.getAtk().play();
            sound.play(); // efeito sonoro de ataque
        } else {
            player.getAtk().stop();
        }

        if (flag_enemy) {
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null && enemies[i].isVisible()) {
                    if (player.player_enemy_collision(enemies[i]) && code == KeyCode.SPACE) {
                        enemies[i].getskatkT().stop();
                        System.out.println("hit");
                        enemylife--;
                        if (enemylife == 0) {
                            kill_enemy(i);
                            kill++;
                            if (kill < 3) {
                                enemylife = 5;
                                spawnEnemy();
                            }
                            System.out.println("matou");
                        }
                    } else {
                        enemies[i].getskatkT().play();
                    }
                }
            }
        }

        // Verificação de colisões com os limites da tela
        if (player.player_collisionXleft()) {
            player.setSpeed(0);
            if (code == KeyCode.D || code == KeyCode.S || code == KeyCode.W)
                player.setSpeed(5);
        }
        if (player.player_collisionXright()) {
            player.setSpeed(0);
            if (code == KeyCode.A || code == KeyCode.W || code == KeyCode.S)
                player.setSpeed(5);
        }
        if (player.player_collisionYdown()) {
            player.setSpeed(0);
            if (code == KeyCode.W || code == KeyCode.A || code == KeyCode.D)
                player.setSpeed(5);
        }
        if (player.player_collisionYup()) {
            player.setSpeed(0);
            if (code == KeyCode.S || code == KeyCode.A || code == KeyCode.D)
                player.setSpeed(5);
        }

        // Verificação de colisões com os cantos da tela
        if(player.player_collisionYup() && player.player_collisionXleft()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYdown() && player.player_collisionXleft()){
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYup() && player.player_collisionXright()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYdown() && player.player_collisionXright()){
            player.setSpeed(0);
            if(code == KeyCode.A || code == KeyCode.W){
                player.setSpeed(5);
            }
        }

        // Verificação de colisões com os obstáculos do mapa

        // p0,p1,p2 - pedra
        if(player.player_rock_collision_p0()||player.player_rock_collision_p1()||player.player_rock_collision_p2()){
            System.out.println("COLLISION UP");
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // q0,q1,q2 - pedra
        if(player.player_rock_collision_q0() || player.player_rock_collision_q1() || player.player_rock_collision_q2()){
            System.out.println("COLLISION DOWN");
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // p0,p1,p2 - altar
        if(player.player_alt_collision_p0() || player.player_alt_collision_p1() || player.player_alt_collision_p2()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // q0,q1,q2 - altar
        if(player.player_alt_collision_q0() || player.player_alt_collision_q1() || player.player_alt_collision_q2()){
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }

        // Inicia o boss quando o player eliminar todos os inimigos do mapa
        if(kill == 3 && flag_cycle == 0){
            flag_cycle++;
            spawnBoss();
            boss.movement(player);
            if(boss.boss_ready_to_atk(player)){
                boss.bossatk();
            }

        }
    }


    private void kill_enemy(int index) {
        if (index >= 0 && index < enemies.length && enemies[index] != null) {
            enemies[index].getskatkT().stop();
            pane.getChildren().remove(enemies[index]);
            enemies[index] = null;
            System.out.println("Inimigo eliminado e removido do pane.");
        }
    }

    public void start() {
    }
}
