package ru.diver.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class RecordsScreen extends StackPane {

    public RecordsScreen (Runnable onBack) {
        Label title = new Label("Рекорды");
        title.setStyle (
                "-fx-font-size: 36px; -fx-text-fill: white;"
        );

        Label message = new Label(
                "Рекордов пока нет"
        );

        message.setStyle(
                "-fx-font-size: 20px; -fx-text-fill: white;"
        );

        Button backButton = new Button("Назад в меню");
        backButton.setPrefWidth(240);
        backButton.setOnAction(event -> onBack.run());

        VBox content = new VBox(
                20,
                title,
                message,
                backButton
        );

        content.setAlignment(Pos.CENTER);
        getChildren().add(content);

        setStyle("-fx-background-color: #0c2438;");
    }
}