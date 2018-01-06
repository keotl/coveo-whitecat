package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeighbourFinder {

    private GameMap gameMap;
    private final int myId;

    public NeighbourFinder(GameMap gameMap, int myId) {
        this.gameMap = gameMap;
        this.myId = myId;
    }

    public Location getWeakestEnemy(Location location, GameMap gameMap) {
        List<Location> locations = new ArrayList<>();
        locations.add(gameMap.getLocation(location, Direction.EAST));
        locations.add(gameMap.getLocation(location, Direction.WEST));
        locations.add(gameMap.getLocation(location, Direction.SOUTH));
        locations.add(gameMap.getLocation(location, Direction.NORTH));

        Location weakest = locations.get(0);
        for (Location tempLocation : locations) {
            if (tempLocation.isEnemy(myId) && tempLocation.getSite().strength < weakest.getSite().strength) {
                weakest = tempLocation;
            }
        }
        if(weakest.isEnemy(myId)) {
            return weakest;
        }
        return location;
    }

    public boolean isSurroundedByFriends(Location location) {
        return Direction.getAllDirections().stream().map(direction -> gameMap.getLocation(location, direction)).noneMatch(l -> l.isEnemy(myId));

    }

}
