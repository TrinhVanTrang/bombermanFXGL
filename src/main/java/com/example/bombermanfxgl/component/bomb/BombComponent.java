package com.example.bombermanfxgl.component.bomb;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.TimerAction;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.BombermanType;
import com.example.bombermanfxgl.component.PlayerComponent;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.bombermanfxgl.BombermanType.FLAME;

public abstract class BombComponent extends Component {

    protected AnimatedTexture animatedTexture;
    protected AnimationChannel animationChannel;
    protected List<javafx.geometry.Point2D> brickPositions=new Vector<javafx.geometry.Point2D>();
    protected Entity player=FXGL.<BombermanApp>getAppCast().getPlayer();
    protected TimerAction timerAction;

    private int bombCode;
    private int radius;
    protected List<Entity>flames=new Vector<>();
    private int time=3;
    private boolean isDestroyed=false;

    public Point2D point;
    public AStarGrid grid= FXGL.<BombermanApp>getAppCast().getGrid();
    public BombComponent(double x, double y,int bombCode, int radius) {
        point=new Point2D.Double(x/40,y/40);
        this.bombCode = bombCode;
        this.radius = radius;
    }
    public LazyValue<EntityGroup> bricks = new LazyValue<>(() -> {
        return entity.getWorld().getGroup(BombermanType.Block.BRICK);
    });
    public LazyValue<EntityGroup> walls = new LazyValue<>(() -> {
        return entity.getWorld().getGroup(BombermanType.Block.WALL);
    });

    public void spawnItem() {
        if(getGameWorld().getEntitiesByType(FLAME).size()==0) {
            for (javafx.geometry.Point2D point:brickPositions) {
                FXGL.<BombermanApp>getAppCast().spawnItem(point);
            }
        } else {
            for (Entity e:FXGL.getGameWorld().getEntitiesByType(FLAME)) {
               e.getBoundingBoxComponent().clearHitBoxes();
            }
            for (javafx.geometry.Point2D point:brickPositions) {
                FXGL.<BombermanApp>getAppCast().spawnItem(point);
            }
        }
    }

    public abstract void explode() ;

    public int getBombCode() {
        return bombCode;
    }

    public void setBombCode(int bombCode) {
        this.bombCode = bombCode;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void reuse() {
        player.getComponent(PlayerComponent.class).reuseBomb();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed() {
        isDestroyed = true;
    }

    public void destroyed() {
        if(!isDestroyed) {
            this.entity.removeFromWorld();
            this.explode();
            this.reuse();
        }
    }

    public void pauseExplore() {
        timerAction.expire();
    }

    public TimerAction getTimerAction() {
        return timerAction;
    }

    public void setTimerAction(TimerAction timerAction) {
        this.timerAction = timerAction;
    }
}

