package se.kjellstrand.robot.engine;

/**
 * Interface for rooms that should support outer walls.
 */
public interface RoomWithWalls extends Room {

    /**
     * Get a list of points outlining the room.
     * 
     * @return a list of points, representing the walls. This start and end
     *         coordinate will not be the same, and the last wall is between
     *         them.
     */
    android.graphics.Point[] getWalls();
}
