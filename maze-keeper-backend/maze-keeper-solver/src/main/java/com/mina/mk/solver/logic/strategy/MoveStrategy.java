package com.mina.mk.solver.logic.strategy;

import com.mina.mk.solver.model.KnowledgeException;
import tws.keeper.model.Action;

public interface MoveStrategy {

    Action nextMove() throws KnowledgeException;

}
