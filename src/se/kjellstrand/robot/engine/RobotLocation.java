package se.kjellstrand.robot.engine;

import android.graphics.Point;

public class RobotLocation {

    private Direction mDirection = Direction.NORTH;

    private Point mPosition = new Point();

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void setPosition(Point position) {
        this.mPosition = position;
    }

    @Override
    public String toString() {
        return mPosition.x + " " + mPosition.y + " " + mDirection.toString();
    }
}
