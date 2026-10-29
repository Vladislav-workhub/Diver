package ru.diver.screen;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class MainMenuScreen extends StackPane{
    public MainMenuScreen(Runnable onPlay){
        Label title = new Label("DIVER");
        title.setStyle(
                "-fx-font-size: 48px; -fx-text-fill: white;"
        );

        Button playButton = createButton("ИГРАТЬ");
        Button recordsButton = createButton("РЕКОРДЫ");
        Button creditsButton = createButton("СОЗДАТЕЛИ");
        Button exitButton = createButton("ВЫХОД");

        playButton.setOnAction(event -> onPlay.run());

        exitButton.setOnAction(event -> Platform.exit());
        VBox menu = new VBox(
                16,
                title,
                playButton,
                recordsButton,
                creditsButton,
                exitButton
        );

        menu.setAlignment(Pos.CENTER);
        getChildren().add(menu);

        setStyle("-fx-background-color:#0c2438;");
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setPrefWidth(240);
        return button;
    }
}
