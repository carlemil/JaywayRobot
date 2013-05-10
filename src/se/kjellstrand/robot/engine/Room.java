package se.kjellstrand.robot.engine;

/**
 * Interface for a room.
 * 
 */
public interface Room {
    /**
     * Tests if a position is contained in a room or not, true if the position is inside the room, else false.
     * 
     * @param position the position to check.
     * @return result of the test.
     */
    boolean contains(android.graphics.Point position);

    /**
     * Returns the start position of the room.
     * 
     * @return the start position.
     */
    android.graphics.Point getStartPosition();
}
