package com.example.bombermanfxgl.component.bomb;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.example.bombermanfxgl.BombermanApp;
import com.example.bombermanfxgl.component.PlayerComponent;
import com.example.bombermanfxgl.component.item.ItemComponent;

import static com.example.bombermanfxgl.BombermanApp.*;
import static com.example.bombermanfxgl.BombermanType.*;

public class FlameComponent extends Component {
    Entity player= FXGL.<BombermanApp>getAppCast().getPlayer();
    boolean isdie;
    @Override
    public void onUpdate(double tpf) {
        isdie=player.getComponent(PlayerComponent.class).isDie();
        if(isdie) {
            //player.getBoundingBoxComponent().clearHitBoxes();
        }
        else if(this.entity.getBoundingBoxComponent().isCollidingWith(player.getBoundingBoxComponent())) {
            player.getComponent(PlayerComponent.class).damage();
            player.getComponent(PlayerComponent.class).setState(5);
        }
    }
}
