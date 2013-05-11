package se.kjellstrand.robot.activitys;

import se.kjellstrand.robot.engine.BoundingBoxRoom;
import android.graphics.Point;

/**
 * Interface for listening to the results from a robot finishing a run of it's program.
 * 
 */
public interface RobotRunResultListener {
    public void robotRunResultReceived(Point[] points, BoundingBoxRoom room);
}
