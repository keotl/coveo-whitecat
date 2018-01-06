package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NeighbourFinder {

    private GameMap gameMap;
    private final int myId;
    private Random randomGenerator;

    public NeighbourFinder(GameMap gameMap, int myId) {
        this.gameMap = gameMap;
        this.myId = myId;
    }

    public Location getWeakestEnemyNeighbour(Location location) {
        List<Position> possibleNeighbours = location.getPossibleNeighbours();

        List<Location> enemies = possibleNeighbours.stream().map(gameMap::getLocation).filter(tile -> tile.isEnemy(myId)).collect(Collectors.toList());

        Location weakest = getWeakest(enemies);
        if (weakest == null){
            int random = randomGenerator.nextInt(possibleNeighbours.size());
            weakest = gameMap.getLocation(possibleNeighbours.get(random));
        }
        return weakest;
    }

    private Location getWeakest(List<Location> enemies) {
        Location weakest = enemies.get(0);
        for (Location neighbour : enemies) {
            if (neighbour.getSite().strength < weakest.getSite().strength) {
                weakest = neighbour;
            }
        }
        return weakest;
    }

}
