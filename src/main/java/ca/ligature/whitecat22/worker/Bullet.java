package ca.ligature.whitecat22.worker;

import ca.ligature.whitecat22.Direction;
import ca.ligature.whitecat22.GameMap;
import ca.ligature.whitecat22.Location;
import ca.ligature.whitecat22.Move;
import ca.ligature.whitecat22.Position;

public class Bullet {

    private Position head;
    private Direction direction = Direction.EAST;
    private BulletState state;

    private int lastHealth = 0;

    private int timeToLive = -1;

    public Bullet(Position head) {
        this.head = head;
        this.state = BulletState.GROWING;
    }

    public Move getMove(GameMap gameMap) {
        Location bulletTile = gameMap.getLocation(head);
        lastHealth = bulletTile.getSite().strength;
        if (state == BulletState.LAUNCHED) {
            if (timeToLive == -1) {

                Double maxTTL = gameMap.height * 0.8;
                this.timeToLive = maxTTL.intValue();
            }
            Move move = new Move(bulletTile, direction);
            Location nextHead = gameMap.getLocation(bulletTile, direction);
            head = new Position(nextHead.x, nextHead.y);
            timeToLive--;
            return move;
        } else {
            return new Move(bulletTile, Direction.STILL);
        }
    }

    public void launch(Direction direction) {
        this.direction = direction;
        state = BulletState.LAUNCHED;
    }

    public boolean isReady(GameMap gameMap) {
        Location bulletTile = gameMap.getLocation(head);
        return bulletTile.getSite().strength > 80;
    }

    public boolean isAtLocation(Location location) {
        return head.x == location.x && head.y == location.y;
    }

    public boolean isAtLocation(Position position) {
        return head.x == position.x && head.y == position.y;
    }

    public boolean isLaunched() {
        return state == BulletState.LAUNCHED;
    }
    public boolean isDed() {
        return timeToLive == 0 || lastHealth < 20;
    }

    public int getColumn() {
        return head.x;
    }
}
