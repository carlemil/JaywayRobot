package se.kjellstrand.robot.gui;

import android.graphics.Point;

public interface RobotResultListener {
    public void result(Point[] points, Point[] room);
}
