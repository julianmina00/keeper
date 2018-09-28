package com.mina.mk.logic;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.model.PathPosition;
import org.junit.Assert;
import org.junit.Test;
import tws.keeper.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KnowledgeTest {

    @Test
    public void testStackDuplicatedItems(){
        Stack<Integer> stack = new Stack<>();
        Integer a = 1;
        Integer b = a;
        Assert.assertEquals(a,b);
        stack.push(a);
        stack.push(b);
        Assert.assertEquals(stack.size(),2);
        Assert.assertEquals(stack.pop(),stack.pop());
        Assert.assertEquals(stack.size(),0);
    }

    @Test
    public void testAddingKnowledge() {
        Knowledge k = new Knowledge();
        PathPosition position = new PathPosition(1,1);
        position.setVisited(true);
        List<PathPosition> neighbors = new ArrayList<>(2);
        neighbors.add(new PathPosition(2,1));
        neighbors.add(new PathPosition(1,2));
        k.addKnowledge(position, neighbors);
        assertNull(k.getAnyKeyNeighbor(position));
        assertEquals(2, k.getKnownNeighbors(position).size());
    }

    @Test
    public void testRecoveringKnownPosition() {
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1,1);
        position1.setVisited(true);
        List<PathPosition> neighbors = new ArrayList<>(2);
        neighbors.add(new PathPosition(2,1));
        neighbors.add(new PathPosition(1,2));
        k.addKnowledge(position1, neighbors);
        // Second position to add and its neighbor
        PathPosition position2 = new PathPosition(2,1);
        position2.setVisited(true);
        List<PathPosition> neighbors2 = Collections.singletonList(new PathPosition(1,1));
        k.addKnowledge(position2, neighbors2);
        // validates the neighbors of the position1
        List<PathPosition> knownNeighbors1 = k.getKnownNeighbors(position1);
        assertNotNull(knownNeighbors1);
        PathPosition knownNeighbor1 = knownNeighbors1.stream()
                                                   .filter(position2::equals)
                                                   .findFirst()
                                                   .orElse(null);
        assertNotNull(knownNeighbor1);
        assertTrue(knownNeighbor1.isVisited());

        // validates the neighbors of the position2
        List<PathPosition> knownNeighbors2 = k.getKnownNeighbors(position2);
        assertNotNull(knownNeighbors2);
        PathPosition knownNeighbor2 = knownNeighbors2.stream()
                                                     .filter(position1::equals)
                                                     .findFirst()
                                                     .orElse(null);
        assertNotNull(knownNeighbor2);
        assertTrue(knownNeighbor2.isVisited());
    }

    @Test
    public void testUpdatingTwoKnownNeighbors() {
        Knowledge k = new Knowledge();
        PathPosition position1 = new PathPosition(1,1);
        PathPosition position2 = new PathPosition(2,2);
        PathPosition position3 = new PathPosition(2,1);
        List<PathPosition> neighborsOf1 = Collections.singletonList(position3);
        List<PathPosition> neighborsOf2 = Collections.singletonList(position3);
        List<PathPosition> neighborsOf3 = new ArrayList<>(2);
        neighborsOf3.add(position1);
        neighborsOf3.add(position2);

        // Adding position1 to the knowledge base and verifying that everything is ok
        position1.setVisited(true);
        k.addKnowledge(position1, neighborsOf1);
        List<PathPosition> knownNeighborsOf1 = k.getKnownNeighbors(position1);
        assertNotNull(knownNeighborsOf1);
        assertEquals(1,knownNeighborsOf1.size());
        assertEquals(knownNeighborsOf1.get(0), position3);
        assertFalse(knownNeighborsOf1.get(0).isVisited());

        // Adding position2 to the knowledge base and verifying that everything is ok
        position2.setVisited(true);
        k.addKnowledge(position2, neighborsOf2);
        List<PathPosition> knownNeighborsOf2 = k.getKnownNeighbors(position2);
        assertNotNull(knownNeighborsOf2);
        assertEquals(1,knownNeighborsOf2.size());
        assertEquals(knownNeighborsOf2.get(0), position3);
        assertFalse(knownNeighborsOf2.get(0).isVisited());

        // Adding position3 to the knowledge base and verifying that everything is ok
        position3.setVisited(true);
        k.addKnowledge(position3, neighborsOf3);
        List<PathPosition> knownNeighborsOf3 = k.getKnownNeighbors(position3);
        assertNotNull(knownNeighborsOf3);
        assertEquals(2,knownNeighborsOf3.size());
        assertTrue(knownNeighborsOf3.get(0).isVisited());
        assertTrue(knownNeighborsOf3.get(1).isVisited());

        // verifies the new status of the neighbors of position1
        knownNeighborsOf1 = k.getKnownNeighbors(position1);
        assertNotNull(knownNeighborsOf1);
        assertEquals(1, knownNeighborsOf1.size());
        assertTrue(knownNeighborsOf1.get(0).isVisited());

        // verifies the new status of the neighbors of position2
        knownNeighborsOf2 = k.getKnownNeighbors(position2);
        assertNotNull(knownNeighborsOf2);
        assertEquals(1, knownNeighborsOf2.size());
        assertTrue(knownNeighborsOf2.get(0).isVisited());
    }

    @Test
    public void testAddingNullNeighbors() {
        Knowledge k = new Knowledge();
        try{
            PathPosition position = new PathPosition(1, 1);
            k.addKnowledge(position, null);
            assertNotNull(k.getKnownNeighbors(position));
        } catch(Exception ex){
            fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void testBacktrackMemory() {
        Knowledge k = new Knowledge();
        // add position1 to the knowledge base
        PathPosition position1 = new PathPosition(1, 1);
        k.addKnowledge(position1);
        Stack<PathPosition> backtrackMemory = k.getBacktrackMemory();
        assertEquals(position1, backtrackMemory.peek());
        // add position2 to the knowledge base
        PathPosition position2 = new PathPosition(2, 2);
        k.addKnowledge(position2);
        assertEquals(position2, backtrackMemory.peek());
        // add position3 to the knowledge base
        PathPosition position3 = new PathPosition(3, 3);
        k.addKnowledge(position3);
        assertEquals(position3, backtrackMemory.peek());
        // verifies the backtrack memory
        assertEquals(3, backtrackMemory.size());
        assertEquals(position3, backtrackMemory.pop());
        assertEquals(position2, backtrackMemory.pop());
        assertEquals(position1, backtrackMemory.pop());
        assertTrue(backtrackMemory.empty());
    }

    @Test
    public void testFarthestVisitedNeighborInBacktrackMemory() {
        Knowledge k = new Knowledge();
        // add position1 to the knowledge base
        PathPosition position1 = new PathPosition(1, 1);
        position1.setVisited(true);
        k.addKnowledge(position1);
        // add position2 to the knowledge base
        PathPosition position2 = new PathPosition(2, 2);
        position2.setVisited(true);
        k.addKnowledge(position2);
        // add position3 to the knowledge base
        PathPosition position3 = new PathPosition(3, 3);
        position3.setVisited(true);
        k.addKnowledge(position3);
        // add position4 to the knowledge base
        PathPosition position4 = new PathPosition(1, 3);
        position4.setVisited(true);
        List<PathPosition> neighborsOf4 = new ArrayList<>(2);
        neighborsOf4.add(position1);
        neighborsOf4.add(position3);
        k.addKnowledge(position4, neighborsOf4);
        PathPosition farthest =
                k.findTheFarthestVisitedNeighborInBacktrack(position4);
        assertNotNull(farthest);
        assertEquals(position1, farthest);
    }

    @Test
    public void testFarthestVisitedNeighborInBacktrackMemoryV2() {
        Knowledge k = new Knowledge();
        // add position1 to the knowledge base
        PathPosition position1 = new PathPosition(1, 1);
        k.addKnowledge(position1);
        // add position2 to the knowledge base
        PathPosition position2 = new PathPosition(2, 2);
        position2.setVisited(true);
        k.addKnowledge(position2);
        // add position3 to the knowledge base
        PathPosition position3 = new PathPosition(3, 3);
        position3.setVisited(true);
        k.addKnowledge(position3);
        // add position4 to the knowledge base
        PathPosition position4 = new PathPosition(1, 3);
        position4.setVisited(true);
        List<PathPosition> neighborsOf4 = new ArrayList<>(2);
        neighborsOf4.add(position1);
        neighborsOf4.add(position3);
        k.addKnowledge(position4, neighborsOf4);
        PathPosition farthest =
                k.findTheFarthestVisitedNeighborInBacktrack(position4);
        assertNotNull(farthest);
        assertEquals(position3, farthest);
    }

    @Test
    public void testFarthestVisitedNeighborInBacktrackMemoryNoneVisited() {
        Knowledge k = new Knowledge();
        // add position1 to the knowledge base
        PathPosition position1 = new PathPosition(1, 1);
        k.addKnowledge(position1);
        // add position2 to the knowledge base
        PathPosition position2 = new PathPosition(2, 2);
        k.addKnowledge(position2);
        // add position3 to the knowledge base
        PathPosition position3 = new PathPosition(3, 3);
        k.addKnowledge(position3);
        // add position4 to the knowledge base
        PathPosition position4 = new PathPosition(1, 3);
        position4.setVisited(true);
        List<PathPosition> neighborsOf4 = new ArrayList<>(2);
        neighborsOf4.add(position1);
        neighborsOf4.add(position3);
        k.addKnowledge(position4, neighborsOf4);
        PathPosition farthest =
                k.findTheFarthestVisitedNeighborInBacktrack(position4);
        assertNull(farthest);
    }
}
