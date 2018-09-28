package com.mina.mk.solver.app;

import com.mina.mk.solver.logic.SmartKeeper;
import tws.keeper.model.Keeper;
import tws.keeper.model.Maze;

public class Application {

    public static void main(String[] args) {
        Keeper keeper = new SmartKeeper();
        Maze maze = new Maze(keeper);
        while(!maze.isMazeCompleted()){
            maze.makeKeeperAct();
        }
        System.out.println("Maze completed!");
    }

}
