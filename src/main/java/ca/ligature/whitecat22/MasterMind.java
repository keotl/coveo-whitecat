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

            Networking.updateFrame(gameMap);

            neighbourFinder = new NeighbourFinder(gameMap, myID);
            List<Move> moves = new ArrayList<Move>();

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

        if (neighbourFinder.isSurroundedByFriends(location) && location.getSite().strength > 15) {
            return pushToBorder(location, gameMap, myID);
        }

        return chooseDirection(location, gameMap);
    }

    private static Direction chooseDirection(Location location, GameMap gameMap) {
        if(neighbourFinder.isSurroundedByFriends(location)) {
            return Direction.STILL;
        }

        Location weakestLocation = neighbourFinder.getWeakestEnemy(location, gameMap);

        if (location.getSite().strength > weakestLocation.getSite().strength) {
            return location.getDirectionTo(weakestLocation);
        }

        return Direction.STILL;

    }


}
