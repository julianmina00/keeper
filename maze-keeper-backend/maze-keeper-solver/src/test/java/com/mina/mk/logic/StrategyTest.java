package com.mina.mk.logic;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.logic.strategy.FindKeysStrategy;
import com.mina.mk.solver.logic.strategy.GoDoorStrategy;
import com.mina.mk.solver.logic.strategy.MoveStrategy;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static tws.keeper.model.Action.GO_DOWN;
import static tws.keeper.model.Action.GO_LEFT;
import static tws.keeper.model.Action.GO_RIGHT;

public class StrategyTest {

    @Test
    public void testMoveToKey() throws KnowledgeException {
        // Creates the knowledge base
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        position1.setKey(true);
        PathPosition position2 = new PathPosition(2, 1);
        position2.setVisited(true);
        PathPosition position3 = new PathPosition(3, 1);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        k.addKnowledge(position2, neighborsOf2);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertEquals(GO_LEFT, strategy.nextMove());
    }

    @Test
    public void testMoveToAnyKey() throws KnowledgeException {
        // Creates the knowledge base
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        position1.setKey(true);
        PathPosition position2 = new PathPosition(2, 1);
        position2.setVisited(true);
        PathPosition position3 = new PathPosition(3, 1);
        position3.setKey(true);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        k.addKnowledge(position2, neighborsOf2);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertTrue(GO_LEFT.equals(strategy.nextMove()) || GO_RIGHT.equals(strategy.nextMove()));
    }

    @Test
    public void testMoveToNotVisited() throws KnowledgeException {
        // Creates the knowledge base
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        position1.setVisited(true);
        PathPosition position2 = new PathPosition(2, 1);
        position2.setVisited(true);
        PathPosition position3 = new PathPosition(3, 1);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        k.addKnowledge(position2, neighborsOf2);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertEquals(GO_RIGHT, strategy.nextMove());
    }

    @Test
    public void testMoveToAnyNotVisited() throws KnowledgeException {
        // Creates the knowledge base
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        PathPosition position2 = new PathPosition(2, 1);
        position2.setVisited(true);
        PathPosition position3 = new PathPosition(3, 1);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        k.addKnowledge(position2, neighborsOf2);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertTrue(GO_LEFT.equals(strategy.nextMove()) || GO_RIGHT.equals(strategy.nextMove()));
    }

    @Test
    public void testEndOfPath() throws KnowledgeException {
        // Creates the knowledge base
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        PathPosition position2 = new PathPosition(2, 1);
        position1.setVisited(true);
        List<PathPosition> neighborsOf1 = Collections.singletonList(position2);
        k.addKnowledge(position1, neighborsOf1);
        // adds position2 as if it was another movement
        position2.setVisited(true);
        List<PathPosition> neighborsOf2 = Collections.singletonList(position1);
        k.addKnowledge(position2, neighborsOf2);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertEquals(GO_LEFT, strategy.nextMove());
        assertFalse(k.getBacktrackMemory().contains(position2));
        assertFalse(k.getBacktrackMemory().contains(position1));
    }

    @Test
    public void testTheTheFarthestVisitedNeighborInBacktrack() throws KnowledgeException {
        Knowledge k = new Knowledge();
        // initialize positions
        PathPosition position0 = new PathPosition(0, 1);
        PathPosition position1 = new PathPosition(1, 1);
        PathPosition position2 = new PathPosition(1, 2);
        PathPosition position3 = new PathPosition(1, 3);
        PathPosition position4 = new PathPosition(2, 1);
        PathPosition position5 = new PathPosition(2, 3);
        PathPosition position6 = new PathPosition(3, 1);
        PathPosition position7 = new PathPosition(3, 2);
        PathPosition position8 = new PathPosition(3, 3);
        // initialize neighbors
        List<PathPosition> neighborsOf0 = Collections.singletonList(position1);
        List<PathPosition> neighborsOf1 = new ArrayList<>(3);
        neighborsOf1.add(position0);
        neighborsOf1.add(position2);
        neighborsOf1.add(position4);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        List<PathPosition> neighborsOf3 = new ArrayList<>(2);
        neighborsOf3.add(position2);
        neighborsOf3.add(position5);
        List<PathPosition> neighborsOf4 = new ArrayList<>(2);
        neighborsOf4.add(position1);
        neighborsOf4.add(position6);
        List<PathPosition> neighborsOf5 = new ArrayList<>(2);
        neighborsOf5.add(position3);
        neighborsOf5.add(position8);
        List<PathPosition> neighborsOf6 = new ArrayList<>(2);
        neighborsOf6.add(position4);
        neighborsOf6.add(position7);
        List<PathPosition> neighborsOf7 = new ArrayList<>(2);
        neighborsOf7.add(position6);
        neighborsOf7.add(position8);
        List<PathPosition> neighborsOf8 = new ArrayList<>(2);
        neighborsOf8.add(position7);
        neighborsOf8.add(position5);
        // adds information to the knowledge base
        position0.setVisited(true);
        k.addKnowledge(position0, neighborsOf0);
        position1.setVisited(true);
        k.addKnowledge(position1, neighborsOf1);
        position2.setVisited(true);
        k.addKnowledge(position2, neighborsOf2);
        position3.setVisited(true);
        k.addKnowledge(position3, neighborsOf3);
        position5.setVisited(true);
        k.addKnowledge(position5, neighborsOf5);
        position8.setVisited(true);
        k.addKnowledge(position8, neighborsOf8);
        position7.setVisited(true);
        k.addKnowledge(position7, neighborsOf7);
        position6.setVisited(true);
        k.addKnowledge(position6, neighborsOf6);
        position4.setVisited(true);
        k.addKnowledge(position4, neighborsOf4);
        // test the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertEquals(GO_LEFT, strategy.nextMove());
        assertFalse(k.getBacktrackMemory().empty());
        assertFalse(k.getBacktrackMemory().contains(position4));
        assertFalse(k.getBacktrackMemory().contains(position6));
        assertFalse(k.getBacktrackMemory().contains(position7));
        assertFalse(k.getBacktrackMemory().contains(position8));
        assertFalse(k.getBacktrackMemory().contains(position5));
        assertFalse(k.getBacktrackMemory().contains(position3));
        assertFalse(k.getBacktrackMemory().contains(position2));
        assertFalse(k.getBacktrackMemory().contains(position1));
        assertEquals(position0, k.getBacktrackMemory().peek());
    }

