package com.example.bombermanfxgl.component.enemy;

import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.BombermanType;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class BacteriumComponent extends EnemyComponent{
    public BacteriumComponent(int code) {
        super(code);
        animationChannel = new AnimationChannel(image("enemy/bacteriumSprite.png"),
                2, 37, 40, Duration.seconds(1), 0, 1);
    }

    @Override
    public void onAdded() {
        setSpeed(2);
        aStarMoveComponent = this.entity.getComponent(AStarMoveComponent.class);

        animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        entity.getViewComponent().addChild(animatedTexture);
        this.entity.getComponent(CellMoveComponent.class).setSpeed(10*speed);

        aStarMoveComponent.getGrid().getCells()
                .stream()
                .filter(e->!e.isWalkable())
                .forEach(e->{
                    getGameWorld().getEntitiesByType(BombermanType.Block.BRICK)
                            .stream()
                            .filter(i->i.getX()/40==e.getX()&&i.getY()/40==e.getY())
                            .forEach(i->{
                                e.setState(CellState.WALKABLE);
                            });

                });
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDie()) {
            if(animatedTexture.getAnimationChannel()==animationChannel) {
                animatedTexture.loopAnimationChannel(animationDie);
            }

            aStarMoveComponent.stopMovement();
        } else {
            move();
            chasePlayer();
            isBurn();
        }
    }

    @Override
    public void move() {

        aStarMoveComponent.moveToRandomCell();

    }
}
