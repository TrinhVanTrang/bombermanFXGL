package com.example.bombermanfxgl;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.handlers.CollectibleHandler;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.example.bombermanfxgl.component.PlayerComponent;
import com.example.bombermanfxgl.component.bomb.BombComponent;
import com.example.bombermanfxgl.component.bomb.CommonBombComponent;
import com.example.bombermanfxgl.component.enemy.BalloonComponent;
import com.example.bombermanfxgl.level.LevelInfomation;
import com.example.bombermanfxgl.level.SaveDataGame;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.util.Pair;


import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import static com.example.bombermanfxgl.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.bombermanfxgl.common.CommonConst.*;

public class BombermanApp extends GameApplication {
    public static final int TILE_SIZE = 40;
    private AStarGrid grid;
    int numberLevel = 1;
    private PlayerComponent playerComponent;
    private Entity player;
    private int score;
    private List<Integer> itemList = new Vector<>();
    // private SaveDataGame save;
    private boolean reStart = false;
    public static Music music;

    public Entity getPlayer() {
        return player;
    }

    public AStarGrid getGrid() {
        return grid;
    }

    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("BombermanFXGL ");
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setVersion("0.1");
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("life_counts", 0);
        vars.put("bom_counts", 0);
        vars.put("flame_counts", 0);
        vars.put("speed", 0);
        vars.put("bricks", 0);

    }

    /**
     * @Override protected void onPreInit() {
     * getSaveLoadService().addHandler(new SaveLoadHandler() {
     * @Override public void onSave(DataFile data) {
     * var bundle = new Bundle("gameData");
     * <p>
     * int score = (int) getd("score");
     * int life_counts = (int) getd("life_counts");
     * int bomb_counts = (int) getd("bom_counts");
     * int flame_counts = (int) getd("flame_counts");
     * int speed = (int) getd("speed");
     * <p>
     * data.putBundle(bundle);
     * }
     * @Override public void onLoad(DataFile data) {
     * var bundle = data.getBundle("gameData");
     * <p>
     * // retrieve some data
     * int score = bundle.get("score");
     * int life_counts = bundle.get("life_counts");
     * int bomb_counts = bundle.get("bom_counts");
     * int flame_counts = bundle.get("flame_counts");
     * int speed = bundle.get("speed");
     * <p>
     * // update your game with saved data
     * set("score", score);
     * set("life_counts", life_counts);
     * set("bom_counts", bomb_counts);
     * set("flame_counts", flame_counts);
     * set("speed", speed);
     * }
     * });
     * }
     **/

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombermanFactory());

        music = getAssetLoader().loadMusic("retro.mp3");
        music.getAudio$fxgl_core().setLooping(true);
        //music.getAudio$fxgl_core().play();

        Level level = getAssetLoader().loadLevel("lv" + String.valueOf(numberLevel) + ".txt",
                new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);

        LevelInfomation lvInfo = new LevelInfomation(numberLevel);

        itemList.add(0);
        for (int i = 0; i < lvInfo.getListItem().size(); i++) {
            int count = Integer.parseInt(String.valueOf(lvInfo.getListItem().get(i).getValue()));
            int code = Integer.parseInt(String.valueOf(lvInfo.getListItem().get(i).getKey()));
            for (int j = 0; j < count; j++) {
                itemList.add(code);
            }
        }

        spawn("BG");

        grid = AStarGrid.fromWorld(getGameWorld(), 15, 15, 40, 40, type -> {
            if (type.equals(Block.WALL)) {
                return CellState.NOT_WALKABLE;
            }
            return CellState.WALKABLE;
        });

        set("grid", grid);

        player = spawn("Player");
        playerComponent = player.getComponent(PlayerComponent.class);

        grid.get(1, 1).setState(CellState.NOT_WALKABLE);
        grid.get(1, 2).setState(CellState.NOT_WALKABLE);
        grid.get(2, 1).setState(CellState.NOT_WALKABLE);
//        spawn("b",40,120);
//        spawn("b",120,40);
//        grid.get(1, 3).setState(CellState.NOT_WALKABLE);
//        grid.get(3, 1).setState(CellState.NOT_WALKABLE);
        for (int i = 0; i < 80; i++) {
            spawn("b");
        }
        for (int i = 0; i < lvInfo.getEnemy_counts(); i++) {
            Entity enemy = spawn("Enemy", new SpawnData().put("code", lvInfo.getEnemyCode()));
            //System.out.println(enemy.getX()+" "+enemy.getY());
        }
        grid.get(1, 1).setState(CellState.WALKABLE);
        grid.get(1, 2).setState(CellState.WALKABLE);
        grid.get(2, 1).setState(CellState.WALKABLE);

