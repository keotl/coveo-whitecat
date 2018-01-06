package ca.ligature.whitecat22;

import ca.ligature.whitecat22.worker.BulletController;

import java.util.ArrayList;
import java.util.List;

public class MasterMind {

    private static NeighbourFinder neighbourFinder;
    private static BulletController bulletController;
    private static Location startingBlock;

    public static void main(String[] args) throws java.io.IOException {

        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;
        startingBlock = getStartingBlock(myID, gameMap);

        bulletController = new BulletController(startingBlock.toPosition(), myID);

        Networking.sendInit("MyJavaBot");

        while (true) {
            Networking.updateFrame(gameMap);
            neighbourFinder = new NeighbourFinder(gameMap, myID);
            List<Move> moves = new ArrayList<Move>();

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();
                    DirectionChooser dc = new DirectionChooser(location, gameMap, myID);


                    if (site.owner == myID) {
                        if (!bulletController.isBullet(location)) {
                            moves.add(new Move(location, dc.chooseDirection(neighbourFinder)));
                        }
                    }
                }
            }
            moves.addAll(bulletController.getMoves(gameMap));
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
