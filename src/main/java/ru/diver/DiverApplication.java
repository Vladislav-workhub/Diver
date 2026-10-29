package ru.diver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.diver.screen.GameScreen;
import ru.diver.screen.MainMenuScreen;

public class DiverApplication extends Application{

    private Scene scene;


    @Override
    public void start(Stage stage){

        scene = new Scene(new StackPane(), 1280, 720);

        showMainMenu();

        stage.setTitle("Diver");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.setResizable(true);

        stage.show();
    }

    private void showMainMenu(){
        MainMenuScreen mainMenu = new MainMenuScreen(this::showGame);
        scene.setRoot(mainMenu);
    }
    private void showGame (){
        GameScreen gameScreen = new GameScreen(this::showMainMenu);
        scene.setRoot(gameScreen);
    }
}


