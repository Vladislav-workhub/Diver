package ru.diver.screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.diver.game.Diver;
import ru.diver.graphics.Ground;

import ru.diver.graphics.FarBackground;

import javafx.animation.AnimationTimer;

import java.awt.*;
import java.net.URL;

public class GameScreen extends StackPane {

    private static final long FRAME_DURATION_NANOS = 100_000_000L;
    private final Canvas canvas = new Canvas();

    private final FarBackground farBackground = new FarBackground(
            "/images/diver/idle/backgrounds/far_background.png"
    );

    private final Ground ground = new Ground(
            "/images/diver/idle/backgrounds/ground.png"
    );

    private final double groundSurfaceY;

    private final Image[] idleFrame = new Image[10];
    private final Image[] swimmingFrame = new Image[8];
    private Image[] currentFrames = idleFrame;
    private final AnimationTimer animationTimer;

    private int currentFrameIndex = 0;
    private final Diver diver = new Diver();
    private boolean spacePressed;

    public GameScreen(Runnable onBack) {
        for (int i = 0; i < idleFrame.length; i++) {
            String path = "/images/diver/idle/"
                    + "__diver_walk_%03d.png".formatted(i);

            URL imageUrl = getClass().getResource(path);

            if (imageUrl == null) {
                throw new IllegalStateException(
                        "Не найден ресурс: " + path
                );
            }

            idleFrame[i] = new Image(imageUrl.toExternalForm());
        }

        for (int i = 0; i < swimmingFrame.length; i++) {
            String path = "/images/diver/idle/swim/"
                    + "__diver_swimming_%03d.png".formatted(i);

            URL imageUrl = getClass().getResource(path);

            if (imageUrl == null) {
                throw new IllegalStateException(
                        "Не найден ресурс: " + path
                );
            }

            swimmingFrame[i] = new Image(imageUrl.toExternalForm());
        }

        double feetRatio = findVisibleBottom(idleFrame[0])
                /idleFrame[0].getHeight();

        groundSurfaceY = diver.getGroundY()
                - Diver.HEIGHT
                + Diver.HEIGHT * feetRatio;

        canvas.setManaged(false);

        Button backButton = new Button("Назад в меню");
        backButton.setPrefWidth(180);
        backButton.setFocusTraversable(false);
        backButton.setOnAction(event -> onBack.run());

        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(16));

        getChildren().addAll(canvas, backButton);

        configureInput();

        animationTimer = new AnimationTimer() {

            private long startTime;
            private long previousTime;
            private boolean initialized;

            @Override
            public void handle(long now) {
                if (!initialized) {
                    startTime = now;
                    previousTime = now;
                    initialized = true;
                }

                double deltaTime =
                        (now - previousTime) / 1_000_000_000.0;

                previousTime = now;

                deltaTime = Math.min(deltaTime, 0.05);
                //farBackground.update(deltaTime);
                ground.update(deltaTime);
                diver.update(deltaTime);

                Image[] nextFrames;

                if (diver.isOnGround()) {
                    nextFrames = idleFrame;
                } else {
                    nextFrames = swimmingFrame;
                }

                if (currentFrames != nextFrames) {
                    currentFrames = nextFrames;
                    startTime = now;
                }

                long framesPaseed =
                        (now - startTime) / FRAME_DURATION_NANOS;

                currentFrameIndex =
                        (int) (framesPaseed % currentFrames.length);

                draw();
            }
        };


        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                animationTimer.start();
                Platform.runLater(this::requestFocus);
            } else {
                animationTimer.stop();
                spacePressed = false;
            }
        });
    }

    private void configureInput() {
        setFocusTraversable(true);

        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (!spacePressed) {
                    spacePressed = true;
                    diver.swim();
                }

                event.consume();
            }
        });
        addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                spacePressed = false;
                event.consume();
            }
        });

        focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (!focused) {
                spacePressed = false;
            }
        });
    }

    private int findVisibleBottom(Image image) {
        for (int y = (int) image.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < (int) image.getWidth(); x++) {
                double opacity = image.getPixelReader()
                        .getColor(x, y)
                        .getOpacity();

                if (opacity > 0.1) {
                    return y+1;
                }
            }
        }
        throw new IllegalStateException(
                "Первый кадр не видно"
        );
    }


    @Override
    protected void layoutChildren(){
        super.layoutChildren();

        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        canvas.relocate(0,0);

        draw();
    }


    private void draw() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        if (width <= 0 || height <= 0){
            return;
        }

        Image diverImage = currentFrames[currentFrameIndex];

        GraphicsContext graphics = canvas.getGraphicsContext2D();

        farBackground.draw(graphics, width, height);

        double scale = height / Diver.WORLD_HEIGHT;

        ground.draw(
                graphics,
                width,
                height,
                groundSurfaceY *scale,
                scale
        );



        double diverHeight = Diver.HEIGHT * scale;
        double diverWidth = diverHeight
                * diverImage.getWidth()
                / diverImage.getHeight();

        double diverX = width * 0.38 - diverWidth/2;
        double diverY = diver.getY() * scale;

        graphics.drawImage(
                diverImage,
                diverX,
                diverY,
                diverWidth,
                diverHeight
        );
    }
}