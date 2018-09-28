package com.mina.mk.solver.logic;

import com.mina.mk.solver.logic.strategy.FindDoorStrategy;
import com.mina.mk.solver.logic.strategy.FindKeysStrategy;
import com.mina.mk.solver.logic.strategy.GoDoorStrategy;
import com.mina.mk.solver.logic.strategy.MoveStrategy;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import com.mina.mk.solver.model.Side;
import tws.keeper.model.Action;
import tws.keeper.model.Cell;
import tws.keeper.model.Keeper;
import tws.keeper.model.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mina.mk.solver.model.Side.DOWN;
import static com.mina.mk.solver.model.Side.LEFT;
import static com.mina.mk.solver.model.Side.RIGHT;
import static com.mina.mk.solver.model.Side.UP;

public class SmartKeeper implements Keeper {

    private Knowledge knowledge;
    private MoveStrategy lookForKeys;
    private MoveStrategy lookForDoor;
    private MoveStrategy goDoor;

    public SmartKeeper() {
        this.knowledge = new Knowledge();
        lookForKeys = new FindKeysStrategy(knowledge);
        lookForDoor = new FindDoorStrategy(knowledge);
        goDoor = new GoDoorStrategy(knowledge);
    }

    @Override
    public Action act(Observable maze) {
        PathPosition position = new PathPosition(maze.getKeeperPosition());
        position.setVisited(true);
        position.setKey(false);
        knowledge.addKnowledge(position, getNeighbors(position, maze));
        try {
            return getStrategy(maze).nextMove();
        } catch (KnowledgeException e) {
            return Action.DO_NOTHING;
        }
    }

    private List<PathPosition> getNeighbors(PathPosition position, Observable maze){
        List<PathPosition> neighbors = new ArrayList<>(4);
        neighbors.add(getNeighborKnowledge(position, UP, maze.lookUp()));
        neighbors.add(getNeighborKnowledge(position, DOWN, maze.lookDown()));
        neighbors.add(getNeighborKnowledge(position, LEFT, maze.lookLeft()));
        neighbors.add(getNeighborKnowledge(position, RIGHT, maze.lookRight()));
        // cleans the list by removing nulls if the exists
        return neighbors.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private PathPosition getNeighborKnowledge(PathPosition position, Side side, Cell cell){
        if(cell == Cell.WALL){
            return null;
        }
        int horizontal = position.getHorizontal() + side.getHorizontalIncrement();
        int vertical = position.getVertical() + side.getVerticalIncrement();
        PathPosition sidePosition = new PathPosition(vertical, horizontal);
        sidePosition.setKey(cell == Cell.KEY);
        if(cell == Cell.DOOR){
            knowledge.setDoorPosition(sidePosition);
        }
        return sidePosition;
    }

    private MoveStrategy getStrategy(Observable maze){
        if(getMissingKeys(maze) != 0){
            return lookForKeys;
        }
        return knowledge.getDoorPosition() != null ? goDoor : lookForDoor;
    }

    private int getMissingKeys(Observable maze){
        return maze.getTotalNumberOfKeys() - maze.getKeysFound();
    }

}
