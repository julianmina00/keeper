package com.mina.mk.solver.logic.strategy;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import org.apache.log4j.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import tws.keeper.model.Action;

import java.util.List;

import static tws.keeper.model.Action.DO_NOTHING;

public class GoDoorStrategy extends AbstractStrategy {

    private Logger logger = Logger.getLogger(GoDoorStrategy.class);

    public GoDoorStrategy(Knowledge keeperKnowledge) {
        super(keeperKnowledge);
    }

    @Override
    public Action nextMove() throws KnowledgeException {
        PathPosition currentPosition = getCurrentPosition();
        PathPosition doorNeighbor = getKnowledge().getAnyDoorNeighbor(currentPosition);
        if(doorNeighbor != null){
            logger.info("Moving from "+currentPosition+" to the door neighbor "+doorNeighbor);
            return getActionToMove(currentPosition, doorNeighbor);
        }
        return nextActionToReachDoor(currentPosition);
    }

    private Action nextActionToReachDoor(PathPosition currentPosition) {
        SimpleGraph<PathPosition, DefaultEdge> graph = createGraphFromKnowledge();
        GraphPath<PathPosition, DefaultEdge> path =
                DijkstraShortestPath.findPathBetween(graph, currentPosition, getKnowledge().getDoorPosition());
        List<PathPosition> vertexList = path.getVertexList();
        if(vertexList != null && !vertexList.isEmpty()){
            vertexList.remove(currentPosition);
            logger.info("Moving from "+currentPosition+" to the next neighbor to reach the door");
            return getActionToMove(currentPosition, vertexList.get(0));
        }
        return DO_NOTHING;
    }

    private SimpleGraph<PathPosition, DefaultEdge> createGraphFromKnowledge() {
        SimpleGraph<PathPosition, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        getKnowledge().getKnownPositions().forEach(position -> {
            if(!graph.containsVertex(position)){
                graph.addVertex(position);
            }
            getKnowledge().getKnownNeighbors(position).forEach(neighbor -> {
                if(!graph.containsVertex(neighbor)){
                    graph.addVertex(neighbor);
                }
                if(!graph.containsEdge(position, neighbor)){
                    graph.addEdge(position, neighbor);
                }
            });
        });
        return graph;
    }
}
