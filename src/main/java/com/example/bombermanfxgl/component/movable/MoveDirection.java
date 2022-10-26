package com.example.bombermanfxgl.component.movable;

import javafx.geometry.Point2D;

public enum MoveDirection {
    UP(new Point2D(0, -1)), RIGHT(new Point2D(1, 0)), DOWN(new Point2D(0, 1)), LEFT(new Point2D(-1, 0)),STOP(new Point2D(0,0));

    public Point2D vector;

    MoveDirection(Point2D vector) {
        this.vector = vector;
    }

    public MoveDirection next() {
        int index = ordinal() + 1;

        if (index == values().length) {
            index = 0;
        }

        return values()[index];
    }

    public MoveDirection prev() {
        int index = ordinal() - 1;

        if (index == -1) {
            index = values().length - 1;
        }

        return values()[index];
    }
}
