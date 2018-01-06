package ca.ligature.whitecat22;

import ca.ligature.whitecat22.worker.BulletController;

import java.util.ArrayList;
import java.util.List;

public class MasterMind {

    private static NeighbourFinder neighbourFinder;
    private static BulletController bulletController;
    private static Location startingBlock;

    private static int myId;

    public static void main(String[] args) throws java.io.IOException {

        final InitPackage iPackage = Networking.getInit();
        myId = iPackage.myID;
        final GameMap gameMap = iPackage.map;
        startingBlock = getStartingBlock(myId, gameMap);

        bulletController = new BulletController(startingBlock.toPosition(), myId);

        Networking.sendInit("MyJavaBot");

        while (true) {
            Networking.updateFrame(gameMap);

            neighbourFinder = new NeighbourFinder(gameMap, myId);
            List<Move> moves = new ArrayList<Move>();

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();


                    if (site.owner == myId) {
                        if (!bulletController.isBullet(location)) {
                            moves.add(new Move(location, whereToMove(location, gameMap, myId)));
                        }
                    }
                }
            }
            moves.addAll(bulletController.getMoves(gameMap));
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
        int iterationCount = 0;
        while (locationInterator.getSite().owner == myId && iterationCount < 100) {
            iterationCount++;
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
        try {

            Double bleg = gameMap.getDistance(location, getStartingBlock(myID, gameMap));

            if (bleg > gameMap.width * 0.15 && bleg < gameMap.width * 0.18){
                if (location.getSite().strength < 70){
                    return Direction.STILL;
                }
            }

            Location east = gameMap.getLocation(location, Direction.EAST);
            Location west = gameMap.getLocation(location, Direction.WEST);
            Location south = gameMap.getLocation(location, Direction.SOUTH);
            Location north = gameMap.getLocation(location, Direction.NORTH);

            Site site = location.getSite();

            if (neighbourFinder.isSurroundedByFriends(location) && location.getSite().strength > getStrengthLimit(gameMap)) {
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
        } catch (Exception e) {
            return Direction.randomDirection();
        }
    }

    private static int getStrengthLimit(GameMap gameMap) {
        return 15;
    }

    private static int getControlledTilesPercentage(GameMap gameMap) {
        int totalTiles = gameMap.height * gameMap.width;
        int controlledTiles = 0;

        for (int i = 0; i < gameMap.width; i++) {
            for (int j = 0; j < gameMap.height; j++) {
                if (gameMap.getLocation(i,j).isFriend(myId)) {
                    controlledTiles++;
                }
            }
        }

        return (controlledTiles / totalTiles) * 100;
    }

    private static int calculateShareThreshold(GameMap gameMap) {
        int totalTiles = gameMap.height * gameMap.width;
        int controlledTiles = 0;

        for (int i = 0; i < gameMap.width; i++) {
            for (int j = 0; j < gameMap.height; j++) {
                if (gameMap.getLocation(i,j).isFriend(myId)) {
                    controlledTiles++;
                }
            }
        }
        int ratio = controlledTiles / totalTiles;
        return (ratio * 10) + 5;
    }

    private static Location getStartingBlock(int myID, GameMap gameMap) {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                final Location location = gameMap.getLocation(x, y);
                final Site site = location.getSite();
                if(site.owner == myID) {
                    return location;
                }
            }
        }
        return null;
    }
}

