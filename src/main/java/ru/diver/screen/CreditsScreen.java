package ru.diver.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class CreditsScreen extends StackPane {

    public CreditsScreen (Runnable onBack) {
        Label title = new Label("Создатели");
        title.setStyle (
                "-fx-font-size: 36px; -fx-text-fill: white;"
        );

        Label decription = new Label(
                "Diver - учебный проект на Java(JavaFX)"
        );

        decription.setStyle(
                "-fx-font-size: 20px; -fx-text-fill: white;"
        );

        Button backButton = new Button("Назад в меню");
        backButton.setPrefWidth(240);
        backButton.setOnAction(event -> onBack.run());

        VBox content = new VBox(
                20,
                title,
                decription,
                backButton
        );

        content.setAlignment(Pos.CENTER);
        getChildren().add(content);

        setStyle("-fx-background-color: #0c2438;");
    }
}