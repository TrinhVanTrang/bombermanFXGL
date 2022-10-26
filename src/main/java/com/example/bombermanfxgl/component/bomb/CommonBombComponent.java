package com.example.bombermanfxgl.component.bomb;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.common.CommonFunction;
import com.example.bombermanfxgl.component.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.example.bombermanfxgl.BombermanType.*;

public class CommonBombComponent extends BombComponent {

    public CommonBombComponent(double x, double y, int bombCode, int radius) {
        super(x, y, bombCode, radius);
        this.setTime(3);
        Image image = image("bomb/commonBomb.png");
        animationChannel = new AnimationChannel(image, 3, 30, 40, Duration.seconds(1.5), 0, 2);
        animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(animatedTexture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (CommonFunction.collisitionEntity(this.entity, player)) {

            this.getEntity().getBoundingBoxComponent().clearHitBoxes();
        } else {
            this.getEntity().getBoundingBoxComponent().clearHitBoxes();
            this.getEntity().getBoundingBoxComponent()
                    .addHitBox(new HitBox(new Point2D(5, 5),
                            BoundingShape.box(25, 25)));
        }

//        getGameWorld().getEntitiesAt(this.getEntity().getPosition())
//                .stream()
//                .filter(e->e.isType(FLAME))
//                .forEach(e->{
//                    explode();
//                    player.getComponent(PlayerComponent.class).reuseBomb();
//                });
    }

    @Override
    public void explode() {
        FXGL.play("explore.wav");
        var blocks_brick = bricks.get().getEntitiesCopy();
        var blocks_wall = walls.get().getEntitiesCopy();
        Entity flame0 = FXGL.spawn("Flame", new SpawnData(entity.getX(), entity.getY()).put("state", 3));
        flames.add(flame0);

        //Right
        for (int i = 1; i <= getRadius(); i++) {
            double x = (point.getX() + i) * 40;
            double y = point.getY() * 40;
            Entity flame;
            if (i == getRadius()) {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 6));
            } else {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 5));
            }
            flames.add(flame);
            boolean flag = true;
            for (int j = 0; j < blocks_brick.size(); j++) {
                if (blocks_brick.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    FXGL.<BombermanApp>getAppCast().destroyedBrick(blocks_brick.get(j));
//                    blocks_brick.get(j).removeFromWorld();
                    brickPositions.add(blocks_brick.get(j).getPosition());
                    inc("bricks", -1);
//                    grid.get((int) blocks_brick.get(j).getX() / 40,
//                                    (int) blocks_brick.get(j).getY() / 40)
//                            .setState(CellState.WALKABLE);
                    break;
                }
            }
            for (int j = 0; j < blocks_wall.size(); j++) {
                if (blocks_wall.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    flame.removeFromWorld();
                    flames.remove(flame);
                    break;
                }
            }
            if (flag == false) {
                break;
            }
        }

        //Left
        for (int i = 1; i <= getRadius(); i++) {
            double x = (point.getX() - i) * 40;
            double y = point.getY() * 40;
            Entity flame;
            if (i == getRadius()) {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 4));
            } else {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 5));
            }
            flames.add(flame);
            boolean flag = true;
            for (int j = 0; j < blocks_brick.size(); j++) {
                if (blocks_brick.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    FXGL.<BombermanApp>getAppCast().destroyedBrick(blocks_brick.get(j));
//                    blocks_brick.get(j).removeFromWorld();
                    brickPositions.add(blocks_brick.get(j).getPosition());
                    inc("bricks", -1);
//                    grid.get((int) blocks_brick.get(j).getX() / 40,
//                                    (int) blocks_brick.get(j).getY() / 40)
//                            .setState(CellState.WALKABLE);
                    break;
                }
            }
            for (int j = 0; j < blocks_wall.size(); j++) {
                if (blocks_wall.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    flame.removeFromWorld();
                    flames.remove(flame);
                    break;
                }
            }
            if (flag == false) {
                break;
            }
        }

        //Down
        for (int i = 1; i <= getRadius(); i++) {
            double y = (point.getY() + i) * 40;
            double x = point.getX() * 40;
            Entity flame;
            if (i == getRadius()) {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 2));
            } else {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 1));
            }
            flames.add(flame);
            boolean flag = true;
            for (int j = 0; j < blocks_brick.size(); j++) {
                if (blocks_brick.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    FXGL.<BombermanApp>getAppCast().destroyedBrick(blocks_brick.get(j));
//                    blocks_brick.get(j).removeFromWorld();
                    brickPositions.add(blocks_brick.get(j).getPosition());
                    inc("bricks", -1);
//                    grid.get((int) blocks_brick.get(j).getX() / 40,
//                                    (int) blocks_brick.get(j).getY() / 40)
//                            .setState(CellState.WALKABLE);
                    break;
                }
            }
            for (int j = 0; j < blocks_wall.size(); j++) {
                if (blocks_wall.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    flame.removeFromWorld();
                    flames.remove(flame);
                    break;
                }
            }
            if (flag == false) {
                break;
            }
        }

        //up
        for (int i = 1; i <= getRadius(); i++) {
            double y = (point.getY() - i) * 40;
            double x = point.getX() * 40;
            Entity flame;
            if (i == getRadius()) {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 0));
            } else {
                flame = FXGL.spawn("Flame", new SpawnData(x, y).put("state", 1));
            }
            flames.add(flame);
            boolean flag = true;
            for (int j = 0; j < blocks_brick.size(); j++) {
                if (blocks_brick.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    FXGL.<BombermanApp>getAppCast().destroyedBrick(blocks_brick.get(j));
//                    blocks_brick.get(j).removeFromWorld();
                    brickPositions.add(blocks_brick.get(j).getPosition());
                    inc("bricks", -1);
                    break;
                }
            }
            for (int j = 0; j < blocks_wall.size(); j++) {
                if (blocks_wall.get(j).getBoundingBoxComponent().isCollidingWith(flame.getBoundingBoxComponent())) {
                    flag = false;
                    flame.removeFromWorld();
                    flames.remove(flame);
                    break;
                }
            }
            if (flag == false) {
                break;
            }
        }

        getGameTimer().runOnceAfter(() -> {
            for (Entity e : flames) {
                grid.get((int) e.getX()/40,(int) e.getY()/40).setState(CellState.WALKABLE);
                e.getBoundingBoxComponent().clearHitBoxes();
                e.removeFromWorld();
                //FXGL.<BombermanApp>getAppCast().spawnItem(e);
            }
            for (Point2D point : brickPositions) {
                FXGL.<BombermanApp>getAppCast().spawnItem(point);
                //grid.get((int) point.getX()/40,(int) point.getY()/40).setState(CellState.WALKABLE);
            }
        }, Duration.seconds(0.5));
//        System.out.println(grid.getWalkableCells().size()+" "+(15*15-grid.getWalkableCells().size()));
//        System.out.println("---->"+getGameWorld().getEntitiesByType(Block.BRICK).size()
//                +" "+getGameWorld().getEntitiesByType(Block.WALL).size());
    }
}
