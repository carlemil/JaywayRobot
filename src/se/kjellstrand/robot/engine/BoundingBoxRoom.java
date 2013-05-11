package se.kjellstrand.robot.engine;

import android.util.Pair;

/**
 * Interface for a room that knows its own bounding box.
 * 
 */
public interface BoundingBoxRoom extends Room {
    
    /**
     * Calculates this rooms bounding box, and returns the bounding box.
     * 
     * @return the bounding box of this room.
     */
    Pair<android.graphics.Point, android.graphics.Point> getBoundingBox();
}
