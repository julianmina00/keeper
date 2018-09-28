package com.mina.mk.solver.logic.strategy;

import com.mina.mk.solver.logic.Knowledge;
import com.mina.mk.solver.model.KnowledgeException;
import com.mina.mk.solver.model.PathPosition;
import org.apache.log4j.Logger;
import tws.keeper.model.Action;

public class FindKeysStrategy extends AbstractStrategy {

    private Logger logger = Logger.getLogger(FindKeysStrategy.class);

    public FindKeysStrategy(Knowledge keeperKnowledge) {
        super(keeperKnowledge);
    }

    @Override
    public Action nextMove() throws KnowledgeException {
        PathPosition currentPosition = getCurrentPosition();
        PathPosition keyNeighbor = getKnowledge().getAnyKeyNeighbor(currentPosition);
        if(keyNeighbor != null){
            logger.info("Moving from "+currentPosition+" to the key neighbor "+keyNeighbor);
            return getActionToMove(currentPosition, keyNeighbor);
        }
        return toNextUnknownPosition(currentPosition);
    }


}
