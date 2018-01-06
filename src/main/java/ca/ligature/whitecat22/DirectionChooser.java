package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;

public class DirectionChooser {

    private Location location;
    private GameMap gameMap;
    private int myID;

    public DirectionChooser(Location location, GameMap gameMap, int myID) {
        this.gameMap = gameMap;
        this.location = location;
        this.myID =  myID;
    }

    public Direction chooseDirection(NeighbourFinder neighbourFinder){

        if (neighbourFinder.isSurroundedByFriends(location) && location.getSite().strength > 15) {
            return pushToBorder();
        }

        return goToWeakerSide();
    }

    private Direction goToWeakerSide() {
        List<Location> possibleLocation = new ArrayList<>();

        Location east = gameMap.getLocation(location, Direction.EAST);
        Location west = gameMap.getLocation(location, Direction.WEST);
        Location south = gameMap.getLocation(location, Direction.SOUTH);
        Location north = gameMap.getLocation(location, Direction.NORTH);

        if (location.isStrongerThan(east) && east.isEnemy(myID)) {
            possibleLocation.add(east);
        }
        if (location.isStrongerThan(west) && west.isEnemy(myID)) {
            possibleLocation.add(west);
        }
        if (location.isStrongerThan(south) && south.isEnemy(myID)) {
            possibleLocation.add(south);
        }
        if (location.isStrongerThan(north) && north.isEnemy(myID)) {
            possibleLocation.add(north);
        }

        if (!possibleLocation.isEmpty()) {
            Location weakestLocation = possibleLocation.get(0);
            for (Location location : possibleLocation) {
                if (weakestLocation.getSite().strength > location.getSite().strength) {
                    weakestLocation = location;
                }
            }
            return location.getDirectionTo(weakestLocation);
        }

        return Direction.STILL;

    }

    public Direction pushToBorder() {
        Direction closestBorderDirection = Direction.STILL;
        int closestBorderDistance = 1000;

        for (Direction direction : Direction.getAllDirections()) {
            int distance = countBorder(direction);
            if (distance < closestBorderDistance) {
                closestBorderDistance = distance;
                closestBorderDirection = direction;
            }
        }
        return closestBorderDirection;
    }

    public int countBorder(Direction direction) {
        int distance = 0;
        Location locationInterator = location;
        int iterationCount = 0;
        while (locationInterator.getSite().owner == myID && iterationCount < 100) {
            iterationCount++;
            if (gameMap.getLocation(locationInterator, direction).getSite().owner == myID) {
                distance++;
                Location nextLocation = gameMap.getLocation(locationInterator, direction);
                locationInterator = nextLocation;
            } else {
                break;
            }

        }
        return distance;
    }
}
