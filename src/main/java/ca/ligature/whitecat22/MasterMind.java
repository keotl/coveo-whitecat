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
                    DirectionChooser dc = new DirectionChooser(location, gameMap, myID);


                    if (site.owner == myID) {
                        moves.add(new Move(location, dc.chooseDirection(neighbourFinder)));
                    }
                }
            }
            Networking.sendFrame(moves);
        }

    }
}
