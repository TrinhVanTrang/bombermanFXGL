package com.example.bombermanfxgl;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.action.ActionComponent;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.bombermanfxgl.common.CommonFunction;
import com.example.bombermanfxgl.component.enemy.*;
import com.example.bombermanfxgl.component.item.FlameItemComponent;
import com.example.bombermanfxgl.component.item.SpeedItemComponent;
import com.example.bombermanfxgl.component.movable.MoveComponent;
import com.example.bombermanfxgl.component.PlayerComponent;
import com.example.bombermanfxgl.component.bomb.BombComponent;
import com.example.bombermanfxgl.component.bomb.CommonBombComponent;
import com.example.bombermanfxgl.component.bomb.FlameComponent;
import com.example.bombermanfxgl.component.item.BomItemComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.example.bombermanfxgl.BombermanType.*;
import static com.example.bombermanfxgl.BombermanApp.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BombermanFactory implements EntityFactory {
    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .at(0, 0)
                .viewWithBBox(FXGL.texture("block/sand.png", 600, 600))
                .zIndex(-1)
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(Block.WALL)
                .viewWithBBox(FXGL.texture("block/rock.png", 40, 40))
                .collidable()
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {

        AStarGrid grid = FXGL.<BombermanApp>getAppCast().getGrid();
        Point2D point2D = CommonFunction.randomSpawn(grid);
        int x = (int) point2D.getX();
        int y = (int) point2D.getY();
        grid.get(x, y).setState(CellState.NOT_WALKABLE);
        return entityBuilder(data)
                .type(Block.BRICK)
                .at(x * 40, y * 40)
                .viewWithBBox(FXGL.texture("block/glass.png", 40, 40))
                //.with(new CollidableComponent(true))
                .collidable()
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
//        int life_counts=data.get("life_counts");
//        int bom_counts=data.get("bom_counts");
//        int flame_counts=data.get("flame_counts");
//        int speed=data.get("speed");
        return entityBuilder()
                .type(PLAYER)
                //.with(new CollidableComponent(true))
                .bbox(new HitBox(new Point2D(7, 7), BoundingShape.box(25, 30)))
                .collidable()
                .at(1 * TILE_SIZE, 1 * TILE_SIZE)
                .with(new MoveComponent())
                .with(new CellMoveComponent(28, 40, 150))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                //.with(new PlayerComponent(speed,bom_counts,flame_counts,life_counts))
                .with(new PlayerComponent())
                .with(new ActionComponent())
                .with(new StateComponent())
                .build();
    }

    @Spawns("Bomb")
    public Entity newBomB(SpawnData data) {
        double x = (double) data.get("x");
        double y = (double) data.get("y");
        int code = data.get("bombCode");
        int radius = data.get("radius");
        BombComponent bomb = null;
        if (code == 1) {
            bomb = new CommonBombComponent(x, y, code, radius);
        }
        return entityBuilder(data)
                .type(BomType.COMMON)
                .collidable()
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(30, 30)))
                // .with(new CommonBombComponent(x, y, code, radius))
                .with(bomb)
                .with(new MoveComponent())
                .build();
    }

    @Spawns("Flame")
    public Entity newFlame(SpawnData data) {
        int state = 3;
        state = data.get("state");
        Image image = image("bomb/flameSprite.png");
        AnimationChannel animationChannel = new AnimationChannel(image,
                7, 40, 40, Duration.seconds(1), state, state);
        AnimatedTexture animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        return entityBuilder(data)
                .type(FLAME)
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(36, 36)))
                .view(animatedTexture)
                .with(new FlameComponent())
                .collidable()
                .build();
    }

    @Spawns("Gate")
    public Entity newGate(SpawnData data) {
        Image image = image("item/itemSprite.png");
        AnimationChannel animationChannel = new AnimationChannel(image,
                12, 40, 40, Duration.seconds(1), 0, 0);
        AnimatedTexture animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        return entityBuilder(data)
                .type(Item.GATE)
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(36, 36)))
                .view(animatedTexture)
                .collidable()
                .build();
    }

    public void createItem(int code, Entity entity) {
        switch (code) {
            case 1:


            case 2:
                entity.addComponent(new SpeedItemComponent(code));
                entity.setType(Item.SPEED);
                break;
            case 3:
                entity.addComponent(new FlameItemComponent(code));
                entity.setType(Item.BOM);
                break;
            case 4:
                entity.addComponent(new BomItemComponent(code));
                entity.setType(Item.BOM);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
        }
    }

    @Spawns("Item")
    public Entity newItem(SpawnData data) {
        int code = data.get("code");

        Image image = image("item/itemSprite.png");
        AnimationChannel animationChannel = new AnimationChannel(image,
                12, 40, 40, Duration.seconds(1), code, code);
        AnimatedTexture animatedTexture = new AnimatedTexture(animationChannel);
        animatedTexture.loop();
        Entity entity = entityBuilder(data)
                .bbox(new HitBox(new Point2D(1, 1), BoundingShape.box(38, 38)))
                .view(animatedTexture)
                .build();
        createItem(code, entity);
        return entity;
    }

    public void createEnemy(int code, Entity entity) {
        switch (code) {
            case 1:
                entity.addComponent(new BalloonComponent(code));
                entity.setType(Enemy.BALLOON);
                break;

            case 2:
                entity.addComponent(new SlimeComponent(code));
                entity.setType(Enemy.SLIME);
                break;
            case 3:
                entity.addComponent(new BarrelComponent(code));
                entity.setType(Enemy.BARREL);
                break;
            case 4:
                entity.addComponent(new CloudComponent(code));
                entity.setType(Enemy.CLOUD);
                break;
            case 5:
                entity.addComponent(new BacteriumComponent(code));
                entity.setType(Enemy.BACTERIUM);
                break;
            case 6:
                entity.addComponent(new BeastComponent(code));
                entity.setType(Enemy.BEAST);
                break;
            case 7:
                entity.addComponent(new OctopusComponent(code));
                entity.setType(Enemy.OCTOPUS);
                break;
            case 8:
                entity.addComponent(new CoinComponent(code));
                entity.setType(Enemy.COIN);
                break;
            case 9:
            case 10:
        }
    }

    @Spawns("Enemy")
    public Entity newEnnemy(SpawnData data) {
        AStarGrid grid = FXGL.<BombermanApp>getAppCast().getGrid();
        Point2D point2D = CommonFunction.randomSpawn(grid);
        int x = (int) point2D.getX();
        int y = (int) point2D.getY();

        int code = data.get("code");

        Entity enemy = entityBuilder(data)
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(36, 36)))
                .collidable()
                .at(x * 40, y * 40)
                .anchorFromCenter()
                .with(new CellMoveComponent(40, 40, 100))
                .with(new AStarMoveComponent(new LazyValue<>(() -> FXGL.geto("grid"))))
                .build();
        createEnemy(code, enemy);
        return enemy;

//        EnemyComponent enemyComponent = null;
//        Enemy enemy=null;
//        if(code==1) {
//            enemyComponent=new BalloonComponent(code);
//            enemy=Enemy.BALLOON;
//        }
//        return entityBuilder(data)
//                .type(Enemy.BALLOON)
//                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(36, 36)))
//                .collidable()
//                .at(x * 40, y * 40)
//                .anchorFromCenter()
//                .with(new CellMoveComponent(40, 40, 100))
//                .with(new AStarMoveComponent(new LazyValue<>(() -> FXGL.geto("grid"))))
//                //.with(new BalloonComponent(1))
//                .with(new BalloonComponent(1).create())
//                .build();
    }

}

