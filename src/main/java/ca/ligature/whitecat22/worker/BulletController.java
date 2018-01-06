package ca.ligature.whitecat22.worker;

import ca.ligature.whitecat22.Direction;
import ca.ligature.whitecat22.GameMap;
import ca.ligature.whitecat22.Location;
import ca.ligature.whitecat22.Move;
import ca.ligature.whitecat22.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BulletController {

    private List<Bullet> bullets = new ArrayList<>();
    private Position startingArea;
    private int myId;

    public BulletController(Position startingArea, int myId) {
        this.startingArea = startingArea;
        this.myId = myId;
    }

    public boolean isBullet(Location location) {
        return bullets.stream().anyMatch(bullet -> bullet.isAtLocation(location));
    }

    public boolean isBullet(Position position) {
        return bullets.stream().anyMatch(bullet -> bullet.isAtLocation(position));
    }

    public List<Move> getMoves(GameMap gameMap) {
        launchIfNeeded(gameMap);
        createBulletsIfNecessary(gameMap);

        return bullets.stream().map(bullet -> bullet.getMove(gameMap)).collect(Collectors.toList());
    }

    private void createBulletsIfNecessary(GameMap gameMap) {
        if (gameMap.getLocation(startingArea).getSite().owner == myId && !isBullet(startingArea)
            && isInAFencedArea(startingArea, gameMap)) {
            bullets.add(new Bullet(startingArea));
        }
    }

    private boolean isInAFencedArea(Position potentialBulletLocation, GameMap gameMap) {
        Location location = gameMap.getLocation(potentialBulletLocation);

        return Direction.getAllDirections().stream().allMatch(direction -> isSafeTwoForwardsInDirection(direction, location, gameMap));
    }

    private boolean isSafeTwoForwardsInDirection(Direction direction, Location location, GameMap gameMap) {
        Location firstNeighbour = gameMap.getLocation(location, direction);
        Location secondNeighbour = gameMap.getLocation(firstNeighbour, direction);
        return firstNeighbour.isFriend(myId) && secondNeighbour.isFriend(myId);
    }

    private void launchIfNeeded(GameMap gameMap) {
        for (Bullet bullet : bullets) {
            if (bullet.isReady(gameMap)) {
                bullet.launch(Direction.randomDirectionExcludingStill());
            }
        }
    }

}
