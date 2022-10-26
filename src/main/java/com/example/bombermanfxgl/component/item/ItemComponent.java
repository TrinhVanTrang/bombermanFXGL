package com.example.bombermanfxgl.component.item;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.component.PlayerComponent;

import static com.example.bombermanfxgl.BombermanType.*;

public abstract class ItemComponent extends Component {
    protected int code;
    protected BoundingBoxComponent bbox;
    protected PlayerComponent playerComponent=FXGL.<BombermanApp>getAppCast().getPlayerComponent();
    protected Entity player=FXGL.<BombermanApp>getAppCast().getPlayer();

    public ItemComponent(int code) {
        this.code = code;
    }

    public void isBurn() {
        FXGL.getGameWorld().getEntitiesAt(entity.getPosition())
                .stream()
                .filter(e->e.isType(FLAME))
                .forEach(e->{
                    //frame burn
                    //......
                    if(e.getBoundingBoxComponent().isCollidingWith(entity.getBoundingBoxComponent())) {
                        detroyitem();
                    }
                });
    }

    public void detroyitem() {
        FXGL.play("getItem.wav");
        this.entity.getBoundingBoxComponent().clearHitBoxes();
        this.entity.removeFromWorld();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
