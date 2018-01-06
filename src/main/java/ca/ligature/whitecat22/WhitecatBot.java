package ca.ligature.whitecat22;

import ca.ligature.whitecat22.worker.Scout;

import java.util.ArrayList;
import java.util.List;

public class WhitecatBot {

    private static Scout scout;
    private static NeighbourFinder neighbourFinder;

    public static void main(String[] args) throws java.io.IOException {

        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;

        neighbourFinder = new NeighbourFinder(gameMap, myID);

        Networking.sendInit("Whitecat22");

        scout = new Scout(getStartingBlock(myID, gameMap), neighbourFinder);

        while(true) {
            List<Move> moves = new ArrayList<>();

            Networking.updateFrame(gameMap);

            moves.addAll(scout.getMoves());

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();
                    if(site.owner == myID) {
                        if (site.strength == 255){
                            Direction direction = Direction.randomDirection();
                            moves.add(new Move(location, direction));
                        }
                    }
                }
            }

            Networking.sendFrame(moves);
        }
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
