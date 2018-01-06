package ca.ligature.whitecat22.worker;

import ca.ligature.whitecat22.Direction;
import ca.ligature.whitecat22.Location;
import ca.ligature.whitecat22.Move;
import ca.ligature.whitecat22.NeighbourFinder;

import java.util.ArrayList;
import java.util.List;

public class Scout {

    private Location head;
    private NeighbourFinder neighbourFinder;
    private List<Location> wire;

    public Scout(Location head, NeighbourFinder neighbourFinder) {
        this.head = head;
        this.neighbourFinder = neighbourFinder;
        wire = new ArrayList<>();
        wire.add(head);
    }

    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        for (int i = wire.size() - 1; i > 0; i++) {
            Location wireTile = wire.get(i);
            if (wireTile == head) {
                continue;
            }

            Location energyDestination = wire.get(i -1);
            Direction direction = wireTile.getDirectionTo(energyDestination);
            moves.add(new Move(wireTile, direction));
        }

        moves.add(getHeadMove());

        return moves;
    }

    private Move getHeadMove() {
        Location weakestNeighbour = neighbourFinder.getWeakestEnemyNeighbour(head);
        if (weakestNeighbour.getSite().strength < head.getSite().strength) {
            Direction direction = head.getDirectionTo(weakestNeighbour);
            return new Move(head, direction);
        }
        return new Move(head, Direction.STILL);
    }

}
