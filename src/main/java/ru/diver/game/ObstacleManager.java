package ru.diver.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObstacleManager {

    private static final double SPAWN_INTERVAL = 6.0;
    private static final double WATER_SURFACE_Y = 220.0;
    private static final double PASSAGE_HIEGHT = 200.0;

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Random random = new Random();

    private final double groundSurfaceY;
    private double spawnTimer;

    public ObstacleManager (double groundSurfaceY) {
        this.groundSurfaceY = groundSurfaceY;
    }

    public void update (
            double deltaTime,
            double screenWidth,
            double screenHeight
    ){
        if (screenWidth <=0 || screenHeight <=0) {
            return;
        }

        for (Obstacle obstacle : obstacles){
            obstacle.update(deltaTime);
        }

        obstacles.removeIf(Obstacle::isOutside);

        spawnTimer -= deltaTime;

        if (spawnTimer <=0) {
            double scale = screenHeight/Diver.WORLD_HEIGHT;
            double worldWidth = screenWidth / scale;

            createObstacle(worldWidth + 100.0);

            spawnTimer = SPAWN_INTERVAL;

        }
    }

    private void createObstacle (double x){
        int type = random.nextInt(3);

        double passageTop;
        double passageBottom;

        if (type == 0) {
            //Только верхнее препятствие: пройти по дну
            passageTop = groundSurfaceY -PASSAGE_HIEGHT;
            passageBottom = groundSurfaceY;
        } else if (type == 1) {
            //Только скала: проплыть серху
            passageTop = WATER_SURFACE_Y;
            passageBottom = 360.0 + random.nextDouble() + 150.0;
        } else {
            //две части: проплыть между ними
            passageTop = 280.0 + random.nextDouble() * 140.0;
            passageBottom = passageTop + PASSAGE_HIEGHT;
        }

        obstacles.add (
                new Obstacle(x, passageTop, passageBottom)
        );
    }

    public void draw (GraphicsContext graphics, double scale){
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(
                    graphics,
                    scale,
                    WATER_SURFACE_Y,
                    groundSurfaceY
            );
        }
    }

    public boolean hasCollision (Rectangle2D diverBounds) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.intersects(diverBounds)){
                return true;
            }
        }
        return false;
    }
}