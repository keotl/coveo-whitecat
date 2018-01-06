package ca.ligature.whitecat22;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Direction {
    STILL, NORTH, EAST, SOUTH, WEST;

    public static final Direction[] DIRECTIONS = new Direction[]{STILL, NORTH, EAST, SOUTH, WEST};
    public static final Direction[] CARDINALS = new Direction[]{NORTH, EAST, SOUTH, WEST};

    public static Direction randomDirection() {
        Direction[] values = values();
        return values[new Random().nextInt(values.length)];
    }

    public static List<Direction> getAllDirections() {
        return Arrays.asList(NORTH, EAST, SOUTH, WEST);
    }

    public static Direction randomDirectionExcludingStill() {
        Direction[] values = CARDINALS;
        return values[new Random().nextInt(values.length)];
    }

    public static Direction randomVerticalDirection() {
        Direction[] values = {NORTH, SOUTH};
        return values[new Random().nextInt(values.length)];
    }

    public static Direction randomHorizontalDirection() {
        Direction[] values = {EAST, WEST};
        return values[new Random().nextInt(values.length)];
    }
}
