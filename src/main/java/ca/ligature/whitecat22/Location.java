package ca.ligature.whitecat22;

import java.util.ArrayList;
import java.util.List;

public class Location {

    // Public for backward compability
    public final int x, y;
    private final Site site;

    public Location(int x, int y, Site site) {
        this.x = x;
        this.y = y;
        this.site = site;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Site getSite() {
        return site;
    }

    public List<Position> getPossibleNeighbours() {
        List<Position> neighbours = new ArrayList<>();
        neighbours.add(new Position(x + 1, y));
        neighbours.add(new Position(x - 1, y));
        neighbours.add(new Position(x, y - 1));
        neighbours.add(new Position(x, y + 1));
        return neighbours;
    }

    public boolean isEnemy(int myId) {
        return site.owner != myId;
    }

    public Direction getDirectionTo(Location neighbour) {
        if (neighbour.x > x) {
            return Direction.EAST;
        }
        if (neighbour.x < x) {
            return Direction.WEST;
        }
        if (neighbour.y > y) {
            return Direction.SOUTH;
        }
        if (neighbour.y < y) {
            return Direction.NORTH;
        }
        return Direction.STILL;
    }

    public boolean isFriend(int owner) {
        return !isEnemy(owner);
    }

    public Position toPosition() {
        return new Position(x,y);
    }
}
