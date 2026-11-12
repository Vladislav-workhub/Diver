package ru.diver.game;

public class Diver {
    public static final double WORLD_HEIGHT = 720.0; // Расчетная высота игрового мира;
    public static final double HEIGHT = 200.0; // Высота ГГ.

    private static final double GROUND_Y = 670.0; //Уровень дна
    private static final double GRAVITY = 600.0;// Ускорение вниз
    private static final double SWIM_VELOCITY = -350.0;// Вертикальная скорость вверх

    private  double y = GROUND_Y - HEIGHT; // координата верхней границы изображения
    private  double velocityY; // текущая вертикальная скорость

    public void swim () {
        velocityY = SWIM_VELOCITY;
    }

    public void update (double deltaTime) {
        y += velocityY * deltaTime
                + 0.5 * GRAVITY * deltaTime * deltaTime;

        velocityY += GRAVITY * deltaTime;

        // уровень моря 140
        if (y<140){
            y=140;
            velocityY= Math.max(0, velocityY);
        }

        double bottomLimit = GROUND_Y - HEIGHT;

        if (y>=bottomLimit) {
            y=bottomLimit;
            velocityY=0;
        }
    }

    public double getY() {
        return y;
    }

    public double getGroundY() {
        return GROUND_Y;
    }

    public boolean isOnGround(){
        return y >= GROUND_Y - HEIGHT && velocityY >=0;
    }
}