    @Test
    public void testRollingBackThePath() throws KnowledgeException {
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1, 1);
        PathPosition position2 = new PathPosition(2, 1);
        PathPosition position3 = new PathPosition(2, 2);
        List<PathPosition> neighborsOf1 = Collections.singletonList(position2);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(position1);
        neighborsOf2.add(position3);
        List<PathPosition> neighborsOf3 = Collections.singletonList(position2);
        // adding info to the knowledge base
        position3.setVisited(true);
        k.addKnowledge(position3, neighborsOf3);
        position2.setVisited(true);
        k.addKnowledge(position2, neighborsOf2);
        position1.setVisited(true);
        k.addKnowledge(position1, neighborsOf1);
        // validate the strategy
        MoveStrategy strategy = new FindKeysStrategy(k);
        assertEquals(GO_RIGHT, strategy.nextMove());
        k.addKnowledge(position2, neighborsOf2);
        assertEquals(GO_DOWN, strategy.nextMove());
        assertTrue(k.getBacktrackMemory().empty());
    }

    @Test
    public void testShortestPathToDoor() throws KnowledgeException {
        Knowledge k = new Knowledge();
        PathPosition p1 = new PathPosition(1,1);
        PathPosition p2 = new PathPosition(2,1);
        PathPosition p3 = new PathPosition(3,1);
        PathPosition p4 = new PathPosition(4,1);
        PathPosition p5 = new PathPosition(1,2);
        PathPosition p6 = new PathPosition(3,2);
        PathPosition p7 = new PathPosition(1,3);
        PathPosition p8 = new PathPosition(3,3);
        PathPosition p9 = new PathPosition(1,4);
        PathPosition p10 = new PathPosition(2,4);
        PathPosition p11 = new PathPosition(3,4);
        p1.setVisited(true);
        p2.setVisited(true);
        p3.setVisited(true);
        p4.setVisited(true);
        p5.setVisited(true);
        p6.setVisited(true);
        p7.setVisited(true);
        p8.setVisited(true);
        p9.setVisited(true);
        p10.setVisited(true);
        p11.setVisited(true);
        List<PathPosition> neighborsOf1 = new ArrayList<>(2);
        neighborsOf1.add(p2);
        neighborsOf1.add(p5);
        List<PathPosition> neighborsOf2 = new ArrayList<>(2);
        neighborsOf2.add(p1);
        neighborsOf2.add(p3);
        List<PathPosition> neighborsOf3 = new ArrayList<>(3);
        neighborsOf3.add(p2);
        neighborsOf3.add(p4);
        neighborsOf3.add(p6);
        List<PathPosition> neighborsOf4 = Collections.singletonList(p3);
        List<PathPosition> neighborsOf5 = new ArrayList<>(2);
        neighborsOf5.add(p1);
        neighborsOf5.add(p7);
        List<PathPosition> neighborsOf6 = new ArrayList<>(2);
        neighborsOf6.add(p3);
        neighborsOf6.add(p8);
        List<PathPosition> neighborsOf7 = new ArrayList<>(2);
        neighborsOf7.add(p5);
        neighborsOf7.add(p9);
        List<PathPosition> neighborsOf8 = new ArrayList<>(2);
        neighborsOf8.add(p6);
        neighborsOf8.add(p11);
        List<PathPosition> neighborsOf9 = new ArrayList<>(2);
        neighborsOf9.add(p7);
        neighborsOf9.add(p10);
        List<PathPosition> neighborsOf10 = new ArrayList<>(2);
        neighborsOf10.add(p9);
        neighborsOf10.add(p11);
        List<PathPosition> neighborsOf11 = new ArrayList<>(2);
        neighborsOf11.add(p10);
        neighborsOf11.add(p8);
        k.setDoorPosition(p7);
        k.addKnowledge(p3, neighborsOf3);
        k.addKnowledge(p6, neighborsOf6);
        k.addKnowledge(p8, neighborsOf8);
        k.addKnowledge(p11, neighborsOf11);
        k.addKnowledge(p10, neighborsOf10);
        k.addKnowledge(p9, neighborsOf9);
        k.addKnowledge(p7, neighborsOf7);
        k.addKnowledge(p5, neighborsOf5);
        k.addKnowledge(p1, neighborsOf1);
        k.addKnowledge(p2, neighborsOf2);
        MoveStrategy strategy = new GoDoorStrategy(k);
        assertEquals(GO_LEFT, strategy.nextMove());
        k.addKnowledge(p1, neighborsOf1);
        assertEquals(GO_DOWN, strategy.nextMove());
        k.addKnowledge(p5, neighborsOf5);
        assertEquals(GO_DOWN, strategy.nextMove());
        k.addKnowledge(p7, neighborsOf7);
        assertEquals(k.getCurrentPosition(), k.getDoorPosition());
    }
}
