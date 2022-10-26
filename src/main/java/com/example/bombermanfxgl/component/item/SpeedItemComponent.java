package com.example.bombermanfxgl.component.item;

public class SpeedItemComponent extends ItemComponent{
    public SpeedItemComponent(int code) {
        super(code);
    }

    @Override
    public void onUpdate(double tpf) {
        isBurn();
        if(bbox.isCollidingWith(player.getBoundingBoxComponent())){
            playerComponent.increaseSpeedCounts();
            detroyitem();
        }
    }
}
