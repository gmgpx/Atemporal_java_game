import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

/*

  Classe GameInit possui a tela de início
  e também é responsável por iniciar o fluxo de telas.

 */
public class GameInit extends Application {

    private Stage primaryStage;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Atemporal");

        // Logo de Atemporal
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(300); // Ajuste de largura da logo
        logoView.setPreserveRatio(true); // Mantém a proporção da imagem

        // Configurar a animação de transição para movimentar o logo
        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), logoView);
        transition.setToY(-20); // Mover para cima 20 pixels
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Repetir infinitamente
        transition.setAutoReverse(true); // Alternar a direção

        // Iniciar a animação
        transition.play();

        // Botão Iniciar Jogo
        Button startButton = new Button("Iniciar Jogo");
        startButton.getStyleClass().add("menu-button"); // Recebe a classe css do PersonSelect-button
        startButton.setOnAction(e -> showCharacterSelection());

        // Botão Configurações
        Button settingsButton = new Button("Opções");
        settingsButton.getStyleClass().add("menu-button");
        settingsButton.setOnAction(e -> openSettings());

        // Botão Sair
        Button exitButton = new Button("Sair");
        exitButton.getStyleClass().add("menu-button");
        exitButton.setOnAction(e -> primaryStage.close());

        // Layout do PersonSelect
        VBox PersonSelectLayout = new VBox(20);
        PersonSelectLayout.getChildren().addAll(logoView, startButton, settingsButton, exitButton); // posição dos botões
        PersonSelectLayout.setAlignment(Pos.CENTER);
        PersonSelectLayout.getStyleClass().add("PersonSelect-layout"); // recebe o estilo css do PersonSelect-layout
        PersonSelectLayout.setStyle("-fx-background-color: black;"); // Define fundo preto

        // Cena principal
        Scene mainScene = new Scene(PersonSelectLayout, WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.getStylesheets().add("styleMain.css"); // arquivo css com as estilizações
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void showCharacterSelection() {
        // Knight
        VBox knightBox = createCharacterBox("res/idle.png", "Knight");

        // Soldier
        VBox soldierBox = createCharacterBox("res/soldado.png", "Soldier");

        // Robot
        VBox robotBox = createCharacterBox("res/robot.png", "Robot");

        // Layout de seleção de personagem (Knight, Soldier e Robot)
        HBox characterSelectionLayout = new HBox(25);
        characterSelectionLayout.getChildren().addAll(knightBox, soldierBox, robotBox);
        characterSelectionLayout.setAlignment(Pos.CENTER);
        characterSelectionLayout.getStyleClass().add("character-selection-layout");
        characterSelectionLayout.setStyle("-fx-background-color: black;"); // Define fundo preto

        // Cena de seleção de personagem
        Scene selectCharacterScene = new Scene(characterSelectionLayout, WIDTH, HEIGHT);
        selectCharacterScene.getStylesheets().add("stylePersonSelect.css"); // Recebe o CSS para a cena de seleção
        primaryStage.setScene(selectCharacterScene);
    }

    private VBox createCharacterBox(String imagePath, String characterName) {
        Button characterButton = new Button(characterName);
        characterButton.getStyleClass().add("character-button");
        characterButton.setOnAction(e -> selectCharacter(characterName));

        ImageView characterImageView = new ImageView(new Image(imagePath));
        characterImageView.setFitHeight(200);
        characterImageView.setPreserveRatio(true);

        VBox characterBox = new VBox(10);
        characterBox.setAlignment(Pos.CENTER);
        characterBox.getChildren().addAll(characterButton, characterImageView);
        characterBox.getStyleClass().add("character-box");
        return characterBox;
    }

    private void selectCharacter(String characterName) {
        System.out.println(characterName + " selected!");
        ControlScreen controlScreen = new ControlScreen();
        controlScreen.start(primaryStage);
    }


    private void openSettings() {
        System.out.println("Abrindo configurações");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
