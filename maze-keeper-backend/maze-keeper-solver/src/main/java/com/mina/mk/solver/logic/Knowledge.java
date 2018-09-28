package com.mina.mk.solver.logic;

import com.mina.mk.solver.model.PathPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Knowledge {

    private PathPosition doorPosition;
    private Stack<PathPosition> backtrackMemory;
    private Map<PathPosition, List<PathPosition>> knowledgeBase;

    public Knowledge() {
        doorPosition = null;
        backtrackMemory = new Stack<>();
        knowledgeBase = new HashMap<>();
    }

    public void addKnowledge(PathPosition position){
        addKnowledge(position, null);
    }

    public void addKnowledge(PathPosition position, List<PathPosition> neighbors){
        PathPosition knownPosition = getKnownPosition(position);
        List<PathPosition> knownNeighbors = new ArrayList<>();
        if(neighbors != null){
            neighbors.forEach(neighbor -> {
                PathPosition known = getKnownPosition(neighbor);
                knownNeighbors.add(known != null ? known : neighbor);
            });
        }
        if(knownPosition == null){
            knownPosition = position;
        }
        else{
            knownPosition.setVisited(position.isVisited());
            knownPosition.setKey(position.hasKey());
        }
        knowledgeBase.put(knownPosition, knownNeighbors);
        backtrackMemory.push(knownPosition);
    }

    private PathPosition getKnownPosition(PathPosition position){
        PathPosition knownPosition = getKnownPositionIfExists(position);
        if(knownPosition != null){
            return knownPosition;
        }
        return getKnownPositionInNeighbors(position);
    }

    private PathPosition getKnownPositionIfExists(PathPosition position){
        if(!knowledgeBase.containsKey(position)){
            return null;
        }
        return knowledgeBase.keySet().stream().filter(position::equals).findFirst().orElse(null);
    }

    private PathPosition getKnownPositionInNeighbors(PathPosition position){
        List<PathPosition> knownNeighbors = knowledgeBase.values()
                                                         .stream()
                                                         .filter(pathPositions -> pathPositions.contains(position))
                                                         .findFirst()
                                                         .orElse(null);
        if(knownNeighbors == null){
            return null;
        }
        return knownNeighbors.stream().filter(position::equals).findFirst().orElse(null);
    }

    public boolean hasAnyNotVisitedNeighbor(PathPosition position){
        List<PathPosition> neighbors = knowledgeBase.get(position);
        if(neighbors != null){
            for (PathPosition neighbor : neighbors) {
                if(!neighbor.isVisited()){
                    return true;
                }
            }
        }
        return false;
    }

    public PathPosition getAnyNotVisitedNeighbor(PathPosition position){
        Predicate<PathPosition> filter = neighbor -> !neighbor.isVisited();
        return getAnyNeighbor(position, filter);
    }

    public PathPosition getAnyKeyNeighbor(PathPosition position){
        Predicate<PathPosition> filter = PathPosition::hasKey;
        return getAnyNeighbor(position, filter);
    }

    public PathPosition getAnyDoorNeighbor(PathPosition position) {
        Predicate<PathPosition> filter =
                neighbor -> doorPosition != null && doorPosition.equals(neighbor);
        return getAnyNeighbor(position, filter);
    }

    private PathPosition getAnyNeighbor(PathPosition position, Predicate<PathPosition> predicate){
        List<PathPosition> neighbors = knowledgeBase.get(position);
        return neighbors.stream().filter(predicate).findAny().orElse(null);
    }

    public PathPosition findTheFarthestVisitedNeighborInBacktrack(PathPosition position){
        List<PathPosition> neighbors = knowledgeBase.get(position);
        Map<PathPosition, Integer> distanceInBacktrack = neighbors.stream()
                                                      .filter(PathPosition::isVisited)
                                                      .collect(Collectors.toMap(neighbor -> neighbor, neighbor -> backtrackMemory
                                                              .search(neighbor)));
        Optional<Entry<PathPosition, Integer>> farthest = Optional.empty();
        for(Entry<PathPosition, Integer> entry : distanceInBacktrack.entrySet()) {
            if((!farthest.isPresent() && entry.getValue() >= 0) ||
                    (farthest.isPresent() && farthest.get().getValue() < entry.getValue())){
                farthest = Optional.of(entry);
            }
        }
        return farthest.map(Entry::getKey).orElse(null);
    }

    public void rollbackMemoryUntil(PathPosition position){
        while(!backtrackMemory.empty() && backtrackMemory.contains(position)){
            backtrackMemory.pop();
        }
    }

    public Set<PathPosition> getKnownPositions() {
        return knowledgeBase.keySet();
    }

    public List<PathPosition> getKnownNeighbors(PathPosition position){
        return knowledgeBase.get(position);
    }

    public PathPosition getDoorPosition() {
        return doorPosition;
    }

    public void setDoorPosition(PathPosition doorPosition) {
        this.doorPosition = doorPosition;
    }

    public Stack<PathPosition> getBacktrackMemory() {
        return backtrackMemory;
    }

    public PathPosition getCurrentPosition(){
        if(backtrackMemory.empty()){
            return null;
        }
        return backtrackMemory.peek();
    }
}
