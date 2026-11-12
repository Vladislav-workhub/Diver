package ru.diver.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URL;

public class Ground {

    private static final int SOURCE_TOP = 400;
    private final Image image;

    private static final double SCROLL_SPEED = 80.0;
    private double scrollDistance;

    public Ground (String resourсePath){
        URL imageUrl = getClass().getResource(resourсePath);

        if (imageUrl == null) {
            throw new IllegalStateException(
                    "Не найден ресурс: " + resourсePath
            );
        }
        image = new Image(imageUrl.toExternalForm());

        if (image.isError()){
            throw new IllegalStateException(
                    "Не удалось загрузить изображения: " + resourсePath,
                    image.getException()
            );
        }
    }

    public void update (double deltaTime) {
        scrollDistance += SCROLL_SPEED * deltaTime;
    }

    public void draw(
            GraphicsContext graphics,
            double width,
            double height,
            double surfaceY,
            double scale
    ){
        double drawingHeight = height - surfaceY;

        if (width <=0 || drawingHeight <=0 || scale <=0) {
            return;
        }

        double sourceHeight = image.getHeight() - SOURCE_TOP;

        double tileWidth = image.getWidth()
                * drawingHeight / sourceHeight;

        double offset = (scrollDistance * scale) % tileWidth;

        for (double x = -offset; x < width; x += tileWidth) {
            graphics.drawImage(
                    image,
                    0,
                    SOURCE_TOP,
                    image.getWidth(),
                    sourceHeight,
                    x,
                    surfaceY,
                    tileWidth,
                    drawingHeight
            );
        }
    }
}