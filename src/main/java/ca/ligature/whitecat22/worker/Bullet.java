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

    public Bullet(Position head) {
        this.head = head;
        this.state = BulletState.GROWING;
    }

    public Move getMove(GameMap gameMap) {
        Location bulletTile = gameMap.getLocation(head);
        if (state == BulletState.LAUNCHED) {
            Move move = new Move(bulletTile, direction);
            Location nextHead = gameMap.getLocation(bulletTile, direction);
            head = new Position(nextHead.x, nextHead.y);
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
        return bulletTile.getSite().strength > 175;
    }

    public boolean isAtLocation(Location location) {
        return head.x == location.x && head.y == location.y;
    }

    public boolean isAtLocation(Position position) {
        return head.x == position.x && head.y == position.y;
    }
}
