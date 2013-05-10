package se.kjellstrand.robot.activitys;

import android.graphics.Point;

/**
 * Interface for listening to the results from a robot finishing a run of it's program.
 * 
 */
public interface RobotRunResultListener {
    public void robotRunResultReceived(Point[] points, Point[] room);
}
