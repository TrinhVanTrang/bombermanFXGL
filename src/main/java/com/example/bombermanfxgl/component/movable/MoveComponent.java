package com.example.bombermanfxgl.component.movable;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.example.bombermanfxgl.BombermanApp;

import static com.example.bombermanfxgl.BombermanType.*;

public class MoveComponent extends Component {
    private BoundingBoxComponent bbox;

    private MoveDirection moveDir;

    private boolean movedThisFrame = false;

    private LazyValue<EntityGroup> bricks = new LazyValue<>(() -> {
        return entity.getWorld().getGroup(Block.WALL, Block.BRICK,BomType.COMMON);
    });

    AStarGrid grid=FXGL.<BombermanApp>getAppCast().getGrid();

    protected int speed_value=1;

    public MoveDirection getMoveDir() {
        return moveDir;
    }

    public void setMoveDirection(MoveDirection moveDir) {
        if (movedThisFrame)
            return;

        movedThisFrame = true;

        this.moveDir = moveDir;

        switch (moveDir) {
            case UP:
                up();
                break;

            case DOWN:
                down();
                break;

            case LEFT:
                left();
                break;

            case RIGHT:
                right();
                break;
            case STOP:
                stop();
                break;
        }
    }

    @Override
    public void onAdded() {
        moveDir = FXGLMath.random(MoveDirection.values()).get();
    }

    private double speed = 0;

    @Override
    public void onUpdate(double tpf) {
        speed = tpf * speed_value*6;

        movedThisFrame = false;
    }

    private void up() {
        move(0, -5 * speed);
    }

    private void down() {
        move(0, 5 * speed);
    }

    private void left() {
        move(-5 * speed, 0);
    }

    private void right() {
        move(5 * speed, 0);
    }

    private void stop() {
        move(0,0);
    }

    private Vec2 velocity = new Vec2();

    public int getSpeed_value() {
        return speed_value;
    }

    public void setSpeed_value(int speed_value) {
        this.speed_value = speed_value;
    }

    private void move(double dx, double dy) {
        if (!this.getEntity().isActive()) {
            return;
        }
        velocity.set((float) dx, (float) dy);

        int length = Math.round(velocity.length());

        velocity.normalizeLocal();

        var blocks = bricks.get().getEntitiesCopy();

        for (int i = 0; i < length; i++) {
            entity.translate(velocity.x, velocity.y);

            boolean collision = false;

            for (int j = 0; j < blocks.size(); j++) {
                if (blocks.get(j).getBoundingBoxComponent().isCollidingWith(bbox)) {
                    //System.out.println(blocks.get(j).getX()+" "+blocks.get(j).getY()+" "+bbox.getEntity().getX()+" "+bbox.getEntity().getY());
                    collision = true;
                    break;
                }
            }

//            if(!grid.get((int) (velocity.x/40),(int) (velocity.y/40)).isWalkable()) {
//                collision=true;
//            }

            if (collision) {
                entity.translate(-velocity.x, -velocity.y);
                break;
            }
        }


    }
}
