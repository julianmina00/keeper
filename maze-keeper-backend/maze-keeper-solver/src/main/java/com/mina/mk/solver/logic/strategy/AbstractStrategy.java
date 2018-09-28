package com.mina.mk.solver.logic.strategy;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import org.apache.log4j.Logger;
import tws.keeper.model.Action;

import static tws.keeper.model.Action.DO_NOTHING;
import static tws.keeper.model.Action.GO_DOWN;
import static tws.keeper.model.Action.GO_LEFT;
import static tws.keeper.model.Action.GO_RIGHT;
import static tws.keeper.model.Action.GO_UP;

public abstract class AbstractStrategy implements MoveStrategy {

    private Logger logger = Logger.getLogger(AbstractStrategy.class);

    private Knowledge knowledge;

    public AbstractStrategy(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    protected Action toNextUnknownPosition(PathPosition currentPosition) throws KnowledgeException {
        PathPosition notVisitedNeighbor =
                getKnowledge().getAnyNotVisitedNeighbor(currentPosition);
        if(notVisitedNeighbor != null){
            logger.info("Moving from "+currentPosition+" to the non visited position "+notVisitedNeighbor);
            return getActionToMove(currentPosition, notVisitedNeighbor);
        }
        if(isEndOfPath()){
            currentPosition = getKnowledge().getBacktrackMemory().pop();
            PathPosition nextPosition = getKnowledge().getBacktrackMemory().pop();
            logger.info("Moving back from "+currentPosition+" to "+nextPosition+" due to the end of the path");
            return getActionToMove(currentPosition, nextPosition);
        }
        PathPosition nextNeighbor =
                getKnowledge().findTheFarthestVisitedNeighborInBacktrack(currentPosition);
        getKnowledge().rollbackMemoryUntil(nextNeighbor);
        logger.info("Moving from "+currentPosition+" to the farthest visited neighbor "+nextNeighbor);
        return getActionToMove(currentPosition, nextNeighbor);
    }

    protected Action getActionToMove(PathPosition position, PathPosition neighbor){
        if(neighbor.isUp(position)) return GO_UP;
        if(neighbor.isDown(position)) return GO_DOWN;
        if(neighbor.isLeft(position)) return GO_LEFT;
        if(neighbor.isRight(position)) return GO_RIGHT;
        return DO_NOTHING;
    }

    protected boolean isEndOfPath() throws KnowledgeException {
        PathPosition currentPosition = getCurrentPosition();
        return knowledge.getKnownNeighbors(currentPosition).size() == 1 && !knowledge.hasAnyNotVisitedNeighbor(currentPosition);
    }

    protected PathPosition getCurrentPosition() throws KnowledgeException {
        PathPosition currentPosition = knowledge.getCurrentPosition();
        if(currentPosition == null){
            throw new KnowledgeException("The keeper does not know its current position");
        }
        return currentPosition;
    }

    protected Knowledge getKnowledge() {
        return knowledge;
    }
}
