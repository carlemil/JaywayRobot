package se.kjellstrand.robot.gui;

import android.graphics.Point;
import se.kjellstrand.robot.engine.RobotLocation;

public interface RobotResultListener {
    public void result(RobotLocation[] robotPath, Point[] room);
}
