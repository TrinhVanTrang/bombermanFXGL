package com.example.bombermanfxgl.component.enemy;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.BombermanType;
import com.example.bombermanfxgl.component.PlayerComponent;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class CloudComponent extends EnemyComponent{

    public CloudComponent(int code) {
        super(code);
    }

    @Override
    public void onAdded() {
        setSpeed(6);
        aStarMoveComponent = this.entity.getComponent(AStarMoveComponent.class);

        animationChannel = new AnimationChannel(image("enemy/cloudSprite.png"),
                2, 37, 40, Duration.seconds(1), 0, 1);
        animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        entity.getViewComponent().addChild(animatedTexture);

        this.entity.getComponent(CellMoveComponent.class).setSpeed(10*speed);

    }

    @Override
    public void onUpdate(double tpf) {
        if (isDie()) {
            entity.getViewComponent().removeChild(animatedTexture);
            AnimationChannel animationChannel = new AnimationChannel(image("enemy/enemyDie.png"),
                    3, 40, 40, Duration.seconds(0.5), 0, 2);
            AnimatedTexture animated = new AnimatedTexture(animationChannel);
            animatedTexture.loop();
            entity.getViewComponent().addChild(animated);

            aStarMoveComponent.stopMovement();
        } else {
            move();
            chasePlayer();
            isBurn();
        }
    }

    @Override
    public void move() {
        var player = FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
        int x = player.getComponent(PlayerComponent.class).getCellX();
        int y = player.getComponent(PlayerComponent.class).getCellY();

        aStarMoveComponent.moveToCell(x, y);

        if(aStarMoveComponent.isAtDestination()) {
            aStarMoveComponent.moveToRandomCell();
            setSpeed(4);
        } else {
            setSpeed(6);
        }
        entity.getComponent(CellMoveComponent.class).setSpeed(speed*10);
    }
}
