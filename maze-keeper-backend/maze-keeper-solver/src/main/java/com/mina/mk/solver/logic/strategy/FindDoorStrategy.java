package com.mina.mk.solver.logic.strategy;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import org.apache.log4j.Logger;
import tws.keeper.model.Action;

public class FindDoorStrategy extends AbstractStrategy {

    private Logger logger = Logger.getLogger(FindDoorStrategy.class);

    public FindDoorStrategy(Knowledge keeperKnowledge) {
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
        return toNextUnknownPosition(currentPosition);
    }
}
