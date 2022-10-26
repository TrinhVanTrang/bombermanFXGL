package com.example.bombermanfxgl.component;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.texture.Texture;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.BombermanType;

import static com.example.bombermanfxgl.common.CommonConst.*;

import com.example.bombermanfxgl.component.bomb.CommonBombComponent;
import com.example.bombermanfxgl.component.movable.MoveComponent;
import com.example.bombermanfxgl.component.movable.MoveDirection;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlayerComponent extends Component {
    private MoveComponent moveComponent;

    //Sprite
    private double frameWidth;
    private double frameHeight;
    private Texture texture;
    private ViewComponent view;

    private int frames = 0;
    private int state = 0;
    private double speed = 0;

    private int bom_code;
    private int bom_counts;     //số lượng bom hiện có
    private int bom_used;       //số bom đã dùng
    private int flame_counts;
    private int speed_counts;
    private int life_counts;

    private boolean isDie;

    List<Entity> bombList = new Vector<>();

    private AStarGrid grid=FXGL.<BombermanApp>getAppCast().getGrid();

    public PlayerComponent() {
        bom_code = 1;
        bom_used = 0;
        bom_counts = 2;
        flame_counts = 2;
        speed_counts = 3;
        life_counts = 2;

        isDie = false;

    }

    public PlayerComponent(int speed_counts, int bom_counts, int flame_counts, int life_counts) {
        this.speed_counts = speed_counts;
        this.bom_counts = bom_counts;
        this.flame_counts = flame_counts;
        this.life_counts = life_counts;
    }

    public void placeBomb() {
        if (bom_used == bom_counts) {
            return;
        }
        bom_used++;

        double x = (int) ((moveComponent.getEntity().getX() + 7) / 40) * 40;
        double y = (int) ((moveComponent.getEntity().getY() + 17) / 40) * 40;

        AtomicBoolean flag= new AtomicBoolean(false);
        getGameWorld().getEntitiesAt(new Point2D(x,y))
                .stream()
                .filter(e->e.isType(BombermanType.BomType.COMMON)||e.isType(BombermanType.Block.BRICK))
                .forEach(e->{
                    bom_used--;
                    flag.set(true);
                    return;
                });
        if(flag.get()) {
            return;
        }

        Entity bomb = spawn("Bomb",
                new SpawnData(x, y)
                        .put("x", x)
                        .put("y", y)
                        .put("bombCode", this.getBom_code())
                        .put("radius", this.getFlame_counts()));
        //bombList.add(bomb);
        grid.get((int)x/40,(int)y/40).setState(CellState.NOT_WALKABLE);
        bomb.getComponent(CommonBombComponent.class).setTimerAction(
                getGameTimer().runOnceAfter(() -> {
                            grid.get((int)x/40,(int)y/40).setState(CellState.WALKABLE);
                            bomb.getComponent(CommonBombComponent.class).destroyed();
                        }, Duration.seconds(bomb.getComponent(CommonBombComponent.class).getTime())
                )
        );

    }

    public void primer() {
        for (Entity e : bombList) {
            reuseBomb();
            e.removeFromWorld();
        }
    }

    @Override
    public void onAdded() {
        texture = texture("bomber/p_sprite.png");
        view.addChild(texture);
        frameWidth = texture.getWidth() / 3;
        frameHeight = texture.getHeight() / 7;
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDie) {
            die();
            //isDie=false;
            BombermanApp.music.getAudio$fxgl_core().pause();
            getGameTimer().runOnceAfter(() -> {
                isDie = false;
                getGameController().startNewGame();
//                inc("life_counts",-1);
//                inc("bom_counts",+this.getBom_counts());
//                inc("flame_counts",+this.getFlame_counts());
//                inc("speed",+this.getSpeed_counts());
            }, Duration.seconds(3));
        }

        moveComponent.setSpeed_value(speed_counts);
        speed = tpf * 60;
        int frame = frames / 10;
        if (frame >= 3) {
            frame = 0;
            frames = 0;
        }
        texture.setViewport(new Rectangle2D(frame * frameWidth, state * frameHeight, frameWidth, frameHeight));

    }

    public void damage() {
        life_counts--;
        isDie = true;
        FXGL.play("lose.wav");
        FXGL.<BombermanApp>getAppCast().onPlayerKiller();
    }

    //Move Function
    public void moveUp() {
       // FXGL.play("walk.wav");
        moveComponent.setMoveDirection(MoveDirection.UP);
        state = 1;
        frames++;
    }

    public void moveDown() {
        moveComponent.setMoveDirection(MoveDirection.DOWN);
        state = 0;
        frames++;
    }

    public void moveLeft() {
        moveComponent.setMoveDirection(MoveDirection.LEFT);
        state = 3;
        frames++;
    }

    public void moveRight() {
        moveComponent.setMoveDirection(MoveDirection.RIGHT);
        state = 2;
        frames++;
    }

    public void stop() {
        moveComponent.setMoveDirection(MoveDirection.STOP);
        frames = 0;
    }

    public void die() {
        frames++;
    }

    public int getBom_counts() {
        return bom_counts;
    }

    public void setBom_counts(int bom_counts) {
        this.bom_counts = bom_counts;
    }

    public int getBom_used() {
        return bom_used;
    }

    public void setBom_used(int bom_used) {
        this.bom_used = bom_used;
    }

    public int getFlame_counts() {
        return flame_counts;
    }

    public void setFlame_counts(int flame_counts) {
        this.flame_counts = flame_counts;
    }

    public int getSpeed_counts() {
        return speed_counts;
    }

    public void setSpeed_counts(int speed_counts) {
        this.speed_counts = speed_counts;
    }

    public int getBom_code() {
        return bom_code;
    }

    public void setBom_code(int bom_code) {
        this.bom_code = bom_code;
    }

    public boolean isDie() {
        return isDie;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setDie(boolean die) {
        isDie = die;
    }

    public int getLife_counts() {
        return life_counts;
    }

    public void setLife_counts(int life_counts) {
        this.life_counts = life_counts;
    }

    public void increaseBomCounts() {
        if (bom_counts < MAX_BOM_COUNTS)
            bom_counts++;
    }

    public void increaseSpeedCounts() {
        if (speed_counts < MAX_SPEED_COUNTS)
            speed_counts++;
    }

    public void increaseFlameCounts() {
        if (flame_counts < MAX_FLAME_COUNTS) {
            flame_counts++;
        }
    }

    public void increaseLifeCounts() {
        if (life_counts < MAX_LIFE_COUNTS) {
            life_counts++;
        }
    }

    public int getCellX() {
        return (int) ((this.entity.getX()+7)/40);
    }

    public int getCellY() {
        return (int) ((this.entity.getY()+17)/40);
    }


    public void reuseBomb() {
        bom_used--;
    }
}
