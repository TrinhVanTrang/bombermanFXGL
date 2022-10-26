package com.example.bombermanfxgl.component.enemy;

import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class BarrelComponent extends EnemyComponent{
    public BarrelComponent(int code) {
        super(code);
    }

    @Override
    public void onAdded() {
        setSpeed(3);
        aStarMoveComponent = this.entity.getComponent(AStarMoveComponent.class);

        animationChannel = new AnimationChannel(image("enemy/barrelSprite.png"),
                2, 37, 40, Duration.seconds(1), 0, 1);
        animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        entity.getViewComponent().addChild(animatedTexture);

        this.entity.getComponent(CellMoveComponent.class).setSpeed(10*speed);

    }

    @Override
    public void onUpdate(double tpf) {

        if(isDie()) {
            entity.getViewComponent().removeChild(animatedTexture);
            AnimationChannel animationChannel = new AnimationChannel(image("enemy/enemyDie.png"),
                    3, 40, 40, Duration.seconds(0.5), 0, 2);
            AnimatedTexture animated = new AnimatedTexture(animationChannel);
            animatedTexture.loop();
            entity.getViewComponent().addChild(animated);

            aStarMoveComponent.stopMovement();
        } else {
            move();
            isBurn();
            chasePlayer();
        }
    }

    @Override
    public void move() {
        if(distance(player)<=5) {
            speed=6;
            entity.getComponent(CellMoveComponent.class).setSpeed(speed*10);
        } else {
            speed=3;
            entity.getComponent(CellMoveComponent.class).setSpeed(speed*10);
        }
        aStarMoveComponent.moveToRandomCell();
    }
}
