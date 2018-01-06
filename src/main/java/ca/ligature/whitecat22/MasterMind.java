package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;

public class MasterMind {


    public static void main(String[] args) throws java.io.IOException {

        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        while(true) {
            List<Move> moves = new ArrayList<Move>();

            Networking.updateFrame(gameMap);

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();


                    if(site.owner == myID) {
                        moves.add(new Move(location, whereToMove(location, gameMap, myID)));
                    }
                }
            }
            Networking.sendFrame(moves);
        }

    }


    /*private static Direction pushToBorder(Location location, GameMap gameMap, int myId) {
        List<Integer> eastWestSouthNorthDistance = new ArrayList<>();
        int distanceEast = 0;
        Location locationInterator =  location;
        while (locationInterator.getSite().owner == myId) {
            if (gameMap.getLocation(locationInterator, Direction.EAST).getSite().owner == myId) {
                distanceEast ++;
                Location nextLocation = new Location()
                locationInterator =
            }
        }

    }*/

    private static Direction whereToMove(Location location, GameMap gameMap, int myID) {

        Location east = gameMap.getLocation(location, Direction.EAST);
        Location west = gameMap.getLocation(location, Direction.WEST);
        Location south = gameMap.getLocation(location, Direction.SOUTH);
        Location north = gameMap.getLocation(location, Direction.NORTH);

        Site site = location.getSite();

        if (site.strength > east.getSite().strength) {
            if(east.getSite().owner != myID) {
                return Direction.EAST;
            }
        }
        if (site.strength > west.getSite().strength) {
            if(west.getSite().owner != myID) {
                return Direction.WEST;
            }
        }
        if (site.strength > south.getSite().strength) {
            if(south.getSite().owner != myID) {
                return Direction.SOUTH;
            }
        }
        if (site.strength > north.getSite().strength) {
            if(north.getSite().owner != myID) {
                return Direction.NORTH;
            }
        }

        return Direction.STILL;
    }

}
