package com.example.bombermanfxgl.component.enemy;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.BombermanType;
import com.example.bombermanfxgl.component.PlayerComponent;
import com.example.bombermanfxgl.component.movable.MoveComponent;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public abstract class EnemyComponent extends Component {

    protected AnimationChannel animationChannel;
    protected AnimatedTexture animatedTexture;
    protected  AnimationChannel animationDie = new AnimationChannel(image("enemy/enemyDie.png"),
            3, 40, 40, Duration.seconds(2), 0, 2);

    protected int code;
    protected int speed = 1;
    protected boolean isDie = false;
    protected int life = 1;

    protected Entity player;
    protected PlayerComponent playerComponent;

    protected MoveComponent moveComponent;
    protected int state;
    protected AStarMoveComponent aStarMoveComponent;
    protected AStarGrid grid = geto("grid");
    protected List<Integer> listState;

    public EnemyComponent(int code) {
        player = FXGL.<BombermanApp>getAppCast().getPlayer();
        playerComponent = FXGL.<BombermanApp>getAppCast().getPlayerComponent();
        this.code = code;
    }

    public abstract void move();

    public void chasePlayer() {
        if (entity.getBoundingBoxComponent().isCollidingWith(player.getBoundingBoxComponent())) {
            if (!isDie() && !playerComponent.isDie()) {
                playerComponent.damage();
                playerComponent.setState(4);
            }
        }
    }

    public void isBurn() {

        AtomicBoolean flag = new AtomicBoolean(false);
        getGameWorld().getEntitiesByType(BombermanType.FLAME)
                .stream()
                .filter(e -> e.getBoundingBoxComponent().isCollidingWith(this.entity.getBoundingBoxComponent()))
                .forEach(e -> {
                        flag.set(true);
                });

        if (flag.get()) {
            life--;
            entity.getBoundingBoxComponent().clearHitBoxes();
            getGameTimer().runOnceAfter(()-> {
                entity.getBoundingBoxComponent().addHitBox(new HitBox(new Point2D(2,2), BoundingShape.box(36,36)));
            },Duration.seconds(1));
        }

        if (isDie()) {
            getGameTimer().runOnceAfter(() -> {
                entity.removeFromWorld();
            }, Duration.seconds(2));
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isDie() {
        if(life<=0) {
            return true;
        }
        return false;
    }

    public void setDie() {
        if(life<=0) {
            isDie = true;
        }

    }

    public void changeState() {
        int random = 0;
        while (true) {
            random = random(1, 4);
            if (random != state) {
                this.state = random;
                break;
            }
        }
    }

    public double distance(Entity e) {
        int x0 = (int) (this.entity.getX() / 40);
        int y0 = (int) (this.entity.getY() / 40);

        int x = (int) ((player.getX() + 7) / 40);
        int y = (int) ((player.getY() + 17) / 40);

        return Math.sqrt(Math.pow(x0 - x, 2) + Math.pow(y0 - y, 2));
    }

    public double distanceEntity(Entity e) {
        int x0 = (int) (this.entity.getX() / 40);
        int y0 = (int) (this.entity.getY() / 40);

        int x = (int) (e.getX()  / 40);
        int y = (int) (e.getY()  / 40);

        return Math.sqrt(Math.pow(x0 - x, 2) + Math.pow(y0 - y, 2));
    }

    public List<Integer> checkMove() {
        List<Integer> list = new Vector<>();
        if (entity.getComponent(CellMoveComponent.class).isMovingUp()) {
            list.add(1);
        }
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
