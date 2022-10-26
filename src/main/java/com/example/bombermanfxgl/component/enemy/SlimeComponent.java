package com.example.bombermanfxgl.component.enemy;


import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class SlimeComponent extends EnemyComponent {
    public SlimeComponent(int code) {
        super(code);
    }

    @Override
    public void onAdded() {
        state=random(1,4);
        speed=4;
        aStarMoveComponent = this.entity.getComponent(AStarMoveComponent.class);

        animationChannel = new AnimationChannel(image("enemy/slimeSprite.png"),
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
        listState=checkMove();
        if(listState.size()!=0&&listState.size()!=2) {
            state=listState.get(random(0,listState.size()-1));
            changeState();
        }
        switch (state) {
            case 1:
                aStarMoveComponent.moveToUpCell();
                if(aStarMoveComponent.isAtDestination()||alert()) {
                    changeState();
                }
                break;
            case 2:
                aStarMoveComponent.moveToDownCell();
                if(aStarMoveComponent.isAtDestination()||alert()) {
                    changeState();
                }
                break;
            case 3:
                aStarMoveComponent.moveToRightCell();
                if(aStarMoveComponent.isAtDestination()||alert()) {
                    changeState();
                }
                break;
            case 4:
                aStarMoveComponent.moveToLeftCell();
                if(aStarMoveComponent.isAtDestination()||alert()) {
                    changeState();
                }
                break;
        }
    }

    public int vectorState() {
        int x0= (int) (entity.getX()/40);
        int y0= (int) (entity.getY()/40);

        int x_player=(int) ((player.getX()+7)/40);
        int y_player=(int) ((player.getY()+17)/40);

        if(x0==x_player) {
            if (y0-y_player>0) {
                return 1;
            } else if(y0-y_player<0) {
                return 2;
            }
        }

        if(y0==y_player) {
            if (x0-x_player>0) {
                return 4;
            } else if(x0-x_player<0) {
                return 3;
            }
        }
        return -1;
    }

    public boolean alert() {
        int x0= (int) (entity.getX()/40);
        int y0= (int) (entity.getY()/40);

        int x_player=(int) ((player.getX()+7)/40);
        int y_player=(int) ((player.getY()+17)/40);

        int alertState=vectorState();
        if(alertState<0) {
            return false;
        }

        if(alertState==state&&(state==3||state==4)) {
            boolean flag=true;
            for (int i=Math.min(x0,x_player)+1;i<Math.max(x0,x_player);i++) {
                if(!grid.get(i,y0).isWalkable()){
                    flag=false;
                    break;
                }
            }
            if(flag) {
                return true;
            }
        }
        if(alertState==state&&(state==1||state==2)) {
            boolean flag=true;
            for (int i=Math.min(y0,y_player)+1;i<Math.max(y0,y_player);i++) {
                if(!grid.get(x0,i).isWalkable()){
                    flag=false;
                    break;
                }
            }
            if(flag) {
                return true;
            }
        }
        return false;
    }
}
