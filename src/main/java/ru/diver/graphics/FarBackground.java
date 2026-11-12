package ru.diver.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URL;

public class FarBackground {

    private final Image image;

    public FarBackground (String resourcePath) {
        URL imageUrl = getClass().getResource(resourcePath);

        if (imageUrl == null) {
            throw new IllegalStateException(
                    "Не найден ресурс: " + resourcePath
            );
        }

        image = new Image(imageUrl.toExternalForm());

        if(image.isError()){
            throw new IllegalStateException(
                    "Не удалось загрузить изображение: " + resourcePath, image.getException()
            );
        }
    }

    public void draw(GraphicsContext graphics,
                     double width,
                     double height)
    {
        if (width <= 0 || height <=0) {
            return;
        }

        double scale = Math.max (
                width / image.getWidth(),
                height / image.getHeight()
        );

        double drawingWidth = image.getWidth() * scale;
        double drawingHeight = image.getHeight() * scale;

        double x = (width - drawingWidth) / 2;

        graphics.drawImage(
                image,
                x,
                0,
                drawingWidth,
                drawingHeight
        );
    }
}