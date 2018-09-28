package com.mina.mk.solver.model;

import tws.keeper.model.Position;

import java.util.Objects;

public class PathPosition extends Position {

    private boolean visited;
    private boolean key;


    public PathPosition(int vertical, int horizontal) {
        super(vertical, horizontal);
        this.key = false;
        this.visited = false;
    }

    public PathPosition(Position position){
        this(position.getVertical(), position.getHorizontal());
    }

    public boolean hasKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isUp(PathPosition other){
        return this.getHorizontal() == other.getHorizontal() && this.getVertical() < other.getVertical();
    }

    public boolean isDown(PathPosition other){
        return this.getHorizontal() == other.getHorizontal() && this.getVertical() > other.getVertical();
    }

    public boolean isLeft(PathPosition other){
        return this.getHorizontal() < other.getHorizontal() && this.getVertical() == other.getVertical();
    }

    public boolean isRight(PathPosition other){
        return this.getHorizontal() > other.getHorizontal() && this.getVertical() == other.getVertical();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PathPosition))
            return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVertical(),getHorizontal());
    }
}
