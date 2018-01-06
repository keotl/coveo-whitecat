package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;

public class MasterMind {

    private static NeighbourFinder neighbourFinder;

    public static void main(String[] args) throws java.io.IOException {

        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;



        Networking.sendInit("MyJavaBot");

        while (true) {
            neighbourFinder = new NeighbourFinder(gameMap, myID);
            List<Move> moves = new ArrayList<Move>();

            Networking.updateFrame(gameMap);

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();


                    if (site.owner == myID) {
                        moves.add(new Move(location, whereToMove(location, gameMap, myID)));
                    }
                }
            }
            Networking.sendFrame(moves);
        }

    }

    private static Direction pushToBorder(Location location, GameMap gameMap, int myId) {
        Direction closestBorderDirection = Direction.STILL;
        int closestBorderDistance = 1000;

        for (Direction direction : Direction.getAllDirections()) {
            int distance = countBorder(direction, location, myId, gameMap);
            if (distance < closestBorderDistance) {
                closestBorderDistance = distance;
                closestBorderDirection = direction;
            }
        }
        return closestBorderDirection;
    }


    private static Integer countBorder(Direction direction, Location location, int myId, GameMap gameMap) {
        int distance = 0;
        Location locationInterator = location;
        while (locationInterator.getSite().owner == myId) {
            if (gameMap.getLocation(locationInterator, direction).getSite().owner == myId) {
                distance++;
                Location nextLocation = gameMap.getLocation(locationInterator, direction);
                locationInterator = nextLocation;
            } else {
                break;
            }

        }
        return distance;
    }

    private static Direction whereToMove(Location location, GameMap gameMap, int myID) {

        Location east = gameMap.getLocation(location, Direction.EAST);
        Location west = gameMap.getLocation(location, Direction.WEST);
        Location south = gameMap.getLocation(location, Direction.SOUTH);
        Location north = gameMap.getLocation(location, Direction.NORTH);

        Site site = location.getSite();

        if (neighbourFinder.isSurroundedByFriends(location) && location.getSite().strength > 15) {
            return pushToBorder(location, gameMap, myID);
        }

        if (site.strength > east.getSite().strength) {
            if (east.getSite().owner != myID) {
                return Direction.EAST;
            }
        }
        if (site.strength > west.getSite().strength) {
            if (west.getSite().owner != myID) {
                return Direction.WEST;
            }
        }
        if (site.strength > south.getSite().strength) {
            if (south.getSite().owner != myID) {
                return Direction.SOUTH;
            }
        }
        if (site.strength > north.getSite().strength) {
            if (north.getSite().owner != myID) {
                return Direction.NORTH;
            }
        }


        return Direction.STILL;
    }


}
