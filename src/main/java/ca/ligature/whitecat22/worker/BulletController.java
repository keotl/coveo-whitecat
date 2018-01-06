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

    public static int MAX_BULLETS = 5;

    private List<Bullet> bullets = new ArrayList<>();
    private Position startingArea;
    private int myId;

    private int elapsedTurns = 0;

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
        elapsedTurns++;
        if (elapsedTurns > 60) {
            removeDeadBullets();
            launchIfNeeded(gameMap);
            createBulletsIfNecessary(gameMap);

            return bullets.stream().map(bullet -> bullet.getMove(gameMap)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void removeDeadBullets() {
        bullets.removeIf(Bullet::isDed);
    }

    private void createBulletsIfNecessary(GameMap gameMap) {
        for (int i = 0; i < gameMap.width; i++) {
            for (int j = 0; j < gameMap.height; j++) {
                Location potentialLocation = gameMap.getLocation(i, j);
                if (bullets.size() < MAX_BULLETS && !isBullet(potentialLocation) && potentialLocation.isFriend(myId) && isInAFencedArea(potentialLocation.toPosition(), gameMap)) {
                    bullets.add(new Bullet(potentialLocation.toPosition()));
                }
            }
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
            if (bullet.isReady(gameMap) && !bullet.isLaunched()) {
                bullet.launch(Direction.randomVerticalDirection());
            }
        }
    }

}
