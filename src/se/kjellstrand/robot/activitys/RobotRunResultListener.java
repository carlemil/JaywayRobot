package se.kjellstrand.robot.activitys;

import se.kjellstrand.robot.engine.BoundingBoxRoom;
import se.kjellstrand.robot.engine.RobotLocation;
import android.graphics.Point;

/**
 * Interface for listening to the results from a robot finishing a run of it's
 * program.
 * 
 */
public interface RobotRunResultListener {
    /**
     * Interface to implement by classes that wants to listen in on the result
     * of robot runs.
     * 
     * @param points Array of points, marking the path the robot moved over.
     * @param room the room the robot is in.
     * @param robotLocation the location of the robot.
     */
    public void robotRunResultReceived(Point[] points, BoundingBoxRoom room, RobotLocation robotLocation);
}
