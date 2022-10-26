package com.example.bombermanfxgl.component.enemy;


import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.BombermanType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class OctopusComponent extends EnemyComponent {
    public OctopusComponent(int code) {
        super(code);
        animationChannel = new AnimationChannel(image("enemy/octopusSprite.png"),
                2, 37, 40, Duration.seconds(1), 0, 1);
    }

    @Override
    public void onAdded() {
        setSpeed(2);
        state=random(1,4);
        aStarMoveComponent = this.entity.getComponent(AStarMoveComponent.class);

        animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        entity.getViewComponent().addChild(animatedTexture);
        this.entity.getComponent(CellMoveComponent.class).setSpeed(10 * speed);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDie()) {
            if (animatedTexture.getAnimationChannel() == animationChannel) {
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
        listState=checkMove();
        if(listState.size()!=0) {
            state=listState.get(random(0,listState.size()-1));
        }
        switch (state) {
            case 1:
                aStarMoveComponent.moveToUpCell();
                if(aStarMoveComponent.isAtDestination()) {
                    changeState();
                }
                break;
            case 2:
                aStarMoveComponent.moveToDownCell();
                if(aStarMoveComponent.isAtDestination()) {
                    changeState();
                }
                break;
            case 3:
                aStarMoveComponent.moveToRightCell();
                if(aStarMoveComponent.isAtDestination()) {
                    changeState();
                }
                break;
            case 4:
                aStarMoveComponent.moveToLeftCell();
                if(aStarMoveComponent.isAtDestination()) {
                    changeState();
                }
                break;
        }
    }

    public List<Integer> checkBrick() {
        List<Integer> list = new Vector<>();
        int cellX=entity.getComponent(CellMoveComponent.class).getCellX();
        int cellY=entity.getComponent(CellMoveComponent.class).getCellY();

//        if (getGameWorld().) {
//            list.add(1);
//        }
        if (entity.getComponent(CellMoveComponent.class).isMovingDown()) {
            list.add(2);
        }
        if (entity.getComponent(CellMoveComponent.class).isMovingRight()) {
            list.add(3);
        }
        if (entity.getComponent(CellMoveComponent.class).isMovingDown()) {
            list.add(4);
        }
        return list;
    }
}
