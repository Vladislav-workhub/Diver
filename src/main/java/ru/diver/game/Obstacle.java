package ru.diver.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint. Color;

import javafx.geometry.Rectangle2D;

public class Obstacle {

    private static final double WIDTH = 100.0;
    private static final double SPEED = 80.0;

    private double x;

    private final double passageTop;
    private final double passageBottom;

    public Obstacle(
            double x,
            double passageTop,
            double passageBottom
    ){
        this.x = x;
        this.passageTop = passageTop;
        this.passageBottom = passageBottom;
    }

    public void update (double deltaTime){
        x -= SPEED * deltaTime;
    }

    public void draw (
            GraphicsContext graphics,
            double scale,
            double waterSurfaceY,
            double groundSurfaceY
    ){
        graphics.setFill(Color.DARKSLATEGRAY);

        // Верхняя часть: от поверхности воды до прохода.

        if (passageTop>waterSurfaceY) {
            graphics.fillRect(
                    x * scale,
                    waterSurfaceY* scale,
                    WIDTH * scale,
                    (passageTop - waterSurfaceY) * scale
            );
        }

        graphics.setFill(Color.SLATEGRAY);

        //Нижняя часть: от прохода до дна.

        if (passageBottom < groundSurfaceY) {
            graphics.fillRect(
                    x * scale,
                    passageBottom * scale,
                    WIDTH * scale,
                    (groundSurfaceY - passageBottom) * scale
            );
        }
    }

    public boolean isOutside (){
        return x + WIDTH < 0;
    }

    public boolean intersects (Rectangle2D diverBounds) {
        boolean overlapsHorizontally =
                diverBounds.getMaxX() > x && diverBounds.getMinX() < x + WIDTH;

        boolean outsidePassage =
                diverBounds.getMinY() < passageTop
                        || diverBounds.getMaxY() > passageBottom;

        return overlapsHorizontally && outsidePassage;
    }
}