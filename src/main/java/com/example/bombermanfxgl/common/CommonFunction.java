package com.example.bombermanfxgl.common;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.example.bombermanfxgl.BombermanApp;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;


import static com.almasb.fxgl.dsl.FXGL.random;

public class CommonFunction {
    public static Point2D randomSpawn(AStarGrid grid) {
        int x, y;
        while (true) {
            x = random(1, 13);
            y = random(1, 13);
            if (grid.get(x, y).isWalkable()) {
                return new Point2D(x, y);
            }
        }
    }

    public static boolean collisitionEntity(Entity e1, Entity e2) {
        double x1 = e1.getX()+5;
        double y1 = e1.getY()+5;
        double w1 = 25;
        double h1 = 25;

        Rectangle2D rect1 = new Rectangle2D(x1, y1, w1, h1);

        double x2 = e2.getX()+7;
        double y2 = e2.getY()+7;
        double w2 = 25;
        double h2 = 30;

        Rectangle2D rect2 = new Rectangle2D(x2, y2, w2, h2);
//        System.out.println(rect1.getMinX() + " " + rect1.getMinY() + " " + rect1.getMaxX() + " " + rect1.getMaxY());
//        System.out.println(rect2.getMinX() + " " + rect2.getMinY() + " " + rect2.getMaxX() + " " + rect2.getMaxY());

        return (rect1.getMaxX() >= rect2.getMinX()
                && rect1.getMaxY() >= rect2.getMinY()
                && rect1.getMinX() <= rect2.getMaxX()
                && rect1.getMinY() <= rect2.getMaxY()) ||
                (rect2.getMaxX() >= rect1.getMinX()
                        && rect2.getMaxY() >= rect1.getMinY()
                        && rect2.getMinX() <= rect1.getMaxX()
                        && rect2.getMinY() <= rect1.getMaxY());
//        if(rect1.intersects(rect2)||rect2.intersects(rect1)) {
//            return true;
//        }
//        return false;
    }
}
