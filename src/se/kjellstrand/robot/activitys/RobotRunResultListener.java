package se.kjellstrand.robot.activitys;

import android.graphics.Point;

/**
 * Interface for 
 * 
 */
public interface RobotRunResultListener {
    public void robotRunResultRecived(Point[] points, Point[] room);
}
