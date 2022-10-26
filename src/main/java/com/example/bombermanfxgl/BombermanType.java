package com.example.bombermanfxgl;

public enum BombermanType {
    PLAYER,FLAME;
    public static enum Block {
        WALL,BRICK
    }

    public static enum BomType {
        COMMON,  //bom thường
        PIERCE,  //bom đào
        PRIMER   //kíp nổ
    }

    public static enum Enemy {
        BALLOON,SLIME,CLOUD,BARREL,BEAST,BACTERIUM,OCTOPUS,COIN
    }

    public static enum Item {
        BOM,FLAME,SPEED,LIFE,PIERCE,PRIMER,KICK,NO_OCSTACLE,GATE
    }
}
