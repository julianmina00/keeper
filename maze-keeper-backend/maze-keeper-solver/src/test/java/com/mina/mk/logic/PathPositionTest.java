package com.mina.mk.logic;

import com.mina.mk.solver.model.PathPosition;
import org.junit.Test;
import tws.keeper.model.Position;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathPositionTest {

    @Test
    public void testPositionEquals() {
        Position position = new Position(1,1);
        PathPosition pathPosition = new PathPosition(1,1);
        assertTrue(position.equals(pathPosition));
        assertFalse(pathPosition.equals(position));
        assertTrue(pathPosition.equals(new PathPosition(position)));
    }

    @Test
    public void testIsUp() {
        PathPosition position1 = new PathPosition(2,1);
        PathPosition position2 = new PathPosition(2,2);
        assertTrue(position1.isUp(position2));
        assertFalse(position2.isUp(position1));
    }

    @Test
    public void testIsDown() {
        PathPosition position1 = new PathPosition(2,1);
        PathPosition position2 = new PathPosition(2,2);
        assertFalse(position1.isDown(position2));
        assertTrue(position2.isDown(position1));
    }

    @Test
    public void testIsLeft() {
        PathPosition position1 = new PathPosition(1,2);
        PathPosition position2 = new PathPosition(2,2);
        assertTrue(position1.isLeft(position2));
        assertFalse(position2.isLeft(position1));
    }

    @Test
    public void testIsRight() {
        PathPosition position1 = new PathPosition(1,2);
        PathPosition position2 = new PathPosition(2,2);
        assertFalse(position1.isRight(position2));
        assertTrue(position2.isRight(position1));
    }
}