//        player = spawn("Player", new SpawnData(40, 40)
//                .put("life_counts", geti("life_counts"))
//                .put("bom_counts", geti("bom_counts"))
//                .put("flame_counts", geti("flame_counts"))
//                .put("speed", geti("speed")));

        set("life_counts", playerComponent.getLife_counts());
        set("bom_counts", playerComponent.getBom_counts());
        set("flame_counts", playerComponent.getFlame_counts());
        set("speed", playerComponent.getSpeed_counts());
        set("bricks", 80);

        System.out.println(grid.getWalkableCells().size());

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BomType.COMMON, FLAME) {
            @Override
            protected void onCollisionBegin(Entity bom, Entity flame) {
                bom.removeFromWorld();
                bom.getComponent(CommonBombComponent.class).explode();
                bom.getComponent(CommonBombComponent.class).reuse();
                bom.getComponent(CommonBombComponent.class).pauseExplore();
            }
        });


//        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, Item.GATE) {
//            @Override
//            protected void onCollisionBegin(Entity player, Entity gate) {
//                numberLevel++;
//                reStart = true;
//                // getGameWorld().reset();
//            }
//        });
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (!playerComponent.isDie())
                    playerComponent.moveUp();
            }

            @Override
            protected void onActionEnd() {
                if (!playerComponent.isDie())
                    playerComponent.stop();
            }
        }, KeyCode.UP);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (!playerComponent.isDie())
                    playerComponent.moveDown();
            }

            @Override
            protected void onActionEnd() {
                if (!playerComponent.isDie())
                    playerComponent.stop();
            }
        }, KeyCode.DOWN);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (!playerComponent.isDie())
                    playerComponent.moveLeft();
            }

            @Override
            protected void onActionEnd() {
                if (!playerComponent.isDie())
                    playerComponent.stop();
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (!playerComponent.isDie())
                    playerComponent.moveRight();
            }

            @Override
            protected void onActionEnd() {
                if (!playerComponent.isDie())
                    playerComponent.stop();
            }
        }, KeyCode.RIGHT);

        FXGL.getInput().addAction(new UserAction("Bomb Placed") {
            @Override
            protected void onActionBegin() {
                if (!playerComponent.isDie())
                    playerComponent.placeBomb();
            }

            @Override
            protected void onActionEnd() {
                if (!playerComponent.isDie())
                    playerComponent.stop();
            }
        }, KeyCode.X);

        FXGL.getInput().addAction(new UserAction("Primer!") {
            @Override
            protected void onAction() {
                if (!playerComponent.isDie())
                    playerComponent.primer();
            }
        }, KeyCode.V);
    }

    public void onPlayerKiller() {
        if (playerComponent.getLife_counts() < 0) {
            reStart = true;
        }
    }

    public void destroyedBrick(Entity brick) {
        brick.removeFromWorld();
        grid.get((int) brick.getX() / 40, (int) brick.getY() / 40);
    }


    @Override
    protected void onUpdate(double tpf) {
        if (reStart) {
            reStart = false;
            //nextLevel();
            //.....
            //getGameController().gotoGameMenu();
//            getGameTimer().runOnceAfter(() -> {
//                initGame();
//                Level level = getAssetLoader().loadLevel("lv" + String.valueOf(numberLevel) + ".txt",
//                        new TextLevelLoader(40, 40, '0'));
//                getGameWorld().setLevel(level);
//            }, Duration.seconds(1));

        }

//        System.out.println(grid.getWalkableCells().size()+" "+(15*15-grid.getWalkableCells().size()));
//        System.out.println("---->"+getGameWorld().getEntitiesByType(Block.BRICK).size()+" "+getGameWorld().getEntitiesByType(Block.WALL).size());
        //inc("life_counts",+1);
//        inc("bom_counts",playerComponent.getBom_counts());
//        inc("flame_counts",playerComponent.getFlame_counts());
//        inc("speed",playerComponent.getSpeed_counts());
    }

//        public void spawnBom(Entity bomb) {
//        int cellX = (int) ((bomb.getX() ) / TILE_SIZE);
//        int cellY = (int) ((bomb.getY() ) / TILE_SIZE);
//
//        grid.get(cellX, cellY).setState(CellState.NOT_WALKABLE);
//    }

    public void spawnItem(Point2D point) {
        if (itemList.size() > 0) {
            int radom = random(1,itemList.size() /**geti("bricks")*/);
            //System.out.println(radom+" "+itemList.get(radom-1)+" "+itemList.size());
            if (radom <= itemList.size()) {
                if (itemList.get(radom - 1) == 0) {
                    spawn("Gate", point.getX(), point.getY());
                } else {
                    spawn("Item", new SpawnData(point.getX(), point.getY())
                            .put("code", itemList.get(radom - 1)));
                }
                itemList.remove(radom - 1);
            }
        }
    }

    public void nextLevel() {
        if(numberLevel==MAX_LEVEL) {
            System.out.println("Finish game!");
            showMessage("You finished the demo!");
            return;
        }
        numberLevel++;
        Level level=getAssetLoader().loadLevel("lv" + String.valueOf(numberLevel) + ".txt",
                new TextLevelLoader(40, 40, '0'));
        //getGameWorld().reset();
        getGameWorld().setLevel(level);
    }


    public static void main(String[] args) {
        launch(args);
    }
}














///Trinh Van Trang - 20020487