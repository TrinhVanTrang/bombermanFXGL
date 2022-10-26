package com.example.bombermanfxgl.component.item;

import com.almasb.fxgl.dsl.FXGL;

import static com.example.bombermanfxgl.BombermanType.FLAME;

public class BomItemComponent extends ItemComponent{
    public BomItemComponent(int code) {
        super(code);
    }

    @Override
    public void onUpdate(double tpf) {
        isBurn();
        if(bbox.isCollidingWith(player.getBoundingBoxComponent())){
            playerComponent.increaseBomCounts();
            detroyitem();
        }
    }
}
