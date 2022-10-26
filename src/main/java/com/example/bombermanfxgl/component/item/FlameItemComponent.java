package com.example.bombermanfxgl.component.item;

public class FlameItemComponent extends ItemComponent{
    public FlameItemComponent(int code) {
        super(code);
    }

    @Override
    public void onUpdate(double tpf) {
        isBurn();
        if(bbox.isCollidingWith(player.getBoundingBoxComponent())){
            playerComponent.increaseFlameCounts();
            detroyitem();
        }
    }
}
