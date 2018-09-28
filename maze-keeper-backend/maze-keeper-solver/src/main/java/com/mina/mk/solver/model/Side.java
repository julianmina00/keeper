package com.mina.mk.solver.model;

import java.util.ArrayList;
import java.util.List;

public enum Side {

    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private static List<Side> sides;
    private int verticalIncrement;
    private int horizontalIncrement;

    Side(int verticalIncrement, int horizontalIncrement) {
        this.verticalIncrement = verticalIncrement;
        this.horizontalIncrement = horizontalIncrement;
    }

    public static List<Side> sides(){
        if(sides == null){
            sides = new ArrayList<>();
            sides.add(UP);
            sides.add(DOWN);
            sides.add(LEFT);
            sides.add(RIGHT);
        }
        return sides;
    }

    public int getHorizontalIncrement() {
        return horizontalIncrement;
    }

    public int getVerticalIncrement() {
        return verticalIncrement;
    }
}
